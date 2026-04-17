'use client'
import { useEffect, useState } from 'react'
import Link from 'next/link'
import { authFetch, getCurrentUsername, logout } from '@/lib/keycloak-auth'

type DashboardUser = { username: string; roles: string[] }

export default function DashboardPage() {
  const [user, setUser] = useState<DashboardUser | null>(null)

  useEffect(() => {
    const loadUser = async () => {
      try {
        const res = await authFetch('/api/identity/users/me')
        if (res.ok) {
          const payload = (await res.json()) as { username: string; roles: string[] }
          setUser({ username: payload.username, roles: payload.roles ?? [] })
          return
        }
      } catch {
        // fallback below
      }

      const username = await getCurrentUsername()
      if (username) {
        setUser({ username, roles: [] })
      } else {
        window.location.href = '/login'
      }
    }

    loadUser()
  }, [])

  const stats = [
    { label: 'Pending Applications', value: '142', color: 'text-yellow-600', bg: 'bg-yellow-50' },
    { label: 'Active Licenses', value: '3,842', color: 'text-green-600', bg: 'bg-green-50' },
    { label: 'Pending Surveillances', value: '28', color: 'text-blue-600', bg: 'bg-blue-50' },
    { label: 'Open Complaints', value: '17', color: 'text-red-600', bg: 'bg-red-50' },
  ]

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white shadow">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-xl font-bold">BIS Manakonline - Officer Dashboard</h1>
          <div className="flex items-center gap-4">
            <span className="text-blue-200">{user?.username}</span>
            <button onClick={logout}
              className="text-sm bg-red-600 hover:bg-red-700 px-3 py-1 rounded">Logout</button>
          </div>
        </div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Welcome back, {user?.username || '...'}</h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {stats.map(s => (
            <div key={s.label} className={`${s.bg} rounded-xl p-6 shadow-sm`}>
              <div className={`text-4xl font-bold ${s.color}`}>{s.value}</div>
              <div className="text-gray-600 mt-2 text-sm font-medium">{s.label}</div>
            </div>
          ))}
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[
            { title: 'Certifications', href: '/certification', desc: 'Manage certification applications' },
            { title: 'Laboratory', href: '/laboratory', desc: 'LIMS - samples and test jobs' },
            { title: 'HR', href: '/hr', desc: 'Employee and leave management' },
            { title: 'Finance', href: '/finance', desc: 'Payments and receipts' },
            { title: 'Procurement', href: '/procurement', desc: 'Purchase orders and stock' },
            { title: 'Legal', href: '/legal', desc: 'Cases and complaints' },
          ].map(m => (
            <Link key={m.href} href={m.href} className="bg-white rounded-xl shadow hover:shadow-md transition p-6 border border-gray-100">
              <h3 className="font-bold text-[#003366] text-lg">{m.title}</h3>
              <p className="text-gray-500 text-sm mt-1">{m.desc}</p>
            </Link>
          ))}
        </div>
      </main>
    </div>
  )
}
