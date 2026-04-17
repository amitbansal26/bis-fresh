'use client'
import { useState } from 'react'
import Link from 'next/link'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError('')
    try {
      const res = await fetch('/api/identity/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      })
      if (!res.ok) throw new Error('Invalid credentials')
      const data = await res.json()
      localStorage.setItem('token', data.token)
      window.location.href = '/dashboard'
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Login failed')
    } finally {
      setLoading(false)
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
          <p className="text-gray-500 mt-2">Sign in to your account</p>
        </div>
        <div className="bg-white py-8 px-6 shadow-lg rounded-xl">
          <form onSubmit={handleSubmit} className="space-y-6">
            {error && <div className="bg-red-50 text-red-700 p-3 rounded-md text-sm">{error}</div>}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
              <input type="text" value={username} onChange={e => setUsername(e.target.value)} required
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-[#003366] focus:border-[#003366]" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
              <input type="password" value={password} onChange={e => setPassword(e.target.value)} required
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-[#003366] focus:border-[#003366]" />
            </div>
            <button type="submit" disabled={loading}
              className="w-full bg-[#003366] text-white py-2 px-4 rounded-md hover:bg-[#004080] transition-colors font-medium disabled:opacity-50">
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
          </form>
          <div className="mt-4 text-center">
            <Link href="/" className="text-sm text-[#003366] hover:underline">← Back to Home</Link>
          </div>
        </div>
      </div>
    </div>
  )
}
