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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class RecargaMonederoView extends JFrame {

    private JComboBox<String> cbBanco;
    private JLabel lblSaldoActual;
    private JTextField txtTelefono;
    private JTextField txtCedula;
    private JTextField txtReferencia;
    private JTextField txtMonto;
    private JButton btnRecargar;
    private JButton btnCancelar;
    private JButton btnIrPago;

    private final Color AZUL_PRIMARIO = new Color(10, 25, 47);
    private final Color AZUL_BOTON = new Color(0, 33, 71);
    private final Color FONDO_CLARO = new Color(248, 249, 250);
    private final Color GRIS_BORDE = new Color(226, 232, 240);
    private final Color ROJO_CANCELAR = new Color(220,53,69);
    private final Font fontInputs = new Font("SansSerif", Font.PLAIN, 14);
    private final Font fontLabels = new Font("SansSerif", Font.BOLD, 13);

    public RecargaMonederoView() {

        setTitle("TransUCV - Recarga de Monedero");
        setSize(950, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(AZUL_PRIMARIO);
        panelCabecera.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Recarga de Monedero");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panelCabecera.add(titulo, BorderLayout.WEST);
        add(panelCabecera, BorderLayout.NORTH);

        lblSaldoActual = new JLabel("SALDO DISPONIBLE: Bs. 0.00", SwingConstants.CENTER);
        lblSaldoActual.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSaldoActual.setForeground(new Color(144, 238, 144));
        panelCabecera.add(lblSaldoActual, BorderLayout.EAST);

        //parte del formulario

        JPanel panelContenedorForma = new JPanel(new BorderLayout());
        panelContenedorForma.setBackground(AZUL_BOTON);
        panelContenedorForma.setBorder(new EmptyBorder(30,80,20,8));

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 15));
        TitledBorder bordeTitulo = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 2),"Ingrese Datos del Pago Movil");
        panelFormulario.setBorder(bordeTitulo);
        bordeTitulo.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
        bordeTitulo.setTitleColor(AZUL_PRIMARIO);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(bordeTitulo, new EmptyBorder(20,30,20,30)));

        panelFormulario.add(new JLabel("Banco"));
        String[] bancos = {"Seleccione", "Banco de Venezuela", "Banesco", "Banco del Tesoro", "Provincial", "Mercantil", "BNC"};
        cbBanco = new JComboBox<>(bancos);
        cbBanco.setFont(fontInputs);
        cbBanco.setBackground(Color.WHITE);
        panelFormulario.add(cbBanco);

        panelFormulario.add(crearLabel("Teléfono (Ej: 04221234567)", fontLabels));
        txtTelefono = crearCampoTexto(fontInputs);
        panelFormulario.add(txtTelefono);

        panelFormulario.add(crearLabel("Cédula", fontLabels));
        txtCedula = crearCampoTexto(fontInputs);
        panelFormulario.add(txtCedula);

        panelFormulario.add(crearLabel("Referencia", fontLabels));
        txtReferencia = crearCampoTexto(fontInputs);
        panelFormulario.add(txtReferencia);

        panelFormulario.add(crearLabel("Monto a Recargar (Bs.): ", fontLabels));
        txtMonto = crearCampoTexto(fontInputs);
        panelFormulario.add(txtMonto);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT,15,10));
        panelBotones.setBackground(FONDO_CLARO);
        panelBotones.setBorder(new EmptyBorder(0,0,20,80));

        btnIrPago = estiloBoton("Pagar Pasaje", AZUL_BOTON, Color.WHITE);
        btnRecargar = estiloBoton("Confirmar Recarga", AZUL_BOTON, Color.WHITE);
        btnCancelar = estiloBoton("Cancelar", ROJO_CANCELAR, Color.WHITE);

        btnIrPago.setBorder(BorderFactory.createLineBorder(AZUL_BOTON));

        panelBotones.add(btnIrPago);
        panelBotones.add(btnRecargar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, Font fuente){
       
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        lbl.setForeground(AZUL_PRIMARIO);
        return lbl;
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
    
    private JTextField crearCampoTexto(Font fuente){
        JTextField txt = new JTextField ();
        txt.setFont(fuente);
        txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GRIS_BORDE,1), new EmptyBorder(5,10,5,10)));
        return txt;
    }

    public void limpiarFormulario() {
        cbBanco.setSelectedIndex(0);
        txtTelefono.setText("");
        txtCedula.setText("");
        txtMonto.setText("");
    }

    // getters para el controlador

    public String getBancoSeleccionado(){
        return (String) cbBanco.getSelectedItem();
    }

    public String getTelefono(){
        return txtTelefono.getText().trim();
    }

    public String getCedula(){
        return txtCedula.getText().trim();
    }
     
    public String getReferencia(){
        return txtReferencia.getText().trim();
    }

    public String getMonto(){
        return txtMonto.getText().trim();
    }

    // Metodos para el controlador

    public void setSaldoActual(String saldo){
        lblSaldoActual.setText("SALDO DISPONIBLE: Bs.S "+ saldo);
    }

    public void setRecargarListener(ActionListener listener) {
        btnRecargar.addActionListener(listener);
    }

    public void setCancelarListener(ActionListener listener){
        btnCancelar.addActionListener(listener);
    }

    public void setIrPagoListener(ActionListener listener){
        btnIrPago.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje, String titulo, boolean error){
        if(error){
            javax.swing.JOptionPane.showMessageDialog(this, mensaje, titulo, javax.swing.JOptionPane.WARNING_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, mensaje, titulo, javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

    }    
//comentario para que git lo vea
    public void cerrarVista(){
        this.dispose();
    }
}
