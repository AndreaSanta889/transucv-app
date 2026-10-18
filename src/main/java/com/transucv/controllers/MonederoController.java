package com.transucv.controllers;

import com.transucv.models.Monedero;
import com.transucv.services.MonederoServicio;
import com.transucv.views.RecargaMonederoView;

import javax.swing.JOptionPane;
import java.math.BigDecimal;

public class MonederoController {

    private final RecargaMonederoView vista;
    private final MonederoServicio monederoServicio;
    private final String idUsuarioActual;

    public MonederoController(RecargaMonederoView vista, MonederoServicio monederoServicio, String idUsuarioActual) {
        this.vista = vista;
        this.monederoServicio = monederoServicio;
        this.idUsuarioActual = idUsuarioActual;

        // Inicializar la vista con el saldo actual del usuario
        actualizarSaldoVista();

        // Conectar los eventos de la vista con la lógica del controlador
        this.vista.setRecargarListener(e -> procesarRecarga());
        this.vista.setCancelarListener(e -> cancelarOperacion());
    }

    //Procesa la recarga capturando los datos desde la vista y validándolos con el servicio
    
    private void procesarRecarga() {
        String banco = vista.getBancoSeleccionado();
        String telefono = vista.getTelefono();
        String cedula = vista.getCedula();
        String referencia = vista.getReferencia();
        String montoStr = vista.getMonto();

        // Validar selección de banco
        if ("Seleccione".equalsIgnoreCase(banco)) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un banco emisor", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convertir y validar monto numérico
        BigDecimal monto;
        try {
            monto = new BigDecimal(montoStr.replace(",", "."));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese un monto numérico válido", "Error de Monto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String resultado = monederoServicio.recargarMonedero(idUsuarioActual, banco, telefono, cedula, referencia, monto);

        if (resultado.startsWith("Recarga exitosa")) {
            JOptionPane.showMessageDialog(vista, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarSaldoVista();
            vista.limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(vista, resultado, "Error en Recarga", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarSaldoVista() {
        Monedero monedero = monederoServicio.obtenerOCrearMonedero(idUsuarioActual);
        vista.setSaldoActual(monedero.getSaldo().toString());
    }

    private void cancelarOperacion() {
        int opcion = JOptionPane.showConfirmDialog(vista, 
            "¿Desea cancelar la operación de recarga?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcion == JOptionPane.YES_OPTION) {
            vista.dispose();
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            
            com.transucv.services.TarifaServicio tarifaServicio = new com.transucv.services.TarifaServicio();
            com.transucv.services.MonederoServicio monederoServicio = new com.transucv.services.MonederoServicio(tarifaServicio);

            com.transucv.views.RecargaMonederoView vista = new com.transucv.views.RecargaMonederoView();

            new MonederoController(vista, monederoServicio, "EST-001");

            vista.setVisible(true);
        });
    }
}
