package com.transucv.controllers;

import com.transucv.models.Monedero;
import com.transucv.models.Rol;
import com.transucv.services.MonederoServicio;
import com.transucv.services.TarifaServicio;
import com.transucv.views.PagoPasajeView;
import com.transucv.views.RecargaMonederoView;

import java.math.BigDecimal;

public class PagoPasajeController {

    private final PagoPasajeView vista;
    private final MonederoServicio monederoServicio;
    private final TarifaServicio tarifaServicio;
    private final String idUsuarioActual;
    private final Rol rolUsuarioActual;

    public PagoPasajeController(PagoPasajeView vista, MonederoServicio monederoServicio, 
                                TarifaServicio tarifaServicio, String idUsuarioActual, Rol rolUsuarioActual) {
        this.vista = vista;
        this.monederoServicio = monederoServicio;
        this.tarifaServicio = tarifaServicio;
        this.idUsuarioActual = idUsuarioActual;
        this.rolUsuarioActual = rolUsuarioActual;

        actualizarDatosUsuario();

        cargarItinerariosDisponibles();

        this.vista.setPagarListener(e -> procesarPago());
        this.vista.setCambioRutaListener(e -> actualizarTarifaAlCambiarRuta());
        this.vista.setIrRecarga(e -> abrirVistaRecarga());
        this.vista.setCancelarListener(e -> vista.cerrarVista());
    }

    private void actualizarDatosUsuario() {
        Monedero monedero = monederoServicio.obtenerOCrearMonedero(idUsuarioActual);
        vista.setDatosUsuario(monedero.getSaldo().toString(), rolUsuarioActual.toString());
    }

    private void cargarItinerariosDisponibles() {
        String[] itinerarios = {
            "IT-101 | Guarenas -> Plaza Venezuela (07:00 AM)",
            "IT-102 | Chacaito -> Plaza Venezuela (08:00 AM)",
            "IT-103 | Plaza Venezuela -> Guatire (09:30 AM)",  
            "IT-104 | La Bandera -> Plaza Venezuela (10:00 AM)",         
            "IT-105 | Plaza Venezuela -> Los Teques (11:00 AM)",         
            "IT-106 | Charallave -> Plaza Venezuela (11:30 AM)",
            "IT-107 | Baruta -> Plaza Venezuela (01:00 PM)",
            "IT-108 | Plaza Venezuela -> Valles del Tuy (02:30 PM)",
            "IT-109 | Plaza Venezuela -> Guatire (04:00 PM)",
            "IT-109 | El Valle -> Plaza Venezuela (05:00 PM)"

        };
        vista.cargarItinerarios(itinerarios);
    }

    private void actualizarTarifaAlCambiarRuta() {
        String itinerario = vista.getItinerarioSeleccionado();
        if (itinerario == null || "Seleccione un Itinerario".equalsIgnoreCase(itinerario)) {
            vista.setTarifaCalculada("Seleccione una ruta");
            return;
        }

        BigDecimal tarifa = tarifaServicio.obtenerTarifaVigente(rolUsuarioActual);
        vista.setTarifaCalculada(tarifa.toString());
    }

    private void procesarPago() {
        String itinerario = vista.getItinerarioSeleccionado();

        if (itinerario == null || "Seleccione un Itinerario".equalsIgnoreCase(itinerario)) {
            vista.mostrarMensaje("Debe seleccionar un itinerario válido para continuar", "Atención", true);
            return;
        }

        String resultado = monederoServicio.pagarPasaje(
            idUsuarioActual, 
            rolUsuarioActual, 
            "Extraurbana", 
            itinerario.split(" \\| ")[0], 
            itinerario
        );

        if (resultado.startsWith("Pago exitoso")) {
            vista.mostrarMensaje(resultado, "Comprobante de Pago", false);
            actualizarDatosUsuario();
        } else {
            vista.mostrarMensaje(resultado, "Error en Pago", true);
        }
    }

    private void abrirVistaRecarga() {
        RecargaMonederoView vistaRecarga = new RecargaMonederoView();
        new MonederoController(vistaRecarga, monederoServicio, idUsuarioActual);
        vistaRecarga.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            TarifaServicio tarifaServicio = new TarifaServicio();
            MonederoServicio monederoServicio = new MonederoServicio(tarifaServicio);
            
            monederoServicio.recargarMonedero("EST-001", "Banesco", "04121234567", "28111222", "REF-001", new BigDecimal("50.00"));

            PagoPasajeView vista = new PagoPasajeView();
            new PagoPasajeController(vista, monederoServicio, tarifaServicio, "EST-001", Rol.ESTUDIANTE);

            vista.setVisible(true);
        });
    }
}
