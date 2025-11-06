'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const router = useRouter()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      console.log('Attempting login with username:', username)
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      })

      console.log('Login response status:', response.status)

      if (response.ok) {
        const data = await response.json()
        console.log('Login successful, received data:', data)
        // Store token and user info
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify({
          fullName: data.fullName,
          role: data.role
        }))

        // Redirect based on role
        switch (data.role) {
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
            router.push('/dashboard')
        }
      } else {
        const errorText = await response.text()
        console.error('Login failed with status:', response.status, 'Error:', errorText)
        setError('Credenciales inválidas')
      }
    } catch (err) {
      console.error('Login error:', err)
      setError('Error de conexión. Verifique que el servidor esté ejecutándose.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4" role="main">
      <div className="max-w-md w-full bg-white rounded-lg shadow-lg p-8" role="dialog" aria-labelledby="login-title">
        <div className="mb-8 text-center">
          <h1 id="login-title" className="text-2xl font-bold text-gray-900 mb-2">
            Iniciar Sesión
          </h1>
          <p className="text-gray-600">
            Ingrese sus credenciales para acceder al sistema
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6" role="form" aria-label="Formulario de inicio de sesión">
          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
              Nombre de usuario
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
              aria-describedby="username-help"
              autoComplete="username"
            />
            <span id="username-help" className="sr-only">Ingrese su nombre de usuario</span>
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
              Contraseña
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
              aria-describedby="password-help"
              autoComplete="current-password"
            />
            <span id="password-help" className="sr-only">Ingrese su contraseña</span>
          </div>

          {error && (
            <div className="text-red-600 text-sm text-center" role="alert" aria-live="polite">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 focus:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 text-white font-medium py-3 px-6 rounded-lg transition duration-200"
            aria-describedby={loading ? "loading-status" : undefined}
          >
            {loading ? 'Iniciando sesión...' : 'Iniciar Sesión'}
          </button>
          {loading && <span id="loading-status" className="sr-only">Procesando inicio de sesión</span>}
        </form>

        <div className="mt-6 text-center">
          <button
            onClick={() => router.push('/')}
            className="text-blue-600 hover:text-blue-800 focus:text-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 text-sm rounded px-2 py-1"
            aria-label="Volver a la página de inicio"
          >
            ← Volver al inicio
          </button>
        </div>
      </div>
    </div>
  )
}