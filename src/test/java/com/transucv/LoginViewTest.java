package com.transucv.views;

import com.transucv.exceptions.CredencialesInvalidasException;
import com.transucv.models.Rol;
import com.transucv.models.Usuario;
import com.transucv.services.AuthServicio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginViewTest {

    private AuthServicio authServicioMock;
    private LoginView vista;

    @BeforeAll
    static void initHeadlessMode() {
        System.setProperty("java.awt.headless", "false");
    }

    @BeforeEach
    void setUp() {
        authServicioMock = mock(AuthServicio.class);
        vista = new LoginView(authServicioMock);
    }

    @AfterEach
    void tearDown() {
        if (vista != null) {
            vista.dispose();
        }
    }

    @Test
    @DisplayName("Debe verificar el estado inicial de la pantalla de autenticación")
    void test_login_estado_inicial_componentes() throws Exception {
        // Arrange & Act
        JTextField txtCorreo = (JTextField) getPrivateField(vista, "txtCorreo");
        JPasswordField txtPass = (JPasswordField) getPrivateField(vista, "txtPassword");

        // Assert
        assertEquals("", txtCorreo.getText());
        assertEquals(0, txtPass.getPassword().length);
    }

    @ParameterizedTest
    @CsvSource({
        "'', ''",
        "'   ', ''",
        "'carlos@ucv.ve', ''",
        "'', '123456'"
    })
    @DisplayName("No debe invocar el servicio de autenticación si faltan campos obligatorios")
    void test_login_bloqueado_si_credenciales_estan_vacias(String correo, String pass) throws Exception {
        // Arrange
        setLoginInputs(correo, pass);

        // Act
        invokeEjecutarLogin(vista);

        // Assert
        verify(authServicioMock, never()).iniciarSesion(anyString(), anyString());
    }

    @Test
    @DisplayName("Debe autenticar con éxito y cerrar la vista de login al redirigir")
    void test_login_exitoso_autentica_y_cierra_pantalla() throws Exception {
        // Arrange
        String correo = "carlos@ucv.ve";
        String pass = "Secreta123";
        Usuario usuarioMock = new Usuario(UUID.randomUUID().toString(), "Carlos", correo, pass, Rol.ADMINISTRADOR);

        when(authServicioMock.iniciarSesion(correo, pass)).thenReturn(usuarioMock);
        setLoginInputs(correo, pass);

        // Act
        invokeEjecutarLogin(vista);

        // Assert
        verify(authServicioMock, times(1)).iniciarSesion(correo, pass);
        assertFalse(vista.isDisplayable(), "La vista de login debe destruirse tras la autenticación exitosa");
    }

    @Test
    @DisplayName("Debe capturar CredencialesInvalidasException sin colapsar la interfaz")
    void test_login_captura_credenciales_invalidas_exception() throws Exception {
        // Arrange
        String correo = "desconocido@ucv.ve";
        String pass = "erronea";

        when(authServicioMock.iniciarSesion(correo, pass)).thenThrow(new CredencialesInvalidasException());
        setLoginInputs(correo, pass);

        // Act & Assert
        assertDoesNotThrow(() -> invokeEjecutarLogin(vista));
        verify(authServicioMock, times(1)).iniciarSesion(correo, pass);
        assertTrue(vista.isDisplayable(), "La ventana debe permanecer abierta tras un intento fallido");
    }

    // --- Métodos Auxiliares de Reflexión para Swing ---
    private void setLoginInputs(String correo, String pass) throws Exception {
        ((JTextField) getPrivateField(vista, "txtCorreo")).setText(correo);
        ((JPasswordField) getPrivateField(vista, "txtPassword")).setText(pass);
    }

    private void invokeEjecutarLogin(LoginView instance) throws Exception {
        Method method = LoginView.class.getDeclaredMethod("ejecutarLogin");
        method.setAccessible(true);
        method.invoke(instance);
    }

    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}