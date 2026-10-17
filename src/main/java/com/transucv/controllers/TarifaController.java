package com.transucv.controllers;

import com.transucv.models.Tarifa;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarifaController {

    private final List<Tarifa> tarifasRegistradas;
    private final String archivoDatos = "tarifas.txt";

    public TarifaController() {
        this.tarifasRegistradas = new ArrayList<>();
        cargarDatosDesdeArchivo();
    }

    public String guardarTarifa(String tipoRuta, String tipoUsuario, String textoMonto) {
        if (tipoRuta == null || tipoRuta.isBlank() ||
            tipoUsuario == null || tipoUsuario.isBlank() ||
            textoMonto == null || textoMonto.isBlank()) {
            return "Error: Todos los campos son obligatorios.";
        }

        double monto;
        try {
            monto = Double.parseDouble(textoMonto.trim());
            if (Double.isNaN(monto) || Double.isInfinite(monto)) {
                return "Error: El monto ingresado no es un número válido.";
            }
        } catch (NumberFormatException e) {
            return "Error: El monto de la tarifa debe ser un número mayor o igual a cero.";
        }

        if (monto < 0) {
            return "Error: El monto de la tarifa debe ser un número mayor o igual a cero.";
        }

        // Regla de negocio: Las rutas urbanas no generan cobro
        if (Tarifa.esRutaUrbana(tipoRuta) && monto > 0) {
            return "Error: Las rutas urbanas son gratuitas y no generan cobro (el monto debe ser 0).";
        }

        actualizarOGuardarEnMemoria(tipoRuta.trim(), tipoUsuario.trim(), monto);
        reescribirArchivo();

        return "Tarifa para ruta " + tipoRuta.trim() + " (" + tipoUsuario.trim() + ") actualizada correctamente.";
    }

    /**
     * Devuelve el monto a cobrar según la ruta y el tipo de usuario.
     * Garantiza que cualquier ruta Urbana retorne siempre 0.0 Bs.
     */
    public double obtenerMontoCobro(String tipoRuta, String tipoUsuario) {
        if (Tarifa.esRutaUrbana(tipoRuta)) {
            return 0.0;
        }

        Optional<Tarifa> tarifa = buscarTarifa(tipoRuta, tipoUsuario);
        return tarifa.map(Tarifa::getMonto).orElse(0.0);
    }

    public Optional<Tarifa> buscarTarifa(String tipoRuta, String tipoUsuario) {
        if (tipoRuta == null || tipoUsuario == null) return Optional.empty();

        for (Tarifa t : tarifasRegistradas) {
            if (t.getTipoRuta().equalsIgnoreCase(tipoRuta.trim()) &&
                t.getTipoUsuario().equalsIgnoreCase(tipoUsuario.trim())) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public List<Tarifa> obtenerTodasLasTarifas() {
        return new ArrayList<>(tarifasRegistradas);
    }

    private void actualizarOGuardarEnMemoria(String tipoRuta, String tipoUsuario, double monto) {
        Optional<Tarifa> existente = buscarTarifa(tipoRuta, tipoUsuario);
        if (existente.isPresent()) {
            existente.get().setMonto(monto);
        } else {
            tarifasRegistradas.add(new Tarifa(tipoRuta, tipoUsuario, monto));
        }
    }

    private void cargarDatosDesdeArchivo() {
        File archivo = new File(archivoDatos);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    String ruta = datos[0];
                    String usuario = datos[1];
                    double monto = Double.parseDouble(datos[2]);
                    // Si en el archivo manipularon una ruta urbana con monto > 0, se corrige a 0.0
                    if (Tarifa.esRutaUrbana(ruta)) {
                        monto = 0.0;
                    }
                    tarifasRegistradas.add(new Tarifa(ruta, usuario, monto));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer el archivo de tarifas: " + e.getMessage());
        }
    }

    private void reescribirArchivo() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(archivoDatos, false)))) {
            for (Tarifa t : tarifasRegistradas) {
                pw.println(t.getTipoRuta() + "," + t.getTipoUsuario() + "," + t.getMonto());
            }
            pw.flush();
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de tarifas: " + e.getMessage());
        }
    }
}