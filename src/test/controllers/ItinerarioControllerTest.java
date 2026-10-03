package com.transucv.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItinerarioControllerTest {

    private ItinerarioController controller;

    @BeforeEach
    public void setUp() {
        controller = new ItinerarioController();
    }

    @Test
    public void testValidarEstadoUnidadInoperativa() {
        String errorMantenimiento = controller.validarEstadoUnidad("En Mantenimiento");
        assertEquals("No se puede asignar un itinerario a una unidad inoperativa", errorMantenimiento);

        String errorFueraServicio = controller.validarEstadoUnidad("Fuera de Servicio");
        assertEquals("No se puede asignar un itinerario a una unidad inoperativa", errorFueraServicio);
    }

    @Test
    public void testValidarEstadoUnidadValido() {
        String resultado = controller.validarEstadoUnidad("Operativo");
        assertNull(resultado, "El estado operativo debe ser válido (retornar null).");
    }

    @Test
    public void testHorarioInicioPosteriorAFin() {
        String error = controller.validarCruceHorarios("BUS-01", "10:00 AM", "08:00 AM");
        assertEquals("La hora de inicio debe ser estrictamente anterior a la hora de fin.", error);
    }

    @Test
    public void testFormatoHoraInvalido() {
        String error = controller.validarCruceHorarios("BUS-01", "25:00", "08:00 AM");
        assertTrue(error.contains("Formato de hora inválido"));
    }
}
