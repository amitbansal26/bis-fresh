import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'BIS Manakonline',
  description: 'Bureau of Indian Standards - Manakonline Platform',
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className="min-h-screen antialiased">
        {children}
      </body>
    </html>
  )
}
