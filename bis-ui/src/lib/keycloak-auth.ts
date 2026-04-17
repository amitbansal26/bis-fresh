'use client'

import { AppModule, AppRole, canAccessModule, normalizeRoles } from '@/lib/authz'

type TokenResponse = {
  access_token: string
  refresh_token?: string
  id_token?: string
  expires_in: number
}

type AuthUser = {
  username: string
  roles: AppRole[]
}

const STORAGE_KEYS = {
  accessToken: 'oauth_access_token',
  refreshToken: 'oauth_refresh_token',
  idToken: 'oauth_id_token',
  expiresAt: 'oauth_expires_at',
  pkceVerifier: 'oauth_pkce_verifier',
  state: 'oauth_state',
  dpopPrivateJwk: 'oauth_dpop_private_jwk',
  dpopPublicJwk: 'oauth_dpop_public_jwk',
  bypassUsername: 'oauth_bypass_username',
  bypassRoles: 'oauth_bypass_roles',
  activeRole: 'oauth_active_role',
} as const

const KEYCLOAK_URL = process.env.NEXT_PUBLIC_KEYCLOAK_URL ?? 'http://localhost:8080'
const KEYCLOAK_REALM = process.env.NEXT_PUBLIC_KEYCLOAK_REALM ?? 'bis'
const KEYCLOAK_CLIENT_ID = process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID ?? 'bis-ui'
const AUTH_BYPASS_ENABLED = process.env.NEXT_PUBLIC_AUTH_BYPASS === 'true'

function getAuthorizationEndpoint() {
  return `${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/auth`
}

function getTokenEndpoint() {
  return `${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token`
}

function getLogoutEndpoint() {
  return `${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/logout`
}

function getRedirectUri() {
  return `${window.location.origin}/login`
}

function toBase64Url(input: ArrayBuffer | Uint8Array | string) {
  let inputBytes: Uint8Array
  if (typeof input === 'string') {
    inputBytes = new TextEncoder().encode(input)
  } else if (input instanceof ArrayBuffer) {
    inputBytes = new Uint8Array(input)
  } else {
    inputBytes = input
  }

  let binary = ''
  inputBytes.forEach(b => {
    binary += String.fromCharCode(b)
  })
  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/g, '')
}

function randomString(size = 64) {
  const bytes = crypto.getRandomValues(new Uint8Array(size))
  return toBase64Url(bytes)
}

async function sha256(input: string) {
  const data = new TextEncoder().encode(input)
  const digest = await crypto.subtle.digest('SHA-256', data)
  return toBase64Url(digest)
}

async function createPkceChallenge(verifier: string) {
  const digest = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(verifier))
  return toBase64Url(digest)
}

async function getOrCreateDpopKeys() {
  const privateJwkRaw = sessionStorage.getItem(STORAGE_KEYS.dpopPrivateJwk)
  const publicJwkRaw = sessionStorage.getItem(STORAGE_KEYS.dpopPublicJwk)

  if (privateJwkRaw && publicJwkRaw) {
    return {
      privateJwk: JSON.parse(privateJwkRaw) as JsonWebKey,
      publicJwk: JSON.parse(publicJwkRaw) as JsonWebKey,
    }
  }

  const keyPair = await crypto.subtle.generateKey(
    {
      name: 'RSASSA-PKCS1-v1_5',
      modulusLength: 2048,
      publicExponent: new Uint8Array([1, 0, 1]),
      hash: 'SHA-256',
    },
    true,
    ['sign', 'verify'],
  )

  const privateJwk = await crypto.subtle.exportKey('jwk', keyPair.privateKey)
  const publicJwk = await crypto.subtle.exportKey('jwk', keyPair.publicKey)

  sessionStorage.setItem(STORAGE_KEYS.dpopPrivateJwk, JSON.stringify(privateJwk))
  sessionStorage.setItem(STORAGE_KEYS.dpopPublicJwk, JSON.stringify(publicJwk))

  return { privateJwk, publicJwk }
}

async function signJwt(privateJwk: JsonWebKey, header: Record<string, unknown>, payload: Record<string, unknown>) {
  const privateKey = await crypto.subtle.importKey(
    'jwk',
    privateJwk,
    {
      name: 'RSASSA-PKCS1-v1_5',
      hash: 'SHA-256',
    },
    false,
    ['sign'],
  )

  const encodedHeader = toBase64Url(JSON.stringify(header))
  const encodedPayload = toBase64Url(JSON.stringify(payload))
  const signingInput = `${encodedHeader}.${encodedPayload}`
  const signature = await crypto.subtle.sign('RSASSA-PKCS1-v1_5', privateKey, new TextEncoder().encode(signingInput))
  return `${signingInput}.${toBase64Url(signature)}`
}

async function createDpopProof(url: string, method: string, accessToken?: string) {
  const { privateJwk, publicJwk } = await getOrCreateDpopKeys()

  const payload: Record<string, unknown> = {
    htm: method.toUpperCase(),
    htu: url,
    iat: Math.floor(Date.now() / 1000),
    jti: crypto.randomUUID(),
  }

  if (accessToken) {
    payload.ath = await sha256(accessToken)
  }

  return signJwt(
    privateJwk,
    {
      typ: 'dpop+jwt',
      alg: 'RS256',
      jwk: publicJwk,
    },
    payload,
  )
}

function saveTokens(response: TokenResponse) {
  sessionStorage.setItem(STORAGE_KEYS.accessToken, response.access_token)
  if (response.refresh_token) {
    sessionStorage.setItem(STORAGE_KEYS.refreshToken, response.refresh_token)
  }
  if (response.id_token) {
    sessionStorage.setItem(STORAGE_KEYS.idToken, response.id_token)
  }
  const expiresAt = Date.now() + response.expires_in * 1000
  sessionStorage.setItem(STORAGE_KEYS.expiresAt, expiresAt.toString())
}

function decodeTokenPayload(token: string): Record<string, unknown> | null {
  const parts = token.split('.')
  if (parts.length < 2) {
    return null
  }

  try {
    const normalized = parts[1].replace(/-/g, '+').replace(/_/g, '/')
    const withPadding = normalized.padEnd(Math.ceil(normalized.length / 4) * 4, '=')
    const payloadJson = atob(withPadding)
    return JSON.parse(payloadJson) as Record<string, unknown>
  } catch {
    return null
  }
}

function getStoredToken() {
  const token = sessionStorage.getItem(STORAGE_KEYS.accessToken)
  const expiresAtRaw = sessionStorage.getItem(STORAGE_KEYS.expiresAt)
  if (!token || !expiresAtRaw) {
    return null
  }
  return {
    token,
    expiresAt: Number(expiresAtRaw),
  }
}

function getBypassRoles() {
  const raw = sessionStorage.getItem(STORAGE_KEYS.bypassRoles)
  if (!raw) {
    return [] as AppRole[]
  }

  try {
    const parsed = JSON.parse(raw) as string[]
    return normalizeRoles(parsed)
  } catch {
    return []
  }
}

function getBypassUser(): AuthUser | null {
  if (!AUTH_BYPASS_ENABLED) {
    return null
  }

  const username = sessionStorage.getItem(STORAGE_KEYS.bypassUsername)
  const roles = getBypassRoles()
  if (!username || roles.length === 0) {
    return null
  }

  return { username, roles }
}

async function refreshAccessToken() {
  const refreshToken = sessionStorage.getItem(STORAGE_KEYS.refreshToken)
  if (!refreshToken) {
    clearAuthSession()
    return null
  }

  const tokenEndpoint = getTokenEndpoint()
  const dpopProof = await createDpopProof(tokenEndpoint, 'POST')
  const body = new URLSearchParams({
    grant_type: 'refresh_token',
    client_id: KEYCLOAK_CLIENT_ID,
    refresh_token: refreshToken,
  })

  const res = await fetch(tokenEndpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      DPoP: dpopProof,
    },
    body,
  })

  if (!res.ok) {
    clearAuthSession()
    return null
  }

  const tokens = (await res.json()) as TokenResponse
  saveTokens(tokens)
  return tokens.access_token
}

export function isAuthBypassEnabled() {
  return AUTH_BYPASS_ENABLED
}

export function startBypassSession(username: string, roles: string[]) {
  const normalizedRoles = normalizeRoles(roles)
  if (!AUTH_BYPASS_ENABLED) {
    throw new Error('Cannot start bypass session: NEXT_PUBLIC_AUTH_BYPASS environment variable is not set to true')
  }

  if (!username.trim() || normalizedRoles.length === 0) {
    throw new Error('Username and at least one role are required')
  }

  clearAuthSession()
  sessionStorage.setItem(STORAGE_KEYS.bypassUsername, username.trim())
  sessionStorage.setItem(STORAGE_KEYS.bypassRoles, JSON.stringify(normalizedRoles))
  sessionStorage.setItem(STORAGE_KEYS.activeRole, normalizedRoles[0])
}

export function getActiveRole(): AppRole | null {
  const roleRaw = sessionStorage.getItem(STORAGE_KEYS.activeRole)
  if (!roleRaw) {
    return null
  }

  const [role] = normalizeRoles([roleRaw])
  return role ?? null
}

export function setActiveRole(role: AppRole) {
  sessionStorage.setItem(STORAGE_KEYS.activeRole, role)
}

export async function getAccessToken() {
  const bypassUser = getBypassUser()
  if (bypassUser) {
    return 'bypass-token'
  }

  const stored = getStoredToken()
  if (!stored) {
    return null
  }

  if (Date.now() < stored.expiresAt - 10_000) {
    return stored.token
  }

  return refreshAccessToken()
}

export function clearAuthSession() {
  Object.values(STORAGE_KEYS).forEach(key => sessionStorage.removeItem(key))
}

export async function beginKeycloakLogin() {
  if (AUTH_BYPASS_ENABLED) {
    throw new Error('Keycloak login is disabled when NEXT_PUBLIC_AUTH_BYPASS is enabled')
  }

  const state = randomString(32)
  const verifier = randomString(64)
  const challenge = await createPkceChallenge(verifier)

  sessionStorage.setItem(STORAGE_KEYS.state, state)
  sessionStorage.setItem(STORAGE_KEYS.pkceVerifier, verifier)

  const authUrl = new URL(getAuthorizationEndpoint())
  authUrl.searchParams.set('client_id', KEYCLOAK_CLIENT_ID)
  authUrl.searchParams.set('response_type', 'code')
  authUrl.searchParams.set('scope', 'openid profile email')
  authUrl.searchParams.set('redirect_uri', getRedirectUri())
  authUrl.searchParams.set('code_challenge_method', 'S256')
  authUrl.searchParams.set('code_challenge', challenge)
  authUrl.searchParams.set('state', state)

  window.location.href = authUrl.toString()
}

export async function handleAuthCallback() {
  if (AUTH_BYPASS_ENABLED) {
    return false
  }

  const url = new URL(window.location.href)
  const code = url.searchParams.get('code')
  const state = url.searchParams.get('state')
  if (!code) {
    return false
  }

  const expectedState = sessionStorage.getItem(STORAGE_KEYS.state)
  const verifier = sessionStorage.getItem(STORAGE_KEYS.pkceVerifier)
  if (!state || !expectedState || state !== expectedState || !verifier) {
    throw new Error('Invalid OAuth callback state')
  }

  const tokenEndpoint = getTokenEndpoint()
  const dpopProof = await createDpopProof(tokenEndpoint, 'POST')
  const body = new URLSearchParams({
    grant_type: 'authorization_code',
    client_id: KEYCLOAK_CLIENT_ID,
    code,
    redirect_uri: getRedirectUri(),
    code_verifier: verifier,
  })

  const res = await fetch(tokenEndpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      DPoP: dpopProof,
    },
    body,
  })

  if (!res.ok) {
    throw new Error('Keycloak token exchange failed')
  }

  const tokens = (await res.json()) as TokenResponse
  saveTokens(tokens)

  const roles = await getCurrentUserRoles()
  if (roles.length > 0) {
    sessionStorage.setItem(STORAGE_KEYS.activeRole, roles[0])
  }

  sessionStorage.removeItem(STORAGE_KEYS.state)
  sessionStorage.removeItem(STORAGE_KEYS.pkceVerifier)
  window.history.replaceState({}, '', '/login')
  return true
}

export async function authFetch(input: string, init?: RequestInit) {
  if (getBypassUser()) {
    return fetch(input, init)
  }

  const token = await getAccessToken()
  if (!token) {
    clearAuthSession()
    window.location.href = '/login'
    throw new Error('User is not authenticated')
  }

  const absoluteUrl = new URL(input, window.location.origin).toString()
  const dpopProof = await createDpopProof(absoluteUrl, init?.method ?? 'GET', token)
  const headers = new Headers(init?.headers ?? {})
  headers.set('Authorization', `DPoP ${token}`)
  headers.set('DPoP', dpopProof)

  return fetch(input, {
    ...init,
    headers,
  })
}

export async function getCurrentUsername() {
  const bypassUser = getBypassUser()
  if (bypassUser) {
    return bypassUser.username
  }

  const token = await getAccessToken()
  if (!token) {
    return null
  }

  const payload = decodeTokenPayload(token)
  if (!payload) {
    return null
  }

  return (payload.preferred_username as string) || (payload.sub as string) || null
}

export async function getCurrentUserRoles() {
  const bypassUser = getBypassUser()
  if (bypassUser) {
    return bypassUser.roles
  }

  const token = await getAccessToken()
  if (!token) {
    return [] as AppRole[]
  }

  const payload = decodeTokenPayload(token)
  if (!payload) {
    return [] as AppRole[]
  }

  const realmAccess = payload.realm_access as { roles?: string[] } | undefined
  const realmRoles = Array.isArray(realmAccess?.roles) ? realmAccess.roles : []

  const resourceAccess = payload.resource_access as Record<string, { roles?: string[] }> | undefined
  const clientRolesRaw = resourceAccess?.[KEYCLOAK_CLIENT_ID]?.roles
  const clientRoles = Array.isArray(clientRolesRaw) ? clientRolesRaw : []

  return normalizeRoles([...realmRoles, ...clientRoles])
}

export async function getCurrentUser() {
  const username = await getCurrentUsername()
  if (!username) {
    return null
  }

  const roles = await getCurrentUserRoles()
  if (roles.length === 0) {
    return { username, roles, activeRole: null }
  }

  const activeRole = getActiveRole()
  if (!activeRole || !roles.includes(activeRole)) {
    sessionStorage.setItem(STORAGE_KEYS.activeRole, roles[0])
    return { username, roles, activeRole: roles[0] }
  }

  return { username, roles, activeRole }
}

export async function canAccessAppModule(module: AppModule) {
  const user = await getCurrentUser()
  if (!user) {
    return false
  }
  return canAccessModule(user.roles, module, user.activeRole)
}

export function logout() {
  const isBypassSession = Boolean(getBypassUser())
  const idToken = sessionStorage.getItem(STORAGE_KEYS.idToken)
  clearAuthSession()

  if (isBypassSession || AUTH_BYPASS_ENABLED) {
    window.location.href = '/login'
    return
  }

  const logoutUrl = new URL(getLogoutEndpoint())
  logoutUrl.searchParams.set('client_id', KEYCLOAK_CLIENT_ID)
  logoutUrl.searchParams.set('post_logout_redirect_uri', window.location.origin)
  if (idToken) {
    logoutUrl.searchParams.set('id_token_hint', idToken)
  }

  window.location.href = logoutUrl.toString()
}
