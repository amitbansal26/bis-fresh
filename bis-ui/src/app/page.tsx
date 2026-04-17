import Link from 'next/link'

const modules = [
  { title: 'Product Certification', description: 'Scheme I, II, III, IV, VI, IX, X, FMCS', href: '/certification', icon: '🏷️', color: 'bg-blue-600' },
  { title: 'Hallmarking', description: 'Jewellers, AHC, Refineries registration and surveillance', href: '/hallmarking', icon: '💍', color: 'bg-yellow-600' },
  { title: 'Laboratory Services', description: 'LIMS - Sample management, testing, reports', href: '/laboratory', icon: '🔬', color: 'bg-green-600' },
  { title: 'Lab Recognition (LRS)', description: 'Recognition, empanelment, audits', href: '/lrs', icon: '🏛️', color: 'bg-teal-600' },
  { title: 'Human Resources', description: 'Employee lifecycle, leave, APAR, recruitment', href: '/hr', icon: '👥', color: 'bg-purple-600' },
  { title: 'Finance & Accounts', description: 'Payments, receipts, reconciliation', href: '/finance', icon: '💰', color: 'bg-indigo-600' },
  { title: 'Procurement', description: 'Purchase orders, stock management', href: '/procurement', icon: '📦', color: 'bg-orange-600' },
  { title: 'Legal & Complaints', description: 'Legal cases, complaints, enforcement', href: '/legal', icon: '⚖️', color: 'bg-red-600' },
  { title: 'Facilities', description: 'Room bookings, library, canteen', href: '/facilities', icon: '🏢', color: 'bg-pink-600' },
  { title: 'Master Data', description: 'Schemes, offices, laboratories, standards', href: '/master-data', icon: '📋', color: 'bg-gray-600' },
  { title: 'Notifications', description: 'Email/SMS notifications and templates', href: '/notifications', icon: '🔔', color: 'bg-cyan-600' },
  { title: 'Integration Hub', description: 'NSWS, payment gateway, external systems', href: '/integration', icon: '🔗', color: 'bg-slate-600' },
]

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-[#003366] text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center">
              <span className="text-[#003366] font-bold text-lg">BIS</span>
            </div>
            <div>
              <h1 className="text-2xl font-bold">BIS Manakonline</h1>
              <p className="text-blue-200 text-sm">Bureau of Indian Standards - Digital Platform</p>
            </div>
          </div>
          <nav className="flex gap-4">
            <Link href="/login" className="bg-[#FF6600] hover:bg-orange-700 px-4 py-2 rounded-md font-medium transition-colors">Login</Link>
          </nav>
        </div>
      </header>

      <section className="bg-gradient-to-b from-[#003366] to-[#004080] text-white py-16">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <h2 className="text-4xl font-bold mb-4">Welcome to BIS Manakonline</h2>
          <p className="text-xl text-blue-200 mb-8">Integrated Digital Platform for Standards, Certification and Quality Management</p>
          <div className="flex justify-center gap-4">
            <Link href="/certification" className="bg-[#FF6600] hover:bg-orange-700 px-6 py-3 rounded-lg font-semibold text-lg transition-colors">Apply for Certification</Link>
            <Link href="/dashboard" className="border border-white hover:bg-white hover:text-[#003366] px-6 py-3 rounded-lg font-semibold text-lg transition-colors">Officer Dashboard</Link>
          </div>
        </div>
      </section>

      <main className="max-w-7xl mx-auto px-4 py-12">
        <h3 className="text-2xl font-bold text-gray-800 mb-8 text-center">Platform Modules</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {modules.map((mod) => (
            <Link key={mod.href} href={mod.href} className="group bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100">
              <div className={`${mod.color} p-4 text-white`}>
                <span className="text-3xl">{mod.icon}</span>
              </div>
              <div className="p-4">
                <h4 className="font-bold text-gray-800 group-hover:text-[#003366] transition-colors">{mod.title}</h4>
                <p className="text-sm text-gray-500 mt-1">{mod.description}</p>
              </div>
            </Link>
          ))}
        </div>
      </main>

      <footer className="bg-[#003366] text-white mt-16 py-8">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <p className="text-blue-200">© 2024 Bureau of Indian Standards. Ministry of Consumer Affairs, Food & Public Distribution, Government of India.</p>
        </div>
      </footer>
    </div>
  )
}
