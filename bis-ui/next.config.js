/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'standalone',
  async rewrites() {
    return [
      { source: '/api/identity/:path*', destination: 'http://localhost:8081/api/:path*' },
      { source: '/api/certifications/:path*', destination: 'http://localhost:8082/api/certifications/:path*' },
      { source: '/api/labs/:path*', destination: 'http://localhost:8083/api/labs/:path*' },
      { source: '/api/lrs/:path*', destination: 'http://localhost:8083/api/lrs/:path*' },
      { source: '/api/hr/:path*', destination: 'http://localhost:8084/api/hr/:path*' },
      { source: '/api/finance/:path*', destination: 'http://localhost:8084/api/finance/:path*' },
      { source: '/api/procurement/:path*', destination: 'http://localhost:8084/api/procurement/:path*' },
      { source: '/api/legal/:path*', destination: 'http://localhost:8084/api/legal/:path*' },
      { source: '/api/notifications/:path*', destination: 'http://localhost:8085/api/notifications/:path*' },
      { source: '/api/payment-gateway/:path*', destination: 'http://localhost:8085/api/payment-gateway/:path*' },
      { source: '/api/master/:path*', destination: 'http://localhost:8086/api/master/:path*' },
    ]
  }
}
module.exports = nextConfig
