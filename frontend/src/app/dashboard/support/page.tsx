'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { api, Medication, Procedure, DiagnosticAid } from '@/lib/api'
import { auth } from '@/lib/auth'
import Layout from '@/components/Layout'

export default function SupportDashboard() {
  const router = useRouter()
  const [activeTab, setActiveTab] = useState<'medications' | 'procedures' | 'diagnostic-aids'>('medications')
  const [medications, setMedications] = useState<Medication[]>([])
  const [procedures, setProcedures] = useState<Procedure[]>([])
  const [diagnosticAids, setDiagnosticAids] = useState<DiagnosticAid[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showMedicationForm, setShowMedicationForm] = useState(false)
  const [showProcedureForm, setShowProcedureForm] = useState(false)
  const [showDiagnosticAidForm, setShowDiagnosticAidForm] = useState(false)

  // Medication form state
  const [medicationForm, setMedicationForm] = useState({
    id: '',
    name: '',
    cost: 0,
    requiresSpecialist: false,
    specialistType: ''
  })

  // Procedure form state
  const [procedureForm, setProcedureForm] = useState({
    id: '',
    name: '',
    cost: 0,
    requiresSpecialist: false,
    specialistType: ''
  })

  // Diagnostic aid form state
  const [diagnosticAidForm, setDiagnosticAidForm] = useState({
    id: '',
    name: '',
    cost: 0,
    requiresSpecialist: false,
    specialistType: ''
  })

  useEffect(() => {
    if (!auth.isAuthenticated() || !auth.canManageInventory()) {
      router.push('/login')
      return
    }
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const [medicationsData, proceduresData, diagnosticAidsData] = await Promise.all([
        api.getMedications(),
        api.getProcedures(),
        api.getDiagnosticAids()
      ])
      setMedications(medicationsData)
      setProcedures(proceduresData)
      setDiagnosticAids(diagnosticAidsData)
    } catch (error) {
      console.error('Error loading data:', error)
      setError('Error al cargar los datos del inventario')
    } finally {
      setLoading(false)
    }
  }

  const handleCreateMedication = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await api.createMedication(medicationForm)
      setShowMedicationForm(false)
      setMedicationForm({
        id: '',
        name: '',
        cost: 0,
        requiresSpecialist: false,
        specialistType: ''
      })
      loadData()
    } catch (error) {
      console.error('Error creating medication:', error)
      setError('Error al crear el medicamento')
    }
  }

  const handleCreateProcedure = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await api.createProcedure(procedureForm)
      setShowProcedureForm(false)
      setProcedureForm({
        id: '',
        name: '',
        cost: 0,
        requiresSpecialist: false,
        specialistType: ''
      })
      loadData()
    } catch (error) {
      console.error('Error creating procedure:', error)
      setError('Error al crear el procedimiento')
    }
  }

  const handleCreateDiagnosticAid = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await api.createDiagnosticAid(diagnosticAidForm)
      setShowDiagnosticAidForm(false)
      setDiagnosticAidForm({
        id: '',
        name: '',
        cost: 0,
        requiresSpecialist: false,
        specialistType: ''
      })
      loadData()
    } catch (error) {
      console.error('Error creating diagnostic aid:', error)
      setError('Error al crear la ayuda diagnóstica')
    }
  }

  if (loading) {
    return (
      <Layout title="Dashboard Soporte de Información">
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
        </div>
      </Layout>
    )
  }

  return (
    <Layout title="Dashboard Soporte de Información">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <p className="mt-2 text-gray-600">Gestión del inventario médico: medicamentos, procedimientos y ayudas diagnósticas</p>
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
              onClick={() => setActiveTab('medications')}
              className={`px-4 py-2 rounded-md font-medium ${
                activeTab === 'medications'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Medicamentos
            </button>
            <button
              onClick={() => setActiveTab('procedures')}
              className={`px-4 py-2 rounded-md font-medium ${
                activeTab === 'procedures'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Procedimientos
            </button>
            <button
              onClick={() => setActiveTab('diagnostic-aids')}
              className={`px-4 py-2 rounded-md font-medium ${
                activeTab === 'diagnostic-aids'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Ayudas Diagnósticas
            </button>
          </nav>
        </div>

        {/* Medications Tab */}
        {activeTab === 'medications' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Medicamentos</h2>
              <button
                onClick={() => setShowMedicationForm(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                Agregar Medicamento
              </button>
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-md">
              <ul className="divide-y divide-gray-200">
                {medications.map((medication) => (
                  <li key={medication.id} className="px-6 py-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <h3 className="text-lg font-medium text-gray-900">{medication.name}</h3>
                        <p className="text-sm text-gray-600">ID: {medication.id}</p>
                        <p className="text-sm text-gray-600">Costo: ${medication.cost.toLocaleString()}</p>
                        {medication.requiresSpecialist && (
                          <p className="text-sm text-blue-600">Requiere especialista: {medication.specialistType}</p>
                        )}
                      </div>
                      <div className="flex space-x-2">
                        <button className="bg-yellow-600 text-white px-3 py-1 rounded-md hover:bg-yellow-700 text-sm">
                          Editar
                        </button>
                        <button className="bg-red-600 text-white px-3 py-1 rounded-md hover:bg-red-700 text-sm">
                          Eliminar
                        </button>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        )}

        {/* Procedures Tab */}
        {activeTab === 'procedures' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Procedimientos</h2>
              <button
                onClick={() => setShowProcedureForm(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                Agregar Procedimiento
              </button>
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-md">
              <ul className="divide-y divide-gray-200">
                {procedures.map((procedure) => (
                  <li key={procedure.id} className="px-6 py-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <h3 className="text-lg font-medium text-gray-900">{procedure.name}</h3>
                        <p className="text-sm text-gray-600">ID: {procedure.id}</p>
                        <p className="text-sm text-gray-600">Costo: ${procedure.cost.toLocaleString()}</p>
                        {procedure.requiresSpecialist && (
                          <p className="text-sm text-blue-600">Requiere especialista: {procedure.specialistType}</p>
                        )}
                      </div>
                      <div className="flex space-x-2">
                        <button className="bg-yellow-600 text-white px-3 py-1 rounded-md hover:bg-yellow-700 text-sm">
                          Editar
                        </button>
                        <button className="bg-red-600 text-white px-3 py-1 rounded-md hover:bg-red-700 text-sm">
                          Eliminar
                        </button>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        )}

        {/* Diagnostic Aids Tab */}
        {activeTab === 'diagnostic-aids' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Ayudas Diagnósticas</h2>
              <button
                onClick={() => setShowDiagnosticAidForm(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                Agregar Ayuda Diagnóstica
              </button>
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-md">
              <ul className="divide-y divide-gray-200">
                {diagnosticAids.map((aid) => (
                  <li key={aid.id} className="px-6 py-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <h3 className="text-lg font-medium text-gray-900">{aid.name}</h3>
                        <p className="text-sm text-gray-600">ID: {aid.id}</p>
                        <p className="text-sm text-gray-600">Costo: ${aid.cost.toLocaleString()}</p>
                        {aid.requiresSpecialist && (
                          <p className="text-sm text-blue-600">Requiere especialista: {aid.specialistType}</p>
                        )}
                      </div>
                      <div className="flex space-x-2">
                        <button className="bg-yellow-600 text-white px-3 py-1 rounded-md hover:bg-yellow-700 text-sm">
                          Editar
                        </button>
                        <button className="bg-red-600 text-white px-3 py-1 rounded-md hover:bg-red-700 text-sm">
                          Eliminar
                        </button>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        )}

        {/* Medication Form Modal */}
        {showMedicationForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Agregar Nuevo Medicamento</h3>
                <form onSubmit={handleCreateMedication} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">ID del Medicamento</label>
                    <input
                      type="text"
                      required
                      value={medicationForm.id}
                      onChange={(e) => setMedicationForm({...medicationForm, id: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="ej: MED001"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Nombre del Medicamento</label>
                    <input
                      type="text"
                      required
                      value={medicationForm.name}
                      onChange={(e) => setMedicationForm({...medicationForm, name: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Costo ($)</label>
                    <input
                      type="number"
                      required
                      min="0"
                      step="0.01"
                      value={medicationForm.cost}
                      onChange={(e) => setMedicationForm({...medicationForm, cost: parseFloat(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="flex items-center">
                      <input
                        type="checkbox"
                        checked={medicationForm.requiresSpecialist}
                        onChange={(e) => setMedicationForm({...medicationForm, requiresSpecialist: e.target.checked})}
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <span className="ml-2 text-sm text-gray-900">Requiere especialista</span>
                    </label>
                  </div>

                  {medicationForm.requiresSpecialist && (
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Tipo de Especialista</label>
                      <input
                        type="text"
                        required
                        value={medicationForm.specialistType}
                        onChange={(e) => setMedicationForm({...medicationForm, specialistType: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="ej: Cardiologo, Pediatra"
                      />
                    </div>
                  )}

                  <div className="flex justify-end space-x-3 pt-4">
                    <button
                      type="button"
                      onClick={() => setShowMedicationForm(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                    >
                      Agregar Medicamento
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}

        {/* Procedure Form Modal */}
        {showProcedureForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Agregar Nuevo Procedimiento</h3>
                <form onSubmit={handleCreateProcedure} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">ID del Procedimiento</label>
                    <input
                      type="text"
                      required
                      value={procedureForm.id}
                      onChange={(e) => setProcedureForm({...procedureForm, id: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="ej: PROC001"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Nombre del Procedimiento</label>
                    <input
                      type="text"
                      required
                      value={procedureForm.name}
                      onChange={(e) => setProcedureForm({...procedureForm, name: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Costo ($)</label>
                    <input
                      type="number"
                      required
                      min="0"
                      step="0.01"
                      value={procedureForm.cost}
                      onChange={(e) => setProcedureForm({...procedureForm, cost: parseFloat(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="flex items-center">
                      <input
                        type="checkbox"
                        checked={procedureForm.requiresSpecialist}
                        onChange={(e) => setProcedureForm({...procedureForm, requiresSpecialist: e.target.checked})}
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <span className="ml-2 text-sm text-gray-900">Requiere especialista</span>
                    </label>
                  </div>

                  {procedureForm.requiresSpecialist && (
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Tipo de Especialista</label>
                      <input
                        type="text"
                        required
                        value={procedureForm.specialistType}
                        onChange={(e) => setProcedureForm({...procedureForm, specialistType: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="ej: Cirujano, Radiologo"
                      />
                    </div>
                  )}

                  <div className="flex justify-end space-x-3 pt-4">
                    <button
                      type="button"
                      onClick={() => setShowProcedureForm(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                    >
                      Agregar Procedimiento
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}

        {/* Diagnostic Aid Form Modal */}
        {showDiagnosticAidForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Agregar Nueva Ayuda Diagnóstica</h3>
                <form onSubmit={handleCreateDiagnosticAid} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">ID de la Ayuda Diagnóstica</label>
                    <input
                      type="text"
                      required
                      value={diagnosticAidForm.id}
                      onChange={(e) => setDiagnosticAidForm({...diagnosticAidForm, id: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="ej: DIAG001"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Nombre de la Ayuda Diagnóstica</label>
                    <input
                      type="text"
                      required
                      value={diagnosticAidForm.name}
                      onChange={(e) => setDiagnosticAidForm({...diagnosticAidForm, name: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Costo ($)</label>
                    <input
                      type="number"
                      required
                      min="0"
                      step="0.01"
                      value={diagnosticAidForm.cost}
                      onChange={(e) => setDiagnosticAidForm({...diagnosticAidForm, cost: parseFloat(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="flex items-center">
                      <input
                        type="checkbox"
                        checked={diagnosticAidForm.requiresSpecialist}
                        onChange={(e) => setDiagnosticAidForm({...diagnosticAidForm, requiresSpecialist: e.target.checked})}
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <span className="ml-2 text-sm text-gray-900">Requiere especialista</span>
                    </label>
                  </div>

                  {diagnosticAidForm.requiresSpecialist && (
                    <div>
                      <label className="block text-sm font-medium text-gray-700">Tipo de Especialista</label>
                      <input
                        type="text"
                        required
                        value={diagnosticAidForm.specialistType}
                        onChange={(e) => setDiagnosticAidForm({...diagnosticAidForm, specialistType: e.target.value})}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="ej: Radiologo, Patologo"
                      />
                    </div>
                  )}

                  <div className="flex justify-end space-x-3 pt-4">
                    <button
                      type="button"
                      onClick={() => setShowDiagnosticAidForm(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                    >
                      Agregar Ayuda Diagnóstica
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