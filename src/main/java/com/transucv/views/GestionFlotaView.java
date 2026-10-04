package com.transucv.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GestionFlotaView extends JFrame {

    private JTextField txtPlaca, txtModelo, txtCapacidad;
    private JComboBox<String> cbEstado;
    private JButton btnRegistrar, btnLimpiar;
    private JTable tablaFlota;
    private DefaultTableModel modeloTabla;
    private Color fondoOscuro = new Color(0, 86, 179); 
    private Color bordeGris = new Color(206, 212, 218);
    private Color btnVerde = new Color(40, 167, 69);
    private Color btnVerdeHover = new Color(33, 136, 56);
    private Color btnGris = new Color(108, 117, 125);
    private Color btnGrisHover = new Color(90, 98, 104);
    
    private com.transucv.controllers.GestionFlotaController controller;

    public GestionFlotaView() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error cargando el tema de la ventana");
        }

        setTitle("TransUCV - Gestión de Flota");
        setSize(950, 580); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        controller = new com.transucv.controllers.GestionFlotaController();
        

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(fondoOscuro);
        panelHeader.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titulo = new JLabel("🚌 Gestión de Unidades de Transporte");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        panelHeader.add(titulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(25, 25));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(25, 30, 30, 30));
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bordeGris, 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        panelIzquierdo.setPreferredSize(new Dimension(350, 600)); 

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.gridx = 0;
        g.gridy = 0;

        Font fuenteLabel = new Font("SansSerif", Font.BOLD, 13);
        Font fuenteInput = new Font("SansSerif", Font.PLAIN, 14);
        
        crearInput(panelIzquierdo, g, "Placa de la Unidad:", txtPlaca = new JTextField(), fuenteLabel, fuenteInput);
        crearInput(panelIzquierdo, g, "Modelo del Vehículo:", txtModelo = new JTextField(), fuenteLabel, fuenteInput);
        crearInput(panelIzquierdo, g, "Capacidad (Pasajeros):", txtCapacidad = new JTextField(), fuenteLabel, fuenteInput);

        JLabel lblEstado = new JLabel("Estado Operativo:");
        lblEstado.setFont(fuenteLabel);
        lblEstado.setForeground(new Color(73, 80, 87));
        g.gridy++;
        g.insets = new Insets(0, 0, 8, 0);
        panelIzquierdo.add(lblEstado, g);

        String[] opcionesEstado = {"Operativo", "En Mantenimiento", "Fuera de Servicio"};
        cbEstado = new JComboBox<>(opcionesEstado);
        cbEstado.setFont(fuenteInput);
        cbEstado.setBackground(Color.WHITE);
        cbEstado.setBorder(new LineBorder(bordeGris, 1));
        g.gridy++;
        g.insets = new Insets(0, 0, 25, 0);
        panelIzquierdo.add(cbEstado, g);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 12, 0));
        panelBotones.setOpaque(false);
        
        btnRegistrar = crearBoton("Registrar", btnVerde, btnVerdeHover);
        btnLimpiar = crearBoton("Limpiar", btnGris, btnGrisHover);
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        
        
        g.gridy++;
        panelIzquierdo.add(panelBotones, g);
        g.gridy++;
        g.weighty = 1.0; 
        panelIzquierdo.add(Box.createVerticalGlue(), g);

        panelCentro.add(panelIzquierdo, BorderLayout.WEST);

        String[] columnas = {"Placa", "Modelo", "Capacidad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        tablaFlota = new JTable(modeloTabla);
        tablaFlota.setRowHeight(35);
        tablaFlota.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaFlota.setShowVerticalLines(false);
        tablaFlota.setGridColor(new Color(233, 236, 239));
        tablaFlota.setSelectionBackground(new Color(173, 216, 230)); 
        
        tablaFlota.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaFlota.getTableHeader().setBackground(Color.WHITE);
        tablaFlota.getTableHeader().setPreferredSize(new Dimension(100, 45));

        JScrollPane scroll = new JScrollPane(tablaFlota);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new LineBorder(bordeGris, 1));
        panelCentro.add(scroll, BorderLayout.CENTER);

        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                guardarUnidad();
            }
        });

        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                txtPlaca.setText("");
            }
        });

        cargarDatosPrevios();
    }

    private void crearInput(JPanel panel, GridBagConstraints g, String texto, JTextField input, Font fLabel, Font fInput) {
        JLabel label = new JLabel(texto);
        label.setFont(fLabel);
        label.setForeground(new Color(73, 80, 87));
        g.gridy++;
        g.weighty = 0.0; 
        g.insets = new Insets(0, 0, 8, 0);
        panel.add(label, g);

        input.setFont(fInput);

        input.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bordeGris, 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        g.gridy++;
        g.insets = new Insets(0, 0, 18, 0);
        panel.add(input, g);
    }

    private JButton crearBoton(String texto, Color normal, Color hover) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(normal);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new LineBorder(normal, 1)); 
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
                btn.setBorder(new LineBorder(hover, 1));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(normal);
                btn.setBorder(new LineBorder(normal, 1));
            }
        });
        return btn;
    }

    private void guardarUnidad() {
        String placa = txtPlaca.getText().trim();
        String modelo = txtModelo.getText().trim();
        String capTexto = txtCapacidad.getText().trim();
        String estado = cbEstado.getSelectedItem().toString();

        String resultado = controller.registrarNuevaUnidad(placa, modelo, capTexto, estado);

        if (resultado.equals("Exito") || resultado.toLowerCase().contains("exitosamente")) {
           
            modeloTabla.addRow(new Object[]{placa, modelo, capTexto, estado});
            JOptionPane.showMessageDialog(this, "Unidad registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtPlaca.setText("");
            txtModelo.setText("");
            txtCapacidad.setText("");
            cbEstado.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosPrevios() {
        if (controller == null) return;

        java.util.List<String[]> unidadesGuardadas = controller.obtenerTodasLasUnidades();
        for (String[] unidad : unidadesGuardadas) {
            if (unidad.length == 4) {
                modeloTabla.addRow(new Object[]{unidad[0], unidad[1], unidad[2], unidad[3]});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionFlotaView().setVisible(true);
            }
        });
    }
}