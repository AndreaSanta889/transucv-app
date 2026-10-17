package com.transucv.models;

public class Tarifa {

    private String tipoRuta;
    private String tipoUsuario;
    private double monto;

    public Tarifa(String tipoRuta, String tipoUsuario, double monto) {
        if (tipoRuta == null || tipoRuta.isBlank()) {
            throw new IllegalArgumentException("El tipo de ruta es obligatorio");
        }
        if (tipoUsuario == null || tipoUsuario.isBlank()) {
            throw new IllegalArgumentException("El tipo de usuario es obligatorio");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (esRutaUrbana(tipoRuta) && monto > 0) {
            throw new IllegalArgumentException("Las rutas urbanas son gratuitas y no pueden generar cobro");
        }

        this.tipoRuta = tipoRuta.trim();
        this.tipoUsuario = tipoUsuario.trim();
        // Garantía de dominio: si es urbana, el monto siempre se fija en 0.0
        this.monto = esRutaUrbana(this.tipoRuta) ? 0.0 : monto;
    }

    public static boolean esRutaUrbana(String ruta) {
        return ruta != null && ruta.trim().equalsIgnoreCase("Urbana");
    }

    public String getTipoRuta() {
        return tipoRuta;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public double getMonto() {
        return esRutaUrbana(tipoRuta) ? 0.0 : monto;
    }

    public void setMonto(double nuevoMonto) {
        if (nuevoMonto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (esRutaUrbana(this.tipoRuta) && nuevoMonto > 0) {
            throw new IllegalArgumentException("Las rutas urbanas no generan cobro");
        }
        this.monto = esRutaUrbana(this.tipoRuta) ? 0.0 : nuevoMonto;
    }
}

