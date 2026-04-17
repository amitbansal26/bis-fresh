import type { Config } from 'tailwindcss'
const config: Config = {
  content: ['./src/pages/**/*.{js,ts,jsx,tsx,mdx}', './src/components/**/*.{js,ts,jsx,tsx,mdx}', './src/app/**/*.{js,ts,jsx,tsx,mdx}'],
  theme: {
    extend: {
      colors: {
        'bis-blue': '#003366',
        'bis-orange': '#FF6600',
        'bis-gold': '#FFD700',
      }
    }
  },
  plugins: []
}
export default config
