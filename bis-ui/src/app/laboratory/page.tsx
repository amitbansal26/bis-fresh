'use client'
import { useState } from 'react'
import Link from 'next/link'
import { authFetch } from '@/lib/keycloak-auth'

export default function LaboratoryPage() {
  const [labCode, setLabCode] = useState('LAB001')
  const [samples, setSamples] = useState<{id: number; sampleNo: string; productName: string; status: string; receivedAt: string}[]>([])
  const [loading, setLoading] = useState(false)

  const loadSamples = async () => {
    setLoading(true)
    try {
      const response = await authFetch(`/api/labs/${labCode}/samples`)
      if (!response.ok) throw new Error('Failed to load samples')
      const payload = (await response.json()) as {id: number; sampleNo: string; productName: string; status: string; receivedAt: string}[]
      setSamples(payload)
    } catch {
      setSamples([])
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-xl font-bold">Laboratory Information System (LIMS)</h1>
          <Link href="/dashboard" className="text-blue-200 hover:text-white text-sm">← Dashboard</Link>
        </div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          {[{title:'Sample Management',desc:'Receive and track samples',icon:'🧪'},{title:'Test Jobs',desc:'Assign and track testing',icon:'🔬'},{title:'Equipment',desc:'Calibration and maintenance',icon:'⚙️'},{title:'Test Reports',desc:'Issue and manage reports',icon:'📄'},{title:'LRS Recognition',desc:'Lab recognition and empanelment',icon:'🏛️'},{title:'QA Checks',desc:'Quality assurance records',icon:'✅'}].map(m => (
            <div key={m.title} className="bg-white rounded-xl shadow p-6"><div className="text-3xl mb-2">{m.icon}</div><h3 className="font-bold text-[#003366]">{m.title}</h3><p className="text-gray-500 text-sm mt-1">{m.desc}</p></div>
          ))}
        </div>
        <div className="bg-white rounded-xl shadow p-6">
          <h2 className="font-bold text-lg mb-4">Sample Tracker</h2>
          <div className="flex gap-3 mb-4">
            <input value={labCode} onChange={e => setLabCode(e.target.value)} placeholder="Lab Code" className="border border-gray-300 rounded px-3 py-2 text-sm" />
            <button onClick={loadSamples} className="bg-[#003366] text-white px-4 py-2 rounded text-sm hover:bg-[#004080]">Load Samples</button>
          </div>
          {loading ? <div className="text-center py-4 text-gray-500">Loading...</div> : (
            <table className="w-full text-sm"><thead className="bg-gray-50"><tr>{['Sample No','Product','Status','Received'].map(h => <th key={h} className="px-3 py-2 text-left font-semibold text-gray-600">{h}</th>)}</tr></thead>
              <tbody className="divide-y">
                {samples.length === 0 ? <tr><td colSpan={4} className="text-center py-4 text-gray-400">No samples for this lab</td></tr>
                  : samples.map(s => <tr key={s.id}><td className="px-3 py-2 font-mono">{s.sampleNo}</td><td className="px-3 py-2">{s.productName}</td><td className="px-3 py-2">{s.status}</td><td className="px-3 py-2">{s.receivedAt ? new Date(s.receivedAt).toLocaleDateString() : '-'}</td></tr>)}
              </tbody>
            </table>
          )}
        </div>
      </main>
    </div>
  )
}
