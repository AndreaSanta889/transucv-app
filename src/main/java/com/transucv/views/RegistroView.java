package com.transucv.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistroView extends JFrame {

    private Color fondoOscuro = new Color(33, 37, 41);
    private Color bordeGris = new Color(206, 212, 218);
    private Color btnVerde = new Color(40, 167, 69);
    private Color btnVerdeHover = new Color(33, 136, 56);

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRol;
    private JButton btnRegistrar;
    private JButton btnVolver;

    public RegistroView() {
        setTitle("TransUCV - Registro de Usuario");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(fondoOscuro);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Registro de Usuario");
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

        crearCampo(panelCentro, g, "Nombre Completo", txtNombre = new JTextField(), fuenteLabel, fuenteInput);
        crearCampo(panelCentro, g, "Correo Electrónico", txtCorreo = new JTextField(), fuenteLabel, fuenteInput);
        crearCampo(panelCentro, g, "Contraseña", txtPassword = new JPasswordField(), fuenteLabel, fuenteInput);

        JLabel lblRol = new JLabel("Rol / Módulo");
        lblRol.setFont(fuenteLabel);
        g.insets = new Insets(0, 0, 5, 0);
        panelCentro.add(lblRol, g);
        g.gridy++;

        cbRol = new JComboBox<>(new String[]{"Usuario Transporte", "Usuario Backoffice"});
        cbRol.setFont(fuenteInput);
        cbRol.setBackground(Color.WHITE);
        cbRol.setBorder(new LineBorder(bordeGris, 1));
        g.insets = new Insets(0, 0, 20, 0);
        panelCentro.add(cbRol, g);
        g.gridy++;

        btnRegistrar = crearBoton("Registrar", btnVerde, btnVerdeHover);
        btnVolver = crearBoton("Volver al Login", new Color(108, 117, 125), new Color(90, 98, 104));

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        panelCentro.add(panelBotones, g);
        add(panelCentro, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnVolver.addActionListener(e -> {
            new LoginView().setVisible(true);
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

    private void registrarUsuario() {
        if (txtNombre.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty() || new String(txtPassword.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        new LoginView().setVisible(true);
        dispose();
    }
}
