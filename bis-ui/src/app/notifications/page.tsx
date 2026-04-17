'use client'
import Link from 'next/link'
import { ModuleGuard } from '@/lib/module-guard'
export default function NotificationsPage() {
  return (
    <ModuleGuard module="notifications">
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between"><h1 className="text-xl font-bold">Notifications</h1><Link href="/dashboard" className="text-blue-200 text-sm">← Dashboard</Link></div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[{title:'Email Notifications',desc:'Manage email alerts',icon:'📧'},{title:'SMS Alerts',desc:'SMS notification management',icon:'📱'},{title:'Templates',desc:'Notification templates',icon:'📝'},{title:'Integration Hub',desc:'NSWS and external system integration',icon:'🔗'},{title:'Audit Log',desc:'Integration and notification logs',icon:'📋'},{title:'Payment Gateway',desc:'Bharatkosh payment integration',icon:'💳'}].map(m => (
            <div key={m.title} className="bg-white rounded-xl shadow p-6"><div className="text-3xl mb-2">{m.icon}</div><h3 className="font-bold text-[#003366] text-lg">{m.title}</h3><p className="text-gray-500 text-sm mt-1">{m.desc}</p></div>
          ))}
        </div>
      </main>
    </div>    </ModuleGuard>
  )
}
