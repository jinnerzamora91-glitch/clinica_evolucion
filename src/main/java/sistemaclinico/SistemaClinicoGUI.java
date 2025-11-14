package sistemaclinico;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// iText 5 imports (no import for com.itextpdf.text.Font to avoid clash)
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class SistemaClinicoGUI extends JFrame {

    private Sistema sistema;
    private JPanel contentPanel;
    private BufferedImage logoImage;
    private static final Color ACCENT = new Color(82, 171, 152);
    private static final Color ACCENT_DARK = new Color(43, 103, 119);

    public SistemaClinicoGUI() {
        sistema = new Sistema();
        loadLogo();
        initUI();
    }
/**
     * Carga el logo desde recursos (dentro del proyecto o ruta absoluta).
     */
    private void loadLogo() {
        // intenta recurso empaquetado (src/main/resources/logo.png)
        try (InputStream is = getClass().getResourceAsStream("/logo.png")) {
            if (is != null) {
                logoImage = ImageIO.read(is);
                return;
            }
        } catch (Exception ignored) {}

        // fallback a la ruta absoluta que diste
        String fallback = "C:/Users/rodal/OneDrive/Documentos/NetBeansProjects/proyecto_clinica/resources/logo.png";
        try {
            File f = new File(fallback);
            if (f.exists()) logoImage = ImageIO.read(f);
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Clínica Zamora - Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(8,12,8,12));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        left.setOpaque(false);
        if (logoImage != null) {
            java.awt.Image scaled = logoImage.getScaledInstance(56,56, java.awt.Image.SCALE_SMOOTH);
            left.add(new JLabel(new ImageIcon(scaled)));
        } else {
            JLabel ph = new JLabel("CZ");
            ph.setOpaque(true);
            ph.setBackground(ACCENT);
            ph.setForeground(Color.WHITE);
            ph.setPreferredSize(new Dimension(56,56));
            ph.setHorizontalAlignment(SwingConstants.CENTER);
            ph.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
            left.add(ph);
        }
        JLabel labelTitle = new JLabel("<html><b>Clínica Zamora</b><br><span style='font-size:11px;color:#2B6777'>Sistema de Gestión Clínica</span></html>");
        labelTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        left.add(labelTitle);
        header.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBackground(ACCENT_DARK); btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "¿Deseas salir?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        right.add(btnSalir);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Lateral menu
        JPanel lateral = new JPanel();
        lateral.setBackground(ACCENT);
        lateral.setPreferredSize(new Dimension(260, getHeight()));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new EmptyBorder(18,12,18,12));
        lateral.add(Box.createRigidArea(new Dimension(0,6)));

        addMenuButton(lateral, "Registrar Pacientes", e -> showPanel(new PanelRegistrarPaciente(sistema)));
        addMenuButton(lateral, "Registrar Cita", e -> showPanel(new PanelRegistrarCita(sistema)));
        addMenuButton(lateral, "Finalizar Cita", e -> showPanel(new PanelFinalizarCita(sistema)));
        addMenuButton(lateral, "Historial Clínico", e -> showPanel(new PanelHistorialClinico(sistema)));
        addMenuButton(lateral, "Historial Pagos", e -> showPanel(new PanelHistorialPagos(sistema)));

        lateral.add(Box.createVerticalGlue());

        JButton btnSalirSide = new JButton("Salir");
        btnSalirSide.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnSalirSide.setBackground(Color.WHITE);
        btnSalirSide.setForeground(ACCENT_DARK);
        btnSalirSide.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "¿Deseas salir?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        lateral.add(btnSalirSide);
        lateral.add(Box.createRigidArea(new Dimension(0,6)));
        add(lateral, BorderLayout.WEST);

        // Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(18,18,18,18));
        add(contentPanel, BorderLayout.CENTER);
        showWelcome();
    }

    private void addMenuButton(JPanel container, String text, java.awt.event.ActionListener al) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setBackground(Color.WHITE);
        btn.setForeground(ACCENT_DARK);
        btn.setFocusPainted(false);
        btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btn.addActionListener(al);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(235,250,247)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(Color.WHITE); }
        });
        container.add(btn);
        container.add(Box.createRigidArea(new Dimension(0,8)));
    }

    private void showPanel(JPanel p) {
        contentPanel.removeAll();
        contentPanel.add(p, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showWelcome() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("<html><center><span style='font-size:22px;font-weight:700'>Bienvenido a Clínica Zamora</span><br><span style='font-size:12px;color:#2B6777'>Seleccione una opción del menú</span></center></html>", SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        showPanel(p);
    }

    // ---------- Panels ----------

    // Registrar Paciente
    class PanelRegistrarPaciente extends JPanel {
        private JTextField tfNombre, tfCedula, tfEdad, tfDireccion, tfTelefono;
        private Sistema sistema;
        public PanelRegistrarPaciente(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JPanel form = new JPanel(new GridLayout(6,2,10,10));
            form.setOpaque(false);
            tfNombre = new JTextField(); tfCedula = new JTextField(); tfEdad = new JTextField(); tfDireccion = new JTextField(); tfTelefono = new JTextField();
            form.add(new JLabel("Nombre:")); form.add(tfNombre);
            form.add(new JLabel("Cédula/TI:")); form.add(tfCedula);
            form.add(new JLabel("Edad:")); form.add(tfEdad);
            form.add(new JLabel("Dirección:")); form.add(tfDireccion);
            form.add(new JLabel("Teléfono:")); form.add(tfTelefono);
            JButton btn = new JButton("Registrar paciente");
            btn.setBackground(ACCENT_DARK); btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> registrarPaciente());
            JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setOpaque(false); bp.add(btn);
            add(new JLabel("<html><b>Registro de Paciente</b></html>"), BorderLayout.NORTH);
            add(form, BorderLayout.CENTER);
            add(bp, BorderLayout.SOUTH);
        }

        private void registrarPaciente() {
            try {
                String nombre = tfNombre.getText().trim();
                String cedula = tfCedula.getText().trim();
                int edad = Integer.parseInt(tfEdad.getText().trim());
                String dir = tfDireccion.getText().trim();
                String tel = tfTelefono.getText().trim();
                if (nombre.isEmpty() || cedula.isEmpty()) { JOptionPane.showMessageDialog(this, "Nombre y cédula requeridos."); return; }
                Paciente p = new Paciente(nombre, cedula, edad, dir, tel);
                sistema.registrarPaciente(p);
                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
                tfNombre.setText(""); tfCedula.setText(""); tfEdad.setText(""); tfDireccion.setText(""); tfTelefono.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Edad inválida.");
            }
        }
    }

    // Registrar Cita
    class PanelRegistrarCita extends JPanel {
        private JComboBox<Paciente> cbPacientes;
        private JTextField tfMotivo, tfCostoHab;
        private Sistema sistema;
        public PanelRegistrarCita(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JPanel left = new JPanel(new GridLayout(5,2,10,10));
            left.setOpaque(false);
            cbPacientes = new JComboBox<>(sistema.getPacientes().toArray(new Paciente[0]));
            tfMotivo = new JTextField();
            tfCostoHab = new JTextField("0");
            left.add(new JLabel("Paciente:")); left.add(cbPacientes);
            left.add(new JLabel("Motivo de la cita:")); left.add(tfMotivo);
            left.add(new JLabel("Costo habitación:")); left.add(tfCostoHab);
            JButton btnVer = new JButton("Ver precio y Asignar médico");
            btnVer.setBackground(ACCENT_DARK); btnVer.setForeground(Color.WHITE);
            btnVer.addActionListener(e -> verYRegistrar());
            left.add(new JLabel()); left.add(btnVer);
            add(new JLabel("<html><b>Registro de Cita Médica</b></html>"), BorderLayout.NORTH);
            add(left, BorderLayout.WEST);
        }

        private void verYRegistrar() {
            Paciente p = (Paciente) cbPacientes.getSelectedItem();
            String motivo = tfMotivo.getText().trim();
            if (p == null || motivo.isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione paciente y escriba motivo."); return; }
            Medico m = sistema.asignarMedicoPorMotivo(motivo);
            double precioConsulta = m.getTarifaConsulta();
            double precioHab;
            try { precioHab = Double.parseDouble(tfCostoHab.getText().trim()); } catch (Exception ex) { precioHab = 0; tfCostoHab.setText("0"); }
            String resumen = "Médico asignado: " + m.getNombre() + " (" + m.getEspecialidad() + ")\n" +
                    "Valor consulta: $" + precioConsulta + "\n" +
                    "Habitación: $" + precioHab + "\n\n¿Desea confirmar la cita?";
            int opt = JOptionPane.showConfirmDialog(this, resumen, "Confirmar cita", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                CitaMedica c = new CitaMedica(p, m, motivo, precioConsulta, precioHab);
                sistema.registrarCita(c);
                JOptionPane.showMessageDialog(this, "Cita registrada correctamente.");
            }
        }
    }

    // Finalizar Cita
    class PanelFinalizarCita extends JPanel {
        private JComboBox<CitaMedica> cbCitas;
        private JTextArea taDiagnostico;
        private JTextField tfObserv;
        private Sistema sistema;
        public PanelFinalizarCita(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new BorderLayout(8,8));
            setBackground(Color.WHITE);
            JPanel top = new JPanel(new GridLayout(3,2,8,8)); top.setOpaque(false);
            cbCitas = new JComboBox<>(sistema.getCitasActivas().toArray(new CitaMedica[0]));
            taDiagnostico = new JTextArea(4,20);
            tfObserv = new JTextField();
            top.add(new JLabel("Citas activas:")); top.add(cbCitas);
            top.add(new JLabel("Diagnóstico:")); top.add(new JScrollPane(taDiagnostico));
            top.add(new JLabel("Observaciones:")); top.add(tfObserv);
            JButton btnAddMed = new JButton("Agregar medicamento y finalizar");
            btnAddMed.setBackground(ACCENT_DARK); btnAddMed.setForeground(Color.WHITE);
            btnAddMed.addActionListener(e -> finalizarCita());
            add(new JLabel("<html><b>Finalizar Cita</b></html>"), BorderLayout.NORTH);
            add(top, BorderLayout.CENTER);
            JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setOpaque(false); bp.add(btnAddMed);
            add(bp, BorderLayout.SOUTH);
        }

        private void finalizarCita() {
            CitaMedica cita = (CitaMedica) cbCitas.getSelectedItem();
            if (cita == null) { JOptionPane.showMessageDialog(this, "Seleccione una cita activa."); return; }
            String diag = taDiagnostico.getText().trim();
            String obs = tfObserv.getText().trim();
            if (diag.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese diagnóstico."); return; }
            List<Medicamento> meds = new ArrayList<>();
            while (true) {
                String nombre = JOptionPane.showInputDialog(this, "Nombre medicamento (Cancelar o dejar vacío para terminar):");
                if (nombre == null || nombre.trim().isEmpty()) break;
                String dosis = JOptionPane.showInputDialog(this, "Dosis:");
                String precioS = JOptionPane.showInputDialog(this, "Precio:");
                double precio = 0;
                try { precio = Double.parseDouble(precioS); } catch (Exception ignored) {}
                meds.add(new Medicamento(nombre, dosis, precio));
            }
            sistema.finalizarCita(cita, diag, obs, meds);
            JOptionPane.showMessageDialog(this, "Cita finalizada. Total: $" + String.format("%.2f", cita.calcularTotal()));
            cbCitas.removeItem(cita);
        }
    }

    // Historial Clínico
    class PanelHistorialClinico extends JPanel {
        private JComboBox<Paciente> cbPacientes;
        private JTable table;
        private Sistema sistema;
        public PanelHistorialClinico(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new BorderLayout(8,8));
            setBackground(Color.WHITE);
            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); top.setOpaque(false);
            cbPacientes = new JComboBox<>(sistema.getPacientes().toArray(new Paciente[0]));
            JButton btnPdf = new JButton("Generar PDF del paciente");
            btnPdf.setBackground(ACCENT_DARK); btnPdf.setForeground(Color.WHITE);
            btnPdf.addActionListener(e -> {
                Paciente p = (Paciente) cbPacientes.getSelectedItem();
                if (p == null) { JOptionPane.showMessageDialog(this, "Seleccione un paciente."); return; }
                generarPDFHistorialClinico(p);
            });
            top.add(new JLabel("Paciente:")); top.add(cbPacientes); top.add(btnPdf);
            add(top, BorderLayout.NORTH);
            table = new JTable();
            add(new JScrollPane(table), BorderLayout.CENTER);
            cbPacientes.addActionListener(e -> refresh());
            refresh();
        }

        private void refresh() {
            Paciente p = (Paciente) cbPacientes.getSelectedItem();
            String[] cols = {"Cita #", "Fecha", "Médico", "Motivo", "Diagnóstico", "Medicamentos", "Observaciones"};
            DefaultTableModel m = new DefaultTableModel(cols, 0);
            if (p != null) {
                for (EvolucionMedica ev : p.getHistoriaClinica().getEvoluciones()) {
                    CitaMedica c = ev.getCita();
                    StringBuilder meds = new StringBuilder();
                    for (Medicamento md : ev.getMedicamentos()) meds.append(md.getNombre()).append(" (").append(md.getDosis()).append(") ");
                    m.addRow(new Object[]{c.getId(), c.getFechaFormatted(), c.getMedico().getNombre(), c.getMotivo(), ev.getDiagnostico(), meds.toString(), ev.getObservaciones()});
                }
            }
            table.setModel(m);
        }
    }

    // Historial Pagos
    class PanelHistorialPagos extends JPanel {
        private JComboBox<Pago> cbPagos;
        private JTable table;
        private Sistema sistema;
        public PanelHistorialPagos(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new BorderLayout(8,8));
            setBackground(Color.WHITE);
            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); top.setOpaque(false);
            cbPagos = new JComboBox<>(sistema.getPagos().toArray(new Pago[0]));
            JButton btnPdf = new JButton("Generar PDF del pago");
            btnPdf.setBackground(ACCENT_DARK); btnPdf.setForeground(Color.WHITE);
            btnPdf.addActionListener(e -> {
                Pago p = (Pago) cbPagos.getSelectedItem();
                if (p == null) { JOptionPane.showMessageDialog(this, "Seleccione un pago."); return; }
                generarPDFHistorialPagos(p);
            });
            top.add(new JLabel("Pago:")); top.add(cbPagos); top.add(btnPdf);
            add(top, BorderLayout.NORTH);
            table = new JTable();
            add(new JScrollPane(table), BorderLayout.CENTER);
            cbPagos.addActionListener(e -> refresh());
            refresh();
        }

        private void refresh() {
            Pago sel = (Pago) cbPagos.getSelectedItem();
            String[] cols = {"Paciente", "Cita #", "Consulta", "Habitación", "Medicamentos", "Total"};
            DefaultTableModel m = new DefaultTableModel(cols, 0);
            if (sel != null) {
                CitaMedica c = sel.getCita();
                StringBuilder meds = new StringBuilder();
                for (Medicamento md : c.getMedicamentos()) meds.append(md.getNombre()).append(" ($").append(md.getPrecio()).append(") ");
                m.addRow(new Object[]{c.getPaciente().getNombre(), c.getId(), c.getCostoConsulta(), c.getCostoHabitacion(), meds.toString(), sel.getTotal()});
            }
            table.setModel(m);
        }
    }

    // -------- PDF Generation using iText 5 --------

    private Image getPdfLogo_iText5() {
        try {
            if (logoImage == null) return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(logoImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            Image img = Image.getInstance(bytes);
            img.scaleToFit(80, 80);
            return img;
        } catch (Exception ex) {
            return null;
        }
    }

    private void generarPDFHistorialClinico(Paciente paciente) {
        String fileName = "HistorialClinico_" + paciente.getNombre().replaceAll("\\s+","_") + ".pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Image logo = getPdfLogo_iText5();
            if (logo != null) {
                logo.setAlignment(Image.LEFT);
                document.add(logo);
            }

            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, new BaseColor(43,103,119));
            Paragraph title = new Paragraph("Clínica Zamora\nHistorial Clínico", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            com.itextpdf.text.Font bold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
            Paragraph datos = new Paragraph();
            datos.add(new Paragraph("Paciente: " + paciente.getNombre(), bold));
            datos.add(new Paragraph("Cédula: " + paciente.getCedula(), bold));
            datos.add(new Paragraph("Edad: " + paciente.getEdad(), bold));
            document.add(datos);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{1.2f,1.6f,2f,2f,2.5f,2.5f});
            table.setWidthPercentage(100);

            addCellHeader(table, "Cita #");
            addCellHeader(table, "Fecha");
            addCellHeader(table, "Médico");
            addCellHeader(table, "Motivo");
            addCellHeader(table, "Diagnóstico");
            addCellHeader(table, "Medicamentos / Observaciones");

            for (EvolucionMedica e : paciente.getHistoriaClinica().getEvoluciones()) {
                CitaMedica c = e.getCita();
                table.addCell(String.valueOf(c.getId()));
                table.addCell(c.getFechaFormatted());
                table.addCell(c.getMedico().getNombre());
                table.addCell(c.getMotivo());
                table.addCell(e.getDiagnostico());
                StringBuilder medsObs = new StringBuilder();
                if (!e.getMedicamentos().isEmpty()) {
                    for (Medicamento m : e.getMedicamentos()) medsObs.append(m.getNombre()).append(" (").append(m.getDosis()).append(") $").append(m.getPrecio()).append("; ");
                }
                if (e.getObservaciones() != null && !e.getObservaciones().isEmpty()) medsObs.append("\nObs: ").append(e.getObservaciones());
                table.addCell(medsObs.toString());
            }

            document.add(table);

            document.add(new Paragraph("\nClínica Zamora — Confianza y bienestar"));
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            document.add(new Paragraph("Generado el: " + fecha));

            JOptionPane.showMessageDialog(this, "PDF generado: " + fileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando PDF: " + ex.getMessage());
        } finally {
            document.close();
        }
    }

    private void generarPDFHistorialPagos(Pago pago) {
        String fileName = "HistorialPago_Cita_" + pago.getCita().getId() + ".pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Image logo = getPdfLogo_iText5();
            if (logo != null) {
                logo.setAlignment(Image.LEFT);
                document.add(logo);
            }

            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, new BaseColor(43,103,119));
            Paragraph title = new Paragraph("Clínica Zamora\nRecibo de Pago", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            CitaMedica c = pago.getCita();
            com.itextpdf.text.Font bold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
            Paragraph datos = new Paragraph();
            datos.add(new Paragraph("Cita #: " + c.getId(), bold));
            datos.add(new Paragraph("Paciente: " + c.getPaciente().getNombre(), bold));
            datos.add(new Paragraph("Cédula: " + c.getPaciente().getCedula(), bold));
            datos.add(new Paragraph("Médico: " + c.getMedico().getNombre(), bold));
            datos.add(new Paragraph("Fecha: " + c.getFechaFormatted(), bold));
            document.add(datos);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{4f,2f});
            table.setWidthPercentage(100);
            addCellHeader(table, "Concepto");
            addCellHeader(table, "Valor");

            table.addCell("Consulta");
            table.addCell("$" + c.getCostoConsulta());
            table.addCell("Habitación");
            table.addCell("$" + c.getCostoHabitacion());

            if (!c.getMedicamentos().isEmpty()) {
                for (Medicamento m : c.getMedicamentos()) {
                    table.addCell("Medicamento: " + m.getNombre() + " (" + m.getDosis() + ")");
                    table.addCell("$" + m.getPrecio());
                }
            }

            PdfPCell totalCell = new PdfPCell(new Paragraph("TOTAL", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD)));
            totalCell.setBackgroundColor(new BaseColor(220,245,240));
            table.addCell(totalCell);
            PdfPCell valCell = new PdfPCell(new Paragraph("$" + pago.getTotal(), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD)));
            table.addCell(valCell);

            document.add(table);

            document.add(new Paragraph("\nClínica Zamora — Confianza y bienestar"));
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            document.add(new Paragraph("Generado el: " + fecha));

            JOptionPane.showMessageDialog(this, "PDF generado: " + fileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando PDF: " + ex.getMessage());
        } finally {
            document.close();
        }
    }

    private void addCellHeader(PdfPTable table, String text) {
        PdfPCell h = new PdfPCell(new Paragraph(text, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD)));
        h.setBackgroundColor(new BaseColor(220,245,240));
        h.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h);
    }

    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaClinicoGUI gui = new SistemaClinicoGUI();
            gui.setVisible(true);
        });
    }
}
