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

public class AdminDashboardView extends JFrame {

    private final Color colorFondo = new Color(248, 249, 250);
    private final Color azulPrincipal = new Color(10, 25, 47);
    private final Color azulBoton = new Color(0, 33, 71); 
    private final Color azulBotonHover = new Color(2, 48, 102); 
    
    private final Color grisTexto = new Color(100, 116, 139);
    private final Color rojoSalir = new Color(220, 53, 69);
    private final Color rojoHover = new Color(200, 35, 51);

    private JButton btnGestionFlota;
    private JButton btnItinerarios;
    private JButton btnTarifas;
    private JButton btnNuevoAdmin;
    private JButton btnSalir;

    private final AuthServicio servicioAutenticacion;

    public AdminDashboardView(AuthServicio servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;

        setTitle("TransUCV - Panel de Administración");
        setSize(550, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(colorFondo);

        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.setBackground(azulPrincipal);
        panelArriba.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel tituloVentana = new JLabel("⚙️ Panel Central de Administración");
        tituloVentana.setFont(new Font("SansSerif", Font.BOLD, 20));
        tituloVentana.setForeground(Color.WHITE);
        panelArriba.add(tituloVentana, BorderLayout.WEST);
        
        add(panelArriba, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(colorFondo);
        panelCentro.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel subtitulo = new JLabel("Seleccione el módulo que desea gestionar:");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(grisTexto);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(25));

        btnGestionFlota = crearBotonMenu("🚌 Gestión de Flota", azulBoton, azulBotonHover);
        btnItinerarios = crearBotonMenu("📅 Control Itinerarios y Conductores", azulBoton, azulBotonHover);
        btnTarifas = crearBotonMenu("💰 Configuración de Tarifas", azulBoton, azulBotonHover);
        btnNuevoAdmin = crearBotonMenu("👥 Registrar Nuevo Administrador", azulBoton, azulBotonHover);

        panelCentro.add(btnGestionFlota); 
        panelCentro.add(Box.createVerticalStrut(15));
        
        panelCentro.add(btnItinerarios); 
        panelCentro.add(Box.createVerticalStrut(15));
        
        panelCentro.add(btnTarifas); 
        panelCentro.add(Box.createVerticalStrut(15));
        
        panelCentro.add(btnNuevoAdmin);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelAbajo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panelAbajo.setBackground(colorFondo);
        panelAbajo.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        btnSalir = crearBotonMenu("Cerrar Sesión", rojoSalir, rojoHover);
        btnSalir.setPreferredSize(new Dimension(150, 35));
        btnSalir.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelAbajo.add(btnSalir);

        add(panelAbajo, BorderLayout.SOUTH);

        btnGestionFlota.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionFlotaView().setVisible(true);
            }
        });

        btnItinerarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ControlItinerariosView().setVisible(true);
            }
        });

        btnTarifas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TarifasView().setVisible(true);
            }
        });

        btnNuevoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirModalNuevoAdmin();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cerrando panel de admin...");
                dispose();
            }
        });
    }

    private void abrirModalNuevoAdmin() {
        JDialog ventanaRegistro = new JDialog(this, "Registrar Administrador", true);
        ventanaRegistro.setSize(400, 350);
        ventanaRegistro.setLocationRelativeTo(this);
        ventanaRegistro.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(Color.WHITE);

        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JPasswordField txtClave = new JPasswordField();

        formPanel.add(crearCampoConLabel("Nombre Completo:", txtNombre));
        formPanel.add(crearCampoConLabel("Correo Institucional:", txtCorreo));
        formPanel.add(crearCampoConLabel("Contraseña de Acceso:", txtClave));

        JButton btnGuardar = new JButton("Crear Administrador");
        btnGuardar.setBackground(azulBoton);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String nombreAdmin = txtNombre.getText().trim();
                String correoAdmin = txtCorreo.getText().trim();
                String claveAdmin = new String(txtClave.getPassword());

                if(nombreAdmin.isEmpty() || correoAdmin.isEmpty() || claveAdmin.isEmpty()) {
                    JOptionPane.showMessageDialog(ventanaRegistro, "Complete todos los campos.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    servicioAutenticacion.registrarUsuario(nombreAdmin, correoAdmin, claveAdmin, Rol.ADMINISTRADOR);
                    JOptionPane.showMessageDialog(ventanaRegistro, "Administrador creado con éxito y guardado en el sistema.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    ventanaRegistro.dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(ventanaRegistro, ex.getMessage(), "Error en Registro", JOptionPane.WARNING_MESSAGE);
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(ventanaRegistro, ex.getMessage(), "Error en Registro", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JPanel panelBtn = new JPanel();
        panelBtn.setBackground(Color.WHITE);
        panelBtn.add(btnGuardar);
        formPanel.add(panelBtn);

        ventanaRegistro.add(formPanel, BorderLayout.CENTER);
        ventanaRegistro.setVisible(true);
    }

    private JPanel crearCampoConLabel(String textoLabel, JComponent campoEntrada) {
        JPanel panelCampo = new JPanel(new BorderLayout(0, 5));
        panelCampo.setBackground(Color.WHITE);
        
        JLabel etiqueta = new JLabel(textoLabel);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        campoEntrada.setPreferredSize(new Dimension(200, 30));
        
        panelCampo.add(etiqueta, BorderLayout.NORTH);
        panelCampo.add(campoEntrada, BorderLayout.CENTER);
        
        return panelCampo;
    }

    private JButton crearBotonMenu(String textoBoton, Color colorNormal, Color colorHover) {
        JButton boton = new JButton(textoBoton);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorNormal);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(350, 45));
        boton.setBorder(new LineBorder(colorNormal, 1)); 
        
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                boton.setBackground(colorHover); 
                boton.setBorder(new LineBorder(colorHover, 1)); 
            }
            public void mouseExited(MouseEvent e) { 
                boton.setBackground(colorNormal); 
                boton.setBorder(new LineBorder(colorNormal, 1)); 
            }
        });
        
        return boton;
    }
}