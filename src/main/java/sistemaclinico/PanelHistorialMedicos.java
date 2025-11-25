package sistemaclinico;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel para mostrar historial de actividad médica y generar PDF (iText 5).
 * - Muestra médicos que han atendido citas finalizadas.
 * - Permite generar PDF con resumen y detalle.
 * - Calcula lo cobrado por cada médico: tarifa de consulta + 10% sobre otros conceptos (medicamentos, habitación).
 */
public class PanelHistorialMedicos extends JPanel {
    private static final Color ACCENT_DARK = new Color(43, 103, 119);
    private Sistema sistema;
    private JComboBox<Medico> cbMedicos;
    private JTable table;
    private JButton btnPdf, btnRefresh;

    public PanelHistorialMedicos(Sistema sistema) {
        this.sistema = sistema;
        setLayout(new BorderLayout(8,8));
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        cbMedicos = new JComboBox<>(obtenerMedicosConActividad().toArray(new Medico[0]));
        btnPdf = new JButton("Generar PDF de actividad del médico");
        btnPdf.setBackground(ACCENT_DARK); btnPdf.setForeground(Color.WHITE);
        btnPdf.addActionListener(e -> {
            Medico sel = (Medico) cbMedicos.getSelectedItem();
            if (sel == null) { JOptionPane.showMessageDialog(this, "Seleccione un médico."); return; }
            generarPDFHistorialMedico(sel);
        });
        btnRefresh = new JButton("Actualizar lista");
        btnRefresh.addActionListener(e -> refresh());
        top.add(new JLabel("Médico:")); top.add(cbMedicos); top.add(btnPdf); top.add(btnRefresh);

        add(top, BorderLayout.NORTH);
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        cbMedicos.addActionListener(e -> refreshTable());
        refresh();
    }

    private List<Medico> obtenerMedicosConActividad() {
        // Medicos que aparecen en citas finalizadas
        List<Medico> res = sistema.getCitasFinalizadas().stream().map(CitaMedica::getMedico).distinct().collect(Collectors.toList());
        return res.isEmpty() ? sistema.getMedicos() : res;
    }

    private void refresh() {
        List<Medico> med = obtenerMedicosConActividad();
        cbMedicos.setModel(new DefaultComboBoxModel<>(med.toArray(new Medico[0])));
        refreshTable();
    }

    private void refreshTable() {
        Medico m = (Medico) cbMedicos.getSelectedItem();
        String[] cols = {"Fecha", "Paciente", "Diagnóstico", "Medicamentos", "Total cobrado (paciente)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        if (m != null) {
            List<CitaMedica> citas = sistema.getTodasCitas().stream()
                    .filter(CitaMedica::isFinalizada)
                    .filter(c -> c.getMedico() != null && c.getMedico().getCedula().equals(m.getCedula()))
                    .collect(Collectors.toList());
            for (CitaMedica c : citas) {
                StringBuilder meds = new StringBuilder();
                for (Medicamento md : c.getMedicamentos()) meds.append(md.getNombre()).append(" ($").append(md.getPrecio()).append(") ");
                model.addRow(new Object[]{c.getFechaFormatted(), c.getPaciente().getNombre(), c.getDiagnostico(), meds.toString(), String.format("%.2f", c.calcularTotal())});
            }
        }
        table.setModel(model);
    }

    private void generarPDFHistorialMedico(Medico medico) {
        try {
            String fileName = "HistorialMedico_" + medico.getNombre().replaceAll("\\s+","_") + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // iText Font (nunca usamos java.awt.Font)
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, new BaseColor(43,103,119)
            );

            Paragraph title = new Paragraph("CLÍNICA ZAMORA\nHISTORIAL DE ACTIVIDAD MÉDICA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            com.itextpdf.text.Font bold = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD
            );

            Paragraph datos = new Paragraph();
            datos.add(new Paragraph("Médico: Dr. " + medico.getNombre(), bold));
            datos.add(new Paragraph("Especialidad: " + medico.getEspecialidad(), bold));
            datos.add(new Paragraph("Identificación: " + medico.getCedula(), bold));
            document.add(datos);
            document.add(Chunk.NEWLINE);

            List<CitaMedica> citas = sistema.getTodasCitas().stream()
                    .filter(CitaMedica::isFinalizada)
                    .filter(c -> c.getMedico() != null && c.getMedico().getCedula().equals(medico.getCedula()))
                    .collect(Collectors.toList());

            int totalCitas = citas.size();
            long pacientesUnicos = citas.stream().map(c -> c.getPaciente().getCedula()).distinct().count();

            String diagFrecuente = citas.stream()
                    .map(CitaMedica::getDiagnostico)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(d -> d, Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");

            String medFrecuente = citas.stream()
                    .flatMap(c -> c.getMedicamentos().stream())
                    .collect(Collectors.groupingBy(Medicamento::getNombre, Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");

            Paragraph resumen = new Paragraph();
            resumen.add(new Paragraph("Resumen General:", bold));
            resumen.add(new Paragraph("- Total de citas atendidas: " + totalCitas));
            resumen.add(new Paragraph("- Pacientes únicos atendidos: " + pacientesUnicos));
            resumen.add(new Paragraph("- Diagnóstico más frecuente: " + diagFrecuente));
            resumen.add(new Paragraph("- Medicamento más recetado: " + medFrecuente));
            document.add(resumen);
            document.add(Chunk.NEWLINE);

            PdfPTable pdfTable = new PdfPTable(new float[]{2f,3f,3f,4f});
            pdfTable.setWidthPercentage(100);
            addCellHeader(pdfTable, "Fecha");
            addCellHeader(pdfTable, "Paciente");
            addCellHeader(pdfTable, "Diagnóstico");
            addCellHeader(pdfTable, "Medicamentos recetados");

            double totalCobradoPorMedico = 0.0;
            for (CitaMedica c : citas) {
                StringBuilder meds = new StringBuilder();
                double totalMedicamentos = 0;
                for (Medicamento md : c.getMedicamentos()) {
                    meds.append(md.getNombre()).append(" (").append(md.getDosis()).append(") $").append(md.getPrecio()).append("; ");
                    totalMedicamentos += md.getPrecio();
                }
                pdfTable.addCell(c.getFechaFormatted());
                pdfTable.addCell(c.getPaciente().getNombre());
                pdfTable.addCell(c.getDiagnostico() == null ? "" : c.getDiagnostico());
                pdfTable.addCell(meds.toString());

                double ingreso = c.getCostoConsulta();
                double extras = c.getCostoHabitacion() + totalMedicamentos;
                ingreso += 0.10 * extras;
                totalCobradoPorMedico += ingreso;
            }

            document.add(pdfTable);
            document.add(Chunk.NEWLINE);

            Paragraph cobro = new Paragraph();
            cobro.add(new Paragraph("Total cobrado por el médico (tarifas + 10% de extras): $" + String.format("%.2f", totalCobradoPorMedico), bold));
            document.add(cobro);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("------------------------------------"));
            document.add(new Paragraph("Firma del sistema:"));
            document.add(new Paragraph("CLÍNICA ZAMORA © " + new SimpleDateFormat("yyyy").format(new Date())));

            document.close();
            JOptionPane.showMessageDialog(this, "PDF generado: " + fileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando PDF médico: " + ex.getMessage());
        }
    }

    private void addCellHeader(PdfPTable table, String text) {
        PdfPCell h = new PdfPCell(new Paragraph(text, new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD
        )));
        h.setBackgroundColor(new BaseColor(220,245,240));
        h.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h);
    }
}
