package com.transucv.controllers;

import com.transucv.models.Rol;
import com.transucv.models.Usuario;

public class PanelRuta {

    public interface VistaNavegacion {
        void abrirPanelEstudiante(Usuario usuario);
        void abrirPanelEmpleado(Usuario usuario);
        void abrirPanelProfesor(Usuario usuario);
        void abrirPanelAdministrador(Usuario usuario);
    }

    private final VistaNavegacion vistaNavegacion;

    public PanelRuta(VistaNavegacion vistaNavegacion) {
        this.vistaNavegacion = vistaNavegacion;
    }

    public void redirigir(Usuario usuario) {
        if (usuario == null || usuario.getRol() == null) {
            throw new IllegalArgumentException("Usuario no autenticado o sin rol asignado");
        }

        switch (usuario.getRol()) {
            case ESTUDIANTE:
                vistaNavegacion.abrirPanelEstudiante(usuario);
                break;
            case EMPLEADO:
                vistaNavegacion.abrirPanelEmpleado(usuario);
                break;
            case PROFESOR:
                vistaNavegacion.abrirPanelProfesor(usuario);
                break;
            case ADMINISTRADOR:
                vistaNavegacion.abrirPanelAdministrador(usuario);
                break;
            default:
                throw new IllegalArgumentException("Rol no soportado: " + usuario.getRol());
        }
    }
}