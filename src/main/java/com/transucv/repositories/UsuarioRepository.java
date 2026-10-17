package com.transucv.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.transucv.models.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    private final List<Usuario> usuarios;
    private final String rutaArchivo;
    private final Gson gson;

    // Constructor por defecto que usa el archivo en la raíz del proyecto
    public UsuarioRepository() {
        this("usuarios.json");
    }

    // Sobrecarga útil para pasarle un archivo de prueba en los tests unitarios
    public UsuarioRepository(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        // setPrettyPrinting() formatea el JSON con sangrías legibles
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.usuarios = cargarDesdeArchivo();
    }

    public boolean existeCorreo(String correo) {
        if (correo == null) return false;

        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())) {
                return true;
            }
        }
        return false;
    }

    public void guardar(Usuario usuario) {
        if (usuario != null) {
            usuarios.add(usuario);
            guardarEnArchivo(); // Sincroniza en disco tras cada inserción
        }
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        if (correo == null) return Optional.empty();

        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }

    // --- Métodos privados de Persistencia (I/O) ---

    private List<Usuario> cargarDesdeArchivo() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            // TypeToken es necesario para que Gson reconozca tipos genéricos (List<Usuario>)
            Type tipoLista = new TypeToken<ArrayList<Usuario>>() {}.getType();
            List<Usuario> datos = gson.fromJson(reader, tipoLista);
            return (datos != null) ? datos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios desde JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void guardarEnArchivo() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en JSON: " + e.getMessage());
        }
    }
}