package com.transucv.views;

import com.transucv.controllers.ItinerarioController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ControlItinerariosView extends JFrame {
    
    private javax.swing.table.DefaultTableModel modeloTabla;
    private ItinerarioController itinerarioController;

    private final Color AZUL_PRIMARIO = new Color(10, 25, 47);
    private final Color AZUL_BOTON = new Color(0, 33, 71);
    private final Color FONDO_CLARO = new Color(248, 249, 250);
    private final Color GRIS_BORDE = new Color(226, 232, 240);

    public ControlItinerariosView(){
        itinerarioController = new ItinerarioController();
        setTitle("TransUCV - Control de Itinerarios y Conductores");
        setSize(950, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(FONDO_CLARO);

        JPanel tarjetaRutas = crearTarjetaTablero("Rutas Activas", "24", "↑ 2 de ayer", 30, 80);
        JPanel tarjetaRendimiento = crearTarjetaTablero("Rendimiento Puntual", "92%", "objetivo: 95%", 270, 80);

        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(AZUL_PRIMARIO);
        panelCabecera.setBorder(new EmptyBorder(20,30,20,30));
        
        JLabel titulo = new JLabel("Control de Itinerarios y Personal");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panelCabecera.add(titulo, BorderLayout.WEST);
        add(panelCabecera, BorderLayout.NORTH);

        JButton btnAgregar = new JButton("Asignar Ruta/Conductor");
        btnAgregar.setBounds(650, 30, 200, 30);
        estiloBoton(btnAgregar, AZUL_BOTON, Color.WHITE);
        btnAgregar.addActionListener(e -> abrirFormularioEstilizado());

        JButton btnTodas = new JButton("Todas las rutas");
        btnTodas.setBounds(70, 180, 150, 30);
        estiloBoton(btnTodas, new Color(220, 235, 255), AZUL_PRIMARIO);
        
        JButton btnUrbano = new JButton("Solo Urbano");
        btnUrbano.setBounds(230, 180, 150, 30);
        estiloBoton(btnUrbano, Color.WHITE, Color.DARK_GRAY);
        
        JButton btnExtraUrbano = new JButton("Solo Extra-Urbano");
        btnExtraUrbano.setBounds(390, 180, 150, 30);
        estiloBoton(btnExtraUrbano, Color.WHITE, Color.DARK_GRAY);
        
        JTextField txtBuscar = new JTextField("Buscar ID o Conductor...");
        txtBuscar.setBounds(680, 180, 200,35);

        JLabel lblUnidad = new JLabel("Itinerarios y Asignaciones");
        lblUnidad.setBounds(30, 5, 300, 30);
        lblUnidad.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JLabel Descripcion = new JLabel("Asigne autobuses y conductores a horarios específicos.");
        Descripcion.setBounds(30, 30, 400, 20);

        panel.add(lblUnidad); panel.add(Descripcion); panel.add(btnAgregar);
        panel.add(btnTodas); panel.add(btnUrbano); panel.add(btnExtraUrbano);
        panel.add(txtBuscar); panel.add(tarjetaRendimiento); panel.add(tarjetaRutas);

        String columnas [] = {"ESTADO", "RUTA", "INFO" ,"TIPO", "CONDUCTOR | BUS"};
        modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas);
        JTable tablaItinerarios = new JTable(modeloTabla);
        
        tablaItinerarios.setRowHeight(45);
        tablaItinerarios.setShowVerticalLines(false);
        tablaItinerarios.setGridColor(new Color(230,230,230));
        tablaItinerarios.setFont(new Font("SansSerif", Font.PLAIN,13));
        tablaItinerarios.setSelectionBackground(new Color(240, 245, 250));
        tablaItinerarios.setSelectionForeground(Color.BLACK);
        tablaItinerarios.setBorder(BorderFactory.createEmptyBorder());

        tablaItinerarios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaItinerarios.getColumnModel().getColumn(0).setPreferredWidth(90);
        tablaItinerarios.getColumnModel().getColumn(1).setPreferredWidth(70);
        tablaItinerarios.getColumnModel().getColumn(2).setPreferredWidth(250);
        tablaItinerarios.getColumnModel().getColumn(3).setPreferredWidth(110);
        tablaItinerarios.getColumnModel().getColumn(4).setPreferredWidth(350);

        tablaItinerarios.getTableHeader().setFont(new Font ("SansSerif", Font.BOLD, 11));
        tablaItinerarios.getTableHeader().setBackground(new Color(245, 247, 250));
        tablaItinerarios.getTableHeader().setForeground(Color.BLACK);
        tablaItinerarios.getTableHeader().setPreferredSize(new Dimension(0,40));
        tablaItinerarios.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220,220,220)));
        
        JScrollPane scrollTabla = new JScrollPane(tablaItinerarios);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBounds(30, 220, 870, 250);

        panel.add(scrollTabla);
        cargarDatosDesdeArchivo();
        add(panel);
    }

    private void abrirFormularioEstilizado() {
        JDialog dialog = new JDialog(this, "Asignar Itinerario y Conductor", true);
        dialog.setSize(520, 580);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel headerDialog = new JPanel(new BorderLayout());
        headerDialog.setBackground(AZUL_PRIMARIO);
        headerDialog.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTituloForm = new JLabel("📝 Asignar Nueva Ruta");
        lblTituloForm.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloForm.setForeground(Color.WHITE);
        headerDialog.add(lblTituloForm, BorderLayout.CENTER);
        dialog.add(headerDialog, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; g.insets = new Insets(8, 5, 8, 5);

        JTextField txtConductor = crearInputEstilizado();
        JTextField txtIdAutobus = crearInputEstilizado();
        JTextField txtRuta = crearInputEstilizado();
        JTextField txtCodigoRuta = crearInputEstilizado();
        JComboBox<String> cmbTipoRuta = new JComboBox<>(new String[]{"Urbana", "Extraurbana"});
        cmbTipoRuta.setBackground(Color.WHITE);
        cmbTipoRuta.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JTextField txtHoraInicio = crearInputEstilizado();
        JRadioButton rbAmInicio = new JRadioButton("AM", true); JRadioButton rbPmInicio = new JRadioButton("PM");
        estilarRadio(rbAmInicio); estilarRadio(rbPmInicio);
        ButtonGroup bgInicio = new ButtonGroup(); bgInicio.add(rbAmInicio); bgInicio.add(rbPmInicio);
        JPanel panelInicio = new JPanel(new BorderLayout(5, 0)); panelInicio.setBackground(Color.WHITE);
        panelInicio.add(txtHoraInicio, BorderLayout.CENTER);
        JPanel radiosInicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); radiosInicio.setBackground(Color.WHITE);
        radiosInicio.add(rbAmInicio); radiosInicio.add(rbPmInicio); panelInicio.add(radiosInicio, BorderLayout.EAST);

        JTextField txtHoraFin = crearInputEstilizado();
        JRadioButton rbAmFin = new JRadioButton("AM"); JRadioButton rbPmFin = new JRadioButton("PM", true);
        estilarRadio(rbAmFin); estilarRadio(rbPmFin);
        ButtonGroup bgFin = new ButtonGroup(); bgFin.add(rbAmFin); bgFin.add(rbPmFin);
        JPanel panelFin = new JPanel(new BorderLayout(5, 0)); panelFin.setBackground(Color.WHITE);
        panelFin.add(txtHoraFin, BorderLayout.CENTER);
        JPanel radiosFin = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); radiosFin.setBackground(Color.WHITE);
        radiosFin.add(rbAmFin); radiosFin.add(rbPmFin); panelFin.add(radiosFin, BorderLayout.EAST);

        int y = 0;
        agregarFila(formPanel, g, "Nombre Conductor:", txtConductor, y++);
        agregarFila(formPanel, g, "ID Autobús / Placa:", txtIdAutobus, y++);
        agregarFila(formPanel, g, "Ruta (Ej. Chacaito - Campus):", txtRuta, y++);
        agregarFila(formPanel, g, "Código Ruta (Ej. R-10A):", txtCodigoRuta, y++);
        agregarFila(formPanel, g, "Tipo de Ruta:", cmbTipoRuta, y++);
        agregarFila(formPanel, g, "Hora Inicio (HH:mm):", panelInicio, y++);
        agregarFila(formPanel, g, "Hora Fin (HH:mm):", panelFin, y++);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setBackground(FONDO_CLARO);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, GRIS_BORDE));

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("Guardar Asignación");
        estiloBoton(btnCancelar, Color.WHITE, Color.DARK_GRAY);
        estiloBoton(btnGuardar, AZUL_BOTON, Color.WHITE); 

        btnPanel.add(btnCancelar); btnPanel.add(btnGuardar);
        btnCancelar.addActionListener(e -> dialog.dispose());

        btnGuardar.addActionListener(e -> {
            String conductor = txtConductor.getText().trim();
            String idAutobus = txtIdAutobus.getText().trim();
            String ruta = txtRuta.getText().trim();
            String codigoRuta = txtCodigoRuta.getText().trim().toUpperCase();
            String tipoRuta = (String) cmbTipoRuta.getSelectedItem();
            String soloHoraInicio = txtHoraInicio.getText().trim();
            String soloHoraFin = txtHoraFin.getText().trim();

            if (conductor.isEmpty() || idAutobus.isEmpty() || ruta.isEmpty() || codigoRuta.isEmpty() || soloHoraInicio.isEmpty() || soloHoraFin.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String regexHora = "^(0?[1-9]|1[0-2]):[0-5][0-9]$";
            if (!soloHoraInicio.matches(regexHora) || !soloHoraFin.matches(regexHora)) {
                JOptionPane.showMessageDialog(dialog, "El formato de hora debe ser HH:mm (Ej: 08:30)", "Error de Formato", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String horaInicioCompleta = soloHoraInicio + " " + (rbAmInicio.isSelected() ? "AM" : "PM");
            String horaFinCompleta = soloHoraFin + " " + (rbAmFin.isSelected() ? "AM" : "PM");

            String resultado = itinerarioController.registrarNuevoItinerario(idAutobus, horaInicioCompleta, horaFinCompleta, conductor, ruta, codigoRuta, tipoRuta);

            if (resultado.equals("Exito") || resultado.toLowerCase().contains("exito")) {
                JOptionPane.showMessageDialog(dialog, "Itinerario y conductor asignados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                String conductorYBus = conductor + " | " + idAutobus;
                modeloTabla.addRow(new Object[]{"Programado", codigoRuta, ruta, tipoRuta, conductorYBus});
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, resultado, "Error al registrar", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private JTextField crearInputEstilizado() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(GRIS_BORDE, 1), new EmptyBorder(6, 10, 6, 10)));
        return txt;
    }

    private void estilarRadio(JRadioButton rb) {
        rb.setBackground(Color.WHITE);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rb.setFocusPainted(false);
    }

    private void agregarFila(JPanel panel, GridBagConstraints g, String labelText, JComponent input, int y) {
        g.gridy = y; g.gridx = 0; g.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(Color.DARK_GRAY);
        panel.add(lbl, g);
        g.gridx = 1; g.weightx = 0.7; panel.add(input, g);
    }

    private void estiloBoton(JButton boton, Color colorFondo, Color colorTexto){
        boton.setBackground(colorFondo); boton.setForeground(colorTexto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setFocusPainted(false); boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GRIS_BORDE), new EmptyBorder(8, 20, 8, 20)));
    } 

    private JPanel crearTarjetaTablero(String titulo, String valor, String descripcion, int x, int y) {
        JPanel tarjeta = new JPanel(); tarjeta.setLayout(null);
        tarjeta.setBounds(x, y, 220, 90); tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1, true));
        JLabel lblTitulo = new JLabel(titulo); lblTitulo.setBounds(15, 10, 180, 20); lblTitulo.setForeground(Color.GRAY);
        JLabel lblValor = new JLabel(valor); lblValor.setBounds(15, 35, 180, 30); lblValor.setForeground(Color.BLACK);
        JLabel lblDescripcion = new JLabel(descripcion); lblDescripcion.setBounds(15, 65, 180, 20); lblDescripcion.setForeground(Color.GRAY);
        tarjeta.add(lblTitulo); tarjeta.add(lblValor); tarjeta.add(lblDescripcion);
        return tarjeta;
    }

    private void cargarDatosDesdeArchivo() {
        if (itinerarioController == null) return;
        java.util.List<String[]> datos = itinerarioController.obtenerItinerariosGuardados();
        for (String[] itinerario : datos) {
            if (itinerario.length >= 7){
                String idAutobus = itinerario[0];
                String conductor = itinerario[3];
                String ruta = itinerario[4];
                String codigoRuta = itinerario[5];
                String tipoRuta = itinerario[6];
                
                String conductorYBus = conductor + " | " + idAutobus;
                
                modeloTabla.addRow(new Object[]{"Programado", codigoRuta, ruta, tipoRuta, conductorYBus});
            }
        }    
    }
}