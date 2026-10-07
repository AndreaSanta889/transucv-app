package com.transucv.controllers;

import java.time.LocalTime;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ItinerarioController {

    private List<String[]> bdTemporal = new ArrayList<>();
    private DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("h:mm a");
    private final String archivoDatos = "datosItinerarios.txt";
    
    public ItinerarioController() {
        cargarDatosDesdeArchivo();
    }

    private void cargarDatosDesdeArchivo() {
        File archivo = new File(archivoDatos);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    if (datos.length >= 3) {
                        bdTemporal.add(datos);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
            }
        } else {
            return;
        }
    }

    private void guardarDatosEnArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoDatos))) {
            for (String[] itinerario : bdTemporal) {
                bw.write(String.join(",", itinerario));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public String validarEstadoUnidad(String estadoUnidad) {
        if (estadoUnidad == null || estadoUnidad.trim().isEmpty()) {
            return "Debe seleccionar un estado válido.";
        }

        if (estadoUnidad.equalsIgnoreCase("En Mantenimiento") || estadoUnidad.equalsIgnoreCase("Fuera de Servicio")) {
            return "No se puede asignar un itinerario a una unidad inoperativa";
        }
        
        return null; 
    }

    public String validarCruceHorarios(String idAutobus, String horaInicioNueva, String horaFinNueva) {
        try {
            LocalTime inicioNuevo = LocalTime.parse(horaInicioNueva.toUpperCase(), formatoHora);
            LocalTime finNuevo = LocalTime.parse(horaFinNueva.toUpperCase(), formatoHora);

            if (inicioNuevo.isAfter(finNuevo) || inicioNuevo.equals(finNuevo)) {
                return "La hora de inicio debe ser estrictamente anterior a la hora de fin.";
            }

            for (String[] itinerario : bdTemporal) {
                String idGuardado = itinerario[0];
                
                if (idGuardado.equalsIgnoreCase(idAutobus)) {
                    LocalTime inicioExistente = LocalTime.parse(itinerario[1].toUpperCase(), formatoHora);
                    LocalTime finExistente = LocalTime.parse(itinerario[2].toUpperCase(), formatoHora);

                    if (inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
                        return "Error: El horario choca con un itinerario ya existente para esta unidad.";
                    }
                }
            }

            return null;

        } catch (DateTimeParseException e) {
            return "Formato de hora inválido. Por favor use el formato 'h:mm AM/PM' (ej. 7:00 AM).";
        }
    }

    public String registrarNuevoItinerario(String idAutobus, String horaInicio, String horaFin) {
       
        GestionFlotaController flotaController = new GestionFlotaController();
       
        String estadoReal = flotaController.obtenerEstadoUnidad(idAutobus);
        
       if (estadoReal == null) {
            return "Error: El autobus "+idAutobus+" no está registrado en la flota.";
        } 

        if (estadoReal.equals("En Mantenimiento") || estadoReal.equals("Fuera de Servicio")) {
            return "No se puede asignar un itinerario a una unidad que está" + estadoReal + ".";
        }

        String errorHorario = validarCruceHorarios(idAutobus, horaInicio, horaFin);
        
        if (errorHorario != null) {
            return errorHorario;
        }

        String[] nuevoRegistro = {idAutobus, horaInicio, horaFin};
        bdTemporal.add(nuevoRegistro);
        guardarDatosEnArchivo();
        return "Exito";
    }

    public List<String[]> obtenerItinerariosGuardados() {
        return bdTemporal;
    }
}