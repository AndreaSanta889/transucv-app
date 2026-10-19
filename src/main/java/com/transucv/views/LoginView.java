package com.transucv.views;

import com.transucv.controllers.PanelRuta;
import com.transucv.exceptions.CredencialesInvalidasException;
import com.transucv.models.Usuario;
import com.transucv.services.AuthServicio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame implements PanelRuta.VistaNavegacion {

    private final Color azulFondo = new Color(10, 25, 47);
    private final Color azulBoton = new Color(0, 33, 71);
    private final Color azulResaltado = new Color(2, 48, 102);
    private final Color bordeGris = new Color(226, 232, 240);
    private final Color textoGris = new Color(100, 116, 139);

    private JTextField campoCorreo;
    private JPasswordField campoPassword;

    private final AuthServicio authServicio;
    private final PanelRuta enrutador;

    public LoginView(AuthServicio authServicio) {
        this.authServicio = authServicio;
        this.enrutador = new PanelRuta(this);

        setTitle("TransUCV - Autenticación Institucional");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));
        panelPrincipal.add(crearParteIzquierda());
        panelPrincipal.add(crearParteDerecha());

        add(panelPrincipal);
    }

    private JPanel crearParteIzquierda() {
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(azulFondo);
        panelIzquierdo.setLayout(new BorderLayout(20, 20));
        panelIzquierdo.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel tituloLogo = new JLabel("🚌 TransUCV");
        tituloLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        tituloLogo.setForeground(Color.WHITE);

        JPanel panelTextos = new JPanel(new GridLayout(4, 1, 5, 5));
        panelTextos.setOpaque(false);

        JLabel subtitulo1 = new JLabel("Sistema Integral");
        subtitulo1.setFont(new Font("SansSerif", Font.BOLD, 24));
        subtitulo1.setForeground(Color.WHITE);
        
        JLabel subtitulo2 = new JLabel("de Gestión");
        subtitulo2.setFont(new Font("SansSerif", Font.BOLD, 24));
        subtitulo2.setForeground(Color.WHITE);

        JLabel desc1 = new JLabel("Plataforma centralizada para la administración");
        desc1.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc1.setForeground(new Color(203, 213, 225));
        
        JLabel desc2 = new JLabel("de flotas, itinerarios y reservas.");
        desc2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc2.setForeground(new Color(203, 213, 225));

        panelTextos.add(subtitulo1);
        panelTextos.add(subtitulo2);
        panelTextos.add(desc1);
        panelTextos.add(desc2);

        panelIzquierdo.add(tituloLogo, BorderLayout.NORTH);
        panelIzquierdo.add(panelTextos, BorderLayout.SOUTH);

        return panelIzquierdo;
    }

    private JPanel crearParteDerecha() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new EmptyBorder(35, 45, 35, 45));

        JLabel tituloLogin = new JLabel("Autenticación Institucional");
        tituloLogin.setFont(new Font("SansSerif", Font.BOLD, 20));
        tituloLogin.setForeground(azulFondo);
        tituloLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtituloLogin = new JLabel("Ingrese sus credenciales para acceder.");
        subtituloLogin.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtituloLogin.setForeground(textoGris);
        subtituloLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelCorreo = new JLabel("Correo Institucional");
        labelCorreo.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoCorreo = new JTextField();
        darFormatoCampo(campoCorreo);

        JLabel labelPass = new JLabel("Contraseña");
        labelPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoPassword = new JPasswordField();
        darFormatoCampo(campoPassword);

        JButton botonEntrar = new JButton("Ingresar al Sistema →");
        botonEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        botonEntrar.setForeground(Color.WHITE);
        botonEntrar.setBackground(azulBoton);
        botonEntrar.setFocusPainted(false);
        botonEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonEntrar.setMaximumSize(new Dimension(350, 42));
        botonEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        botonEntrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { 
                botonEntrar.setBackground(azulResaltado); 
            }
            @Override
            public void mouseExited(MouseEvent e) { 
                botonEntrar.setBackground(azulBoton); 
            }
        });

        botonEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hacerLogin();
            }
        });

        JLabel textoRegistro = new JLabel("¿No tienes cuenta? Registrate aquí");
        textoRegistro.setFont(new Font("SansSerif", Font.BOLD, 12));
        textoRegistro.setForeground(azulBoton);
        textoRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        textoRegistro.setAlignmentX(Component.LEFT_ALIGNMENT);

        textoRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Abriendo ventana de registro...");
                new RegistroView(authServicio).setVisible(true);
                dispose();
            }
        });

        panelDerecho.add(tituloLogin);
        panelDerecho.add(Box.createVerticalStrut(4));
        panelDerecho.add(subtituloLogin);
        panelDerecho.add(Box.createVerticalStrut(20));
        
        panelDerecho.add(labelCorreo);
        panelDerecho.add(Box.createVerticalStrut(6));
        panelDerecho.add(campoCorreo);
        panelDerecho.add(Box.createVerticalStrut(12));
        
        panelDerecho.add(labelPass);
        panelDerecho.add(Box.createVerticalStrut(6));
        panelDerecho.add(campoPassword);
        panelDerecho.add(Box.createVerticalStrut(20));
        
        panelDerecho.add(botonEntrar);
        panelDerecho.add(Box.createVerticalStrut(15));
        panelDerecho.add(textoRegistro);

        return panelDerecho;
    }

    private void darFormatoCampo(JTextField cajaTexto) {
        cajaTexto.setMaximumSize(new Dimension(350, 38));
        cajaTexto.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cajaTexto.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bordeGris, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        cajaTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void hacerLogin() {
        String correoIngresado = campoCorreo.getText().trim();
        String claveIngresada = new String(campoPassword.getPassword());

        try {
            Usuario usuarioActual = authServicio.iniciarSesion(correoIngresado, claveIngresada);
            enrutador.redirigir(usuarioActual);
            dispose();
        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, "Datos incorrectos: " + ex.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void abrirPanelEstudiante(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Bienvenido al Panel Estudiantil: " + usuario.getNombre());
        abrirVentanaPagoPasaje(usuario);
    }

    @Override
    public void abrirPanelEmpleado(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Bienvenido al Panel de Empleados: " + usuario.getNombre());
        abrirVentanaPagoPasaje(usuario);
    }

    @Override
    public void abrirPanelProfesor(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Bienvenido al Panel Docente: " + usuario.getNombre());
        abrirVentanaPagoPasaje(usuario);
    }

    private void abrirVentanaPagoPasaje(Usuario usuario){

        com.transucv.services.TarifaServicio tarifaServicio = new com.transucv.services.TarifaServicio();
        com.transucv.services.MonederoServicio monederoServicio = new com.transucv.services.MonederoServicio(tarifaServicio);
        com.transucv.views.PagoPasajeView vistaPago = new com.transucv.views.PagoPasajeView();

        new com.transucv.controllers.PagoPasajeController(vistaPago, monederoServicio, tarifaServicio, usuario.getId(), usuario.getRol());
        vistaPago.setVisible(true);
    }

    @Override
    public void abrirPanelAdministrador(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Sesión de Administrador iniciada: " + usuario.getNombre());

        AdminDashboardView ventanaAdmin = new AdminDashboardView(authServicio);
        ventanaAdmin.setVisible(true);
   
        dispose();
    }
}