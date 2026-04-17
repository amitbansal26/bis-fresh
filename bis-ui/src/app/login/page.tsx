'use client'

import { useEffect, useState } from 'react'
import Link from 'next/link'
import { beginKeycloakLogin, handleAuthCallback } from '@/lib/keycloak-auth'

export default function LoginPage() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    const runCallback = async () => {
      try {
        setLoading(true)
        const completed = await handleAuthCallback()
        if (completed) {
          window.location.href = '/dashboard'
        }
      } catch (err: unknown) {
        setError(err instanceof Error ? err.message : 'Login failed')
      } finally {
        setLoading(false)
      }
    }

    runCallback()
  }, [])

  const handleLogin = async () => {
    setError('')
    setLoading(true)
    try {
      await beginKeycloakLogin()
    } catch (err: unknown) {
      setLoading(false)
      setError(err instanceof Error ? err.message : 'Unable to start Keycloak login')
    }
  }

  return (
    <div className="min-h-screen flex flex-col justify-center px-4">
      <div className="max-w-md w-full mx-auto">
        <div className="text-center mb-8">
          <div className="w-16 h-16 bg-[#003366] rounded-full flex items-center justify-center mx-auto mb-4 shadow-md">
            <span className="text-white font-bold text-xl">BIS</span>
          </div>
          <h1 className="text-3xl font-bold text-[#003366] tracking-tight">BIS Manakonline</h1>
          <p className="text-slate-500 mt-2">Secure sign-in with Keycloak (OAuth2 + DPoP)</p>
        </div>
        <div className="surface-card py-8 px-6">
          {error && <div className="bg-red-50 text-red-700 p-3 rounded-md text-sm mb-4 border border-red-100">{error}</div>}
          <p className="text-sm text-slate-500 mb-5">
            Use your official credentials to access BIS platform services.
          </p>
          <button
            onClick={handleLogin}
            disabled={loading}
            className="w-full primary-button py-2.5 px-4 rounded-md font-semibold disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? 'Signing in...' : 'Sign In with Keycloak'}
          </button>
          <div className="mt-4 text-center">
            <Link href="/" className="text-sm text-[#003366] hover:underline underline-offset-2">← Back to Home</Link>
          </div>
        </div>
      </div>
    </div>
  )
}
