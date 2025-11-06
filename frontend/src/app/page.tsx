import Link from 'next/link'

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="max-w-md w-full bg-white rounded-lg shadow-lg p-8 text-center">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
            Sistema de Gestión Clínica
          </h1>
          <p className="text-gray-600">
            Bienvenido al sistema de gestión de información médica
          </p>
        </div>

        <div className="space-y-4">
          <Link
            href="/login"
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-6 rounded-lg transition duration-200 inline-block"
          >
            Iniciar Sesión
          </Link>

          <div className="text-sm text-gray-500">
            <p>Acceso restringido a personal autorizado</p>
          </div>
        </div>
      </div>
    </div>
  )
}
