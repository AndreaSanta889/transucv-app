package com.transucv.views;

import com.transucv.controllers.ItinerarioController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ControlItinerariosView extends JFrame {
    
    private javax.swing.table.DefaultTableModel modeloTabla;

    public ControlItinerariosView(){
        setTitle("TransUCV - Control de Itinerarios");
        setSize(950, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();

        JPanel tarjetaRutas = crearTarjetaTablero("Rutas Activas", "24", "↑ 2 de ayer", 30, 80);

        JPanel tarjetaRendimiento = crearTarjetaTablero("Rendimiento Puntual", "92%", "objetivo: 95%", 270, 80);

        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(new Color(0, 86, 179));
        panelCabecera.setBorder(new EmptyBorder(20,30,20,30));
        
        JLabel titulo = new JLabel("Control de Itinerarios");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panelCabecera.add(titulo, BorderLayout.WEST);
        add(panelCabecera, BorderLayout.NORTH);

        panel.setLayout(null);
        panel.setBackground(new Color(250,247,250));

        JButton btnAgregar = new JButton("Agregar Ruta");
        btnAgregar.setBounds(700, 30, 150, 30);
        estiloBoton(btnAgregar, new Color(0, 86, 179), Color.WHITE);

        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
            
                ItinerarioController controller = new ItinerarioController();

                String idAutobus =JOptionPane.showInputDialog(null, "Ingrese el ID del autobús ");

                if(idAutobus == null || idAutobus.trim().isEmpty()){
                    return;
                }

                String [] opcionesEstado = {"Activa", "En Mantenimiento", "Fuera de Servicio"};
                String estadoUnidad = (String) JOptionPane.showInputDialog(null, "Seleccione el estado de la unidad:", "Estado de la Unidad",JOptionPane.QUESTION_MESSAGE, null, opcionesEstado, opcionesEstado[0]);

                if(estadoUnidad == null) return;

                String horaInicio= JOptionPane.showInputDialog(null, "Ingrese la hora de inicio del itinerario (HH:mm AM/PM):");

                String horaFin= JOptionPane.showInputDialog(null, "Ingrese la hora de fin del itinerario (HH:mm AM/PM):");

                String resultado = controller.registrarNuevoItinerario(idAutobus, estadoUnidad, horaInicio, horaFin);

                if (resultado.equals("Exito")){
                    JOptionPane.showMessageDialog(panel, "Itinerario registrado correctamente", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

                    String infoCronograma = horaInicio + " - " + horaFin + ", Conductor Asignado (BUS-" + idAutobus + ")";

                    modeloTabla.addRow(new Object[]{"Programado", "RUTA-NUEVA:", "Nueva Ruta (ITIN-XXXX)", estadoUnidad.toUpperCase(), infoCronograma});

                } else {
                    JOptionPane.showMessageDialog(panel, resultado, "Error de Registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnTodas = new JButton("Todas las rutas");
        btnTodas.setBounds(70, 180, 150, 30);
        estiloBoton(btnTodas, new Color(220, 235, 255), new Color(0,86,179));
        
        JButton btnUrbano = new JButton("Solo Urbano");
        btnUrbano.setBounds(230, 180, 150, 30);
        estiloBoton(btnUrbano, Color.WHITE, Color.DARK_GRAY);
        
        JButton btnExtraUrbano = new JButton("Solo Extra-Urbano");
        btnExtraUrbano.setBounds(390, 180, 150, 30);
        estiloBoton(btnExtraUrbano, Color.WHITE, Color.DARK_GRAY);
        
        JTextField txtBuscar = new JTextField("Buscar ID o Conductor...");
        txtBuscar.setBounds(680, 180, 200,35);

        JLabel lblUnidad = new JLabel("Itinerarios de rutas");
        lblUnidad.setBounds(30, 5, 300, 30);
        lblUnidad.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        
        JLabel Descripcion = new JLabel("Gestionar y supervisar los horarios del transporte en el campus.");
        Descripcion.setBounds(30, 30, 400, 20);

        panel.add(lblUnidad);
        panel.add(Descripcion);
        panel.add(btnAgregar);
        panel.add(btnTodas);
        panel.add(btnUrbano);
        panel.add(btnExtraUrbano);
        panel.add(txtBuscar);
        panel.add(tarjetaRendimiento);
        panel.add(tarjetaRutas);

        String columnas [] = {"ESTADO", "RUTA", "INFO" ,"TIPO", "CONDUCTOR | BUS"};
        Object[][] datos = {
            {"En curso", "R-10A:", "Plaza Vzla - Campus (ITIN-4092)", "URBANO", "7:00 AM - 7:45 AM, Carlos M. (BUS-014)"},
            {"Retraso","R-45X:", "Guarenas - Campus(ITIN-4093)","EXTRA-URBANO", "6:00 AM - Est. 8:30 AM, Maria L. (BUS-052)"},
            {"Programado","R-12B:", "Chacaito - Campus (ITIN-4094)","URBANO", "8:30 AM - 9:15 AM, Ana R. (BUS-021)"}
        };

        modeloTabla = new javax.swing.table.DefaultTableModel(datos, columnas);
        JTable tablaItinerarios = new JTable(modeloTabla);
        
        tablaItinerarios.setRowHeight(45);
        tablaItinerarios.setShowVerticalLines(false);
        tablaItinerarios.setGridColor(new Color(230,230,230));
        tablaItinerarios.setFont(new Font("SansSerif", Font.PLAIN,13));
        tablaItinerarios.setSelectionBackground(new Color(240, 245, 250));
        tablaItinerarios.setSelectionForeground(Color.BLACK);
        tablaItinerarios.setBorder(BorderFactory.createEmptyBorder());

        tablaItinerarios.getTableHeader().setFont(new Font ("SansSerif", Font.BOLD, 11));
        tablaItinerarios.getTableHeader().setBackground(new Color(245, 247, 250));
        tablaItinerarios.getTableHeader().setForeground(Color.BLACK);
        tablaItinerarios.getTableHeader().setPreferredSize(new Dimension(0,40));
        tablaItinerarios.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220,220,220)));
        
        JScrollPane scrollTabla = new JScrollPane(tablaItinerarios);

        scrollTabla.setBorder(BorderFactory.createEmptyBorder());
        scrollTabla.setBackground(getForeground());
        scrollTabla.getViewport().setBackground(Color.WHITE);

        scrollTabla.setBounds(30, 220, 870, 250);


        panel.add(scrollTabla);

        add(panel);
    }

    private void estiloBoton(JButton boton, Color colorFondo, Color colorTexto){
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(5,15,5,15)));
    } 

    private JPanel crearTarjetaTablero(String titulo, String valor, String descripcion, int x, int y) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(null);
        tarjeta.setBounds(x, y, 220, 90);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1, true));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setBounds(15, 10, 180, 20);
        lblTitulo.setForeground(Color.GRAY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setBounds(15, 35, 180, 30);
        lblValor.setForeground(Color.BLACK);

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setBounds(15, 65, 180, 20);
        lblDescripcion.setForeground(Color.GRAY);
        
        tarjeta.add(lblTitulo);
        tarjeta.add(lblValor);
        tarjeta.add(lblDescripcion);

        return tarjeta;
    }

    public static void main(String[] args) {
        
    java.util.Locale.setDefault(java.util.Locale.US);

    ControlItinerariosView ventana = new ControlItinerariosView();
    ventana.setVisible(true);
    }
}
