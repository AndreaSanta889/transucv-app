package com.transucv.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame {

    private Color fondoOscuro = new Color(33, 37, 41);
    private Color bordeGris = new Color(206, 212, 218);
    private Color btnAzul = new Color(13, 110, 253);
    private Color btnAzulHover = new Color(11, 94, 215);

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRol;
    private JButton btnIngresar;
    private JButton btnIrRegistro;

    public LoginView() {
        setTitle("TransUCV - Inicio de Sesión");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(fondoOscuro);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblTitulo);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1.0;

        Font fuenteLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fuenteInput = new Font("Segoe UI", Font.PLAIN, 12);

        // Campo Usuario
        crearCampo(panelCentro, g, "Usuario / Correo", txtUsuario = new JTextField(), fuenteLabel, fuenteInput);
        
        // Campo Contraseña
        crearCampo(panelCentro, g, "Contraseña", txtPassword = new JPasswordField(), fuenteLabel, fuenteInput);

        // Rol
        JLabel lblRol = new JLabel("Tipo de Usuario");
        lblRol.setFont(fuenteLabel);
        g.insets = new Insets(0, 0, 5, 0);
        panelCentro.add(lblRol, g);
        g.gridy++;

        cbRol = new JComboBox<>(new String[]{"Servicio de Transporte", "Backoffice / Admin"});
        cbRol.setFont(fuenteInput);
        cbRol.setBackground(Color.WHITE);
        cbRol.setBorder(new LineBorder(bordeGris, 1));
        g.insets = new Insets(0, 0, 20, 0);
        panelCentro.add(cbRol, g);
        g.gridy++;

        // Botones
        btnIngresar = crearBoton("Ingresar", btnAzul, btnAzulHover);
        btnIrRegistro = crearBoton("Registrarse", new Color(108, 117, 125), new Color(90, 98, 104));

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnIngresar);
        panelBotones.add(btnIrRegistro);

        panelCentro.add(panelBotones, g);
        add(panelCentro, BorderLayout.CENTER);

        // Listeners
        btnIngresar.addActionListener(e -> ejecutarLogin());
        btnIrRegistro.addActionListener(e -> {
            new RegistroView().setVisible(true);
            dispose();
        });
    }

    private void crearCampo(JPanel panel, GridBagConstraints g, String texto, JTextField input, Font fLabel, Font fInput) {
        JLabel label = new JLabel(texto);
        label.setFont(fLabel);
        g.insets = new Insets(0, 0, 5, 0);
        panel.add(label, g);
        g.gridy++;

        input.setFont(fInput);
        input.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bordeGris, 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        g.insets = new Insets(0, 0, 15, 0);
        panel.add(input, g);
        g.gridy++;
    }

    private JButton crearBoton(String texto, Color normal, Color hover) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(normal);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new LineBorder(normal, 1));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
        return btn;
    }

    private void ejecutarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
