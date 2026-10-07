package com.transucv.views;

import com.transucv.controllers.PanelRuta;
import com.transucv.exceptions.CredencialesInvalidasException;
import com.transucv.models.Usuario;
import com.transucv.services.AuthServicio;
import com.transucv.views.ControlItinerariosView;
import com.transucv.views.GestionFlotaView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame implements PanelRuta.VistaNavegacion {

    private final Color AZUL_OBSCURO = new Color(10, 25, 47);
    private final Color AZUL_BOTON = new Color(0, 33, 71);
    private final Color AZUL_HOVER = new Color(2, 48, 102);
    private final Color BORDE_INPUT = new Color(226, 232, 240);
    private final Color TEXTO_MUTED = new Color(100, 116, 139);

    private JTextField txtCorreo;
    private JPasswordField txtPassword;

    private final AuthServicio authServicio;
    private final PanelRuta router;

    public LoginView(AuthServicio authServicio) {
        this.authServicio = authServicio;
        this.router = new PanelRuta(this);

        setTitle("TransUCV - Autenticación Institucional");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(crearPanelIzquierdo());
        mainPanel.add(crearPanelDerecho());

        add(mainPanel);
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setBackground(AZUL_OBSCURO);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblLogo = new JLabel("🚌 TransUCV");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("<html><body style='width: 250px;'><h2>Sistema Integral de Gestión</h2></body></html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel("<html><body style='width: 250px;'>Plataforma centralizada para la administración de flotas, itinerarios y reservas de la comunidad universitaria.</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(203, 213, 225));

        centerPanel.add(lblTitulo);
        centerPanel.add(lblDesc);

        panel.add(lblLogo, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(35, 45, 35, 45));

        JLabel lblAuth = new JLabel("Autenticación Institucional");
        lblAuth.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblAuth.setForeground(AZUL_OBSCURO);
        lblAuth.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Ingrese sus credenciales para acceder al sistema.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(TEXTO_MUTED);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblCorreo = new JLabel("Correo Institucional");
        lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCorreo = new JTextField();
        estilarInput(txtCorreo);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        estilarInput(txtPassword);

        JButton btnIngresar = new JButton("Ingresar al Sistema →");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(AZUL_BOTON);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnIngresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnIngresar.setBackground(AZUL_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btnIngresar.setBackground(AZUL_BOTON); }
        });

        btnIngresar.addActionListener(e -> ejecutarLogin());

        JLabel lblRegistro = new JLabel("<html>¿No tienes cuenta? <font color='#002147'><b>Regístrate aquí</b></font></html>");
        lblRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegistro.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegistroView(authServicio).setVisible(true);
                dispose();
            }
        });

        panel.add(lblAuth);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblCorreo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(txtCorreo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblPass);
        panel.add(Box.createVerticalStrut(6));
        panel.add(txtPassword);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnIngresar);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblRegistro);

        return panel;
    }

    private void estilarInput(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDE_INPUT, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void ejecutarLogin() {
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtPassword.getPassword());

        try {
            Usuario usuario = authServicio.iniciarSesion(correo, pass);
            router.redirigir(usuario);
            this.dispose();
        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos de redirección del PanelRuta (VistaNavegacion)
    @Override
    public void abrirPanelEstudiante(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Bienvenido al Panel de Estudiante: " + usuario.getNombre());
    }

    @Override
    public void abrirPanelEmpleado(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Bienvenido al Panel de Empleado: " + usuario.getNombre());
    }

    @Override
    public void abrirPanelProfesor(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Bienvenido al Panel de Profesor: " + usuario.getNombre());
    }

    @Override
    public void abrirPanelAdministrador(Usuario usuario) {
        JOptionPane.showMessageDialog(this, "Bienvenido al Panel de Administrador: " + usuario.getNombre());

        GestionFlotaView ventanaFlota = new GestionFlotaView(); 
        ventanaFlota.setVisible(true);
        ControlItinerariosView ventanaItinerarios = new ControlItinerariosView();
        ventanaItinerarios.setVisible(true);
        dispose();
    }
}
