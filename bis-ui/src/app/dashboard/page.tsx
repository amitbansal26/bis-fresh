'use client'
import { useEffect, useState } from 'react'
import Link from 'next/link'
import { useRouter } from 'next/navigation'
import { AppModule, AppRole, getRoleLabel } from '@/lib/authz'
import { authFetch, getCurrentUser, logout, setActiveRole } from '@/lib/keycloak-auth'
import { canAccessModule } from '@/lib/authz'

type DashboardUser = { username: string; roles: AppRole[]; activeRole: AppRole | null }

export default function DashboardPage() {
  const router = useRouter()
  const [user, setUser] = useState<DashboardUser | null>(null)

  useEffect(() => {
    const loadUser = async () => {
      try {
        const res = await authFetch('/api/identity/users/me')
        if (res.ok) {
          const payload = (await res.json()) as { username: string; roles: string[] }
          const currentUser = await getCurrentUser()
          if (currentUser) {
            setUser(currentUser)
            return
          }
          setUser({ username: payload.username, roles: [], activeRole: null })
          return
        }
      } catch {
        // fallback below
      }

      const currentUser = await getCurrentUser()
      if (currentUser) {
        setUser(currentUser)
      } else {
        router.push('/login')
      }
    }

    loadUser()
  }, [router])

  const stats = [
    { label: 'Pending Applications', value: '142', color: 'text-yellow-600', bg: 'bg-yellow-50' },
    { label: 'Active Licenses', value: '3,842', color: 'text-green-600', bg: 'bg-green-50' },
    { label: 'Pending Surveillances', value: '28', color: 'text-blue-600', bg: 'bg-blue-50' },
    { label: 'Open Complaints', value: '17', color: 'text-red-600', bg: 'bg-red-50' },
  ]

  const modules: { key: AppModule; title: string; href: string; desc: string }[] = [
    { key: 'certification', title: 'Certifications', href: '/certification', desc: 'Manage certification applications' },
    { key: 'laboratory', title: 'Laboratory', href: '/laboratory', desc: 'LIMS - samples and test jobs' },
    { key: 'hr', title: 'HR', href: '/hr', desc: 'Employee and leave management' },
    { key: 'finance', title: 'Finance', href: '/finance', desc: 'Payments and receipts' },
    { key: 'procurement', title: 'Procurement', href: '/procurement', desc: 'Purchase orders and stock' },
    { key: 'legal', title: 'Legal', href: '/legal', desc: 'Cases and complaints' },
    { key: 'hallmarking', title: 'Hallmarking', href: '/hallmarking', desc: 'Jewellers, AHC and refineries' },
    { key: 'lrs', title: 'LRS', href: '/lrs', desc: 'Laboratory recognition and empanelment' },
    { key: 'facilities', title: 'Facilities', href: '/facilities', desc: 'Bookings, attendance and support' },
    { key: 'master-data', title: 'Master Data', href: '/master-data', desc: 'Schemes, offices and standards' },
    { key: 'notifications', title: 'Notifications', href: '/notifications', desc: 'Email/SMS and templates' },
    { key: 'integration', title: 'Integration', href: '/integration', desc: 'NSWS and external connectors' },
  ]

  const visibleModules = user
    ? modules.filter(module => canAccessModule(user.roles, module.key, user.activeRole))
    : []

  const onRoleChange = (role: AppRole) => {
    if (!user) {
      return
    }
    setActiveRole(role)
    setUser({ ...user, activeRole: role })
  }

  return (
    <div className="min-h-screen">
      <header className="bg-[#003366] text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-xl font-bold">BIS Manakonline - Officer Dashboard</h1>
          <div className="flex items-center gap-4">
            <span className="text-blue-100/90 text-sm">{user?.username}</span>
            <button onClick={logout}
              className="text-sm bg-red-600 hover:bg-red-700 px-3 py-1.5 rounded-md font-medium transition-colors">Logout</button>
          </div>
        </div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <h2 className="text-2xl font-bold text-slate-800 mb-6">Welcome back, {user?.username || '...'}</h2>
        <div className="bg-white border border-slate-200 rounded-xl p-4 mb-6">
          <p className="text-sm font-semibold text-slate-700 mb-2">Roles</p>
          <div className="flex flex-wrap gap-2 mb-3">
            {user?.roles?.length ? user.roles.map(role => (
              <span key={role} className={`px-2 py-1 rounded-full text-xs font-semibold ${user.activeRole === role ? 'bg-[#003366] text-white' : 'bg-slate-100 text-slate-700'}`}>
                {getRoleLabel(role)}
              </span>
            )) : <span className="text-xs text-slate-500">No mapped roles found</span>}
          </div>
          {user?.roles && user.roles.length > 1 && (
            <div className="flex items-center gap-2">
              <label className="text-xs text-slate-600">Active role</label>
              <select
                value={user.activeRole ?? user.roles[0] ?? ''}
                onChange={event => onRoleChange(event.target.value as AppRole)}
                className="border border-slate-300 rounded-md px-2 py-1 text-xs"
              >
                {user.roles.map(role => (
                  <option key={role} value={role}>{getRoleLabel(role)}</option>
                ))}
              </select>
            </div>
          )}
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {stats.map(s => (
            <div key={s.label} className={`${s.bg} surface-card p-6`}>
              <div className={`text-4xl font-bold ${s.color}`}>{s.value}</div>
              <div className="text-slate-600 mt-2 text-sm font-medium">{s.label}</div>
            </div>
          ))}
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {visibleModules.map(m => (
            <Link key={m.href} href={m.href} className="surface-card hover:shadow-md transition p-6">
              <h3 className="font-bold text-[#003366] text-lg">{m.title}</h3>
              <p className="text-slate-500 text-sm mt-1">{m.desc}</p>
            </Link>
          ))}
        </div>
        {user && visibleModules.length === 0 && (
          <div className="mt-6 bg-amber-50 border border-amber-200 rounded-xl p-4 text-amber-800 text-sm">
            No module is available for your active role. Switch role or contact admin.
          </div>
        )}
      </main>
    </div>
  )
}
