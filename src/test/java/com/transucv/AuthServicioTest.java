package com.transucv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.transucv.exceptions.CredencialesInvalidasException;
import com.transucv.services.AuthServicio;
import com.transucv.models.Usuario;
import com.transucv.repositories.UsuarioRepository;
import com.transucv.models.Rol;

import java.util.Optional;

public class AuthServicioTest {

    private UsuarioRepository repositorioFalso;
    private AuthServicio authServicio;

    @BeforeEach
    public void prepararPrueba() {
    
        repositorioFalso = new UsuarioRepository() {
    
            private Usuario usuarioGuardado = null;

            @Override

            public void guardar(Usuario usuario) {
                this.usuarioGuardado = usuario;
            }

            @Override
 
            public Optional buscarPorCorreo(String correo) {
 
                if (usuarioGuardado != null && usuarioGuardado.getCorreo().equals(correo)) {
                    return Optional.of(usuarioGuardado);
                }
 
                return Optional.empty();
 
            }

            @Override
 
            public boolean existeCorreo(String correo) {
 
                return usuarioGuardado != null && usuarioGuardado.getCorreo().equals(correo);
 
            }
 
        };

        authServicio = new AuthServicio(repositorioFalso);
        authServicio.registrarUsuario("Cesar Herrera", "cesar@transucv.com", "secreta123", Rol.ADMINISTRADOR); 
    }
    
    @Test
    public void testInicioDeSesionExitoso() {
        try {
            Usuario usuarioLogueado = authServicio.iniciarSesion("cesar@transucv.com", "secreta123");
        
            assertNotNull(usuarioLogueado, "El usuario no debería ser nulo");
        
            assertEquals("cesar@transucv.com", usuarioLogueado.getCorreo());

        } catch (CredencialesInvalidasException e) {
        
            fail("No debería lanzar excepción con los datos correctos");
        
        }
    }

    @Test
    public void testInicioDeSesionContrasenaIncorrecta() {
     
        assertThrows(CredencialesInvalidasException.class, () -> {
     
            authServicio.iniciarSesion("cesar@transucv.com", "claveEquivocada");
     
        });
    
    }

    @Test
    
    public void testInicioDeSesionUsuarioNoExiste() {
     
        assertThrows(CredencialesInvalidasException.class, () -> {
     
            authServicio.iniciarSesion("fantasma@transucv.com", "secreta123");
     
        });
    
    }

}