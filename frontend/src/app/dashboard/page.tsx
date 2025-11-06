'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { auth, User } from '@/lib/auth'

export default function DashboardPage() {
  const router = useRouter()
  const [user] = useState<User | null>(auth.getUser())

  useEffect(() => {
    if (!user) {
      router.push('/login')
      return
    }

    // Redirect to role-specific dashboard
    const role = auth.getRole()
    switch (role) {
      case 'RECURSOS_HUMANOS':
        router.push('/dashboard/hr')
        break
      case 'PERSONAL_ADMINISTRATIVO':
        router.push('/dashboard/admin')
        break
      case 'SOPORTE_INFORMACION':
        router.push('/dashboard/support')
        break
      case 'ENFERMERA':
        router.push('/dashboard/nurse')
        break
      case 'MEDICO':
        router.push('/dashboard/doctor')
        break
      default:
        // Stay on generic dashboard
        break
    }
  }, [router, user])

  if (!user) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Cargando...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Bienvenido, {user.fullName}
        </h2>
        <p className="text-gray-600">
          Rol: {user.role}
        </p>
        <p className="text-gray-600 mt-2">
          Redirigiendo a tu dashboard específico...
        </p>
      </div>
    </div>
  )
}