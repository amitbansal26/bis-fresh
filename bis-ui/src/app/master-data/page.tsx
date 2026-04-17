'use client'
import { useState, useEffect } from 'react'
import Link from 'next/link'
import { canAccessAppModule } from '@/lib/keycloak-auth'
import { ModuleGuard } from '@/lib/module-guard'

export default function MasterDataPage() {
  const [schemes, setSchemes] = useState<{id: number; code: string; name: string; schemeType: string; active: boolean}[]>([])
  const [offices, setOffices] = useState<{id: number; officeCode: string; officeName: string; officeType: string; state: string; active: boolean}[]>([])
  const [activeTab, setActiveTab] = useState<'schemes'|'offices'|'standards'>('schemes')

  useEffect(() => {
    const loadData = async () => {
      const hasAccess = await canAccessAppModule('master-data')
      if (!hasAccess) {
        return
      }

      fetch('/api/master/schemes').then(r => r.json()).then(setSchemes).catch(() => {})
      fetch('/api/master/offices').then(r => r.json()).then(setOffices).catch(() => {})
    }

    loadData()
  }, [])

  return (
    <ModuleGuard module="master-data">
      <div className="min-h-screen bg-gray-50">
        <header className="bg-[#003366] text-white px-4 py-4">
          <div className="max-w-7xl mx-auto flex justify-between items-center">
            <h1 className="text-xl font-bold">Master Data Management</h1>
            <Link href="/" className="text-blue-200 hover:text-white text-sm">← Home</Link>
          </div>
        </header>
        <main className="max-w-7xl mx-auto px-4 py-8">
          <div className="flex gap-4 mb-6 border-b">
            {(['schemes','offices','standards'] as const).map(t => (
              <button key={t} onClick={() => setActiveTab(t)} className={`pb-3 px-4 font-medium capitalize ${activeTab===t ? 'border-b-2 border-[#003366] text-[#003366]' : 'text-gray-500 hover:text-gray-700'}`}>{t}</button>
            ))}
          </div>
          {activeTab === 'schemes' && (
            <div className="bg-white rounded-xl shadow overflow-hidden">
              <table className="w-full"><thead className="bg-gray-50"><tr>{['Code','Name','Type','Status'].map(h => <th key={h} className="px-4 py-3 text-left text-sm font-semibold text-gray-600">{h}</th>)}</tr></thead>
                <tbody className="divide-y">
                  {schemes.length === 0 ? <tr><td colSpan={4} className="text-center py-8 text-gray-400">No schemes found</td></tr>
                    : schemes.map(s => <tr key={s.id} className="hover:bg-gray-50"><td className="px-4 py-3 text-sm font-mono">{s.code}</td><td className="px-4 py-3 text-sm">{s.name}</td><td className="px-4 py-3 text-sm">{s.schemeType}</td><td className="px-4 py-3 text-sm"><span className={`px-2 py-1 rounded-full text-xs ${s.active ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>{s.active ? 'Active' : 'Inactive'}</span></td></tr>)}
                </tbody>
              </table>
            </div>
          )}
          {activeTab === 'offices' && (
            <div className="bg-white rounded-xl shadow overflow-hidden">
              <table className="w-full"><thead className="bg-gray-50"><tr>{['Code','Name','Type','State','Status'].map(h => <th key={h} className="px-4 py-3 text-left text-sm font-semibold text-gray-600">{h}</th>)}</tr></thead>
                <tbody className="divide-y">
                  {offices.length === 0 ? <tr><td colSpan={5} className="text-center py-8 text-gray-400">No offices found</td></tr>
                    : offices.map(o => <tr key={o.id} className="hover:bg-gray-50"><td className="px-4 py-3 text-sm font-mono">{o.officeCode}</td><td className="px-4 py-3 text-sm">{o.officeName}</td><td className="px-4 py-3 text-sm">{o.officeType}</td><td className="px-4 py-3 text-sm">{o.state}</td><td className="px-4 py-3 text-sm"><span className={`px-2 py-1 rounded-full text-xs ${o.active ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>{o.active ? 'Active' : 'Inactive'}</span></td></tr>)}
                </tbody>
              </table>
            </div>
          )}
        </main>
      </div>
    </ModuleGuard>
  )
}
