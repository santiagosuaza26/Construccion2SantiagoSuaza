'use client'

import { useState, useEffect } from 'react'
import { api, User } from '@/lib/api'
import { auth } from '@/lib/auth'

export default function HRDashboard() {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [formData, setFormData] = useState({
    fullName: '',
    identificationNumber: '',
    email: '',
    phone: '',
    dateOfBirth: '',
    address: '',
    role: 'MEDICO',
    username: '',
    password: ''
  })

  useEffect(() => {
    if (!auth.canManageUsers()) {
      return
    }
    loadUsers()
  }, [])

  const loadUsers = async () => {
    try {
      const userList = await api.getUsers()
      setUsers(userList)
    } catch (error) {
      console.error('Error loading users:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleCreateUser = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await api.createUser(formData)
      setShowCreateForm(false)
      setFormData({
        fullName: '',
        identificationNumber: '',
        email: '',
        phone: '',
        dateOfBirth: '',
        address: '',
        role: 'MEDICO',
        username: '',
        password: ''
      })
      loadUsers()
    } catch (error) {
      console.error('Error creating user:', error)
      alert('Error al crear usuario')
    }
  }

  const handleDeleteUser = async (id: string) => {
    if (confirm('¿Está seguro de que desea eliminar este usuario?')) {
      try {
        await api.deleteUser(id)
        loadUsers()
      } catch (error) {
        console.error('Error deleting user:', error)
        alert('Error al eliminar usuario')
      }
    }
  }

  if (!auth.canManageUsers()) {
    return (
      <div className="bg-red-50 border border-red-200 rounded-md p-4">
        <p className="text-red-800">No tiene permisos para gestionar usuarios.</p>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Dashboard de Recursos Humanos</h1>
        <button
          onClick={() => setShowCreateForm(true)}
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition duration-200"
        >
          Crear Usuario
        </button>
      </div>

      {showCreateForm && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold mb-4">Crear Nuevo Usuario</h2>
          <form onSubmit={handleCreateUser} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Nombre Completo
                </label>
                <input
                  type="text"
                  value={formData.fullName}
                  onChange={(e) => setFormData({...formData, fullName: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Número de Cédula
                </label>
                <input
                  type="text"
                  value={formData.identificationNumber}
                  onChange={(e) => setFormData({...formData, identificationNumber: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Correo Electrónico
                </label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({...formData, email: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Teléfono
                </label>
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({...formData, phone: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Fecha de Nacimiento (DD/MM/YYYY)
                </label>
                <input
                  type="text"
                  value={formData.dateOfBirth}
                  onChange={(e) => setFormData({...formData, dateOfBirth: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Dirección
                </label>
                <input
                  type="text"
                  value={formData.address}
                  onChange={(e) => setFormData({...formData, address: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Rol
                </label>
                <select
                  value={formData.role}
                  onChange={(e) => setFormData({...formData, role: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="MEDICO">Médico</option>
                  <option value="ENFERMERA">Enfermera</option>
                  <option value="PERSONAL_ADMINISTRATIVO">Personal Administrativo</option>
                  <option value="RECURSOS_HUMANOS">Recursos Humanos</option>
                  <option value="SOPORTE_INFORMACION">Soporte de Información</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Nombre de Usuario
                </label>
                <input
                  type="text"
                  value={formData.username}
                  onChange={(e) => setFormData({...formData, username: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Contraseña
                </label>
                <input
                  type="password"
                  value={formData.password}
                  onChange={(e) => setFormData({...formData, password: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
            </div>
            <div className="flex space-x-4">
              <button
                type="submit"
                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md transition duration-200"
              >
                Crear Usuario
              </button>
              <button
                type="button"
                onClick={() => setShowCreateForm(false)}
                className="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md transition duration-200"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">Lista de Usuarios</h2>
        </div>
        {loading ? (
          <div className="p-6 text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-2 text-gray-600">Cargando usuarios...</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Nombre
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Usuario
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Rol
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {users.map((user) => (
                  <tr key={user.identificationNumber}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {user.fullName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {user.username}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {user.role}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {user.email}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <button
                        onClick={() => handleDeleteUser(user.identificationNumber)}
                        className="text-red-600 hover:text-red-900"
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}