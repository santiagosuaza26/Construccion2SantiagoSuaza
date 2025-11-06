// API utilities for the clinic management system

const API_BASE_URL = 'http://localhost:8080/api'

export interface AuthResponse {
  token: string
  fullName: string
  role: string
  expirationTime: number
}

export interface User {
  identificationNumber: string
  fullName: string
  email: string
  phone: string
  dateOfBirth: string
  address: string
  role: string
  username: string
}

export interface Patient {
  identificationNumber: string
  fullName: string
  dateOfBirth: string
  gender: string
  address: string
  phone: string
  email: string
  emergencyName: string
  emergencyRelation: string
  emergencyPhone: string
  companyName: string
  policyNumber: string
  insuranceActive: boolean
  validityDate: string
}

export interface Medication {
  id: string
  name: string
  cost: number
  requiresSpecialist: boolean
  specialistType: string
}

export interface Procedure {
  id: string
  name: string
  cost: number
  requiresSpecialist: boolean
  specialistType: string
}

export interface DiagnosticAid {
  id: string
  name: string
  cost: number
  requiresSpecialist: boolean
  specialistType: string
}

export interface VitalSigns {
  id: string
  patientId: string
  bloodPressure: string
  temperature: number
  pulse: number
  oxygenLevel: number
  recordedAt: string
  recordedBy: string
}

export interface Order {
  orderNumber: string
  patientIdentificationNumber: string
  doctorIdentificationNumber: string
  date: string
  diagnosis: string
  medications: string[]
  procedures: string[]
  diagnosticAids: string[]
}

export interface Billing {
  id: string
  patientId: string
  patientName: string
  doctorName: string
  orderNumber: string
  totalCost: number
  copayAmount: number
  insuranceCoverage: number
  finalAmount: number
  appliedMedications: any[]
  appliedProcedures: any[]
  appliedDiagnosticAids: any[]
  generatedAt: string
  generatedBy: string
}

class ApiClient {
  private getAuthHeaders(): HeadersInit {
    const token = localStorage.getItem('token')
    return {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    }
  }

  async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`
    console.log(`Making request to: ${url}`)

    try {
      const response = await fetch(url, {
        ...options,
        headers: {
          ...this.getAuthHeaders(),
          ...options.headers,
        },
      })

      console.log(`Response status: ${response.status}`)

      if (!response.ok) {
        const error = await response.text()
        console.error(`Request failed: ${error}`)
        throw new Error(error || `HTTP ${response.status}`)
      }

      return response.json()
    } catch (error) {
      console.error(`Network error: ${error}`)
      throw error
    }
  }

  // Auth
  async login(username: string, password: string): Promise<AuthResponse> {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    })

    if (!response.ok) {
      throw new Error('Credenciales inválidas')
    }

    return response.json()
  }

  async logout(): Promise<void> {
    await fetch(`${API_BASE_URL}/auth/logout`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
    })
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  // Users
  async getUsers(): Promise<User[]> {
    return this.request('/users')
  }

  async createUser(userData: any): Promise<User> {
    return this.request('/users', {
      method: 'POST',
      body: JSON.stringify(userData),
    })
  }

  async updateUser(id: string, userData: any): Promise<User> {
    return this.request(`/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(userData),
    })
  }

  async deleteUser(id: string): Promise<void> {
    await this.request(`/users/${id}`, {
      method: 'DELETE',
    })
  }

  async canViewPatients(): Promise<{ canView: boolean }> {
    return this.request('/users/can-view-patients')
  }

  async canManageUsers(): Promise<{ canManage: boolean }> {
    return this.request('/users/can-manage-users')
  }

  async canRegisterPatients(): Promise<{ canRegister: boolean }> {
    return this.request('/users/can-register-patients')
  }

  // Patients
  async getPatients(): Promise<Patient[]> {
    return this.request('/patients')
  }

  async createPatient(patientData: any): Promise<Patient> {
    return this.request('/patients', {
      method: 'POST',
      body: JSON.stringify(patientData),
    })
  }

  async updatePatient(id: string, patientData: any): Promise<Patient> {
    return this.request(`/patients/${id}`, {
      method: 'PUT',
      body: JSON.stringify(patientData),
    })
  }

  async getPatientOrders(patientId: string): Promise<Order[]> {
    return this.request(`/patients/${patientId}/orders`)
  }

  // Inventory
  async getMedications(): Promise<Medication[]> {
    return this.request('/inventory/medications')
  }

  async createMedication(medicationData: any): Promise<Medication> {
    return this.request('/inventory/medications', {
      method: 'POST',
      body: JSON.stringify(medicationData),
    })
  }

  async updateMedication(id: string, medicationData: any): Promise<Medication> {
    return this.request(`/inventory/medications/${id}`, {
      method: 'PUT',
      body: JSON.stringify(medicationData),
    })
  }

  async getProcedures(): Promise<Procedure[]> {
    return this.request('/inventory/procedures')
  }

  async createProcedure(procedureData: any): Promise<Procedure> {
    return this.request('/inventory/procedures', {
      method: 'POST',
      body: JSON.stringify(procedureData),
    })
  }

  async updateProcedure(id: string, procedureData: any): Promise<Procedure> {
    return this.request(`/inventory/procedures/${id}`, {
      method: 'PUT',
      body: JSON.stringify(procedureData),
    })
  }

  async getDiagnosticAids(): Promise<DiagnosticAid[]> {
    return this.request('/inventory/diagnostic-aids')
  }

  async createDiagnosticAid(diagnosticAidData: any): Promise<DiagnosticAid> {
    return this.request('/inventory/diagnostic-aids', {
      method: 'POST',
      body: JSON.stringify(diagnosticAidData),
    })
  }

  async updateDiagnosticAid(id: string, diagnosticAidData: any): Promise<DiagnosticAid> {
    return this.request(`/inventory/diagnostic-aids/${id}`, {
      method: 'PUT',
      body: JSON.stringify(diagnosticAidData),
    })
  }

  // Nurse
  async getPatient(patientId: string): Promise<Patient> {
    return this.request(`/nurse/patients/${patientId}`)
  }

  async recordVitalSigns(vitalSignsData: any): Promise<VitalSigns> {
    return this.request('/nurse/vital-signs', {
      method: 'POST',
      body: JSON.stringify(vitalSignsData),
    })
  }

  async getVitalSigns(patientId: string): Promise<VitalSigns[]> {
    return this.request(`/nurse/vital-signs?patientId=${patientId}`)
  }

  async recordMedicationAdministration(data: any): Promise<void> {
    await this.request('/nurse/medication-administration', {
      method: 'POST',
      body: JSON.stringify(data),
    })
  }

  async recordProcedureRealization(data: any): Promise<void> {
    await this.request('/nurse/procedure-realization', {
      method: 'POST',
      body: JSON.stringify(data),
    })
  }

  // Medical Records
  async getMedicalRecord(patientId: string): Promise<any> {
    return this.request(`/medical/records/${patientId}`)
  }

  async addMedicalRecord(patientId: string, recordData: any): Promise<void> {
    await this.request(`/medical/records/${patientId}`, {
      method: 'POST',
      body: JSON.stringify(recordData),
    })
  }

  async updateMedicalRecord(patientId: string, recordData: any): Promise<void> {
    await this.request(`/medical/records/${patientId}`, {
      method: 'PUT',
      body: JSON.stringify(recordData),
    })
  }

  // Billing
  async getBilling(orderNumber: string): Promise<Billing> {
    return this.request(`/billing/order/${orderNumber}`)
  }

  async printInvoice(orderNumber: string): Promise<Blob> {
    const response = await fetch(`${API_BASE_URL}/billing/order/${orderNumber}/print`, {
      headers: this.getAuthHeaders(),
    })

    if (!response.ok) {
      throw new Error('Error al generar factura')
    }

    return response.blob()
  }
}

export const api = new ApiClient()