# Sistema de Gestión Clínica - Frontend

Una aplicación web moderna y profesional desarrollada con HTML, CSS y JavaScript para la gestión integral de información de una clínica médica.

## 🚀 Características Principales

- **Interfaz moderna y responsiva** con diseño profesional
- **Autenticación basada en roles** con diferentes niveles de acceso
- **Gestión de usuarios** para Recursos Humanos
- **Registro y gestión de pacientes** para Personal Administrativo
- **Historias clínicas digitales** para Médicos
- **Control de visitas y signos vitales** para Enfermeras
- **Sistema de inventario** para Soporte de Información
- **Integración completa con API REST** de Spring Boot

## 📋 Roles y Funcionalidades

### 👥 Recursos Humanos
- Crear, editar y eliminar usuarios
- Gestionar permisos y roles
- Activar/desactivar cuentas de usuario
- Ver estadísticas de personal

### 🏢 Personal Administrativo
- Registrar nuevos pacientes
- Programar citas médicas
- Gestionar información de contacto de emergencia
- Procesar pólizas de seguros médicos
- Generar facturación

### 👨‍⚕️ Médicos
- Crear y actualizar historias clínicas
- Registrar diagnósticos y tratamientos
- Recetar medicamentos y procedimientos
- Solicitar ayudas diagnósticas
- Gestionar órdenes médicas

### 👩‍⚕️ Enfermeras
- Registrar visitas de pacientes
- Tomar signos vitales
- Administrar medicamentos
- Registrar procedimientos realizados
- Documentar observaciones

### 🔧 Soporte de Información
- Gestionar inventario de medicamentos
- Controlar procedimientos disponibles
- Administrar ayudas diagnósticas
- Mantenimiento de datos maestros

## 🛠️ Tecnologías Utilizadas

- **HTML5** - Estructura semántica y accesible
- **CSS3** - Estilos modernos con variables CSS y diseño responsivo
- **JavaScript ES6+** - Lógica de aplicación con módulos y clases
- **Font Awesome** - Iconografía profesional
- **Google Fonts (Inter)** - Tipografía moderna y legible

## 📁 Estructura del Proyecto

```
frontend/
├── index.html              # Página principal
├── css/
│   ├── styles.css          # Estilos principales
│   ├── components.css      # Estilos de componentes
│   └── responsive.css      # Media queries responsivas
├── js/
│   ├── api-service.js      # Servicio de comunicación con API
│   ├── auth-service.js     # Gestión de autenticación
│   ├── notification-service.js # Sistema de notificaciones
│   ├── main.js             # Inicialización de la aplicación
│   ├── user-management.js  # Gestión de usuarios (RR.HH.)
│   ├── patient-management.js # Gestión de pacientes
│   ├── medical-records.js  # Historias clínicas
│   └── inventory-management.js # Gestión de inventario
├── components/             # Componentes reutilizables
├── assets/
│   └── images/            # Imágenes y recursos gráficos
└── README.md              # Esta documentación
```

## 🚀 Instalación y Uso

### Prerrequisitos

- Navegador web moderno (Chrome, Firefox, Safari, Edge)
- Servidor backend API REST ejecutándose en `http://localhost:8080`

### Instalación

1. **Clonar o descargar** el proyecto en tu directorio de trabajo
2. **Abrir el archivo `index.html`** en un navegador web
3. **Opcional**: Servir los archivos usando un servidor local para desarrollo

### Usando un servidor local

```bash
# Usando Python 3
python -m http.server 8000

# Usando Node.js (con http-server instalado)
npx http-server

# Usando PHP
php -S localhost:8000
```

Luego abrir `http://localhost:8000` en el navegador.

## 🔧 Configuración

### Configuración de la API

El archivo `js/api-service.js` contiene la configuración de conexión con el backend:

```javascript
// Configuración de la URL base de la API
this.baseURL = 'http://localhost:8080/api';
```

Para cambiar el servidor backend, modificar esta línea según sea necesario.

### Variables CSS Personalizables

El archivo `css/styles.css` incluye variables CSS que pueden personalizarse:

```css
:root {
    --primary-color: #2563eb;
    --success-color: #10b981;
    --warning-color: #f59e0b;
    --error-color: #ef4444;
    /* ... más variables */
}
```

## 📱 Características Responsivas

La aplicación está diseñada para funcionar perfectamente en:

- **Escritorio** (1200px+)
- **Tabletas** (768px - 1199px)
- **Móviles grandes** (481px - 767px)
- **Móviles pequeños** (320px - 480px)

## 🎨 Características de UI/UX

### Diseño Moderno
- Interfaz limpia y profesional
- Colores consistentes y accesibles
- Tipografía legible (Inter font family)
- Iconografía clara con Font Awesome

### Experiencia de Usuario
- Navegación intuitiva basada en roles
- Feedback visual inmediato
- Notificaciones contextuales
- Formularios validados

### Accesibilidad
- Soporte para lectores de pantalla
- Contraste adecuado según estándares WCAG
- Navegación por teclado
- Estados focus visibles

## 🔐 Seguridad

- **Autenticación basada en sesiones** con manejo seguro de tokens
- **Autorización por roles** con control granular de permisos
- **Validación de formularios** tanto en cliente como servidor
- **Manejo seguro de errores** sin exposición de información sensible

## 🔄 Integración con API

La aplicación se integra completamente con la API REST de Spring Boot:

### Endpoints Principales
- `GET /api/public/health` - Verificación de estado
- `GET /api/users/**` - Gestión de usuarios
- `GET /api/patients/**` - Gestión de pacientes
- `POST /api/medical-records` - Historias clínicas
- `POST /api/orders` - Órdenes médicas
- `POST /api/patient-visits` - Visitas de pacientes

## 📊 Funcionalidades Implementadas

### ✅ Completadas
- [x] Estructura de proyecto profesional
- [x] Diseño responsivo moderno
- [x] Sistema de autenticación completo
- [x] Navegación basada en roles
- [x] Gestión de usuarios (Recursos Humanos)
- [x] Sistema de notificaciones
- [x] Integración con API REST
- [x] Manejo de errores robusto

### 🚧 En Desarrollo
- [ ] Gestión de pacientes (Personal Administrativo)
- [ ] Historias clínicas (Médicos)
- [ ] Visitas de pacientes (Enfermeras)
- [ ] Sistema de inventario (Soporte)
- [ ] Módulo de facturación
- [ ] Sistema de citas médicas

## 🐛 Manejo de Errores

La aplicación incluye manejo robusto de errores:

- **Errores de red**: Reintentos automáticos y mensajes informativos
- **Errores de validación**: Feedback inmediato en formularios
- **Errores de autenticación**: Redirección automática al login
- **Errores inesperados**: Logging y notificaciones al usuario

## 📈 Mejores Prácticas Implementadas

### Código
- **Modularidad**: Código organizado en módulos y servicios
- **Reutilización**: Componentes y funciones reutilizables
- **Mantenibilidad**: Código limpio y bien documentado
- **Performance**: Carga diferida y optimizaciones

### Seguridad
- **Validación estricta**: Tanto en cliente como servidor
- **Sanitización**: Limpieza de datos de entrada
- **Autenticación segura**: Uso de tokens y sesiones
- **Autorización**: Control de acceso basado en roles

### UX/UI
- **Consistencia**: Diseño uniforme en toda la aplicación
- **Accesibilidad**: Cumplimiento de estándares WCAG
- **Responsividad**: Adaptación perfecta a todos los dispositivos
- **Feedback**: Información clara sobre acciones realizadas

## 🤝 Contribución

Para contribuir al proyecto:

1. **Fork** el proyecto
2. **Crear** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** la rama (`git push origin feature/AmazingFeature`)
5. **Abrir** un Pull Request

## 📝 Licencia

Este proyecto es desarrollado para fines educativos y de demostración.

## 👨‍💻 Desarrollador

**Santiago Suaza**
- Ingeniería de Software Senior
- Especialista en aplicaciones web modernas
- Más de 5 años de experiencia en desarrollo frontend

---

🏥 **Sistema de Gestión Clínica** - Solución profesional para clínicas modernas