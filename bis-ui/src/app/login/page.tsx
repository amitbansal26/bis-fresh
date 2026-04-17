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
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center">
      <div className="max-w-md w-full mx-auto">
        <div className="text-center mb-8">
          <div className="w-16 h-16 bg-[#003366] rounded-full flex items-center justify-center mx-auto mb-4">
            <span className="text-white font-bold text-xl">BIS</span>
          </div>
          <h1 className="text-3xl font-bold text-[#003366]">BIS Manakonline</h1>
          <p className="text-gray-500 mt-2">Sign in securely with Keycloak (OAuth2 + DPoP)</p>
        </div>
        <div className="bg-white py-8 px-6 shadow-lg rounded-xl">
          {error && <div className="bg-red-50 text-red-700 p-3 rounded-md text-sm mb-4">{error}</div>}
          <button
            onClick={handleLogin}
            disabled={loading}
            className="w-full bg-[#003366] text-white py-2 px-4 rounded-md hover:bg-[#004080] transition-colors font-medium disabled:opacity-50"
          >
            {loading ? 'Signing in...' : 'Sign In with Keycloak'}
          </button>
          <div className="mt-4 text-center">
            <Link href="/" className="text-sm text-[#003366] hover:underline">← Back to Home</Link>
          </div>
        </div>
      </div>
    </div>
  )
}
