# 🌐 Sistema de Gestión Clínica - Frontend

[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue.svg)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-4.0-yellow.svg)](https://vitejs.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.0-38B2AC.svg)](https://tailwindcss.com/)
[![Node.js](https://img.shields.io/badge/Node.js-18+-green.svg)](https://nodejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Tabla de Contenidos

- [🌐 Sistema de Gestión Clínica - Frontend](#-sistema-de-gestión-clínica---frontend)
  - [📋 Tabla de Contenidos](#-tabla-de-contenidos)
  - [🎯 Visión General](#-visión-general)
  - [✨ Características Principales](#-características-principales)
  - [🏗️ Arquitectura](#️-arquitectura)
  - [🛠️ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
  - [🚀 Inicio Rápido](#-inicio-rápido)
  - [📁 Estructura del Proyecto](#-estructura-del-proyecto)
  - [🔧 Configuración](#-configuración)
  - [🎨 Estilos y UI/UX](#-estilos-y-uiux)
  - [🔗 Integración con Backend](#-integración-con-backend)
  - [🧪 Pruebas](#-pruebas)
  - [🚢 Despliegue](#-despliegue)
  - [📱 Características Responsive](#-características-responsive)
  - [🔒 Seguridad en Frontend](#-seguridad-en-frontend)
  - [📊 Estado de la Aplicación](#-estado-de-la-aplicación)
  - [🔄 Gestión de Estado](#-gestión-de-estado)
  - [🌐 Internacionalización](#-internacionalización)
  - [♿ Accesibilidad](#-accesibilidad)
  - [👥 Equipo de Desarrollo](#-equipo-de-desarrollo)
  - [🤝 Contribución](#-contribución)
  - [📄 Licencia](#-licencia)

## 🎯 Visión General

El frontend del Sistema de Gestión Clínica es una aplicación web moderna desarrollada con **React 18** y **TypeScript** que proporciona una interfaz de usuario intuitiva y responsiva para la gestión integral de clínicas y centros médicos. Implementa las mejores prácticas de desarrollo frontend con un enfoque en la experiencia del usuario y la accesibilidad.

## ✨ Características Principales

### 🔐 **Autenticación y Autorización**
- ✅ Inicio de sesión seguro con JWT
- ✅ Gestión automática de sesiones
- ✅ Protección de rutas basada en roles
- ✅ Refresh automático de tokens
- ✅ Logout seguro con limpieza de datos

### 👥 **Gestión de Usuarios**
- ✅ Dashboard administrativo completo
- ✅ Gestión de usuarios (CRUD)
- ✅ Asignación de roles y permisos
- ✅ Perfiles de usuario detallados
- ✅ Búsqueda y filtrado avanzado

### 🏥 **Gestión de Pacientes**
- ✅ Registro de nuevos pacientes
- ✅ Información médica completa
- ✅ Contactos de emergencia
- ✅ Historial médico integrado
- ✅ Búsqueda por múltiples criterios

### 📅 **Sistema de Citas**
- ✅ Programación de citas médicas
- ✅ Calendario interactivo
- ✅ Gestión de disponibilidad
- ✅ Recordatorios y notificaciones
- ✅ Estados de citas en tiempo real

### 💊 **Inventario Médico**
- ✅ Control de medicamentos
- ✅ Gestión de suministros médicos
- ✅ Alertas de vencimiento
- ✅ Órdenes de compra automáticas
- ✅ Reportes de inventario

### 💰 **Facturación**
- ✅ Cálculo automático de copagos
- ✅ Gestión de seguros médicos
- ✅ Facturación electrónica
- ✅ Estados de pago
- ✅ Reportes financieros

### 📋 **Historia Clínica**
- ✅ Registros médicos digitales
- ✅ Signos vitales
- ✅ Diagnósticos y tratamientos
- ✅ Evolución del paciente
- ✅ Archivos médicos adjuntos

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                    Frontend - React Application                 │
├─────────────────────────────────────────────────────────────────┤
│  🎨 Presentation Layer (Components & Pages)                     │
│  🔄 Application Layer (Services & Hooks)                       │
│  💾 State Layer (Context & State Management)                   │
│  🌐 HTTP Layer (API Client & Interceptors)                     │
├─────────────────────────────────────────────────────────────────┤
│  🔗 REST API (Backend Communication)                           │
│  📡 WebSocket (Real-time Updates)                              │
│  💾 Local Storage (Client-side Persistence)                    │
└─────────────────────────────────────────────────────────────────┘
```

### **Patrón Arquitectónico**
- **Component-Based Architecture** con React
- **Custom Hooks** para lógica reutilizable
- **Context API** para gestión de estado global
- **Service Layer** para comunicación con APIs
- **TypeScript** para type safety

## 🛠️ Tecnologías Utilizadas

### **Frontend Core**
- **React 18.2** - Librería principal de UI
- **TypeScript 5.0** - Tipado estático
- **Vite 4.0** - Build tool y dev server
- **React Router 6** - Navegación SPA

### **Estilos y UI**
- **Tailwind CSS 3.0** - Framework CSS utilitario
- **Headless UI** - Componentes accesibles
- **Heroicons** - Iconografía moderna
- **React Hook Form** - Manejo de formularios

### **Estado y Datos**
- **React Query** - Fetching y caché de datos
- **Zustand** - Gestión de estado ligera
- **React Context** - Estado global
- **Local Storage** - Persistencia local

### **Desarrollo y Calidad**
- **ESLint** - Linting de código
- **Prettier** - Formateo automático
- **Husky** - Git hooks
- **Vitest** - Testing framework
- **Playwright** - E2E testing

## 🚀 Inicio Rápido

### **Prerrequisitos**
- Node.js 18+
- npm o yarn
- Git

### **Instalación**

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd clinica/frontend

# 2. Instalar dependencias
npm install

# 3. Configurar variables de entorno
cp .env.example .env.local

# 4. Iniciar servidor de desarrollo
npm run dev

# 5. Abrir navegador
# Frontend: http://localhost:3000
# Storybook: http://localhost:6006
```

### **Scripts Disponibles**

```bash
# Desarrollo
npm run dev              # Servidor de desarrollo
npm run build           # Build para producción
npm run preview         # Preview del build

# Calidad de código
npm run lint            # Ejecutar ESLint
npm run lint:fix        # Corregir problemas automáticamente
npm run format          # Formatear código con Prettier

# Pruebas
npm run test            # Ejecutar pruebas unitarias
npm run test:ui         # Ejecutar pruebas con interfaz
npm run test:e2e        # Pruebas end-to-end
npm run test:coverage   # Cobertura de pruebas

# Documentación
npm run storybook       # Storybook para componentes
npm run build:storybook # Build de Storybook

# Análisis
npm run analyze         # Análisis de bundle
npm run lighthouse      # Auditoría de performance
```

## 📁 Estructura del Proyecto

```
frontend/
├── public/                    # Archivos estáticos
├── src/
│   ├── assets/               # Imágenes, fuentes, etc.
│   ├── components/           # Componentes reutilizables
│   │   ├── ui/              # Componentes básicos (Button, Input, etc.)
│   │   ├── forms/           # Componentes de formularios
│   │   ├── layout/          # Componentes de layout (Header, Sidebar)
│   │   └── features/        # Componentes específicos de funcionalidades
│   ├── hooks/               # Custom hooks personalizados
│   ├── pages/               # Páginas de la aplicación
│   ├── services/            # Servicios para APIs
│   ├── stores/              # Gestión de estado (Zustand)
│   ├── types/               # Definiciones de tipos TypeScript
│   ├── utils/               # Utilidades y helpers
│   ├── styles/              # Estilos globales
│   └── main.tsx            # Punto de entrada
├── index.html              # Template HTML
├── vite.config.ts          # Configuración Vite
├── tailwind.config.js      # Configuración Tailwind
└── package.json           # Dependencias y scripts
```

## 🔧 Configuración

### **Variables de Entorno**

```bash
# API Configuration
VITE_API_BASE_URL=http://localhost:8080/api
VITE_API_TIMEOUT=10000

# Authentication
VITE_JWT_STORAGE_KEY=clinic_auth_token
VITE_TOKEN_REFRESH_THRESHOLD=300000

# Application
VITE_APP_NAME=Sistema de Gestión Clínica
VITE_APP_VERSION=1.0.0

# Features
VITE_ENABLE_PWA=true
VITE_ENABLE_ANALYTICS=false
VITE_ENABLE_SENTRY=true
```

### **Configuración de Tailwind**

```javascript
// tailwind.config.js
module.exports = {
  content: [
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          500: '#3b82f6',
          900: '#1e3a8a',
        },
        medical: {
          50: '#f0fdf4',
          500: '#22c55e',
          900: '#14532d',
        }
      }
    },
  },
  plugins: [],
}
```

## 🎨 Estilos y UI/UX

### **Sistema de Diseño**
- **Colores médicos** profesionales (azul médico, verde sanitario)
- **Tipografía** legible y accesible
- **Espaciado** consistente basado en 4px grid
- **Border radius** suave para elementos modernos
- **Sombras** sutiles para profundidad

### **Componentes UI**
- **Diseño responsivo** para todos los dispositivos
- **Modo oscuro/claro** automático
- **Animaciones suaves** con Framer Motion
- **Estados de carga** elegantes
- **Feedback visual** inmediato

### **Paleta de Colores**

```css
/* Colores principales */
--primary-50: #eff6ff;
--primary-500: #3b82f6;
--primary-900: #1e3a8a;

/* Colores médicos */
--medical-50: #f0fdf4;
--medical-500: #22c55e;
--medical-900: #14532d;

/* Estados */
--success: #22c55e;
--warning: #f59e0b;
--error: #ef4444;
--info: #3b82f6;
```

## 🔗 Integración con Backend

### **Cliente API**
```typescript
// src/services/api.ts
import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
});

// Interceptors para autenticación
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('clinic_auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default apiClient;
```

### **Servicios por Módulo**

```typescript
// src/services/authService.ts
export const authService = {
  async login(credentials: LoginCredentials) {
    const response = await apiClient.post('/auth/login', credentials);
    return response.data;
  },

  async logout() {
    await apiClient.post('/auth/logout');
    localStorage.removeItem('clinic_auth_token');
  },

  async refreshToken() {
    const response = await apiClient.post('/auth/refresh');
    return response.data;
  }
};
```

## 🧪 Pruebas

### **Estrategia de Testing**
- ✅ **Pruebas Unitarias** - Componentes individuales
- ✅ **Pruebas de Integración** - Servicios y hooks
- ✅ **Pruebas E2E** - Flujos completos de usuario
- ✅ **Pruebas de Accesibilidad** - WCAG 2.1 AA

### **Ejemplos de Pruebas**

```typescript
// Component test
describe('LoginForm', () => {
  it('should render form fields', () => {
    render(<LoginForm />);
    expect(screen.getByLabelText('Usuario')).toBeInTheDocument();
    expect(screen.getByLabelText('Contraseña')).toBeInTheDocument();
  });
});

// Service test
describe('authService', () => {
  it('should login successfully with valid credentials', async () => {
    const mockResponse = { token: 'jwt-token', user: mockUser };
    mocked(apiClient.post).mockResolvedValue({ data: mockResponse });

    const result = await authService.login(validCredentials);
    expect(result.token).toBe('jwt-token');
  });
});
```

## 🚢 Despliegue

### **Build para Producción**
```bash
# Build optimizado
npm run build

# Preview del build
npm run preview

# Análisis del bundle
npm run analyze
```

### **Plataformas de Despliegue**
- ✅ **Vercel** - Despliegue automático desde Git
- ✅ **Netlify** - Static site hosting
- ✅ **AWS S3 + CloudFront** - CDN global
- ✅ **Docker** - Containerización

### **Configuración de Producción**
```bash
# Variables de entorno para producción
VITE_API_BASE_URL=https://api.clinica.com
VITE_ENABLE_ANALYTICS=true
VITE_ENABLE_SENTRY=true
VITE_SENTRY_DSN=https://your-sentry-dsn
```

## 📱 Características Responsive

### **Breakpoints**
```typescript
// tailwind.config.js
screens: {
  'xs': '475px',
  'sm': '640px',
  'md': '768px',
  'lg': '1024px',
  'xl': '1280px',
  '2xl': '1536px',
}
```

### **Diseño Mobile-First**
- ✅ **Navegación táctil** optimizada
- ✅ **Formularios adaptativos** para móviles
- ✅ **Tablas responsivas** con scroll horizontal
- ✅ **Menús colapsables** para espacio limitado

## 🔒 Seguridad en Frontend

### **Medidas de Seguridad**
- ✅ **Content Security Policy** (CSP)
- ✅ **HTTPS obligatorio** en producción
- ✅ **Validación de formularios** en cliente y servidor
- ✅ **Sanitización de datos** de entrada
- ✅ **Prevención de XSS** con React escaping

### **Autenticación**
```typescript
// src/hooks/useAuth.ts
export const useAuth = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar token en localStorage
    const token = localStorage.getItem('clinic_auth_token');
    if (token) {
      // Validar token con backend
      validateToken(token).then(setUser);
    }
    setLoading(false);
  }, []);

  return { user, loading, login, logout };
};
```

## 📊 Estado de la Aplicación

### **Gestión de Estado Global**
```typescript
// src/stores/authStore.ts
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      login: async (credentials) => {
        // Lógica de login
      },
      logout: () => {
        set({ user: null, token: null, isAuthenticated: false });
      }
    }),
    {
      name: 'auth-storage',
    }
  )
);
```

## 🔄 Gestión de Estado

### **Estrategias Implementadas**
- **Zustand** para estado global complejo
- **React Query** para estado de servidor
- **Context API** para estado de aplicación
- **Local Storage** para persistencia

## 🌐 Internacionalización

### **Configuración i18n**
```typescript
// src/i18n/config.ts
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

i18n.use(initReactI18next).init({
  resources: {
    es: {
      translation: {
        "welcome": "Bienvenido",
        "login": "Iniciar Sesión",
        "patients": "Pacientes"
      }
    },
    en: {
      translation: {
        "welcome": "Welcome",
        "login": "Login",
        "patients": "Patients"
      }
    }
  },
  lng: 'es',
  fallbackLng: 'es',
  interpolation: {
    escapeValue: false
  }
});
```

## ♿ Accesibilidad

### **Características de Accesibilidad**
- ✅ **WCAG 2.1 AA** compliance
- ✅ **Navegación por teclado** completa
- ✅ **Screen reader** optimización
- ✅ **Contraste de colores** adecuado
- ✅ **Etiquetas ARIA** apropiadas
- ✅ **Focus management** automático

## 👥 Equipo de Desarrollo

| Rol | Nombre | Contacto |
|-----|--------|----------|
| **Tech Lead Frontend** | Equipo Frontend | frontend@clinica.com |
| **UI/UX Designer** | Equipo Diseño | diseño@clinica.com |
| **QA Frontend** | Equipo QA | qa.frontend@clinica.com |

## 🤝 Contribución

### **Flujo de Trabajo**
1. Crear rama desde `develop`
2. Desarrollar funcionalidad
3. Ejecutar pruebas y linting
4. Crear Pull Request a `develop`
5. Code review por al menos 2 desarrolladores
6. Merge después de aprobación

### **Convenciones de Código**
- **Commits**: Conventional Commits
- **Ramas**: `feature/`, `bugfix/`, `hotfix/`
- **PRs**: Template estructurado con descripción, pruebas y capturas

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

---

<div align="center">

**🌐 Sistema de Gestión Clínica - Frontend**

*Interfaz moderna y accesible para la gestión médica*

[📧 Contacto](mailto:frontend@clinica.com) • [🐛 Reportar Bug](https://github.com/clinica/frontend/issues) • [📖 Storybook](https://storybook.clinica.com)

</div>