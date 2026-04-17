import Link from 'next/link'

const modules = [
  { title: 'Product Certification', description: 'Scheme I, II, III, IV, VI, IX, X, FMCS', href: '/certification', color: 'bg-blue-700' },
  { title: 'Hallmarking', description: 'Jewellers, AHC, Refineries registration and surveillance', href: '/hallmarking', color: 'bg-amber-600' },
  { title: 'Laboratory Services', description: 'LIMS - Sample management, testing, reports', href: '/laboratory', color: 'bg-emerald-700' },
  { title: 'Lab Recognition (LRS)', description: 'Recognition, empanelment, audits', href: '/lrs', color: 'bg-teal-700' },
  { title: 'Human Resources', description: 'Employee lifecycle, leave, APAR, recruitment', href: '/hr', color: 'bg-violet-700' },
  { title: 'Finance & Accounts', description: 'Payments, receipts, reconciliation', href: '/finance', color: 'bg-indigo-700' },
  { title: 'Procurement', description: 'Purchase orders, stock management', href: '/procurement', color: 'bg-orange-700' },
  { title: 'Legal & Complaints', description: 'Legal cases, complaints, enforcement', href: '/legal', color: 'bg-rose-700' },
  { title: 'Facilities', description: 'Room bookings, library, canteen', href: '/facilities', color: 'bg-fuchsia-700' },
  { title: 'Master Data', description: 'Schemes, offices, laboratories, standards', href: '/master-data', color: 'bg-slate-700' },
  { title: 'Notifications', description: 'Email/SMS notifications and templates', href: '/notifications', color: 'bg-cyan-700' },
  { title: 'Integration Hub', description: 'NSWS, payment gateway, external systems', href: '/integration', color: 'bg-stone-700' },
]

export default function Home() {
  return (
    <div className="min-h-screen">
      <header className="bg-[#003366] text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-white/95 rounded-full flex items-center justify-center shadow-sm">
              <span className="text-[#003366] font-bold text-lg">BIS</span>
            </div>
            <div>
              <h1 className="text-2xl font-bold">BIS Manakonline</h1>
              <p className="text-blue-100/90 text-sm">Bureau of Indian Standards - Digital Platform</p>
            </div>
          </div>
          <nav className="flex gap-4">
            <Link href="/login" className="bg-[#FF6600] hover:bg-orange-700 px-4 py-2 rounded-md font-semibold transition-colors">Login</Link>
          </nav>
        </div>
      </header>

      <section className="bg-gradient-to-b from-[#003366] via-[#003d78] to-[#004080] text-white py-20">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <h2 className="text-4xl md:text-5xl font-bold mb-4 tracking-tight">Welcome to BIS Manakonline</h2>
          <p className="text-lg md:text-xl text-blue-100/95 mb-9 max-w-3xl mx-auto">Integrated digital platform for standards, certification and quality management services.</p>
          <div className="flex flex-wrap justify-center gap-4">
            <Link href="/certification" className="px-6 py-3 rounded-lg font-semibold text-base primary-button">Apply for Certification</Link>
            <Link href="/dashboard" className="px-6 py-3 rounded-lg font-semibold text-base border border-white/80 hover:bg-white hover:text-[#003366] transition-colors">Officer Dashboard</Link>
          </div>
        </div>
      </section>

      <main className="max-w-7xl mx-auto px-4 py-14">
        <div className="text-center mb-8">
          <h3 className="text-2xl md:text-3xl font-bold text-slate-800">Platform Modules</h3>
          <p className="text-slate-500 mt-2">Access all BIS services through a unified, role-based interface.</p>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5">
          {modules.map((mod) => (
            <Link key={mod.href} href={mod.href} className="group surface-card overflow-hidden transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg">
              <div className={`${mod.color} p-4 text-white`}>
                <span className="inline-flex h-8 w-8 rounded-full border border-white/40 bg-white/20 text-sm font-semibold items-center justify-center">
                  {mod.title.split(' ').map(word => word[0]).join('').slice(0, 2)}
                </span>
              </div>
              <div className="p-4">
                <h4 className="font-bold text-slate-800 group-hover:text-[#003366] transition-colors">{mod.title}</h4>
                <p className="text-sm text-slate-500 mt-1 leading-relaxed">{mod.description}</p>
              </div>
            </Link>
          ))}
        </div>
      </main>

      <footer className="bg-[#003366] text-white mt-16 py-8 border-t border-blue-900/60">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <p className="text-blue-100/90 text-sm">© 2024 Bureau of Indian Standards. Ministry of Consumer Affairs, Food & Public Distribution, Government of India.</p>
        </div>
      </footer>
    </div>
  )
}
