'use client'
import Link from 'next/link'
import { ModuleGuard } from '@/lib/module-guard'
export default function HallmarkingPage() {
  return (
    <ModuleGuard module="hallmarking">
      <div className="min-h-screen bg-gray-50">
        <header className="bg-[#003366] text-white px-4 py-4">
          <div className="max-w-7xl mx-auto flex justify-between"><h1 className="text-xl font-bold">Hallmarking Management</h1><Link href="/" className="text-blue-200 text-sm">← Home</Link></div>
        </header>
        <main className="max-w-7xl mx-auto px-4 py-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {[{title:'Jewellers',desc:'Registration and renewal of jewellers',icon:'💍'},{title:'Assay & Hallmarking Centres',desc:'AHC recognition, surveillance',icon:'🏛️'},{title:'Refineries',desc:'Refinery licensing and audits',icon:'🏭'},{title:'Hallmarking Fee',desc:'Fee collection and receipts',icon:'💰'},{title:'Surveillance',desc:'Market surveillance for hallmarking',icon:'🔍'},{title:'Complaints',desc:'Hallmarking related complaints',icon:'📮'}].map(m => (
              <div key={m.title} className="bg-white rounded-xl shadow p-6"><div className="text-3xl mb-2">{m.icon}</div><h3 className="font-bold text-[#003366] text-lg">{m.title}</h3><p className="text-gray-500 text-sm mt-1">{m.desc}</p></div>
            ))}
          </div>
        </main>
      </div>
    </ModuleGuard>
  )
}
