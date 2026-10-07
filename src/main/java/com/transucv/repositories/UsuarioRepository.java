package com.transucv.repositories;

import com.transucv.models.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository 
{
    private final List<Usuario> usuarios = new ArrayList<>();

    public boolean existeCorreo(String correo) 
    {
        if (correo == null) return false;

        for (Usuario u : usuarios) 
        {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())) //Se compara cadenas de texto si tienen lo mismo caracteres, ignora diferencias mayus y minus
                {
                return true;
                }
        }
        return false;
    }

    public void guardar(Usuario usuario) 
    {
        if (usuario != null) 
        {
            usuarios.add(usuario);
        }
    }

    public Optional<Usuario> buscarPorCorreo(String correo) 
    {
        if (correo == null) return Optional.empty();

        for (Usuario u : usuarios) 
        {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())) 
            {
                return Optional.of(u); // Devuelve en un contenedor un objeto que si existe 
            }
        }
        return Optional.empty();
    }
}
