'use client'
import { useState, useEffect } from 'react'
import Link from 'next/link'
import { authFetch } from '@/lib/keycloak-auth'

interface Application {
  id: string
  applicationNo: string
  schemeCode: string
  applicantName: string
  companyName: string
  productName: string
  status: string
  submittedAt: string
}

export default function CertificationPage() {
  const [applications, setApplications] = useState<Application[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ schemeCode: 'SCHEME_I', applicantName: '', companyName: '', productName: '', productCategory: '', standardNo: '', factoryAddress: '', factoryState: '', factoryPinCode: '' })

  useEffect(() => {
    const loadApplications = async () => {
      try {
        const response = await authFetch('/api/certifications')
        if (!response.ok) throw new Error('Failed to load applications')
        const payload = (await response.json()) as Application[]
        setApplications(payload)
      } catch {
        setApplications([])
      } finally {
        setLoading(false)
      }
    }

    loadApplications()
  }, [])

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    const res = await authFetch('/api/certifications', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
    })
    if (res.ok) {
      const newApp = (await res.json()) as Application
      setApplications(prev => [newApp, ...prev])
      setShowForm(false)
    }
  }

  const statusColor = (s: string) => ({ APPROVED: 'text-green-600 bg-green-50', REJECTED: 'text-red-600 bg-red-50', SUBMITTED: 'text-yellow-600 bg-yellow-50' }[s] || 'text-gray-600 bg-gray-50')

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-xl font-bold">Certification Management</h1>
          <Link href="/dashboard" className="text-blue-200 hover:text-white text-sm">← Dashboard</Link>
        </div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Applications</h2>
          <button onClick={() => setShowForm(!showForm)} className="bg-[#003366] text-white px-4 py-2 rounded-lg hover:bg-[#004080]">+ New Application</button>
        </div>
        {showForm && (
          <form onSubmit={submit} className="bg-white p-6 rounded-xl shadow mb-6 grid grid-cols-2 gap-4">
            <h3 className="col-span-2 font-bold text-lg">New Certification Application</h3>
            {Object.entries(form).map(([k, v]) => (
              <div key={k}>
                <label className="block text-sm font-medium text-gray-700 mb-1 capitalize">{k.replace(/([A-Z])/g, ' $1')}</label>
                {k === 'schemeCode' ? (
                  <select value={v} onChange={e => setForm(f => ({...f, [k]: e.target.value}))} className="w-full border border-gray-300 rounded px-3 py-2">
                    {['SCHEME_I','CRS','MSCS','COC','OTR','FMCS','HALLMARKING'].map(s => <option key={s} value={s}>{s}</option>)}
                  </select>
                ) : (
                  <input value={v} onChange={e => setForm(f => ({...f, [k]: e.target.value}))} className="w-full border border-gray-300 rounded px-3 py-2" />
                )}
              </div>
            ))}
            <div className="col-span-2 flex gap-2">
              <button type="submit" className="bg-[#003366] text-white px-6 py-2 rounded hover:bg-[#004080]">Submit Application</button>
              <button type="button" onClick={() => setShowForm(false)} className="border border-gray-300 px-6 py-2 rounded hover:bg-gray-50">Cancel</button>
            </div>
          </form>
        )}
        {loading ? <div className="text-center py-12 text-gray-500">Loading...</div> : (
          <div className="bg-white rounded-xl shadow overflow-hidden">
            <table className="w-full">
              <thead className="bg-gray-50 border-b">
                <tr>{['App No', 'Scheme', 'Applicant', 'Company', 'Product', 'Status', 'Date'].map(h => <th key={h} className="px-4 py-3 text-left text-sm font-semibold text-gray-600">{h}</th>)}</tr>
              </thead>
              <tbody className="divide-y">
                {applications.length === 0 ? (
                  <tr><td colSpan={7} className="text-center py-8 text-gray-400">No applications yet</td></tr>
                ) : applications.map(a => (
                  <tr key={a.id} className="hover:bg-gray-50">
                    <td className="px-4 py-3 text-sm font-mono text-[#003366]">{a.applicationNo}</td>
                    <td className="px-4 py-3 text-sm">{a.schemeCode}</td>
                    <td className="px-4 py-3 text-sm">{a.applicantName}</td>
                    <td className="px-4 py-3 text-sm">{a.companyName}</td>
                    <td className="px-4 py-3 text-sm">{a.productName}</td>
                    <td className="px-4 py-3"><span className={`text-xs px-2 py-1 rounded-full font-medium ${statusColor(a.status)}`}>{a.status}</span></td>
                    <td className="px-4 py-3 text-sm text-gray-500">{a.submittedAt ? new Date(a.submittedAt).toLocaleDateString() : '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </main>
    </div>
  )
}
