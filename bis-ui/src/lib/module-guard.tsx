'use client'

import Link from 'next/link'
import { useEffect, useState } from 'react'
import { AppModule } from '@/lib/authz'
import { getCurrentUser } from '@/lib/keycloak-auth'
import { canAccessModule } from '@/lib/authz'

export function ModuleGuard({ module, children }: { module: AppModule; children: React.ReactNode }) {
  const [loading, setLoading] = useState(true)
  const [authorized, setAuthorized] = useState(false)

  useEffect(() => {
    let cancelled = false

    const validate = async () => {
      const user = await getCurrentUser()
      if (cancelled) {
        return
      }

      if (!user) {
        window.location.href = '/login'
        return
      }

      setAuthorized(canAccessModule(user.roles, module, user.activeRole))
      setLoading(false)
    }

    validate()

    return () => {
      cancelled = true
    }
  }, [module])

  if (loading) {
    return <div className="min-h-screen flex items-center justify-center text-slate-500">Loading...</div>
  }

  if (!authorized) {
    return (
      <div className="min-h-screen flex items-center justify-center px-4 bg-gray-50">
        <div className="bg-white border border-red-100 rounded-xl p-6 max-w-lg text-center shadow-sm">
          <h2 className="text-xl font-bold text-[#003366]">Access Restricted</h2>
          <p className="text-sm text-slate-600 mt-2">Your active role is not allowed for this module.</p>
          <Link href="/dashboard" className="inline-block mt-4 bg-[#003366] text-white px-4 py-2 rounded-md text-sm hover:bg-[#004080]">
            Go to Dashboard
          </Link>
        </div>
      </div>
    )
  }

  return <>{children}</>
}
