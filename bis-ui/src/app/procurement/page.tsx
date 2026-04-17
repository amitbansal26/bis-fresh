'use client'
import Link from 'next/link'
export default function ProcurementPage() {
  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between"><h1 className="text-xl font-bold">Procurement & Inventory</h1><Link href="/dashboard" className="text-blue-200 text-sm">← Dashboard</Link></div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[{title:'Purchase Orders',desc:'Create and track POs',icon:'📦'},{title:'Stock Management',desc:'Inventory and stock levels',icon:'🗄️'},{title:'Vendors',desc:'Vendor registration and rating',icon:'🏪'},{title:'GRN',desc:'Goods receipt notes',icon:'✅'},{title:'Scrap Sales',desc:'Disposal of condemned items',icon:'♻️'},{title:'Asset Register',desc:'Track organizational assets',icon:'🏛️'}].map(m => (
            <div key={m.title} className="bg-white rounded-xl shadow p-6"><div className="text-3xl mb-2">{m.icon}</div><h3 className="font-bold text-[#003366] text-lg">{m.title}</h3><p className="text-gray-500 text-sm mt-1">{m.desc}</p></div>
          ))}
        </div>
      </main>
    </div>
  )
}
