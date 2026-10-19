package com.transucv;

import com.transucv.models.Monedero;
import com.transucv.models.Rol;
import com.transucv.models.TipoTransaccion;
import com.transucv.services.MonederoServicio;
import com.transucv.services.TarifaServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MonederoServicioTest {

    private TarifaServicio tarifaServicio;
    private MonederoServicio monederoServicio;

    @BeforeEach
    void setUp() {
        tarifaServicio = new TarifaServicio();
        monederoServicio = new MonederoServicio(tarifaServicio);
    }

    // HU-005: PRUEBAS DE RECARGA DE MONEDERO (PAGO MÓVIL)

    @Test
    @DisplayName("HU-005: Debe realizar una recarga exitosa y actualizar el saldo e historial")
    void test_hu005_recargarMonedero_exitoso() {
    
        String idUsuario = "user-01";
        BigDecimal montoRecarga = new BigDecimal("50.00");

        String respuesta = monederoServicio.recargarMonedero(
                idUsuario, "Banco de Venezuela", "04121234567", "V-28123456", "REF-1001", montoRecarga
        );

        assertEquals("Recarga exitosa. Saldo disponible: Bs. 50.00", respuesta);
        Monedero monedero = monederoServicio.obtenerOCrearMonedero(idUsuario);
        assertEquals(new BigDecimal("50.00"), monedero.getSaldo());
        assertEquals(1, monedero.getHistorial().size());
        assertEquals(TipoTransaccion.RECARGA, monedero.getHistorial().get(0).getTipo());
        assertEquals("REF-1001", monedero.getHistorial().get(0).getReferencia());
    }

    @Test
    @DisplayName("HU-005: Debe rechazar la recarga si el número de referencia está duplicado")
    void test_hu005_recargarMonedero_falla_si_referencia_es_duplicada() {
        monederoServicio.recargarMonedero(
                "user-01", "Banesco", "04147654321", "V-25000111", "REF-9999", new BigDecimal("20.00")
        );

        String respuestaDuplicada = monederoServicio.recargarMonedero(
                "user-02", "Mercantil", "04241112233", "V-19888777", "  REF-9999  ", new BigDecimal("100.00")
        );

        assertEquals("El número de referencia ya fue registrado", respuestaDuplicada);
        assertEquals(BigDecimal.ZERO, monederoServicio.obtenerOCrearMonedero("user-02").getSaldo());
    }

    @ParameterizedTest
    @CsvSource({
        "'', '04121234567', 'V-12345678', 'REF-01'",
        "'   ', '04121234567', 'V-12345678', 'REF-02'",
        "'Banesco', '', 'V-12345678', 'REF-03'",
        "'Banesco', '04121234567', '', 'REF-04'",
        "'Banesco', '04121234567', 'V-12345678', ''"
    })
    @DisplayName("HU-005: Debe rechazar la recarga si algún campo obligatorio está vacío o en blanco")
    void test_hu005_recargarMonedero_falla_si_campos_son_vacios(String banco, String telefono, String cedula, String ref) {

        String respuesta = monederoServicio.recargarMonedero(
                "user-01", banco, telefono, cedula, ref, new BigDecimal("30.00")
        );

        assertEquals("Todos los campos del pago móvil son obligatorios", respuesta);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "02121234567",  // No empieza por 04
        "0412123456",   // Menos de 11 dígitos (10)
        "041212345678", // Más de 11 dígitos (12)
        "0412123456a",  // Contiene letras
        "4121234567"    // Sin el cero inicial
    })
    @DisplayName("HU-005: Debe rechazar la recarga si el formato del teléfono es inválido")
    void test_hu005_recargarMonedero_falla_si_formato_telefono_es_invalido(String telefonoInvalido) {

        String respuesta = monederoServicio.recargarMonedero(
                "user-01", "Mercantil", telefonoInvalido, "V-20111222", "REF-500", new BigDecimal("25.00")
        );

        assertEquals("Formato de teléfono inválido. Debe ser de 11 dígitos (Ej: 04121234567)", respuesta);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.00", "-0.01", "-50.00"})
    @DisplayName("HU-005: Debe rechazar la recarga si el monto es cero o negativo")
    void test_hu005_recargarMonedero_falla_si_monto_es_cero_o_negativo(String montoTexto) {
   
        BigDecimal montoInvalido = new BigDecimal(montoTexto);

        String respuesta = monederoServicio.recargarMonedero(
                "user-01", "Provincial", "04169876543", "V-15222333", "REF-700", montoInvalido
        );


        assertEquals("El monto de la recarga debe ser mayor a cero", respuesta);
    }

    @Test
    @DisplayName("HU-005: Debe rechazar la recarga si el monto es nulo")
    void test_hu005_recargarMonedero_falla_si_monto_es_nulo() {

        String respuesta = monederoServicio.recargarMonedero(
                "user-01", "Provincial", "04169876543", "V-15222333", "REF-701", null
        );

        assertEquals("El monto de la recarga debe ser mayor a cero", respuesta);
    }

    // HU-006: PRUEBAS DE PAGO DE PASAJE, SALDO INSUFICIENTE Y COBRO POR ROL
    @Test
    @DisplayName("HU-006: Debe procesar el pago exitoso en ruta extraurbana y descontar el saldo")
    void test_hu006_pagarPasaje_exitoso_descuenta_saldo_y_registra_historial() {
        
        String idUsuario = "est-01";
        monederoServicio.recargarMonedero(
                idUsuario, "Venezuela", "04120001122", "V-29000111", "REF-PAGO-1", new BigDecimal("20.00")
        );

        String respuesta = monederoServicio.pagarPasaje(
                idUsuario, Rol.ESTUDIANTE, "Extraurbana", "ITIN-01", "Caracas - Guarenas"
        );
        
        assertEquals("Pago exitoso. Tarifa cobrada: Bs. 5.00. Saldo restante: Bs 15.00", respuesta);
        Monedero monedero = monederoServicio.obtenerOCrearMonedero(idUsuario);
        assertEquals(new BigDecimal("15.00"), monedero.getSaldo());
        assertEquals(2, monedero.getHistorial().size()); // 1 recarga + 1 pago
        assertEquals(TipoTransaccion.PAGO_PASAJE, monedero.getHistorial().get(1).getTipo());
    }

    @Test
    @DisplayName("HU-006: Debe bloquear el pago si el usuario tiene saldo insuficiente")
    void test_hu006_pagarPasaje_bloquea_si_saldo_es_insuficiente() {
        // Estudiante necesita 5.00 Bs, pero solo recarga 4.50 Bs
        String idUsuario = "est-sin-saldo";
        monederoServicio.recargarMonedero(
                idUsuario, "Banesco", "04149998877", "V-30111222", "REF-PAGO-2", new BigDecimal("4.50")
        );

        String respuesta = monederoServicio.pagarPasaje(
                idUsuario, Rol.ESTUDIANTE, "Extraurbana", "ITIN-02", "Caracas - La Guaira"
        );

        assertEquals("Saldo insuficiente. Recargue su monedero para continuar", respuesta);
        // Verificar que no se descontó nada del saldo
        assertEquals(new BigDecimal("4.50"), monederoServicio.obtenerOCrearMonedero(idUsuario).getSaldo());
    }

    @ParameterizedTest
    @CsvSource({
        "ESTUDIANTE, 5.00, 45.00",
        "EMPLEADO, 10.00, 40.00",
        "PROFESOR, 10.00, 40.00",
        "ADMINISTRADOR, 15.00, 35.00"
    })
    @DisplayName("HU-006: Debe aplicar el cobro diferenciado según el rol del usuario en ruta extraurbana")
    void test_hu006_pagarPasaje_cobro_diferenciado_por_rol(Rol rol, String tarifaEsperada, String saldoRestanteEsperado) {
        String idUsuario = "user-" + rol.name();
        String referencia = "REF-" + rol.name();
        monederoServicio.recargarMonedero(
                idUsuario, "Mercantil", "04241234567", "V-20000000", referencia, new BigDecimal("50.00")
        );

        
        String respuesta = monederoServicio.pagarPasaje(
                idUsuario, rol, "Extraurbana", "ITIN-03", "Caracas - Maracay"
        );

        String mensajeEsperado = "Pago exitoso. Tarifa cobrada: Bs. " + tarifaEsperada + ". Saldo restante: Bs " + saldoRestanteEsperado;
        assertEquals(mensajeEsperado, respuesta);
        assertEquals(new BigDecimal(saldoRestanteEsperado), monederoServicio.obtenerOCrearMonedero(idUsuario).getSaldo());
    }

    @Test
    @DisplayName("HU-006: Debe permitir pagar cuando el saldo es exactamente igual a la tarifa (caso límite)")
    void test_hu006_pagarPasaje_limite_saldo_exacto() {
        //Profesor requiere exactamente 10.00 Bs
        String idUsuario = "prof-exacto";
        monederoServicio.recargarMonedero(
                idUsuario, "Venezuela", "04125554433", "V-12333444", "REF-EXACTO", new BigDecimal("10.00")
        );

        String respuesta = monederoServicio.pagarPasaje(
                idUsuario, Rol.PROFESOR, "Extraurbana", "ITIN-04", "Caracas - Los Teques"
        );

        assertEquals("Pago exitoso. Tarifa cobrada: Bs. 10.00. Saldo restante: Bs 0.00", respuesta);
        assertEquals(new BigDecimal("0.00"), monederoServicio.obtenerOCrearMonedero(idUsuario).getSaldo());

    }

    @Test
    @DisplayName("HU-006: Las rutas urbanas deben ser gratuitas (0 Bs) sin descontar saldo")
    void test_hu006_pagarPasaje_ruta_urbana_es_gratuita() {
        // Arrange: Recargamos 15.00 Bs al usuario
        String idUsuario = "est-urbano";
        monederoServicio.recargarMonedero(
                idUsuario, "Venezuela", "04121112233", "V-28000111", "REF-URB-1", new BigDecimal("15.00")
        );

        // Act: Pagar pasaje especificando tipoRuta = "Urbana"
        String respuesta = monederoServicio.pagarPasaje(
                idUsuario, Rol.ESTUDIANTE, "Urbana", "ITIN-URB", "Plaza Venezuela - UCV"
        );

        // Assert: Comprobamos que el mensaje sea de ruta gratuita y el saldo permanezca intacto
        assertEquals("Pago exitoso (Ruta Gratuita). Saldo restante: Bs 15.00", respuesta);
        assertEquals(new BigDecimal("15.00"), monederoServicio.obtenerOCrearMonedero(idUsuario).getSaldo());
    }
}