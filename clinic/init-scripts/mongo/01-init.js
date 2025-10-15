// MongoDB initialization script for Clinic Management System

// Switch to the clinical history database
db = db.getSiblingDB('clinic_history');

// Create collections for clinical history
db.createCollection('clinical_records');

// Create indexes for better performance
db.clinical_records.createIndex(
    { "patientNationalId": 1, "records.date": -1 },
    { unique: true, partialFilterExpression: { "patientNationalId": { $exists: true } } }
);

// Create a sample clinical record for testing
db.clinical_records.insertOne({
    "patientNationalId": "CC-12345678",
    "records": [
        {
            "date": "2024-01-15T10:30:00Z",
            "doctorNationalId": "DOC-001",
            "diagnosis": "Hipertensión arterial leve",
            "symptoms": ["Dolor de cabeza ocasional", "Mareos matutinos"],
            "observations": "Paciente presenta presión arterial elevada. Se recomienda control regular y cambios en el estilo de vida.",
            "vitalSigns": {
                "bloodPressureSystolic": 140,
                "bloodPressureDiastolic": 90,
                "temperature": 36.5,
                "pulse": 75,
                "oxygenLevel": 98
            },
            "medications": [
                {
                    "name": "Losartán 50mg",
                    "dosage": "1 tableta al día",
                    "duration": "30 días",
                    "frequency": "Cada 24 horas"
                }
            ],
            "procedures": [
                {
                    "name": "Control de presión arterial",
                    "type": "Monitoreo",
                    "notes": "Control semanal durante el primer mes"
                }
            ],
            "diagnosticAids": [
                {
                    "name": "Hemograma completo",
                    "results": "Dentro de parámetros normales",
                    "notes": "No se encontraron anomalías significativas"
                }
            ],
            "followUp": {
                "nextAppointment": "2024-02-15T10:30:00Z",
                "reason": "Control de hipertensión",
                "notes": "Evaluar respuesta al tratamiento"
            }
        },
        {
            "date": "2024-02-15T10:30:00Z",
            "doctorNationalId": "DOC-001",
            "diagnosis": "Hipertensión arterial controlada",
            "symptoms": ["Mejoría en síntomas"],
            "observations": "Paciente muestra buena respuesta al tratamiento. Presión arterial estable.",
            "vitalSigns": {
                "bloodPressureSystolic": 130,
                "bloodPressureDiastolic": 85,
                "temperature": 36.4,
                "pulse": 72,
                "oxygenLevel": 99
            },
            "medications": [
                {
                    "name": "Losartán 50mg",
                    "dosage": "1 tableta al día",
                    "duration": "30 días más",
                    "frequency": "Cada 24 horas"
                }
            ],
            "procedures": [
                {
                    "name": "Control de presión arterial",
                    "type": "Monitoreo",
                    "notes": "Control mensual a partir de ahora"
                }
            ],
            "followUp": {
                "nextAppointment": "2024-03-15T10:30:00Z",
                "reason": "Control de hipertensión",
                "notes": "Continuar monitoreo"
            }
        }
    ],
    "createdAt": new Date(),
    "updatedAt": new Date()
});

// Create user for application access
db.createUser({
    user: "clinic_app",
    pwd: "clinic_password_2024",
    roles: [
        {
            role: "readWrite",
            db: "clinic_history"
        }
    ]
});

// Create additional sample records for testing
db.clinical_records.insertOne({
    "patientNationalId": "CC-87654321",
    "records": [
        {
            "date": "2024-01-20T14:15:00Z",
            "doctorNationalId": "DOC-002",
            "diagnosis": "Diabetes tipo 2",
            "symptoms": ["Sed excesiva", "Fatiga", "Visión borrosa"],
            "observations": "Paciente diagnosticado con diabetes tipo 2. Se inicia tratamiento y educación sobre manejo de la condición.",
            "vitalSigns": {
                "bloodPressureSystolic": 135,
                "bloodPressureDiastolic": 88,
                "temperature": 36.7,
                "pulse": 80,
                "oxygenLevel": 97
            },
            "medications": [
                {
                    "name": "Metformina 500mg",
                    "dosage": "1 tableta dos veces al día",
                    "duration": "90 días",
                    "frequency": "Cada 12 horas"
                }
            ],
            "procedures": [
                {
                    "name": "Educación sobre diabetes",
                    "type": "Consulta educativa",
                    "notes": "Sesión informativa sobre manejo de diabetes"
                }
            ],
            "diagnosticAids": [
                {
                    "name": "Hemoglobina glicosilada",
                    "results": "7.2%",
                    "notes": "Indicativo de diabetes no controlada"
                },
                {
                    "name": "Glucemia en ayunas",
                    "results": "145 mg/dL",
                    "notes": "Por encima del rango normal"
                }
            ],
            "followUp": {
                "nextAppointment": "2024-02-20T14:15:00Z",
                "reason": "Control de diabetes",
                "notes": "Evaluar niveles de glucemia"
            }
        }
    ],
    "createdAt": new Date(),
    "updatedAt": new Date()
});

print("MongoDB initialization completed successfully!");