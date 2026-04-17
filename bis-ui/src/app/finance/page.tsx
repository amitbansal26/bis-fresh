'use client'
import Link from 'next/link'
import { ModuleGuard } from '@/lib/module-guard'
export default function FinancePage() {
  return (
    <ModuleGuard module="finance">
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between"><h1 className="text-xl font-bold">Finance & Accounts</h1><Link href="/dashboard" className="text-blue-200 text-sm">← Dashboard</Link></div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[{title:'Payments',desc:'Manage outgoing payments',icon:'💳'},{title:'Receipts',desc:'Fee receipts and reconciliation',icon:'🧾'},{title:'Bank Reconciliation',desc:'Match bank statements',icon:'🏦'},{title:'Budget',desc:'Budget allocation and utilization',icon:'📊'},{title:'Refunds',desc:'Process fee refunds',icon:'↩️'},{title:'Reports',desc:'Financial reports and MIS',icon:'📈'}].map(m => (
            <div key={m.title} className="bg-white rounded-xl shadow p-6"><div className="text-3xl mb-2">{m.icon}</div><h3 className="font-bold text-[#003366] text-lg">{m.title}</h3><p className="text-gray-500 text-sm mt-1">{m.desc}</p></div>
          ))}
        </div>
      </main>
    </div>    </ModuleGuard>
  )
}
