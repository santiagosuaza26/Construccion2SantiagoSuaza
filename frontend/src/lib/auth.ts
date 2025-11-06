// Authentication utilities

export interface User {
  fullName: string
  role: string
}

export const auth = {
  getToken(): string | null {
    if (typeof window === 'undefined') return null
    return localStorage.getItem('token')
  },

  getUser(): User | null {
    if (typeof window === 'undefined') return null
    const userStr = localStorage.getItem('user')
    return userStr ? JSON.parse(userStr) : null
  },

  setUser(token: string, user: User): void {
    if (typeof window === 'undefined') return
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify(user))
  },

  clearAuth(): void {
    if (typeof window === 'undefined') return
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  },

  isAuthenticated(): boolean {
    return !!this.getToken()
  },

  getRole(): string | null {
    const user = this.getUser()
    return user?.role || null
  },

  hasRole(role: string): boolean {
    return this.getRole() === role
  },

  canAccessPatientData(): boolean {
    const role = this.getRole()
    return role === 'MEDICO' || role === 'ENFERMERA' || role === 'PERSONAL_ADMINISTRATIVO'
  },

  canManageUsers(): boolean {
    return this.hasRole('RECURSOS_HUMANOS')
  },

  canRegisterPatients(): boolean {
    return this.hasRole('PERSONAL_ADMINISTRATIVO')
  },

  canManageInventory(): boolean {
    return this.hasRole('SOPORTE_INFORMACION')
  },

  canRecordVitalSigns(): boolean {
    return this.hasRole('ENFERMERA')
  },

  canManageMedicalRecords(): boolean {
    const role = this.getRole()
    return role === 'MEDICO' || role === 'ENFERMERA'
  },

  canGenerateBilling(): boolean {
    const role = this.getRole()
    return role === 'PERSONAL_ADMINISTRATIVO' || role === 'MEDICO'
  },
}