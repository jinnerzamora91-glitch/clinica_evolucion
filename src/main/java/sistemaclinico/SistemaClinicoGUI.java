package sistemaclinico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;

/**
 * GUI principal de la clínica VitalSalud.
 */
public class SistemaClinicoGUI extends JFrame {

    private Sistema sistema;
    private JPanel contentPanel;

    public SistemaClinicoGUI() {
        sistema = new Sistema();
        initUI();
    }

    private void initUI() {
        setTitle("Clínica VitalSalud");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel lateral = new JPanel();
        lateral.setBackground(new Color(82, 171, 152));
        lateral.setPreferredSize(new Dimension(260, getHeight()));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel lblLogo = new JLabel("LOGO");
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel lblName = new JLabel("VitalSalud");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        lateral.add(lblLogo);
        lateral.add(Box.createRigidArea(new Dimension(0, 10)));
        lateral.add(lblName);
        lateral.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] botones = {"Registrar Paciente", "Registrar Cita", "Finalizar Cita", "Historial Clínico", "Historial Pagos"};
        for (String b : botones) {
            JButton btn = new JButton(b);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(43, 103, 119));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.addActionListener(e -> onMenuSelected(b));
            lateral.add(btn);
            lateral.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        add(lateral, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);

        showWelcome();
    }

    private void showWelcome() {
        contentPanel.removeAll();
        JLabel label = new JLabel("<html><center>Bienvenido a <b>VitalSalud</b><br>Seleccione una opción del menú</center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        contentPanel.add(label, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void onMenuSelected(String item) {
        contentPanel.removeAll();
        switch (item) {
            case "Registrar Paciente":
                contentPanel.add(new PanelRegistrarPaciente(sistema));
                break;
            case "Registrar Cita":
                contentPanel.add(new PanelRegistrarCita(sistema));
                break;
            case "Finalizar Cita":
                contentPanel.add(new PanelFinalizarCita(sistema));
                break;
            case "Historial Clínico":
                contentPanel.add(new PanelHistorialClinico(sistema));
                break;
            case "Historial Pagos":
                contentPanel.add(new PanelHistorialPagos(sistema));
                break;
            default:
                showWelcome();
        }
        revalidate();
        repaint();
    }

    // =================== PANEL: REGISTRAR PACIENTE ===================
    class PanelRegistrarPaciente extends JPanel {
        private JTextField txtNombre, txtCedula, txtEdad, txtDireccion, txtTelefono;
        private Sistema sistema;

        public PanelRegistrarPaciente(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new GridLayout(7, 2, 10, 10));

            add(new JLabel("Nombre:")); txtNombre = new JTextField(); add(txtNombre);
            add(new JLabel("Cédula:")); txtCedula = new JTextField(); add(txtCedula);
            add(new JLabel("Edad:")); txtEdad = new JTextField(); add(txtEdad);
            add(new JLabel("Dirección:")); txtDireccion = new JTextField(); add(txtDireccion);
            add(new JLabel("Teléfono:")); txtTelefono = new JTextField(); add(txtTelefono);

            JButton btnRegistrar = new JButton("Registrar");
            btnRegistrar.addActionListener(e -> registrarPaciente());
            add(btnRegistrar);
        }

        private void registrarPaciente() {
            try {
                String nombre = txtNombre.getText();
                String cedula = txtCedula.getText();
                int edad = Integer.parseInt(txtEdad.getText());
                String direccion = txtDireccion.getText();
                String telefono = txtTelefono.getText();

                Paciente p = new Paciente(nombre, cedula, edad, direccion, telefono);
                sistema.registrarPaciente(p);
                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
                txtNombre.setText(""); txtCedula.setText(""); txtEdad.setText(""); txtDireccion.setText(""); txtTelefono.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    // =================== PANEL: REGISTRAR CITA ===================
    class PanelRegistrarCita extends JPanel {
        private JComboBox<Paciente> comboPaciente;
        private JTextField txtMotivo;
        private Sistema sistema;

        public PanelRegistrarCita(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new GridLayout(4, 2, 10, 10));

            add(new JLabel("Paciente:"));
            comboPaciente = new JComboBox<>(sistema.getPacientes().toArray(new Paciente[0]));
            add(comboPaciente);

            add(new JLabel("Motivo de la cita:"));
            txtMotivo = new JTextField();
            add(txtMotivo);

            JButton btnRegistrar = new JButton("Registrar Cita");
            btnRegistrar.addActionListener(e -> registrarCita());
            add(btnRegistrar);
        }

        private void registrarCita() {
            Paciente p = (Paciente) comboPaciente.getSelectedItem();
            String motivo = txtMotivo.getText();

            if (p == null || motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            Medico m = sistema.asignarMedicoPorMotivo(motivo);
            CitaMedica cita = new CitaMedica(p, m, motivo, m.getTarifaConsulta(), 0);
            sistema.registrarCita(cita);
            JOptionPane.showMessageDialog(this, "Cita registrada con el médico: " + m.getNombre());
        }
    }

    // =================== PANEL: FINALIZAR CITA ===================
    class PanelFinalizarCita extends JPanel {
        private JComboBox<CitaMedica> comboCitas;
        private JTextField txtDiagnostico;
        private JTextField txtObservaciones;
        private Sistema sistema;

        public PanelFinalizarCita(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new GridLayout(6, 2, 10, 10));

            add(new JLabel("Cita:"));
            comboCitas = new JComboBox<>(sistema.getCitasActivas().toArray(new CitaMedica[0]));
            add(comboCitas);

            add(new JLabel("Diagnóstico:")); txtDiagnostico = new JTextField(); add(txtDiagnostico);
            add(new JLabel("Observaciones:")); txtObservaciones = new JTextField(); add(txtObservaciones);

            JButton btnFinalizar = new JButton("Finalizar Cita");
            btnFinalizar.addActionListener(e -> finalizarCita());
            add(btnFinalizar);
        }

        private void finalizarCita() {
            CitaMedica cita = (CitaMedica) comboCitas.getSelectedItem();
            if (cita == null) { JOptionPane.showMessageDialog(this, "Seleccione una cita."); return; }

            String diag = txtDiagnostico.getText();
            String obs = txtObservaciones.getText();

            // Pedir medicamentos y dosis
            List<Medicamento> meds = new ArrayList<>();
            boolean agregar = true;
            while (agregar) {
                String nombreMed = JOptionPane.showInputDialog(this, "Nombre del medicamento (Cancelar para finalizar):");
                if (nombreMed == null || nombreMed.isEmpty()) break;
                String dosis = JOptionPane.showInputDialog(this, "Dosis del medicamento:");
                String precioStr = JOptionPane.showInputDialog(this, "Precio del medicamento:");
                double precio = 0;
                try { precio = Double.parseDouble(precioStr); } catch (Exception e) {}
                meds.add(new Medicamento(nombreMed, dosis, precio));
            }

            sistema.finalizarCita(cita, diag, obs, meds);
            JOptionPane.showMessageDialog(this, "Cita finalizada correctamente.");
            comboCitas.removeItem(cita);
        }
    }

    // =================== PANEL: HISTORIAL CLÍNICO ===================
    class PanelHistorialClinico extends JPanel {
        private JTable table;
        private Sistema sistema;

        public PanelHistorialClinico(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new BorderLayout());
            table = new JTable();
            JButton btnPDF = new JButton("Exportar PDF");
            btnPDF.addActionListener(e -> generarPDF());
            add(btnPDF, BorderLayout.NORTH);
            refreshTable();
            add(new JScrollPane(table), BorderLayout.CENTER);
        }

        private void refreshTable() {
            String[] columnas = {"Paciente", "Evoluciones"};
            DefaultTableModel model = new DefaultTableModel(columnas, 0);
            for (Paciente p : sistema.getPacientes()) {
                StringBuilder sb = new StringBuilder();
                for (EvolucionMedica e : p.getHistoriaClinica().getEvoluciones()) {
                    sb.append(e.getDiagnostico()).append("; ");
                }
                model.addRow(new Object[]{p.getNombre(), sb.toString()});
            }
            table.setModel(model);
        }

        private void generarPDF() {
            try {
                String path = "Historial_Clinico.pdf";
                PdfWriter writer = new PdfWriter(path);
                PdfDocument pdf = new PdfDocument(writer);
                Document doc = new Document(pdf);

                for (Paciente p : sistema.getPacientes()) {
                    doc.add(new Paragraph("Paciente: " + p.getNombre()));
                    for (EvolucionMedica e : p.getHistoriaClinica().getEvoluciones()) {
                        doc.add(new Paragraph(" - Diagnóstico: " + e.getDiagnostico()));
                        doc.add(new Paragraph("   Observaciones: " + e.getObservaciones()));
                        if (!e.getMedicamentos().isEmpty()) {
                            doc.add(new Paragraph("   Medicamentos:"));
                            for (Medicamento m : e.getMedicamentos()) {
                                doc.add(new Paragraph("      " + m));
                            }
                        }
                    }
                    doc.add(new Paragraph("-----------------------------------------------------"));
                }

                doc.close();
                JOptionPane.showMessageDialog(this, "PDF generado correctamente en: " + path);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage());
            }
        }
    }

    // =================== PANEL: HISTORIAL PAGOS ===================
    class PanelHistorialPagos extends JPanel {
        private JTable table;
        private Sistema sistema;

        public PanelHistorialPagos(Sistema sistema) {
            this.sistema = sistema;
            setLayout(new BorderLayout());
            table = new JTable();
            JButton btnPDF = new JButton("Exportar PDF");
            btnPDF.addActionListener(e -> generarPDF());
            add(btnPDF, BorderLayout.NORTH);
            refreshTable();
            add(new JScrollPane(table), BorderLayout.CENTER);
        }

        private void refreshTable() {
            String[] columnas = {"Cita", "Paciente", "Total"};
            DefaultTableModel model = new DefaultTableModel(columnas, 0);
            for (Pago p : sistema.getPagos()) {
                model.addRow(new Object[]{p.getCita().getId(), p.getCita().getPaciente().getNombre(), p.getTotal()});
            }
            table.setModel(model);
        }

        private void generarPDF() {
            try {
                String path = "Historial_Pagos.pdf";
                PdfWriter writer = new PdfWriter(path);
                PdfDocument pdf = new PdfDocument(writer);
                Document doc = new Document(pdf);

                for (Pago p : sistema.getPagos()) {
                    doc.add(new Paragraph("Cita #" + p.getCita().getId() + " - Paciente: " + p.getCita().getPaciente().getNombre() + " - Total: $" + p.getTotal()));
                }

                doc.close();
                JOptionPane.showMessageDialog(this, "PDF generado correctamente en: " + path);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage());
            }
        }
    }

    // =================== MAIN ===================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaClinicoGUI gui = new SistemaClinicoGUI();
            gui.setVisible(true);
        });
    }
}