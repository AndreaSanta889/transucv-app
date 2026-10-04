package com.transucv.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestionFlotaController {
    private List<String> placasRegistradas;
    
    private final String archivoDatos ="datosFlota.txt";

    public GestionFlotaController() {
        placasRegistradas = new ArrayList<>();
        cargarDatosDesdeArchivo();
    }

    private void cargarDatosDesdeArchivo() {
        File archivo = new File(archivoDatos);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    if (datos.length >= 1) {
                        placasRegistradas.add(datos[0]);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
            }
        } else {
            return;
        }
    }

    public List<String[]> obtenerTodasLasUnidades() {
        List<String[]> unidades = new ArrayList<>();
        File archivo = new File(archivoDatos);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    unidades.add(linea.split(","));
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
            }
        }
        return unidades;
    }

    public String registrarNuevaUnidad(String placa, String modelo, String capTexto, String estado){

        if (placa.isEmpty() || modelo.isEmpty() || capTexto.isEmpty() || estado.isEmpty()) {
            return "Error: Todos los campos son obligatorios.";
        }

        int capacidad;
        try {
            capacidad = Integer.parseInt(capTexto);
            if (capacidad <= 0) {
                return "Error: La capacidad debe ser un número positivo.";
            }
      } catch (NumberFormatException e) {
            return "Error: La capacidad debe ser un número válido.";
        }

        if (placasRegistradas.contains(placa)) {
            return "Error: La placa ya está registrada.";
        }

        placasRegistradas.add(placa);
        guardarEnArchivo(placa, modelo, capTexto, estado);
        return "Unidad registrada exitosamente.";
    }

    public String obtenerEstadoUnidad(String placaBuscada) {
        
        List<String[]> unidades = obtenerTodasLasUnidades();

        for (String[] unidad : unidades) {
            
            if (unidad[0].equalsIgnoreCase(placaBuscada)) {
                return unidad[3]; 
            }

        }
        return null;

    }

    public void guardarEnArchivo(String placa, String modelo, String capTexto, String estado){
        try (FileWriter fw = new FileWriter(archivoDatos, true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(placa + "," + modelo + "," + capTexto + "," + estado);
            pw.flush();
            pw.close();

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }

    }

}
