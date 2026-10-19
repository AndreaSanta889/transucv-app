package com.transucv.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class PagoPasajeView extends JFrame {
    
    private JComboBox<String> cbItinerarios;
    private JLabel lblSaldoActual;
    private JLabel lblRolUsuario;
    private JLabel lblTarifaCalculada;
    private JButton btnPagar;
    private JButton btnCancelar;
    private JButton btnIrRecarga;

    private final Color AZUL_PRIMARIO = new Color(10, 25, 47);
    private final Color AZUL_BOTON = new Color(0, 33, 71);
    private final Color FONDO_CLARO = new Color(248, 249, 250);
    private final Color GRIS_BORDE = new Color(226, 232, 240);
    private final Color ROJO_CANCELAR = new Color(220,53,69);
    public PagoPasajeView(){
        
        setTitle("TransUCV - Pago de Pasaje");
        setSize(950, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(AZUL_PRIMARIO);
        panelCabecera.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Pago con Monedero Virtual");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panelCabecera.add(titulo, BorderLayout.WEST);
        add(panelCabecera, BorderLayout.NORTH);

        JPanel panelInfoUsuario = new JPanel (new GridLayout(2,1));
        panelInfoUsuario.setOpaque(false);

        lblSaldoActual = new JLabel("SALDO DISPONIBLE: Bs. 0.00", SwingConstants.CENTER);
        lblSaldoActual.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSaldoActual.setForeground(new Color(144, 238, 144));

        lblRolUsuario = new JLabel ("Rol: ", SwingConstants.RIGHT);
        lblRolUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRolUsuario.setForeground(Color.LIGHT_GRAY);

        panelInfoUsuario.add(lblSaldoActual);
        panelInfoUsuario.add(lblRolUsuario);
        panelCabecera.add(panelInfoUsuario, BorderLayout.EAST);

        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBackground(FONDO_CLARO);
        panelContenedor.setBorder(new EmptyBorder(30,50,20,50));

        JPanel panelFormulario = new JPanel (new GridLayout(3,1,10,15));
        panelFormulario.setBackground(Color.WHITE);

        TitledBorder bordeTitulo = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 2),"Seleccione su Ruta");
        panelFormulario.setBorder(bordeTitulo);
        bordeTitulo.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
        bordeTitulo.setTitleColor(AZUL_PRIMARIO);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(bordeTitulo, new EmptyBorder(20,30,20,30)));
        
        JLabel lblInstruccion = new JLabel("Itinerarios Disponibles (Solo Extraurbanos): ");
        lblInstruccion.setFont (new Font("SansSerif", Font.BOLD,13));
        lblInstruccion.setForeground(Color.DARK_GRAY);
        panelFormulario.add(lblInstruccion);

        cbItinerarios = new JComboBox<>();
        cbItinerarios.setFont(new Font("SansSerif", Font.PLAIN,14));
        cbItinerarios.setBackground(Color.WHITE);

        panelFormulario.add(cbItinerarios);

        lblTarifaCalculada = new JLabel("Tarifa a debitar: Seleccione una ruta", SwingConstants.CENTER);
        lblTarifaCalculada.setFont(new Font("SansSerif", Font.BOLD,15));
        lblTarifaCalculada.setForeground(AZUL_PRIMARIO);
        panelFormulario.add(lblTarifaCalculada);
        
        panelContenedor.add(panelFormulario, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);
    
        JPanel panelBotones = new JPanel (new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(FONDO_CLARO);
        panelBotones.setBorder(new EmptyBorder(0,0,20,50));

        btnIrRecarga = estiloBoton("Ir a Recargar", AZUL_BOTON, Color.WHITE);
        btnPagar = estiloBoton("Confirmar Pago", AZUL_BOTON, Color.WHITE);
        btnCancelar = estiloBoton("Cancelar", ROJO_CANCELAR, Color.WHITE);

        panelBotones.add(btnIrRecarga);
        panelBotones.add(btnPagar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        
    }

    private JButton estiloBoton(String texto, Color fondo, Color textoColor){
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(180,40));
        return btn;
    }

    public void setDatosUsuario (String saldo, String rol){
        lblSaldoActual.setText("Saldo: Bs. " + saldo);
        lblRolUsuario.setText("Rol: " + rol);
    }

    public void setTarifaCalculada (String monto){
        lblTarifaCalculada.setText("Tarifa a debitar: Bs. " + monto);
    }

    public String getItinerarioSeleccionado(){
        return (String) cbItinerarios.getSelectedItem();
    }

    public void cargarItinerarios(String [] itinerarios){
        cbItinerarios.removeAllItems();
        cbItinerarios.addItem("Seleccione un Itinerario");
        for(String it: itinerarios){
            cbItinerarios.addItem(it);
        }
    }

    public void setPagarListener(ActionListener listener){
        btnPagar.addActionListener(listener);
    }

    public void setCancelarListener(ActionListener listener){
        btnCancelar.addActionListener(listener);
    }

    public void setCambioRutaListener (ActionListener listener){
        cbItinerarios.addActionListener(listener);
    }

    public void setIrRecarga(ActionListener listener){
        btnIrRecarga.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje, String titulo, boolean error){
        if(error){
            javax.swing.JOptionPane.showMessageDialog(this, mensaje, titulo, javax.swing.JOptionPane.WARNING_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, mensaje, titulo, javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cerrarVista(){
        this.dispose();
    }

     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PagoPasajeView().setVisible(true));
    }
}
