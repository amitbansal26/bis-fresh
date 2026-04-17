'use client'
import Link from 'next/link'
import { ModuleGuard } from '@/lib/module-guard'
export default function LegalPage() {
  return (
    <ModuleGuard module="legal">
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between"><h1 className="text-xl font-bold">Legal & Complaints</h1><Link href="/dashboard" className="text-blue-200 text-sm">← Dashboard</Link></div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[{title:'Legal Cases',desc:'Track court cases and hearings',icon:'⚖️'},{title:'Complaints',desc:'Consumer and trade complaints',icon:'📮'},{title:'Enforcement',desc:'Market enforcement actions',icon:'🚔'},{title:'Prosecution',desc:'Legal prosecution cases',icon:'📋'},{title:'RTI',desc:'Right to Information requests',icon:'📜'},{title:'Legal Opinions',desc:'Internal legal advice',icon:'💼'}].map(m => (
            <div key={m.title} className="bg-white rounded-xl shadow p-6"><div className="text-3xl mb-2">{m.icon}</div><h3 className="font-bold text-[#003366] text-lg">{m.title}</h3><p className="text-gray-500 text-sm mt-1">{m.desc}</p></div>
          ))}
        </div>
      </main>
    </div>
    </ModuleGuard>
  )
}
