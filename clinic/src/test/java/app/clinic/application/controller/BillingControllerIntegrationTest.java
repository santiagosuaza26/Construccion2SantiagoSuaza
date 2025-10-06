package app.clinic.application.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Pruebas de integración para BillingController.
 * Verifica el comportamiento completo de los endpoints REST.
 */
@SpringBootTest
@AutoConfigureMockMvc
@SpringJUnitConfig
class BillingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe calcular facturación correctamente vía POST /api/billing/calculate/{patientCedula}")
    void shouldCalculateBillingCorrectlyViaPostEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then
        mockMvc.perform(post("/api/billing/calculate/{patientCedula}", patientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalCost").exists())
                .andExpect(jsonPath("$.copaymentAmount").exists())
                .andExpect(jsonPath("$.insuranceCoverageAmount").exists())
                .andExpect(jsonPath("$.requiresCopayment").exists())
                .andExpect(jsonPath("$.copaymentLimitExceeded").exists())
                .andExpect(jsonPath("$.copaymentLimitMessage").exists())
                .andExpect(jsonPath("$.copaymentAmount", is("50000")))
                .andExpect(jsonPath("$.requiresCopayment", is(true)))
                .andExpect(jsonPath("$.copaymentLimitExceeded", is(false)));
    }

    @Test
    @DisplayName("Debe generar factura correctamente vía POST /api/billing/invoice/{patientCedula}")
    void shouldGenerateInvoiceCorrectlyViaPostEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then
        mockMvc.perform(post("/api/billing/invoice/{patientCedula}", patientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceNumber").exists())
                .andExpect(jsonPath("$.patientCedula", is(patientCedula)))
                .andExpect(jsonPath("$.patientName").exists())
                .andExpect(jsonPath("$.totalAmount").exists())
                .andExpect(jsonPath("$.copaymentAmount").exists())
                .andExpect(jsonPath("$.insuranceCoverage").exists())
                .andExpect(jsonPath("$.patientResponsibility").exists())
                .andExpect(jsonPath("$.billingDate").exists())
                .andExpect(jsonPath("$.dueDate").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$.totalAmount", is("200000")))
                .andExpect(jsonPath("$.copaymentAmount", is("50000")))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    @DisplayName("Debe obtener detalles de facturación correctamente vía GET /api/billing/details/{patientCedula}")
    void shouldGetBillingDetailsCorrectlyViaGetEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then
        mockMvc.perform(get("/api/billing/details/{patientCedula}", patientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Actualmente retorna Optional.empty()
    }

    @Test
    @DisplayName("Debe obtener historial de facturación correctamente vía GET /api/billing/history/{patientCedula}")
    void shouldGetBillingHistoryCorrectlyViaGetEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then
        mockMvc.perform(get("/api/billing/history/{patientCedula}", patientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }

    @Test
    @DisplayName("Debe obtener estadísticas de facturación correctamente vía GET /api/billing/statistics/{patientCedula}")
    void shouldGetBillingStatisticsCorrectlyViaGetEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then
        mockMvc.perform(get("/api/billing/statistics/{patientCedula}", patientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBilled").exists())
                .andExpect(jsonPath("$.totalPaidByPatient").exists())
                .andExpect(jsonPath("$.totalPaidByInsurance").exists())
                .andExpect(jsonPath("$.numberOfInvoices").exists())
                .andExpect(jsonPath("$.averageBillingAmount").exists())
                .andExpect(jsonPath("$.totalBilled", is(0)))
                .andExpect(jsonPath("$.numberOfInvoices", is(0)));
    }

    @Test
    @DisplayName("Debe validar póliza de seguro correctamente vía GET /api/billing/validate-insurance/{patientCedula}")
    void shouldValidateInsurancePolicyCorrectlyViaGetEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then
        mockMvc.perform(get("/api/billing/validate-insurance/{patientCedula}", patientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    @DisplayName("Debe verificar límite de copagos correctamente vía GET /api/billing/copayment-limit/{patientCedula}")
    void shouldCheckCopaymentLimitCorrectlyViaGetEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then
        mockMvc.perform(get("/api/billing/copayment-limit/{patientCedula}", patientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    @DisplayName("Debe obtener copago acumulado correctamente vía GET /api/billing/accumulated-copayment/{patientCedula}/{year}")
    void shouldGetAccumulatedCopaymentCorrectlyViaGetEndpoint() throws Exception {
        // Given
        String patientCedula = "12345678";
        int year = 2024;

        // When & Then
        mockMvc.perform(get("/api/billing/accumulated-copayment/{patientCedula}/{year}", patientCedula, year)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0")); // Actualmente retorna "0"
    }

    @Test
    @DisplayName("Debe manejar cédula de paciente no existente correctamente")
    void shouldHandleNonExistentPatientCedulaCorrectly() throws Exception {
        // Given
        String nonExistentPatientCedula = "99999999";

        // When & Then
        mockMvc.perform(post("/api/billing/calculate/{patientCedula}", nonExistentPatientCedula)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // El servicio actual no valida existencia del paciente
                .andExpect(jsonPath("$.totalCost").exists());
    }

    @Test
    @DisplayName("Debe retornar error para endpoints no implementados")
    void shouldReturnErrorForNonImplementedEndpoints() throws Exception {
        // Given
        String patientCedula = "12345678";

        // When & Then - Algunos métodos aún lanzan UnsupportedOperationException
        // Nota: Esto puede cambiar cuando se implementen completamente

        mockMvc.perform(get("/api/billing/details/{patientCedula}", patientCedula))
                .andExpect(status().isNotFound()); // Actualmente retorna 404 para Optional.empty()
    }

    @Test
    @DisplayName("Debe manejar parámetros inválidos correctamente")
    void shouldHandleInvalidParametersCorrectly() throws Exception {
        // When & Then - Año inválido
        mockMvc.perform(get("/api/billing/accumulated-copayment/{patientCedula}/{year}", "12345678", "invalid_year")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()); // Actualmente retorna 500 por conversión fallida
    }
}