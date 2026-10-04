package com.transucv.controllers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionFlotaControllerTest {

    private static final String ARCHIVO_PRUEBA = "datosFlota.txt";
    private GestionFlotaController controller;

    @BeforeEach
    void setUp() {
        limpiarArchivoPrueba();
        controller = new GestionFlotaController();
    }

    @AfterEach
    void tearDown() {
        limpiarArchivoPrueba();
    }

    private void limpiarArchivoPrueba() {
        File archivo = new File(ARCHIVO_PRUEBA);
        if (archivo.exists()) {
            archivo.delete();
        }
    }

    // --- Pruebas de Registro (Casos Felices y Límites) ---

    @Test
    @DisplayName("Debe registrar exitosamente una unidad con todos los datos válidos")
    void test_registrarNuevaUnidad_exitoso() {
        // Arrange
        String placa = "UCV-101";
        String modelo = "Encava 2020";
        String capacidad = "32";
        String estado = "Operativo";

        // Act
        String resultado = controller.registrarNuevaUnidad(placa, modelo, capacidad, estado);

        // Assert
        assertEquals("Unidad registrada exitosamente.", resultado);
        assertEquals(estado, controller.obtenerEstadoUnidad(placa));
    }

    @ParameterizedTest
    @CsvSource({
        "'', 'Encava', '30', 'Operativo'",
        "'UCV-102', '', '30', 'Operativo'",
        "'UCV-103', 'Encava', '', 'Operativo'",
        "'UCV-104', 'Encava', '30', ''"
    })
    @DisplayName("Debe rechazar el registro si algún campo requerido está vacío")
    void test_registrarNuevaUnidad_falla_si_campo_esta_vacio(String placa, String modelo, String cap, String estado) {
        // Act
        String resultado = controller.registrarNuevaUnidad(placa, modelo, cap, estado);

        // Assert
        assertEquals("Error: Todos los campos son obligatorios.", resultado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-45", "-2147483648"})
    @DisplayName("Debe rechazar el registro si la capacidad es menor o igual a cero")
    void test_registrarNuevaUnidad_falla_si_capacidad_es_cero_o_negativa(String capacidadInvalida) {
        // Act
        String resultado = controller.registrarNuevaUnidad("UCV-200", "Yutong", capacidadInvalida, "Operativo");

        // Assert
        assertEquals("Error: La capacidad debe ser un número positivo.", resultado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"diez", "40.5", "cero", "12a", " ", "--5"})
    @DisplayName("Debe rechazar el registro si la capacidad no tiene formato entero válido")
    void test_registrarNuevaUnidad_falla_si_capacidad_no_es_numerica(String formatoInvalido) {
        // Act
        String resultado = controller.registrarNuevaUnidad("UCV-300", "Yutong", formatoInvalido, "Operativo");

        // Assert
        assertEquals("Error: La capacidad debe ser un número válido.", resultado);
    }

    @Test
    @DisplayName("Debe aceptar la capacidad mínima válida positiva (1)")
    void test_registrarNuevaUnidad_limite_capacidad_minima_positiva() {
        // Act
        String resultado = controller.registrarNuevaUnidad("MIN-01", "MiniBus", "1", "Operativo");

        // Assert
        assertEquals("Unidad registrada exitosamente.", resultado);
    }

    @Test
    @DisplayName("Debe aceptar el límite superior entero de capacidad (Integer.MAX_VALUE)")
    void test_registrarNuevaUnidad_limite_capacidad_maxima_entera() {
        // Act
        String resultado = controller.registrarNuevaUnidad("MAX-01", "MegaBus", String.valueOf(Integer.MAX_VALUE), "Operativo");

        // Assert
        assertEquals("Unidad registrada exitosamente.", resultado);
    }

    @Test
    @DisplayName("Debe impedir el registro de una placa duplicada")
    void test_registrarNuevaUnidad_falla_si_placa_ya_esta_registrada() {
        // Arrange
        controller.registrarNuevaUnidad("DUP-123", "Modelo A", "20", "Operativo");

        // Act
        String resultadoDuplicado = controller.registrarNuevaUnidad("DUP-123", "Modelo B", "40", "Mantenimiento");

        // Assert
        assertEquals("Error: La placa ya está registrada.", resultadoDuplicado);
    }

    @ParameterizedTest
    @CsvSource({
        "NULL, 'Modelo', '30', 'Operativo'",
        "'UCV-01', NULL, '30', 'Operativo'",
        "'UCV-02', 'Modelo', NULL, 'Operativo'",
        "'UCV-03', 'Modelo', '30', NULL"
    })
    @DisplayName("Documentar fallo de diseño: entradas null arrojan NullPointerException")
    void test_registrarNuevaUnidad_lanza_npe_si_parametro_es_nulo(String placa, String modelo, String cap, String estado) {
        // Reemplazar el marcador literal "NULL" por null real
        String p = "NULL".equals(placa) ? null : placa;
        String m = "NULL".equals(modelo) ? null : modelo;
        String c = "NULL".equals(cap) ? null : cap;
        String e = "NULL".equals(estado) ? null : estado;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> controller.registrarNuevaUnidad(p, m, c, e));
    }

    // --- Pruebas de Consulta y Búsqueda ---

    @Test
    @DisplayName("Debe retornar el estado exacto de una unidad registrada previamente")
    void test_obtenerEstadoUnidad_retorna_estado_correcto() {
        // Arrange
        controller.registrarNuevaUnidad("BUS-555", "Marcopolo", "45", "En Taller");

        // Act
        String estadoObtenido = controller.obtenerEstadoUnidad("BUS-555");

        // Assert
        assertEquals("En Taller", estadoObtenido);
    }

    @Test
    @DisplayName("Debe encontrar la unidad independientemente de mayúsculas o minúsculas en la placa")
    void test_obtenerEstadoUnidad_insensible_a_mayusculas_minusculas() {
        // Arrange
        controller.registrarNuevaUnidad("bus-abc", "Iveco", "24", "Activo");

        // Act
        String estado = controller.obtenerEstadoUnidad("BUS-ABC");

        // Assert
        assertEquals("Activo", estado);
    }

    @Test
    @DisplayName("Debe retornar null cuando se consulta una placa que no existe")
    void test_obtenerEstadoUnidad_retorna_null_si_no_existe() {
        // Act
        String estado = controller.obtenerEstadoUnidad("INEXISTENTE-999");

        // Assert
        assertNull(estado);
    }

    @Test
    @DisplayName("Debe retornar lista vacía si el archivo físico aún no ha sido creado")
    void test_obtenerTodasLasUnidades_retorna_lista_vacia_si_archivo_no_existe() {
        // Act
        List<String[]> unidades = controller.obtenerTodasLasUnidades();

        // Assert
        assertNotNull(unidades);
        assertTrue(unidades.isEmpty());
    }

    @Test
    @DisplayName("El constructor debe hidratar las placas registradas si el archivo ya tiene datos preexistentes")
    void test_constructor_carga_datos_existentes_desde_archivo() throws IOException {
        // Arrange: Escribir directamente en el archivo simulando ejecuciones anteriores
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_PRUEBA))) {
            pw.println("PRE-001,Volvo,50,Operativo");
            pw.println("PRE-002,Scania,55,En Reparacion");
        }

        // Act: Instanciar un nuevo controlador sobre el archivo existente
        GestionFlotaController nuevoController = new GestionFlotaController();

        // Assert: La placa precargada no debe permitirse registrar de nuevo
        String resultadoRegistro = nuevoController.registrarNuevaUnidad("PRE-001", "Volvo", "50", "Operativo");
        assertEquals("Error: La placa ya está registrada.", resultadoRegistro);
    }
}