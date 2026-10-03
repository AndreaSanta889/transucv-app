package com.transucv.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistroView extends JFrame {

    private final Color AZUL_OBSCURO = new Color(10, 25, 47);
    private final Color AZUL_BOTON = new Color(0, 33, 71);
    private final Color AZUL_HOVER = new Color(2, 48, 102);
    private final Color BORDE_INPUT = new Color(226, 232, 240);
    private final Color TEXTO_MUTED = new Color(100, 116, 139);

    private JTextField txtNombre, txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRol;

    public RegistroView() {
        setTitle("TransUCV - Registro de Usuario");
        setSize(520, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Crear Cuenta Institucional");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(AZUL_OBSCURO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Complete los datos para acceder al sistema de gestión.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(TEXTO_MUTED);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombre = new JTextField();
        estilarInput(txtNombre);

        txtCorreo = new JTextField();
        estilarInput(txtCorreo);

        txtPassword = new JPasswordField();
        estilarInput(txtPassword);

        cbRol = new JComboBox<>(new String[]{"Estudiante", "Personal Administrativo", "Conductor"});
        cbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbRol.setBackground(Color.WHITE);
        cbRol.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBackground(AZUL_BOTON);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnRegistrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnRegistrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnRegistrar.setBackground(AZUL_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btnRegistrar.setBackground(AZUL_BOTON); }
        });

        btnRegistrar.addActionListener(e -> ejecutarRegistro());

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(20));
        
        agregarCampo(panel, "Nombre Completo", txtNombre);
        agregarCampo(panel, "Correo Institucional", txtCorreo);
        agregarCampo(panel, "Contraseña", txtPassword);
        
        JLabel lblRol = new JLabel("Tipo de Usuario / Rol");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblRol);
        panel.add(Box.createVerticalStrut(6));
        panel.add(cbRol);
        
        panel.add(Box.createVerticalStrut(25));
        panel.add(btnRegistrar);

        add(panel);
    }

    private void agregarCampo(JPanel panel, String label, JComponent input) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(input);
        panel.add(Box.createVerticalStrut(12));
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

    private void ejecutarRegistro() {
        if (txtNombre.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Registro completado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroView().setVisible(true));
    }
}
