package com.transucv.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TarifasView extends JFrame {

    private final Color azulFondo = new Color(10, 25, 47);
    private final Color colorFondo = new Color(248, 249, 250);
    private final Color grisBorde = new Color(226, 232, 240);
    private final Color azulBoton = new Color(0, 33, 71); 
    private final Color azulResaltado = new Color(2, 48, 102);
    private final Color grisNormal = new Color(100, 116, 139);
    private final Color grisResaltado = new Color(90, 98, 104);

    private JComboBox<String> comboTipoRuta;
    private JComboBox<String> comboTipoUsuario;
    private JTextField campoMonto;
    private JButton botonGuardar;
    private JButton botonVolver;

    public TarifasView() {
        setTitle("TransUCV - Configuración de Tarifas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(colorFondo);

        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.setBackground(azulFondo);
        panelArriba.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel etiquetaTitulo = new JLabel("💰 Configuración General de Tarifas");
        etiquetaTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        etiquetaTitulo.setForeground(Color.WHITE);
        
        panelArriba.add(etiquetaTitulo, BorderLayout.WEST);
        add(panelArriba, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(20, 30, 20, 30),
            new LineBorder(grisBorde, 1)
        ));
        
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(10, 15, 10, 15);
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.weightx = 1.0;

        Font fuenteEtiqueta = new Font("SansSerif", Font.BOLD, 13);
        Font fuenteCampo = new Font("SansSerif", Font.PLAIN, 14);

        restricciones.gridx = 0; 
        restricciones.gridy = 0;
        JLabel etiquetaRuta = new JLabel("Tipo de Ruta:");
        etiquetaRuta.setFont(fuenteEtiqueta);
        panelCentro.add(etiquetaRuta, restricciones);

        restricciones.gridx = 1;
        String[] opcionesRuta = {"Urbana", "Extraurbana"};
        comboTipoRuta = new JComboBox<>(opcionesRuta);
        comboTipoRuta.setFont(fuenteCampo);
        comboTipoRuta.setBackground(Color.WHITE);
        panelCentro.add(comboTipoRuta, restricciones);

        restricciones.gridx = 0; 
        restricciones.gridy = 1;
        JLabel etiquetaUsuario = new JLabel("Tipo de Usuario:");
        etiquetaUsuario.setFont(fuenteEtiqueta);
        panelCentro.add(etiquetaUsuario, restricciones);

        restricciones.gridx = 1;
        String[] opcionesUsuario = {"Estudiante", "Empleado", "Profesor", "Público General"};
        comboTipoUsuario = new JComboBox<>(opcionesUsuario);
        comboTipoUsuario.setFont(fuenteCampo);
        comboTipoUsuario.setBackground(Color.WHITE);
        panelCentro.add(comboTipoUsuario, restricciones);

        restricciones.gridx = 0; 
        restricciones.gridy = 2;
        JLabel etiquetaMonto = new JLabel("Monto de la Tarifa (Bs.):");
        etiquetaMonto.setFont(fuenteEtiqueta);
        panelCentro.add(etiquetaMonto, restricciones);

        restricciones.gridx = 1;
        campoMonto = new JTextField();
        campoMonto.setFont(fuenteCampo);
        campoMonto.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(grisBorde, 1), 
            new EmptyBorder(5, 8, 5, 8)
        ));
        panelCentro.add(campoMonto, restricciones);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelAbajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelAbajo.setBackground(colorFondo);

        botonGuardar = armarBoton("Guardar Tarifa", azulBoton, azulResaltado);
        botonVolver = armarBoton("Volver al Panel", grisNormal, grisResaltado);

        panelAbajo.add(botonGuardar);
        panelAbajo.add(botonVolver);
        add(panelAbajo, BorderLayout.SOUTH);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarYGuardarTarifa();
            }
        });

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void validarYGuardarTarifa() {
        String textoMonto = campoMonto.getText().trim();
        
        try {
            double monto = Double.parseDouble(textoMonto);
            
            if (monto < 0) {
                JOptionPane.showMessageDialog(this, "El monto de la tarifa debe ser un número mayor o igual a cero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("Tarifa actualizada: " + monto + " para ruta " + comboTipoRuta.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Tarifa para ruta " + comboTipoRuta.getSelectedItem() + " actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            campoMonto.setText(""); 
            campoMonto.requestFocus();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El monto de la tarifa debe ser un número mayor o igual a cero", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton armarBoton(String texto, Color normal, Color hover) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(normal);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 35));
        boton.setBorder(new LineBorder(normal, 1)); 
        
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                boton.setBackground(hover); 
                boton.setBorder(new LineBorder(hover, 1)); 
            }
            public void mouseExited(MouseEvent e) { 
                boton.setBackground(normal); 
                boton.setBorder(new LineBorder(normal, 1)); 
            }
        });
        
        return boton;
    }
}