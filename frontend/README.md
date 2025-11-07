# 🏥 Frontend - Sistema de Gestión Clínica

[![Next.js](https://img.shields.io/badge/Next.js-16.0.1-black)](https://nextjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-blue)](https://www.typescriptlang.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-3.4.0-38B2AC)](https://tailwindcss.com/)
[![React](https://img.shields.io/badge/React-19.2.0-61DAFB)](https://reactjs.org/)

## 📋 Descripción

Interfaz de usuario moderna y responsiva para el **Sistema de Gestión Clínica**, construida con Next.js 16, TypeScript y Tailwind CSS. Proporciona una experiencia de usuario intuitiva para la gestión integral de clínicas médicas, incluyendo autenticación, gestión de pacientes, citas médicas, registros médicos y facturación.

## ✨ Características

- 🎨 **Interfaz Moderna**: Diseño responsivo con Tailwind CSS
- 🔐 **Autenticación Segura**: Integración con JWT del backend
- 👥 **Control de Roles**: Dashboards específicos por rol (Admin, Doctor, Enfermera, Soporte)
- 📱 **Responsive Design**: Optimizado para desktop, tablet y móvil
- ⚡ **Performance**: Optimización con Next.js App Router
- 🧪 **TypeScript**: Tipado fuerte para mayor robustez
- 🎯 **UX/UI**: Experiencia de usuario centrada en la eficiencia médica

## 🛠️ Tecnologías

### **Framework & Runtime**

- **Next.js 16.0.1**: Framework React con App Router
- **React 19.2.0**: Biblioteca de UI
- **TypeScript 5**: Tipado estático
- **Node.js 18+**: Runtime de JavaScript

### **Styling & UI**

- **Tailwind CSS 3.4.0**: Framework CSS utility-first
- **PostCSS**: Procesador CSS
- **ESLint**: Linting de código

### **Integración Backend**

- **Fetch API**: Comunicación con API REST
- **JWT**: Gestión de autenticación
- **Local Storage**: Persistencia de sesión

## 🚀 Inicio Rápido

### **Prerrequisitos**

- Node.js 18 o superior
- npm, yarn, pnpm o bun
- Backend corriendo en `http://localhost:8080`

### **Instalación**

```bash
# Clonar repositorio
git clone https://github.com/santiagosuaza26/Construccion2SantiagoSuaza
cd Construccion2SantiagoSuaza/frontend

# Instalar dependencias
npm install
# o
yarn install
# o
pnpm install
# o
bun install

# Iniciar servidor de desarrollo
npm run dev
# o
yarn dev
# o
pnpm dev
# o
bun dev
```

### **Acceder a la Aplicación**

Abre [http://localhost:3000](http://localhost:3000) en tu navegador.

## 📁 Estructura del Proyecto

frontend/
├── src/
│   ├── app/                     # 📱 Páginas Next.js (App Router)
│   │   ├── (auth)/              # Rutas de autenticación
│   │   │   ├── login/           # Página de login
│   │   │   └── layout.tsx       # Layout de auth
│   │   ├── dashboard/           # Dashboard principal
│   │   │   ├── layout.tsx       # Layout del dashboard
│   │   │   ├── page.tsx         # Dashboard general
│   │   │   ├── admin/           # Dashboard administrador
│   │   │   ├── doctor/          # Dashboard médico
│   │   │   ├── nurse/           # Dashboard enfermera
│   │   │   ├── hr/              # Dashboard RRHH
│   │   │   └── support/         # Dashboard soporte
│   │   ├── globals.css          # Estilos globales
│   │   ├── layout.tsx           # Layout raíz
│   │   └── page.tsx             # Página de inicio
│   ├── components/              # 🧩 Componentes React
│   │   ├── ui/                  # Componentes de UI reutilizables
│   │   │   ├── Button.tsx       # Botón personalizado
│   │   │   ├── Input.tsx        # Input personalizado
│   │   │   ├── Modal.tsx        # Modal reutilizable
│   │   │   └── LoadingSpinner.tsx # Spinner de carga
│   │   ├── forms/               # Formularios específicos
│   │   │   ├── PatientForm.tsx  # Formulario de paciente
│   │   │   ├── AppointmentForm.tsx # Formulario de cita
│   │   │   └── MedicalRecordForm.tsx # Formulario médico
│   │   ├── Layout.tsx           # Layout principal
│   │   └── ErrorAlert.tsx       # Componente de errores
│   └── lib/                     # 🛠️ Utilidades
│       ├── api.ts               # Cliente API
│       ├── auth.ts              # Utilidades de autenticación
│       └── utils.ts             # Funciones auxiliares
├── public/                      # 📁 Archivos estáticos
│   ├── favicon.ico
│   └── *.svg
├── Dockerfile                   # 🐳 Docker
├── next.config.ts               # ⚙️ Configuración Next.js
├── package.json                 # 📦 Dependencias
├── tailwind.config.js           # 🎨 Configuración Tailwind
└── tsconfig.json                # 🔧 Configuración TypeScript

## 🔐 Autenticación y Roles

### **Sistema de Roles**

| Rol | Dashboard | Permisos Principales |
|-----|-----------|---------------------|
| **ADMIN** | `/dashboard/admin` | Gestión completa del sistema |
| **HUMAN_RESOURCES** | `/dashboard/hr` | Gestión de usuarios y personal |
| **ADMINISTRATIVE_STAFF** | `/dashboard/admin` | Gestión de pacientes y citas |
| **SUPPORT_STAFF** | `/dashboard/support` | Gestión de inventario y soporte |
| **DOCTOR** | `/dashboard/doctor` | Acceso a historias clínicas |
| **NURSE** | `/dashboard/nurse` | Registro de signos vitales |

### **Flujo de Autenticación**

```typescript
// src/lib/auth.ts
export const login = async (credentials: LoginCredentials) => {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(credentials)
  });

  if (response.ok) {
    const { token, user } = await response.json();
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    return { success: true, user };
  }

  return { success: false, error: 'Credenciales inválidas' };
};
```

## 🎨 Diseño y UI

### **Paleta de Colores**

```css
/* Tailwind config personalizado */
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
        },
        medical: {
          50: '#f0f9ff',
          500: '#0ea5e9',
          600: '#0284c7',
        }
      }
    }
  }
}
```

### **Componentes Principales**

#### **Layout Responsivo**

```tsx
// src/components/Layout.tsx
export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-gray-50">
      <Sidebar />
      <main className="lg:pl-64">
        <Header />
        <div className="px-4 py-8 sm:px-6 lg:px-8">
          {children}
        </div>
      </main>
    </div>
  );
}
```

#### **Formularios con Validación**

```tsx
// src/components/forms/PatientForm.tsx
export default function PatientForm() {
  const [formData, setFormData] = useState<PatientFormData>({
    firstName: '',
    lastName: '',
    email: '',
    phone: ''
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.createPatient(formData);
      // Manejar éxito
    } catch (error) {
      // Manejar error
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {/* Campos del formulario */}
    </form>
  );
}
```

## 🔗 Integración con Backend

### **Cliente API**

```typescript
// src/lib/api.ts
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

class ApiClient {
  private getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      ...(token && { Authorization: `Bearer ${token}` })
    };
  }

  async getPatients() {
    const response = await fetch(`${API_BASE_URL}/api/patients`, {
      headers: this.getAuthHeaders()
    });
    return response.json();
  }

  async createAppointment(appointmentData: AppointmentData) {
    const response = await fetch(`${API_BASE_URL}/api/appointments`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(appointmentData)
    });
    return response.json();
  }
}

export const api = new ApiClient();
```

### **Endpoints Utilizados**

#### **Autenticación**

```http
POST /api/auth/login
POST /api/auth/logout
```

#### **Pacientes**

```http
GET    /api/patients
POST   /api/patients
GET    /api/patients/{id}
PUT    /api/patients/{id}
```

#### **Citas**

```http
GET    /api/appointments
POST   /api/appointments
PUT    /api/appointments/{id}/status
```

#### **Registros Médicos**

```http
GET    /api/medical-records/{patientId}
POST   /api/medical-records
```

## 🚀 Despliegue

### **Con Docker**

```dockerfile
# Dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production

FROM node:18-alpine AS runner
WORKDIR /app
COPY --from=builder /app/node_modules ./node_modules
COPY . .

EXPOSE 3000
ENV PORT 3000
CMD ["npm", "start"]
```

### **Con Docker Compose**

```yaml
version: '3.8'
services:
  frontend:
    build: .
    ports:
      - "3000:3000"
    environment:
      - NEXT_PUBLIC_API_URL=http://localhost:8080
    depends_on:
      - backend
```

### **Variables de Entorno**

```env
# .env.local
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_APP_NAME=Sistema de Gestión Clínica
NEXT_PUBLIC_APP_VERSION=1.0.0
```

## 🧪 Desarrollo

### **Comandos Disponibles**

```bash
# Desarrollo
npm run dev          # Servidor de desarrollo
npm run build        # Build de producción
npm run start        # Servidor de producción
npm run lint         # Linting
npm run type-check   # Verificación de tipos

# Testing (si implementado)
npm run test         # Ejecutar tests
npm run test:watch   # Tests en modo watch
npm run test:coverage # Cobertura de tests
```

### **Configuración TypeScript**

```json
// tsconfig.json
{
  "compilerOptions": {
    "target": "es5",
    "lib": ["dom", "dom.iterable", "es6"],
    "allowJs": true,
    "skipLibCheck": true,
    "strict": true,
    "noEmit": true,
    "esModuleInterop": true,
    "module": "esnext",
    "moduleResolution": "bundler",
    "resolveJsonModule": true,
    "isolatedModules": true,
    "jsx": "preserve",
    "incremental": true,
    "plugins": [
      {
        "name": "next"
      }
    ],
    "baseUrl": ".",
    "paths": {
      "@/*": ["./src/*"],
      "@/components/*": ["./src/components/*"],
      "@/lib/*": ["./src/lib/*"]
    }
  },
  "include": ["next-env.d.ts", "**/*.ts", "**/*.tsx", ".next/types/**/*.ts"],
  "exclude": ["node_modules"]
}
```

## 🐛 Troubleshooting

### **Problemas Comunes**

#### **Error de Conexión con Backend**

```bash
# Verificar que el backend esté corriendo
curl http://localhost:8080/actuator/health

# Verificar variables de entorno
echo $NEXT_PUBLIC_API_URL
```

#### **Errores de Build**

```bash
# Limpiar cache de Next.js
rm -rf .next
npm run build

# Verificar dependencias
npm ls --depth=0
```

#### **Problemas de CORS**

- Asegurarse de que el backend tenga configurado CORS para `http://localhost:3000`
- Verificar configuración en `SecurityConfig.java`

## 📊 Rendimiento

### **Optimizaciones Implementadas**

- **Code Splitting**: Carga lazy de componentes
- **Image Optimization**: Optimización automática de imágenes
- **Static Generation**: Páginas estáticas donde aplica
- **Caching**: Estrategias de cache inteligente

### **Métricas de Performance**

- **First Contentful Paint**: < 1.5s
- **Largest Contentful Paint**: < 2.5s
- **Cumulative Layout Shift**: < 0.1
- **First Input Delay**: < 100ms

## 🤝 Contribución

### **Estándares de Código**

1. **TypeScript**: Uso obligatorio de tipos
2. **ESLint**: Seguir reglas configuradas
3. **Commits**: Conventional Commits
4. **Componentes**: Documentación con JSDoc

### **Flujo de Desarrollo**

```bash
# 1. Crear rama
git checkout -b feature/patient-dashboard

# 2. Desarrollar
npm run dev

# 3. Commit
git commit -m "feat: add patient dashboard component"

# 4. Push y PR
git push origin feature/patient-dashboard
```

## 📞 Soporte

- **📧 Email**: santiago.suaza@correo.tdea.edu.co
- **🐛 Issues**: [GitHub Issues](https://github.com/santiagosuaza26/Construccion2SantiagoSuaza/issues)
- **📖 Docs**: [Documentación Completa](../README.md)
- **🔗 API Docs**: [Swagger UI](http://localhost:8081/swagger-ui.html)

---

*"Interfaz intuitiva para la transformación digital de la salud"*

🎨 Desarrollado con ❤️ por el equipo frontend
