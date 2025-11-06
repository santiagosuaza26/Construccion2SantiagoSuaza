'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { api, Patient, VitalSigns } from '@/lib/api'
import { auth } from '@/lib/auth'
import Layout from '@/components/Layout'

interface VitalSignsForm {
  patientId: string
  bloodPressure: string
  temperature: number
  pulse: number
  oxygenLevel: number
}

export default function NurseDashboard() {
  const router = useRouter()
  const [activeTab, setActiveTab] = useState<'patients' | 'vital-signs' | 'medications' | 'procedures'>('patients')
  const [patients, setPatients] = useState<Patient[]>([])
  const [selectedPatient, setSelectedPatient] = useState<string>('')
  const [patientVitalSigns, setPatientVitalSigns] = useState<VitalSigns[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showVitalSignsForm, setShowVitalSignsForm] = useState(false)
  const [showMedicationForm, setShowMedicationForm] = useState(false)
  const [showProcedureForm, setShowProcedureForm] = useState(false)

  // Vital signs form state
  const [vitalSignsForm, setVitalSignsForm] = useState<VitalSignsForm>({
    patientId: '',
    bloodPressure: '',
    temperature: 36.5,
    pulse: 70,
    oxygenLevel: 98
  })

  // Medication administration form state
  const [medicationForm, setMedicationForm] = useState({
    orderNumber: '',
    item: 1,
    administrationDetails: ''
  })

  // Procedure realization form state
  const [procedureForm, setProcedureForm] = useState({
    orderNumber: '',
    item: 1,
    realizationDetails: ''
  })

  useEffect(() => {
    if (!auth.isAuthenticated() || !auth.canRecordVitalSigns()) {
      router.push('/login')
      return
    }
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const patientsData = await api.getPatients()
      setPatients(patientsData)
    } catch (error) {
      console.error('Error loading data:', error)
      setError('Error al cargar los datos')
    } finally {
      setLoading(false)
    }
  }

  const loadPatientVitalSigns = async (patientId: string) => {
    try {
      const vitalSignsData = await api.getVitalSigns(patientId)
      setPatientVitalSigns(vitalSignsData)
    } catch (error) {
      console.error('Error loading vital signs:', error)
      setError('Error al cargar los signos vitales')
    }
  }

  const handlePatientChange = (patientId: string) => {
    setSelectedPatient(patientId)
    if (patientId) {
      loadPatientVitalSigns(patientId)
    } else {
      setPatientVitalSigns([])
    }
  }

  const handleRecordVitalSigns = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!selectedPatient) return

    try {
      await api.recordVitalSigns({
        patientId: selectedPatient,
        bloodPressure: vitalSignsForm.bloodPressure,
        temperature: vitalSignsForm.temperature,
        pulse: vitalSignsForm.pulse,
        oxygenLevel: vitalSignsForm.oxygenLevel
      })
      setShowVitalSignsForm(false)
      setVitalSignsForm({
        patientId: '',
        bloodPressure: '',
        temperature: 36.5,
        pulse: 70,
        oxygenLevel: 98
      })
      loadPatientVitalSigns(selectedPatient)
    } catch (error) {
      console.error('Error recording vital signs:', error)
      setError('Error al registrar los signos vitales')
    }
  }

  const handleMedicationAdministration = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!selectedPatient) return

    try {
      const user = auth.getUser()
      await api.recordMedicationAdministration({
        patientId: selectedPatient,
        nurseId: user?.fullName || '',
        orderNumber: medicationForm.orderNumber,
        item: medicationForm.item,
        administrationDetails: medicationForm.administrationDetails
      })
      setShowMedicationForm(false)
      setMedicationForm({
        orderNumber: '',
        item: 1,
        administrationDetails: ''
      })
      // Could reload patient orders if needed
    } catch (error) {
      console.error('Error recording medication administration:', error)
      setError('Error al registrar la administración de medicamentos')
    }
  }

  const handleProcedureRealization = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!selectedPatient) return

    try {
      const user = auth.getUser()
      await api.recordProcedureRealization({
        patientId: selectedPatient,
        nurseId: user?.fullName || '',
        orderNumber: procedureForm.orderNumber,
        item: procedureForm.item,
        realizationDetails: procedureForm.realizationDetails
      })
      setShowProcedureForm(false)
      setProcedureForm({
        orderNumber: '',
        item: 1,
        realizationDetails: ''
      })
      // Could reload patient orders if needed
    } catch (error) {
      console.error('Error recording procedure realization:', error)
      setError('Error al registrar la realización del procedimiento')
    }
  }

  if (loading) {
    return (
      <Layout title="Dashboard Enfermera">
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
        </div>
      </Layout>
    )
  }

  return (
    <Layout title="Dashboard Enfermera">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <p className="mt-2 text-gray-600">Registro de signos vitales y administración de tratamientos</p>
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

        {/* Patient Selector */}
        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-2">Seleccionar Paciente</label>
          <select
            value={selectedPatient}
            onChange={(e) => handlePatientChange(e.target.value)}
            className="block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Seleccionar paciente</option>
            {patients.map((patient) => (
              <option key={patient.identificationNumber} value={patient.identificationNumber}>
                {patient.fullName} - {patient.identificationNumber}
              </option>
            ))}
          </select>
        </div>

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
              onClick={() => setActiveTab('vital-signs')}
              className={`px-4 py-2 rounded-md font-medium ${
                activeTab === 'vital-signs'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              Signos Vitales
            </button>
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
          </nav>
        </div>

        {/* Patients Tab */}
        {activeTab === 'patients' && (
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Pacientes</h2>

            {selectedPatient ? (
              <div className="bg-white shadow overflow-hidden sm:rounded-md">
                {patients
                  .filter(patient => patient.identificationNumber === selectedPatient)
                  .map((patient) => (
                    <div key={patient.identificationNumber} className="px-6 py-4">
                      <div className="flex items-center justify-between">
                        <div>
                          <h3 className="text-lg font-medium text-gray-900">{patient.fullName}</h3>
                          <p className="text-sm text-gray-600">ID: {patient.identificationNumber}</p>
                          <p className="text-sm text-gray-600">Fecha nacimiento: {patient.dateOfBirth}</p>
                          <p className="text-sm text-gray-600">Género: {patient.gender}</p>
                          <p className="text-sm text-gray-600">Teléfono: {patient.phone}</p>
                          <p className="text-sm text-gray-600">Email: {patient.email}</p>
                          <p className="text-sm text-gray-600">Dirección: {patient.address}</p>
                        </div>
                        <div className="text-right">
                          <h4 className="text-md font-medium text-gray-900">Contacto de Emergencia</h4>
                          <p className="text-sm text-gray-600">{patient.emergencyName}</p>
                          <p className="text-sm text-gray-600">Relación: {patient.emergencyRelation}</p>
                          <p className="text-sm text-gray-600">Teléfono: {patient.emergencyPhone}</p>
                          {patient.insuranceActive && (
                            <div className="mt-4">
                              <h4 className="text-md font-medium text-green-600">Seguro Médico Activo</h4>
                              <p className="text-sm text-gray-600">Compañía: {patient.companyName}</p>
                              <p className="text-sm text-gray-600">Póliza: {patient.policyNumber}</p>
                              <p className="text-sm text-gray-600">Vigencia: {patient.validityDate}</p>
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
              </div>
            ) : (
              <p className="text-gray-500">Selecciona un paciente para ver su información</p>
            )}
          </div>
        )}

        {/* Vital Signs Tab */}
        {activeTab === 'vital-signs' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Signos Vitales</h2>
              {selectedPatient && (
                <button
                  onClick={() => setShowVitalSignsForm(true)}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
                >
                  Registrar Signos Vitales
                </button>
              )}
            </div>

            {selectedPatient ? (
              <div className="bg-white shadow overflow-hidden sm:rounded-md">
                {patientVitalSigns.length > 0 ? (
                  <ul className="divide-y divide-gray-200">
                    {patientVitalSigns.map((vitalSigns) => (
                      <li key={vitalSigns.id} className="px-6 py-4">
                        <div className="flex items-center justify-between">
                          <div>
                            <h3 className="text-lg font-medium text-gray-900">
                              Registro - {new Date(vitalSigns.recordedAt).toLocaleString()}
                            </h3>
                            <p className="text-sm text-gray-600">Registrado por: {vitalSigns.recordedBy}</p>
                          </div>
                          <div className="text-right">
                            <div className="grid grid-cols-2 gap-4">
                              <div>
                                <p className="text-sm font-medium text-gray-900">Presión Arterial</p>
                                <p className="text-lg text-blue-600">{vitalSigns.bloodPressure}</p>
                              </div>
                              <div>
                                <p className="text-sm font-medium text-gray-900">Temperatura</p>
                                <p className="text-lg text-red-600">{vitalSigns.temperature}°C</p>
                              </div>
                              <div>
                                <p className="text-sm font-medium text-gray-900">Pulso</p>
                                <p className="text-lg text-green-600">{vitalSigns.pulse} bpm</p>
                              </div>
                              <div>
                                <p className="text-sm font-medium text-gray-900">Oxígeno</p>
                                <p className="text-lg text-purple-600">{vitalSigns.oxygenLevel}%</p>
                              </div>
                            </div>
                          </div>
                        </div>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="p-6 text-gray-500">No hay registros de signos vitales para este paciente</p>
                )}
              </div>
            ) : (
              <p className="text-gray-500">Selecciona un paciente para ver sus signos vitales</p>
            )}
          </div>
        )}

        {/* Medications Tab */}
        {activeTab === 'medications' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Administración de Medicamentos</h2>
              {selectedPatient && (
                <button
                  onClick={() => setShowMedicationForm(true)}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
                >
                  Registrar Administración
                </button>
              )}
            </div>

            {selectedPatient ? (
              <div className="bg-white shadow overflow-hidden sm:rounded-md">
                <div className="p-6">
                  <p className="text-gray-600 mb-4">
                    Aquí puedes registrar la administración de medicamentos según las órdenes médicas.
                  </p>
                  <p className="text-sm text-gray-500">
                    Los medicamentos disponibles dependen de las órdenes activas del paciente.
                  </p>
                </div>
              </div>
            ) : (
              <p className="text-gray-500">Selecciona un paciente para administrar medicamentos</p>
            )}
          </div>
        )}

        {/* Procedures Tab */}
        {activeTab === 'procedures' && (
          <div>
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Realización de Procedimientos</h2>
              {selectedPatient && (
                <button
                  onClick={() => setShowProcedureForm(true)}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
                >
                  Registrar Realización
                </button>
              )}
            </div>

            {selectedPatient ? (
              <div className="bg-white shadow overflow-hidden sm:rounded-md">
                <div className="p-6">
                  <p className="text-gray-600 mb-4">
                    Aquí puedes registrar la realización de procedimientos según las órdenes médicas.
                  </p>
                  <p className="text-sm text-gray-500">
                    Los procedimientos disponibles dependen de las órdenes activas del paciente.
                  </p>
                </div>
              </div>
            ) : (
              <p className="text-gray-500">Selecciona un paciente para realizar procedimientos</p>
            )}
          </div>
        )}

        {/* Vital Signs Modal */}
        {showVitalSignsForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Registrar Signos Vitales</h3>
                <form onSubmit={handleRecordVitalSigns} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Presión Arterial</label>
                    <input
                      type="text"
                      required
                      placeholder="ej: 120/80"
                      value={vitalSignsForm.bloodPressure}
                      onChange={(e) => setVitalSignsForm({...vitalSignsForm, bloodPressure: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Temperatura (°C)</label>
                    <input
                      type="number"
                      required
                      step="0.1"
                      min="30"
                      max="45"
                      value={vitalSignsForm.temperature}
                      onChange={(e) => setVitalSignsForm({...vitalSignsForm, temperature: parseFloat(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Pulso (bpm)</label>
                    <input
                      type="number"
                      required
                      min="40"
                      max="200"
                      value={vitalSignsForm.pulse}
                      onChange={(e) => setVitalSignsForm({...vitalSignsForm, pulse: parseInt(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Nivel de Oxígeno (%)</label>
                    <input
                      type="number"
                      required
                      min="70"
                      max="100"
                      value={vitalSignsForm.oxygenLevel}
                      onChange={(e) => setVitalSignsForm({...vitalSignsForm, oxygenLevel: parseInt(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div className="flex justify-end space-x-3 pt-4">
                    <button
                      type="button"
                      onClick={() => setShowVitalSignsForm(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                    >
                      Registrar
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}

        {/* Medication Administration Modal */}
        {showMedicationForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Administración de Medicamentos</h3>
                <form onSubmit={handleMedicationAdministration} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Número de Orden</label>
                    <input
                      type="text"
                      required
                      value={medicationForm.orderNumber}
                      onChange={(e) => setMedicationForm({...medicationForm, orderNumber: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Ítem</label>
                    <input
                      type="number"
                      required
                      min="1"
                      value={medicationForm.item}
                      onChange={(e) => setMedicationForm({...medicationForm, item: parseInt(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Detalles de Administración</label>
                    <textarea
                      required
                      value={medicationForm.administrationDetails}
                      onChange={(e) => setMedicationForm({...medicationForm, administrationDetails: e.target.value})}
                      rows={4}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

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
                      Registrar
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}

        {/* Procedure Realization Modal */}
        {showProcedureForm && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Realización de Procedimientos</h3>
                <form onSubmit={handleProcedureRealization} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Número de Orden</label>
                    <input
                      type="text"
                      required
                      value={procedureForm.orderNumber}
                      onChange={(e) => setProcedureForm({...procedureForm, orderNumber: e.target.value})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Ítem</label>
                    <input
                      type="number"
                      required
                      min="1"
                      value={procedureForm.item}
                      onChange={(e) => setProcedureForm({...procedureForm, item: parseInt(e.target.value)})}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Detalles de Realización</label>
                    <textarea
                      required
                      value={procedureForm.realizationDetails}
                      onChange={(e) => setProcedureForm({...procedureForm, realizationDetails: e.target.value})}
                      rows={4}
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>

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
                      Registrar
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