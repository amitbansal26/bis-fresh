'use client'
import { useState, useEffect } from 'react'
import Link from 'next/link'

interface Employee { id: number; employeeNo: string; name: string; designation: string; department: string; officeCode: string; status: string }

export default function HRPage() {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [loading, setLoading] = useState(true)
  const token = typeof window !== 'undefined' ? localStorage.getItem('token') : ''
  const headers = { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) }

  useEffect(() => {
    fetch('/api/hr/employees', { headers }).then(r => r.json()).then(setEmployees).catch(() => setEmployees([])).finally(() => setLoading(false))
  }, [])

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-xl font-bold">Human Resources</h1>
          <Link href="/dashboard" className="text-blue-200 hover:text-white text-sm">← Dashboard</Link>
        </div>
      </header>
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          {[{ title: 'Employee Records', href: '/hr', desc: 'View and manage employees' }, { title: 'Leave Management', href: '/hr/leaves', desc: 'Apply and approve leaves' }, { title: 'APAR', href: '/hr/apar', desc: 'Annual Performance Appraisal' }].map(m => (
            <Link key={m.href} href={m.href} className="bg-white rounded-xl shadow p-6 hover:shadow-md transition">
              <h3 className="font-bold text-[#003366]">{m.title}</h3>
              <p className="text-gray-500 text-sm mt-1">{m.desc}</p>
            </Link>
          ))}
        </div>
        <div className="bg-white rounded-xl shadow overflow-hidden">
          <div className="p-4 border-b flex justify-between">
            <h2 className="font-bold text-lg">Employees</h2>
          </div>
          {loading ? <div className="p-8 text-center text-gray-500">Loading...</div> : (
            <table className="w-full">
              <thead className="bg-gray-50"><tr>{['Emp No','Name','Designation','Department','Office','Status'].map(h => <th key={h} className="px-4 py-3 text-left text-sm font-semibold text-gray-600">{h}</th>)}</tr></thead>
              <tbody className="divide-y">
                {employees.length === 0 ? <tr><td colSpan={6} className="text-center py-8 text-gray-400">No employees found</td></tr>
                  : employees.map(e => (
                    <tr key={e.id} className="hover:bg-gray-50">
                      <td className="px-4 py-3 text-sm font-mono">{e.employeeNo}</td>
                      <td className="px-4 py-3 text-sm font-medium">{e.name}</td>
                      <td className="px-4 py-3 text-sm">{e.designation}</td>
                      <td className="px-4 py-3 text-sm">{e.department}</td>
                      <td className="px-4 py-3 text-sm">{e.officeCode}</td>
                      <td className="px-4 py-3 text-sm"><span className={`px-2 py-1 rounded-full text-xs font-medium ${e.status === 'ACTIVE' ? 'bg-green-50 text-green-700' : 'bg-gray-50 text-gray-700'}`}>{e.status}</span></td>
                    </tr>
                  ))}
              </tbody>
            </table>
          )}
        </div>
      </main>
    </div>
  )
}
