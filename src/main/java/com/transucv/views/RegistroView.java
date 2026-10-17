package com.transucv.views;

import com.transucv.models.Rol;
import com.transucv.services.AuthServicio;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistroView extends JFrame {

    private final Color azulFondo = new Color(10, 25, 47);
    private final Color azulBoton = new Color(0, 33, 71);
    private final Color azulResaltado = new Color(2, 48, 102);
    private final Color grisBorde = new Color(226, 232, 240);
    private final Color textoGris = new Color(100, 116, 139);

    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JPasswordField campoPassword;
    private JComboBox<Rol> comboRol;
    
    private final AuthServicio servicioAutenticacion;

    public RegistroView(AuthServicio servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;

        setTitle("TransUCV - Registro de Usuario");
        setSize(520, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelFondo = new JPanel();
        panelFondo.setBackground(Color.WHITE);
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel etiquetaTitulo = new JLabel("Crear Cuenta Institucional");
        etiquetaTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        etiquetaTitulo.setForeground(azulFondo);
        etiquetaTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel etiquetaSubtitulo = new JLabel("Complete los datos para acceder al sistema de gestión.");
        etiquetaSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        etiquetaSubtitulo.setForeground(textoGris);
        etiquetaSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoNombre = new JTextField();
        darFormatoInput(campoNombre);

        campoCorreo = new JTextField();
        darFormatoInput(campoCorreo);

        campoPassword = new JPasswordField();
        darFormatoInput(campoPassword);

        comboRol = new JComboBox<>();
        
        for (Rol r : Rol.values()) {
            if (!r.name().equalsIgnoreCase("ADMINISTRADOR") && !r.name().equalsIgnoreCase("ADMIN")) {
                comboRol.addItem(r);
            }
        }
        
        comboRol.setMaximumSize(new Dimension(400, 38));
        comboRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboRol.setBackground(Color.WHITE);
        comboRol.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton botonRegistrar = new JButton("Registrar Usuario");
        botonRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setBackground(azulBoton);
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRegistrar.setMaximumSize(new Dimension(400, 42));
        botonRegistrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        botonRegistrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { 
                botonRegistrar.setBackground(azulResaltado); 
            }
            @Override
            public void mouseExited(MouseEvent e) { 
                botonRegistrar.setBackground(azulBoton); 
            }
        });

        botonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hacerRegistro();
            }
        });

        panelFondo.add(etiquetaTitulo);
        panelFondo.add(Box.createVerticalStrut(4));
        panelFondo.add(etiquetaSubtitulo);
        panelFondo.add(Box.createVerticalStrut(20));
        
        crearCampo(panelFondo, "Nombre Completo", campoNombre);
        crearCampo(panelFondo, "Correo Institucional", campoCorreo);
        crearCampo(panelFondo, "Contraseña", campoPassword);
        
        JLabel etiquetaRol = new JLabel("Tipo de Usuario / Rol");
        etiquetaRol.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiquetaRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelFondo.add(etiquetaRol);
        panelFondo.add(Box.createVerticalStrut(6));
        panelFondo.add(comboRol);
        
        panelFondo.add(Box.createVerticalStrut(25));
        panelFondo.add(botonRegistrar);

        add(panelFondo);
    }

    private void crearCampo(JPanel panelPrincipal, String textoLabel, JComponent cajaInput) {
        JLabel etiqueta = new JLabel(textoLabel);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelPrincipal.add(etiqueta);
        panelPrincipal.add(Box.createVerticalStrut(6));
        panelPrincipal.add(cajaInput);
        panelPrincipal.add(Box.createVerticalStrut(12));
    }

    private void darFormatoInput(JTextField cajaFila) {
        cajaFila.setMaximumSize(new Dimension(400, 38));
        cajaFila.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cajaFila.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(grisBorde, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        cajaFila.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void hacerRegistro() {
        String textoNombre = campoNombre.getText().trim();
        String textoCorreo = campoCorreo.getText().trim();
        String textoPass = new String(campoPassword.getPassword());
        Rol rolElegido = (Rol) comboRol.getSelectedItem();

        try {
            servicioAutenticacion.registrarUsuario(textoNombre, textoCorreo, textoPass, rolElegido);
            System.out.println("Usuario guardado con exito");
            JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            new LoginView(servicioAutenticacion).setVisible(true);
            this.dispose();
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en Registro", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en Registro", JOptionPane.WARNING_MESSAGE);
        }
    }
}