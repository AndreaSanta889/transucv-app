package com.transucv.views;

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
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RegistroViewTest {

    private AuthServicio authServicioMock;
    private RegistroView vista;

    @BeforeAll
    static void initHeadlessMode() {
        // Permite la inicialización de componentes AWT en servidores/CI
        System.setProperty("java.awt.headless", "false");
    }

    @BeforeEach
    void setUp() {
        authServicioMock = mock(AuthServicio.class);
        vista = new RegistroView(authServicioMock);
    }

    @AfterEach
    void tearDown() {
        if (vista != null) {
            vista.dispose();
        }
    }

    @Test
    @DisplayName("Debe inicializar la ventana y sus campos con valores limpios")
    void test_inicializacion_componentes_correcta() throws Exception {
        // Arrange & Act
        JTextField txtNombre = (JTextField) getPrivateField(vista, "txtNombre");
        JTextField txtCorreo = (JTextField) getPrivateField(vista, "txtCorreo");
        JPasswordField txtPass = (JPasswordField) getPrivateField(vista, "txtPassword");
        @SuppressWarnings("unchecked")
        JComboBox<Rol> cbRol = (JComboBox<Rol>) getPrivateField(vista, "cbRol");

        // Assert
        assertEquals("", txtNombre.getText());
        assertEquals("", txtCorreo.getText());
        assertEquals(0, txtPass.getPassword().length);
        assertNotNull(cbRol.getSelectedItem());
        assertEquals(Rol.values().length, cbRol.getItemCount());
    }

    @ParameterizedTest
    @CsvSource({
        "'', 'usuario@ucv.ve', '123456'",
        "'   ', 'usuario@ucv.ve', '123456'",
        "'Carlos Hidalgo', '', '123456'",
        "'Carlos Hidalgo', '   ', '123456'"
    })
    @DisplayName("No debe invocar el servicio si existen campos en blanco")
    void test_registro_bloqueado_si_campos_estan_vacios(String nombre, String correo, String pass) throws Exception {
        // Arrange
        setFormValues(nombre, correo, pass, Rol.ESTUDIANTE);

        // Act
        invokeEjecutarRegistro(vista);

        // Assert
        verify(authServicioMock, never()).registrarUsuario(anyString(), anyString(), anyString(), any(Rol.class));
    }

    @Test
    @DisplayName("Debe procesar el registro y delegar en AuthServicio con los datos exactos")
    void test_registro_exitoso_delega_en_servicio_y_cierra_ventana() throws Exception {
        // Arrange
        String nombre = "Carlos Hidalgo";
        String correo = "carlos@ucv.ve";
        String pass = "ClaveSegura123";
        Rol rol = Rol.ESTUDIANTE;

        setFormValues(nombre, correo, pass, rol);

        Usuario usuarioCreado = new Usuario(UUID.randomUUID().toString(), nombre, correo, pass, rol);
        when(authServicioMock.registrarUsuario(nombre, correo, pass, rol)).thenReturn(usuarioCreado);

        // Act
        invokeEjecutarRegistro(vista);

        // Assert
        verify(authServicioMock, times(1)).registrarUsuario(nombre, correo, pass, rol);
        assertFalse(vista.isDisplayable(), "La vista de registro debió cerrarse tras el registro exitoso");
    }

    @Test
    @DisplayName("Debe manejar IllegalArgumentException del servicio sin propagar el error")
    void test_registro_captura_error_validacion_negocio() throws Exception {
        // Arrange
        setFormValues("Carlos", "correo-invalido", "123", Rol.ESTUDIANTE);
        doThrow(new IllegalArgumentException("El formato del correo es inválido"))
            .when(authServicioMock).registrarUsuario(anyString(), anyString(), anyString(), any(Rol.class));

        // Act & Assert
        assertDoesNotThrow(() -> invokeEjecutarRegistro(vista));
        verify(authServicioMock, times(1)).registrarUsuario(anyString(), anyString(), anyString(), any(Rol.class));
    }

    @Test
    @DisplayName("Debe manejar IllegalStateException cuando el correo ya existe")
    void test_registro_captura_error_correo_duplicado() throws Exception {
        // Arrange
        setFormValues("Carlos", "carlos@ucv.ve", "123456", Rol.ADMINISTRADOR);
        doThrow(new IllegalStateException("El correo ya se encuentra registrado"))
            .when(authServicioMock).registrarUsuario(anyString(), anyString(), anyString(), any(Rol.class));

        // Act & Assert
        assertDoesNotThrow(() -> invokeEjecutarRegistro(vista));
        verify(authServicioMock, times(1)).registrarUsuario(anyString(), anyString(), anyString(), any(Rol.class));
    }

    // --- Métodos Auxiliares de Reflexión para Swing ---
    private void setFormValues(String nombre, String correo, String pass, Rol rol) throws Exception {
        ((JTextField) getPrivateField(vista, "txtNombre")).setText(nombre);
        ((JTextField) getPrivateField(vista, "txtCorreo")).setText(correo);
        ((JPasswordField) getPrivateField(vista, "txtPassword")).setText(pass);
        @SuppressWarnings("unchecked")
        JComboBox<Rol> cb = (JComboBox<Rol>) getPrivateField(vista, "cbRol");
        cb.setSelectedItem(rol);
    }

    private void invokeEjecutarRegistro(RegistroView instance) throws Exception {
        Method method = RegistroView.class.getDeclaredMethod("ejecutarRegistro");
        method.setAccessible(true);
        method.invoke(instance);
    }

    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}