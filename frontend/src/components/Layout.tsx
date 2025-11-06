'use client'

import { useRouter } from 'next/navigation'
import { auth } from '@/lib/auth'
import { api } from '@/lib/api'

interface LayoutProps {
  children: React.ReactNode
  title: string
}

export default function Layout({ children, title }: LayoutProps) {
  const router = useRouter()
  const user = auth.getUser()

  const handleLogout = async () => {
    try {
      await api.logout()
      router.push('/')
    } catch (error) {
      // Clear local storage anyway
      auth.clearAuth()
      router.push('/')
    }
  }

  const getDashboardPath = () => {
    const role = auth.getRole()
    switch (role) {
      case 'RECURSOS_HUMANOS':
        return '/dashboard/hr'
      case 'PERSONAL_ADMINISTRATIVO':
        return '/dashboard/admin'
      case 'SOPORTE_INFORMACION':
        return '/dashboard/support'
      case 'ENFERMERA':
        return '/dashboard/nurse'
      case 'MEDICO':
        return '/dashboard/doctor'
      default:
        return '/dashboard'
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b" role="banner">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <h1 className="text-xl font-semibold text-gray-900" id="main-title">
                Sistema de Gestión Clínica
              </h1>
            </div>

            <nav className="flex items-center space-x-4" role="navigation" aria-label="Navegación principal">
              {user && (
                <div className="flex items-center space-x-4">
                  <span className="text-sm text-gray-700" aria-label={`Usuario: ${user.fullName}, Rol: ${user.role}`}>
                    {user.fullName} ({user.role})
                  </span>
                  <button
                    onClick={() => router.push(getDashboardPath())}
                    className="text-blue-600 hover:text-blue-800 focus:text-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 text-sm font-medium rounded px-2 py-1"
                    aria-label="Ir al dashboard principal"
                  >
                    Dashboard
                  </button>
                  <button
                    onClick={handleLogout}
                    className="bg-red-600 hover:bg-red-700 focus:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 text-white text-sm px-3 py-1 rounded-md transition duration-200"
                    aria-label="Cerrar sesión"
                  >
                    Cerrar Sesión
                  </button>
                </div>
              )}
            </nav>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8" role="main" aria-labelledby="main-title">
        <div className="mb-6">
          <h2 className="text-2xl font-bold text-gray-900">{title}</h2>
        </div>
        {children}
      </main>
    </div>
  )
}