'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { api, Patient } from '@/lib/api'
import { auth } from '@/lib/auth'
import Layout from '@/components/Layout'

interface Appointment {
  id: string
  patientId: string
  patientName: string
  adminId: string
  adminName: string
  doctorId: string
  doctorName: string
  dateTime: string
  reason: string
  status: string
}

export default function AdminDashboard() {
  const router = useRouter()
  const [activeTab, setActiveTab] = useState<'patients' | 'appointments' | 'billing'>('patients')
  const [patients, setPatients] = useState<Patient[]>([])
  const [appointments, setAppointments] = useState<Appointment[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showPatientForm, setShowPatientForm] = useState(false)
  const [showAppointmentForm, setShowAppointmentForm] = useState(false)

  // Patient form state
  const [patientForm, setPatientForm] = useState({
    identificationNumber: '',
    fullName: '',
    dateOfBirth: '',
    gender: '',
    address: '',
    phone: '',
    email: '',
    emergencyName: '',
    emergencyRelation: '',
    emergencyPhone: '',
    companyName: '',
    policyNumber: '',
    insuranceActive: false,
    validityDate: ''
  })

  // Appointment form state
  const [appointmentForm, setAppointmentForm] = useState({
    patientId: '',
    doctorId: '',
    dateTime: '',
    reason: ''
  })

  useEffect(() => {
    if (!auth.isAuthenticated() || !auth.canRegisterPatients()) {
      router.push('/login')
      return
    }
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const [patientsData] = await Promise.all([
        api.getPatients()
      ])
      setPatients(patientsData)
    } catch (error) {
      console.error('Error loading data:', error)
      setError('Error al cargar los datos')
    } finally {
      setLoading(false)
    }
  }

  const handleCreatePatient = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await api.createPatient(patientForm)
      setShowPatientForm(false)
      setPatientForm({
        identificationNumber: '',
        fullName: '',
        dateOfBirth: '',
        gender: '',
        address: '',
        phone: '',
        email: '',
        emergencyName: '',
        emergencyRelation: '',
        emergencyPhone: '',
        companyName: '',
        policyNumber: '',
        insuranceActive: false,
        validityDate: ''
      })
      loadData()
    } catch (error) {
      console.error('Error creating patient:', error)
      setError('Error al crear el paciente')
    }
  }

  const handleScheduleAppointment = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const user = auth.getUser()
      await fetch('http://localhost:8080/api/appointments', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({
          patientId: appointmentForm.patientId,
          adminId: user?.fullName,
          doctorId: appointmentForm.doctorId,
          dateTime: appointmentForm.dateTime,
          reason: appointmentForm.reason
        })
      })
      setShowAppointmentForm(false)
      setAppointmentForm({
        patientId: '',
        doctorId: '',
        dateTime: '',
        reason: ''
      })
      // Reload appointments if needed
    } catch (error) {
      console.error('Error scheduling appointment:', error)
      setError('Error al programar la cita')
    }
  }

  const handlePrintInvoice = async (orderNumber: string) => {
    try {
      const response = await api.printInvoice(orderNumber)
      const url = window.URL.createObjectURL(response)
      const a = document.createElement('a')
      a.href = url
      a.download = `factura_${orderNumber}.txt`
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
    } catch (error) {
      console.error('Error printing invoice:', error)
      setError('Error al imprimir la factura')
    }
  }

  if (loading) {
    return (
      <Layout title="Dashboard Personal Administrativo">
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
        </div>
      </Layout>
    )
  }

  return (
    <Layout title="Dashboard Personal Administrativo">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <p className="mt-2 text-gray-600">Gestión de pacientes, citas y facturación</p>
        </div>

        {error && (
          <div className="mb-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
            <button
              onClick={() => setError('')}
              className="float-right font-bold"
            >
              ×
            </button>
          </div>
        )}

        {/* Tabs */}
        <div className="mb-6">
          <nav className="flex space-x-4">
            <button
              onClick={() => setActiveTab('patients')}
              className={`px-4 py-2 rounded-md font-medium ${
                activeTab === 'patients'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Pacientes
            </button>
            <button
              onClick={() => setActiveTab('appointments')}
              className={`px-4 py-2 rounded-md font-medium ${
                activeTab === 'appointments'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Citas
            </button>
            <button
              onClick={() => setActiveTab('billing')}
              className={`px-4 py-2 rounded-md font-medium ${
                activeTab === 'billing'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Facturación
            </button>
          </nav>
        </div>

        {/* Patients Tab */}
        {activeTab === 'patients' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Pacientes</h2>
              <button
                onClick={() => setShowPatientForm(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                Registrar Paciente
              </button>
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-md">
              <ul className="divide-y divide-gray-200">
                {patients.map((patient) => (
                  <li key={patient.identificationNumber} className="px-6 py-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <h3 className="text-lg font-medium text-gray-900">{patient.fullName}</h3>
                        <p className="text-sm text-gray-600">ID: {patient.identificationNumber}</p>
                        <p className="text-sm text-gray-600">Teléfono: {patient.phone}</p>
                        <p className="text-sm text-gray-600">Email: {patient.email}</p>
                      </div>
                      <div className="text-right">
                        <p className="text-sm text-gray-600">Fecha nacimiento: {patient.dateOfBirth}</p>
                        <p className="text-sm text-gray-600">Género: {patient.gender}</p>
                        {patient.insuranceActive && (
                          <p className="text-sm text-green-600">Seguro activo</p>
                        )}
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        )}

        {/* Appointments Tab */}
        {activeTab === 'appointments' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Citas</h2>
              <button
                onClick={() => setShowAppointmentForm(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                Programar Cita
              </button>
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-md">
              <p className="p-6 text-gray-500">Funcionalidad de citas próximamente disponible</p>
            </div>
          </div>
        )}

        {/* Billing Tab */}
        {activeTab === 'billing' && (
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Facturación</h2>

            <div className="bg-white shadow overflow-hidden sm:rounded-md">
              <div className="p-6">
                <p className="text-gray-600 mb-4">
                  Para imprimir una factura, ingrese el número de orden:
                </p>
                <div className="flex gap-2">
                  <input
                    type="text"
                    placeholder="Número de orden"
                    className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <button
                    onClick={() => handlePrintInvoice('')}
                    className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
                  >
                    Imprimir Factura
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Patient Registration Modal */}
        {showPatientForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Registrar Nuevo Paciente</h3>
                <form onSubmit={handleCreatePatient} className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Número de Identificación</label>
                      <input
                        type="text"
                        required
                        value={patientForm.identificationNumber}
                        onChange={(e) => setPatientForm({...patientForm, identificationNumber: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Nombre Completo</label>
                      <input
                        type="text"
                        required
                        value={patientForm.fullName}
                        onChange={(e) => setPatientForm({...patientForm, fullName: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Fecha de Nacimiento (DD/MM/YYYY)</label>
                      <input
                        type="text"
                        required
                        value={patientForm.dateOfBirth}
                        onChange={(e) => setPatientForm({...patientForm, dateOfBirth: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Género</label>
                      <select
                        required
                        value={patientForm.gender}
                        onChange={(e) => setPatientForm({...patientForm, gender: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      >
                        <option value="">Seleccionar</option>
                        <option value="MASCULINO">Masculino</option>
                        <option value="FEMENINO">Femenino</option>
                        <option value="OTRO">Otro</option>
                      </select>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Dirección</label>
                    <input
                      type="text"
                      required
                      maxLength={30}
                      value={patientForm.address}
                      onChange={(e) => setPatientForm({...patientForm, address: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Teléfono</label>
                      <input
                        type="text"
                        required
                        value={patientForm.phone}
                        onChange={(e) => setPatientForm({...patientForm, phone: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Email</label>
                      <input
                        type="email"
                        required
                        value={patientForm.email}
                        onChange={(e) => setPatientForm({...patientForm, email: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                  </div>

                  <div className="border-t pt-4">
                    <h4 className="text-md font-medium text-gray-900 mb-2">Contacto de Emergencia</h4>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Nombre</label>
                        <input
                          type="text"
                          required
                          value={patientForm.emergencyName}
                          onChange={(e) => setPatientForm({...patientForm, emergencyName: e.target.value})}
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Relación</label>
                        <input
                          type="text"
                          required
                          value={patientForm.emergencyRelation}
                          onChange={(e) => setPatientForm({...patientForm, emergencyRelation: e.target.value})}
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Teléfono de Emergencia</label>
                      <input
                        type="text"
                        required
                        value={patientForm.emergencyPhone}
                        onChange={(e) => setPatientForm({...patientForm, emergencyPhone: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                  </div>

                  <div className="border-t pt-4">
                    <h4 className="text-md font-medium text-gray-900 mb-2">Información de Seguro Médico</h4>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Compañía de Seguros</label>
                        <input
                          type="text"
                          value={patientForm.companyName}
                          onChange={(e) => setPatientForm({...patientForm, companyName: e.target.value})}
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Número de Póliza</label>
                        <input
                          type="text"
                          value={patientForm.policyNumber}
                          onChange={(e) => setPatientForm({...patientForm, policyNumber: e.target.value})}
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Vigencia (DD/MM/YYYY)</label>
                        <input
                          type="text"
                          value={patientForm.validityDate}
                          onChange={(e) => setPatientForm({...patientForm, validityDate: e.target.value})}
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          checked={patientForm.insuranceActive}
                          onChange={(e) => setPatientForm({...patientForm, insuranceActive: e.target.checked})}
                          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        />
                        <label className="ml-2 block text-sm text-gray-900">Seguro activo</label>
                      </div>
                    </div>
                  </div>

                  <div className="flex justify-end space-x-3 pt-4">
                    <button
                      type="button"
                      onClick={() => setShowPatientForm(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                    >
                      Registrar Paciente
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}

        {/* Appointment Scheduling Modal */}
        {showAppointmentForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Programar Cita</h3>
                <form onSubmit={handleScheduleAppointment} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Paciente</label>
                    <select
                      required
                      value={appointmentForm.patientId}
                      onChange={(e) => setAppointmentForm({...appointmentForm, patientId: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="">Seleccionar paciente</option>
                      {patients.map((patient) => (
                        <option key={patient.identificationNumber} value={patient.identificationNumber}>
                          {patient.fullName} - {patient.identificationNumber}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Médico</label>
                    <input
                      type="text"
                      required
                      placeholder="ID del médico"
                      value={appointmentForm.doctorId}
                      onChange={(e) => setAppointmentForm({...appointmentForm, doctorId: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Fecha y Hora</label>
                    <input
                      type="datetime-local"
                      required
                      value={appointmentForm.dateTime}
                      onChange={(e) => setAppointmentForm({...appointmentForm, dateTime: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Motivo</label>
                    <textarea
                      required
                      value={appointmentForm.reason}
                      onChange={(e) => setAppointmentForm({...appointmentForm, reason: e.target.value})}
                      rows={3}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div className="flex justify-end space-x-3 pt-4">
                    <button
                      type="button"
                      onClick={() => setShowAppointmentForm(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                    >
                      Programar Cita
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  )
}