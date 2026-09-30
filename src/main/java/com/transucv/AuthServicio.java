package main.java.com.transucv;

import main.java.com.transucv.ExcepcionesCredenciales;
import main.java.com.transucv.Rol;
import main.java.com.transucv.Usuario;
import main.java.com.transucv.UsuarioRepositorio;
import java.util.UUID; // Una clase de java para dar identificadores unicos

public class AuthServicio 
{
public class AuthServicio 
{
    private final UsuarioRepositorio UsuarioRepositorio;

    public AuthService(UsuarioRepositorio UsuarioRepositorio) 
    {
        this.UsuarioRepositorio = UsuarioRepositorio;
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
        if (contrasena == null || contrasena.length() < 6) 
        {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        if (rol == null) 
        {
            throw new IllegalArgumentException("Debe asignar un rol válido al usuario");
        }
        if (UsuarioRepositorio.existeCorreo(correo)) 
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

        usuarioRepository.guardar(nuevoUsuario);
        return nuevoUsuario;
    }

    public Usuario iniciarSesion(String correo, String contra) throws CredencialesInvalidasException 
    {
        if (correo == null || contra == null) 
        {
            throw new CredencialesInvalidasException();
        }

        return usuarioRepository.buscarPorCorreo(correo)
            .filter(u -> u.getContrasena().equals(contra))
            .orElseThrow(CredencialesInvalidasException::new);
    }
}

}
