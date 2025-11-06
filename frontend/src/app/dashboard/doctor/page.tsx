'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { api, Patient, Medication, Procedure, DiagnosticAid } from '@/lib/api'
import { auth } from '@/lib/auth'
import Layout from '@/components/Layout'
import Modal from '@/components/Modal'
import LoadingSpinner from '@/components/LoadingSpinner'
import ErrorAlert from '@/components/ErrorAlert'

interface MedicalRecord {
  patientId: string
  records: any[]
  medications: any[]
  procedures: any[]
  diagnosticAids: any[]
}

interface Order {
  orderNumber: string
  patientIdentificationNumber: string
  doctorIdentificationNumber: string
  date: string
  diagnosis: string
  medications: string[]
  procedures: string[]
  diagnosticAids: string[]
}

export default function DoctorDashboard() {
  const router = useRouter()
  const [activeTab, setActiveTab] = useState<'records' | 'orders' | 'billing' | 'history'>('records')
  const [patients, setPatients] = useState<Patient[]>([])
  const [medications, setMedications] = useState<Medication[]>([])
  const [procedures, setProcedures] = useState<Procedure[]>([])
  const [diagnosticAids, setDiagnosticAids] = useState<DiagnosticAid[]>([])
  const [selectedPatient, setSelectedPatient] = useState<string>('')
  const [medicalRecord, setMedicalRecord] = useState<MedicalRecord | null>(null)
  const [patientOrders, setPatientOrders] = useState<Order[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showRecordForm, setShowRecordForm] = useState(false)
  const [showOrderForm, setShowOrderForm] = useState(false)
  const [showBillingForm, setShowBillingForm] = useState(false)

  // Medical record form state
  const [recordForm, setRecordForm] = useState({
    reason: '',
    symptoms: '',
    diagnosis: ''
  })

  // Order form state
  const [orderForm, setOrderForm] = useState({
    diagnosis: '',
    medications: [] as any[],
    procedures: [] as any[],
    diagnosticAids: [] as any[]
  })

  useEffect(() => {
    if (!auth.isAuthenticated() || !auth.canManageMedicalRecords()) {
      router.push('/login')
      return
    }
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const [patientsData, medicationsData, proceduresData, diagnosticAidsData] = await Promise.all([
        api.getPatients(),
        api.getMedications(),
        api.getProcedures(),
        api.getDiagnosticAids()
      ])
      setPatients(patientsData)
      setMedications(medicationsData)
      setProcedures(proceduresData)
      setDiagnosticAids(diagnosticAidsData)
    } catch (error) {
      console.error('Error loading data:', error)
      setError('Error al cargar los datos')
    } finally {
      setLoading(false)
    }
  }

  const loadPatientData = async (patientId: string) => {
    try {
      const [recordData, ordersData] = await Promise.all([
        api.getMedicalRecord(patientId),
        api.getPatientOrders(patientId)
      ])
      setMedicalRecord(recordData)
      setPatientOrders(ordersData)
    } catch (error) {
      console.error('Error loading patient data:', error)
      setError('Error al cargar los datos del paciente')
    }
  }

  const handlePatientChange = (patientId: string) => {
    setSelectedPatient(patientId)
    if (patientId) {
      loadPatientData(patientId)
    } else {
      setMedicalRecord(null)
      setPatientOrders([])
    }
  }

  const handleCreateRecord = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!selectedPatient) return

    try {
      const user = auth.getUser()
      await api.addMedicalRecord(selectedPatient, {
        doctorId: user?.fullName || '',
        reason: recordForm.reason,
        symptoms: recordForm.symptoms,
        diagnosis: recordForm.diagnosis
      })
      setShowRecordForm(false)
      setRecordForm({ reason: '', symptoms: '', diagnosis: '' })
      loadPatientData(selectedPatient)
    } catch (error) {
      console.error('Error creating record:', error)
      setError('Error al crear el registro médico')
    }
  }

  const handleCreateOrder = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!selectedPatient) return

    try {
      const user = auth.getUser()
      const orderData = {
        patientId: selectedPatient,
        doctorId: user?.fullName || '',
        diagnosis: orderForm.diagnosis,
        medications: orderForm.medications,
        procedures: orderForm.procedures,
        diagnosticAids: orderForm.diagnosticAids
      }

      // Determine order type based on content
      if (orderForm.diagnosticAids.length > 0) {
        // Diagnostic aid order
        await fetch('http://localhost:8080/api/medical/orders/diagnostic-aids', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(orderData)
        })
      } else if (orderForm.medications.length > 0 && orderForm.procedures.length > 0) {
        // Combined medication and procedure order
        await fetch('http://localhost:8080/api/medical/orders/medications', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(orderData)
        })
        await fetch('http://localhost:8080/api/medical/orders/procedures', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(orderData)
        })
      } else if (orderForm.medications.length > 0) {
        // Medication order
        await fetch('http://localhost:8080/api/medical/orders/medications', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(orderData)
        })
      } else if (orderForm.procedures.length > 0) {
        // Procedure order
        await fetch('http://localhost:8080/api/medical/orders/procedures', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(orderData)
        })
      }

      setShowOrderForm(false)
      setOrderForm({ diagnosis: '', medications: [], procedures: [], diagnosticAids: [] })
      loadPatientData(selectedPatient)
    } catch (error) {
      console.error('Error creating order:', error)
      setError('Error al crear la orden médica')
    }
  }

  const addMedicationToOrder = () => {
    setOrderForm({
      ...orderForm,
      medications: [...orderForm.medications, { medicationId: '', dosage: '', duration: '', item: orderForm.medications.length + 1 }]
    })
  }

  const addProcedureToOrder = () => {
    setOrderForm({
      ...orderForm,
      procedures: [...orderForm.procedures, { procedureId: '', quantity: 1, frequency: '', requiresSpecialist: false, specialistId: '', item: orderForm.procedures.length + 1 }]
    })
  }

  const addDiagnosticAidToOrder = () => {
    setOrderForm({
      ...orderForm,
      diagnosticAids: [...orderForm.diagnosticAids, { diagnosticAidId: '', quantity: 1, requiresSpecialist: false, specialistId: '', item: orderForm.diagnosticAids.length + 1 }]
    })
  }

  if (loading) {
    return (
      <Layout title="Dashboard Médico">
        <LoadingSpinner size="lg" message="Cargando datos del dashboard..." />
      </Layout>
    )
  }

  return (
    <Layout title="Dashboard Médico">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <p className="mt-2 text-gray-600">Gestión de registros médicos y órdenes médicas</p>
        </div>

        {/* Mobile Tabs - Hidden on desktop */}
        <div className="md:hidden mb-6">
           <select
             value={activeTab}
             onChange={(e) => setActiveTab(e.target.value as 'records' | 'orders' | 'billing' | 'history')}
             className="block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
           >
             <option value="records">Registros Médicos</option>
             <option value="orders">Órdenes Médicas</option>
             <option value="billing">Facturación</option>
             <option value="history">Historial Clínico</option>
           </select>
         </div>

        {error && (
          <ErrorAlert message={error} onClose={() => setError('')} />
        )}

        {/* Patient Selector */}
        <div className="mb-6 animate-fade-in">
          <label htmlFor="patient-select" className="block text-sm font-medium text-gray-700 mb-2">
            Seleccionar Paciente
          </label>
          <select
            id="patient-select"
            value={selectedPatient}
            onChange={(e) => handlePatientChange(e.target.value)}
            className="block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            aria-describedby="patient-select-help"
          >
            <option value="">Seleccionar paciente</option>
            {patients.map((patient) => (
              <option key={patient.identificationNumber} value={patient.identificationNumber}>
                {patient.fullName} - {patient.identificationNumber}
              </option>
            ))}
          </select>
          <span id="patient-select-help" className="sr-only">Seleccione un paciente para gestionar sus registros médicos</span>
        </div>

        {/* Desktop Tabs - Hidden on mobile */}
        <div className="hidden md:block mb-6">
          <nav className="flex space-x-4" role="tablist" aria-label="Secciones del dashboard médico">
            <button
              onClick={() => setActiveTab('records')}
              className={`px-4 py-2 rounded-md font-medium focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${
                activeTab === 'records'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
              role="tab"
              aria-selected={activeTab === 'records'}
              aria-controls="records-panel"
              id="records-tab"
            >
              Registros Médicos
            </button>
            <button
              onClick={() => setActiveTab('orders')}
              className={`px-4 py-2 rounded-md font-medium focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${
                activeTab === 'orders'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
              role="tab"
              aria-selected={activeTab === 'orders'}
              aria-controls="orders-panel"
              id="orders-tab"
            >
              Órdenes Médicas
            </button>
            <button
               onClick={() => setActiveTab('billing')}
               className={`px-4 py-2 rounded-md font-medium focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${
                 activeTab === 'billing'
                   ? 'bg-blue-600 text-white'
                   : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
               }`}
               role="tab"
               aria-selected={activeTab === 'billing'}
               aria-controls="billing-panel"
               id="billing-tab"
             >
               Facturación
             </button>
            <button
               onClick={() => setActiveTab('history')}
               className={`px-4 py-2 rounded-md font-medium focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 ${
                 activeTab === 'history'
                   ? 'bg-blue-600 text-white'
                   : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
               }`}
               role="tab"
               aria-selected={activeTab === 'history'}
               aria-controls="history-panel"
               id="history-tab"
             >
               Historial Clínico
             </button>
          </nav>
        </div>

        {/* Records Tab */}
        {activeTab === 'records' && (
          <div role="tabpanel" id="records-panel" aria-labelledby="records-tab" className="animate-fade-in">
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Registros Médicos</h2>
              {selectedPatient && (
                <button
                  onClick={() => setShowRecordForm(true)}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transform hover:scale-105 transition-all duration-200"
                  aria-label="Crear nuevo registro médico"
                >
                  Nuevo Registro
                </button>
              )}
            </div>

            {selectedPatient ? (
              <div className="bg-white shadow overflow-hidden sm:rounded-md">
                {medicalRecord?.records && medicalRecord.records.length > 0 ? (
                  <ul className="divide-y divide-gray-200">
                    {medicalRecord.records.map((record: any, index: number) => (
                      <li key={index} className="px-4 sm:px-6 py-4">
                        <div className="flex flex-col sm:flex-row sm:items-center justify-between">
                          <div className="flex-1">
                            <h3 className="text-lg font-medium text-gray-900">Consulta - {record.date}</h3>
                            <p className="text-sm text-gray-600">Médico: {record.doctorId}</p>
                            <p className="text-sm text-gray-600">Motivo: {record.reason}</p>
                            <p className="text-sm text-gray-600">Síntomas: {record.symptoms}</p>
                            <p className="text-sm text-gray-600">Diagnóstico: {record.diagnosis}</p>
                          </div>
                        </div>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="p-4 sm:p-6 text-gray-500 text-center">No hay registros médicos para este paciente</p>
                )}
              </div>
            ) : (
              <p className="text-gray-500 text-center">Selecciona un paciente para ver sus registros médicos</p>
            )}
          </div>
        )}

        {/* Orders Tab */}
        {activeTab === 'orders' && (
          <div role="tabpanel" id="orders-panel" aria-labelledby="orders-tab" className="animate-fade-in">
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Órdenes Médicas</h2>
              {selectedPatient && (
                <button
                  onClick={() => setShowOrderForm(true)}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transform hover:scale-105 transition-all duration-200"
                  aria-label="Crear nueva orden médica"
                >
                  Nueva Orden
                </button>
              )}
            </div>

            {selectedPatient ? (
              <div className="bg-white shadow overflow-hidden sm:rounded-md">
                {patientOrders.length > 0 ? (
                  <ul className="divide-y divide-gray-200">
                    {patientOrders.map((order) => (
                      <li key={order.orderNumber} className="px-4 sm:px-6 py-4">
                        <div className="flex flex-col sm:flex-row sm:items-center justify-between">
                          <div className="flex-1">
                            <h3 className="text-lg font-medium text-gray-900">Orden #{order.orderNumber}</h3>
                            <p className="text-sm text-gray-600">Fecha: {order.date}</p>
                            <p className="text-sm text-gray-600">Diagnóstico: {order.diagnosis}</p>
                            <div className="text-sm text-gray-600 mt-2">
                              <p className="mb-1"><strong>Medicamentos:</strong> {order.medications.join(', ') || 'Ninguno'}</p>
                              <p className="mb-1"><strong>Procedimientos:</strong> {order.procedures.join(', ') || 'Ninguno'}</p>
                              <p><strong>Ayudas diagnósticas:</strong> {order.diagnosticAids.join(', ') || 'Ninguno'}</p>
                            </div>
                          </div>
                        </div>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="p-4 sm:p-6 text-gray-500 text-center">No hay órdenes médicas para este paciente</p>
                )}
              </div>
            ) : (
              <p className="text-gray-500 text-center">Selecciona un paciente para ver sus órdenes médicas</p>
            )}
          </div>
        )}

        {/* Billing Tab */}
        {activeTab === 'billing' && (
          <div role="tabpanel" id="billing-panel" aria-labelledby="billing-tab" className="animate-fade-in">
            <div className="mb-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Facturación</h2>
              {selectedPatient && (
                <button
                  onClick={() => setShowBillingForm(true)}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transform hover:scale-105 transition-all duration-200"
                  aria-label="Generar nueva factura"
                >
                  Nueva Factura
                </button>
              )}
            </div>

            {selectedPatient ? (
              <div className="bg-white shadow overflow-hidden sm:rounded-md">
                <p className="p-4 sm:p-6 text-gray-500 text-center">Funcionalidad de facturación próximamente disponible</p>
              </div>
            ) : (
              <p className="text-gray-500 text-center">Selecciona un paciente para ver su facturación</p>
            )}
          </div>
        )}

        {/* History Tab */}
        {activeTab === 'history' && (
          <div role="tabpanel" id="history-panel" aria-labelledby="history-tab" className="animate-fade-in">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Historial Clínico Completo</h2>

            {selectedPatient && medicalRecord ? (
              <div className="space-y-4 sm:space-y-6">
                {/* Medical Records */}
                <div className="bg-white shadow overflow-hidden sm:rounded-md">
                  <div className="px-4 sm:px-6 py-4 bg-gray-50 border-b">
                    <h3 className="text-lg font-medium text-gray-900">Registros Médicos</h3>
                  </div>
                  {medicalRecord.records.length > 0 ? (
                    <ul className="divide-y divide-gray-200">
                      {medicalRecord.records.map((record: any, index: number) => (
                        <li key={index} className="px-4 sm:px-6 py-4">
                          <div>
                            <p className="text-sm font-medium text-gray-900">{record.date}</p>
                            <p className="text-sm text-gray-600">Síntomas: {record.symptoms}</p>
                            <p className="text-sm text-gray-600">Diagnóstico: {record.diagnosis}</p>
                          </div>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="p-4 sm:p-6 text-gray-500 text-center">No hay registros médicos</p>
                  )}
                </div>

                {/* Medications */}
                <div className="bg-white shadow overflow-hidden sm:rounded-md">
                  <div className="px-4 sm:px-6 py-4 bg-gray-50 border-b">
                    <h3 className="text-lg font-medium text-gray-900">Medicamentos Recetados</h3>
                  </div>
                  {medicalRecord.medications.length > 0 ? (
                    <ul className="divide-y divide-gray-200">
                      {medicalRecord.medications.map((med: any, index: number) => (
                        <li key={index} className="px-4 sm:px-6 py-4">
                          <div>
                            <p className="text-sm font-medium text-gray-900">Orden #{med.orderNumber}</p>
                            <p className="text-sm text-gray-600">Medicamento ID: {med.medicationId}</p>
                            <p className="text-sm text-gray-600">Dosis: {med.dosage}</p>
                            <p className="text-sm text-gray-600">Duración: {med.duration}</p>
                          </div>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="p-4 sm:p-6 text-gray-500 text-center">No hay medicamentos recetados</p>
                  )}
                </div>

                {/* Procedures */}
                <div className="bg-white shadow overflow-hidden sm:rounded-md">
                  <div className="px-4 sm:px-6 py-4 bg-gray-50 border-b">
                    <h3 className="text-lg font-medium text-gray-900">Procedimientos</h3>
                  </div>
                  {medicalRecord.procedures.length > 0 ? (
                    <ul className="divide-y divide-gray-200">
                      {medicalRecord.procedures.map((proc: any, index: number) => (
                        <li key={index} className="px-4 sm:px-6 py-4">
                          <div>
                            <p className="text-sm font-medium text-gray-900">Orden #{proc.orderNumber}</p>
                            <p className="text-sm text-gray-600">Procedimiento ID: {proc.procedureId}</p>
                            <p className="text-sm text-gray-600">Cantidad: {proc.quantity}</p>
                            <p className="text-sm text-gray-600">Frecuencia: {proc.frequency}</p>
                          </div>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="p-4 sm:p-6 text-gray-500 text-center">No hay procedimientos</p>
                  )}
                </div>

                {/* Diagnostic Aids */}
                <div className="bg-white shadow overflow-hidden sm:rounded-md">
                  <div className="px-4 sm:px-6 py-4 bg-gray-50 border-b">
                    <h3 className="text-lg font-medium text-gray-900">Ayudas Diagnósticas</h3>
                  </div>
                  {medicalRecord.diagnosticAids.length > 0 ? (
                    <ul className="divide-y divide-gray-200">
                      {medicalRecord.diagnosticAids.map((aid: any, index: number) => (
                        <li key={index} className="px-4 sm:px-6 py-4">
                          <div>
                            <p className="text-sm font-medium text-gray-900">Orden #{aid.orderNumber}</p>
                            <p className="text-sm text-gray-600">Ayuda diagnóstica ID: {aid.diagnosticAidId}</p>
                            <p className="text-sm text-gray-600">Cantidad: {aid.quantity}</p>
                          </div>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="p-4 sm:p-6 text-gray-500 text-center">No hay ayudas diagnósticas</p>
                  )}
                </div>
              </div>
            ) : (
              <p className="text-gray-500">Selecciona un paciente para ver su historial clínico completo</p>
            )}
          </div>
        )}

        {/* Medical Record Modal */}
        <Modal isOpen={showRecordForm} onClose={() => setShowRecordForm(false)} title="Nuevo Registro Médico">
          <form onSubmit={handleCreateRecord} className="space-y-4">
            <div>
              <label htmlFor="record-reason" className="block text-sm font-medium text-gray-700">Motivo de la consulta</label>
              <input
                id="record-reason"
                type="text"
                required
                value={recordForm.reason}
                onChange={(e) => setRecordForm({...recordForm, reason: e.target.value})}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                aria-describedby="record-reason-help"
              />
              <span id="record-reason-help" className="sr-only">Ingrese el motivo de la consulta médica</span>
            </div>

            <div>
              <label htmlFor="record-symptoms" className="block text-sm font-medium text-gray-700">Síntomas</label>
              <textarea
                id="record-symptoms"
                required
                value={recordForm.symptoms}
                onChange={(e) => setRecordForm({...recordForm, symptoms: e.target.value})}
                rows={3}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                aria-describedby="record-symptoms-help"
              />
              <span id="record-symptoms-help" className="sr-only">Describa los síntomas del paciente</span>
            </div>

            <div>
              <label htmlFor="record-diagnosis" className="block text-sm font-medium text-gray-700">Diagnóstico</label>
              <textarea
                id="record-diagnosis"
                required
                value={recordForm.diagnosis}
                onChange={(e) => setRecordForm({...recordForm, diagnosis: e.target.value})}
                rows={3}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                aria-describedby="record-diagnosis-help"
              />
              <span id="record-diagnosis-help" className="sr-only">Ingrese el diagnóstico médico</span>
            </div>

            <div className="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                onClick={() => setShowRecordForm(false)}
                className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 focus:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Guardar Registro
              </button>
            </div>
          </form>
        </Modal>

        {/* Order Modal */}
        <Modal isOpen={showOrderForm} onClose={() => setShowOrderForm(false)} title="Nueva Orden Médica" size="xl">
          <form onSubmit={handleCreateOrder} className="space-y-6">
            <div>
              <label htmlFor="order-diagnosis" className="block text-sm font-medium text-gray-700">Diagnóstico</label>
              <textarea
                id="order-diagnosis"
                required
                value={orderForm.diagnosis}
                onChange={(e) => setOrderForm({...orderForm, diagnosis: e.target.value})}
                rows={3}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                aria-describedby="order-diagnosis-help"
              />
              <span id="order-diagnosis-help" className="sr-only">Ingrese el diagnóstico para esta orden médica</span>
            </div>

                  {/* Medications Section */}
                  <div className="border rounded-md p-4">
                    <div className="flex justify-between items-center mb-4">
                      <h4 className="text-md font-medium text-gray-900">Medicamentos</h4>
                      <button
                        type="button"
                        onClick={addMedicationToOrder}
                        className="bg-green-600 text-white px-3 py-1 rounded-md hover:bg-green-700 text-sm"
                      >
                        Agregar Medicamento
                      </button>
                    </div>
                    {orderForm.medications.map((med, index) => (
                      <div key={index} className="grid grid-cols-4 gap-4 mb-4 p-4 bg-gray-50 rounded">
                        <select
                          value={med.medicationId}
                          onChange={(e) => {
                            const newMeds = [...orderForm.medications]
                            newMeds[index].medicationId = e.target.value
                            setOrderForm({...orderForm, medications: newMeds})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                          <option value="">Seleccionar medicamento</option>
                          {medications.map((medication) => (
                            <option key={medication.id} value={medication.id}>
                              {medication.name}
                            </option>
                          ))}
                        </select>
                        <input
                          type="text"
                          placeholder="Dosis"
                          value={med.dosage}
                          onChange={(e) => {
                            const newMeds = [...orderForm.medications]
                            newMeds[index].dosage = e.target.value
                            setOrderForm({...orderForm, medications: newMeds})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <input
                          type="text"
                          placeholder="Duración"
                          value={med.duration}
                          onChange={(e) => {
                            const newMeds = [...orderForm.medications]
                            newMeds[index].duration = e.target.value
                            setOrderForm({...orderForm, medications: newMeds})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <button
                          type="button"
                          onClick={() => {
                            const newMeds = orderForm.medications.filter((_, i) => i !== index)
                            setOrderForm({...orderForm, medications: newMeds})
                          }}
                          className="bg-red-600 text-white px-3 py-2 rounded-md hover:bg-red-700"
                        >
                          Eliminar
                        </button>
                      </div>
                    ))}
                  </div>

                  {/* Procedures Section */}
                  <div className="border rounded-md p-4">
                    <div className="flex justify-between items-center mb-4">
                      <h4 className="text-md font-medium text-gray-900">Procedimientos</h4>
                      <button
                        type="button"
                        onClick={addProcedureToOrder}
                        className="bg-green-600 text-white px-3 py-1 rounded-md hover:bg-green-700 text-sm"
                      >
                        Agregar Procedimiento
                      </button>
                    </div>
                    {orderForm.procedures.map((proc, index) => (
                      <div key={index} className="grid grid-cols-5 gap-4 mb-4 p-4 bg-gray-50 rounded">
                        <select
                          value={proc.procedureId}
                          onChange={(e) => {
                            const newProcs = [...orderForm.procedures]
                            newProcs[index].procedureId = e.target.value
                            setOrderForm({...orderForm, procedures: newProcs})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                          <option value="">Seleccionar procedimiento</option>
                          {procedures.map((procedure) => (
                            <option key={procedure.id} value={procedure.id}>
                              {procedure.name}
                            </option>
                          ))}
                        </select>
                        <input
                          type="number"
                          placeholder="Cantidad"
                          value={proc.quantity}
                          onChange={(e) => {
                            const newProcs = [...orderForm.procedures]
                            newProcs[index].quantity = parseInt(e.target.value)
                            setOrderForm({...orderForm, procedures: newProcs})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <input
                          type="text"
                          placeholder="Frecuencia"
                          value={proc.frequency}
                          onChange={(e) => {
                            const newProcs = [...orderForm.procedures]
                            newProcs[index].frequency = e.target.value
                            setOrderForm({...orderForm, procedures: newProcs})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <div className="flex items-center">
                          <input
                            type="checkbox"
                            checked={proc.requiresSpecialist}
                            onChange={(e) => {
                              const newProcs = [...orderForm.procedures]
                              newProcs[index].requiresSpecialist = e.target.checked
                              setOrderForm({...orderForm, procedures: newProcs})
                            }}
                            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                          />
                          <label className="ml-2 text-sm text-gray-900">Requiere especialista</label>
                        </div>
                        <button
                          type="button"
                          onClick={() => {
                            const newProcs = orderForm.procedures.filter((_, i) => i !== index)
                            setOrderForm({...orderForm, procedures: newProcs})
                          }}
                          className="bg-red-600 text-white px-3 py-2 rounded-md hover:bg-red-700"
                        >
                          Eliminar
                        </button>
                      </div>
                    ))}
                  </div>

                  {/* Diagnostic Aids Section */}
                  <div className="border rounded-md p-4">
                    <div className="flex justify-between items-center mb-4">
                      <h4 className="text-md font-medium text-gray-900">Ayudas Diagnósticas</h4>
                      <button
                        type="button"
                        onClick={addDiagnosticAidToOrder}
                        className="bg-green-600 text-white px-3 py-1 rounded-md hover:bg-green-700 text-sm"
                      >
                        Agregar Ayuda Diagnóstica
                      </button>
                    </div>
                    {orderForm.diagnosticAids.map((aid, index) => (
                      <div key={index} className="grid grid-cols-4 gap-4 mb-4 p-4 bg-gray-50 rounded">
                        <select
                          value={aid.diagnosticAidId}
                          onChange={(e) => {
                            const newAids = [...orderForm.diagnosticAids]
                            newAids[index].diagnosticAidId = e.target.value
                            setOrderForm({...orderForm, diagnosticAids: newAids})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                          <option value="">Seleccionar ayuda diagnóstica</option>
                          {diagnosticAids.map((diagnosticAid) => (
                            <option key={diagnosticAid.id} value={diagnosticAid.id}>
                              {diagnosticAid.name}
                            </option>
                          ))}
                        </select>
                        <input
                          type="number"
                          placeholder="Cantidad"
                          value={aid.quantity}
                          onChange={(e) => {
                            const newAids = [...orderForm.diagnosticAids]
                            newAids[index].quantity = parseInt(e.target.value)
                            setOrderForm({...orderForm, diagnosticAids: newAids})
                          }}
                          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <div className="flex items-center">
                          <input
                            type="checkbox"
                            checked={aid.requiresSpecialist}
                            onChange={(e) => {
                              const newAids = [...orderForm.diagnosticAids]
                              newAids[index].requiresSpecialist = e.target.checked
                              setOrderForm({...orderForm, diagnosticAids: newAids})
                            }}
                            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                          />
                          <label className="ml-2 text-sm text-gray-900">Requiere especialista</label>
                        </div>
                        <button
                          type="button"
                          onClick={() => {
                            const newAids = orderForm.diagnosticAids.filter((_, i) => i !== index)
                            setOrderForm({...orderForm, diagnosticAids: newAids})
                          }}
                          className="bg-red-600 text-white px-3 py-2 rounded-md hover:bg-red-700"
                        >
                          Eliminar
                        </button>
                      </div>
                    ))}
                  </div>

            <div className="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                onClick={() => setShowOrderForm(false)}
                className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 focus:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Crear Orden
              </button>
            </div>
          </form>
        </Modal>
      </div>
    </Layout>
  )
}