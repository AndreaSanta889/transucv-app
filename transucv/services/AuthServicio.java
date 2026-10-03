package com.transucv.services;

import com.transucv.models.Rol;
import com.transucv.models.Usuario;
import com.transucv.repositories.UsuarioRepository;
import com.transucv.exceptions.CredencialesInvalidasException;
import java.util.UUID; // Una clase de java para dar identificadores unicos


public class AuthServicio 
{
    private final UsuarioRepository usuarioRepositorio;

    public AuthServicio(UsuarioRepository usuarioRepositorio) 
    {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Usuario registrarUsuario(String nombre, String correo, String contra, Rol rol) 
    {
        if (nombre == null || nombre.isBlank())
        {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (correo == null || !correo.matches("^\\w+@\\w+\\.com$")) 
        {
            throw new IllegalArgumentException("El formato del correo es inválido");
        }
        if (contra == null || contra.length() < 6) 
        {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        if (rol == null) 
        {
            throw new IllegalArgumentException("Debe asignar un rol válido al usuario");
        }
        if (usuarioRepositorio.existeCorreo(correo)) 
        {
            throw new IllegalStateException("El correo ya se encuentra registrado");
        }

        Usuario nuevoUsuario = new Usuario
        (
            UUID.randomUUID().toString(), //Transformar lo numeros del UUID en 36 caracteres
            nombre.trim(),
            correo.trim(),
            contra,
            rol
        );

        usuarioRepositorio.guardar(nuevoUsuario);
        return nuevoUsuario;
    }

    public Usuario iniciarSesion(String correo, String contra) throws CredencialesInvalidasException
    {
        if (correo == null || contra == null) 
        {
            throw new CredencialesInvalidasException();
        }

        return usuarioRepositorio.buscarPorCorreo(correo)
            .filter(u -> u.getContra().equals(contra))
            .orElseThrow(CredencialesInvalidasException::new);
    }
}


