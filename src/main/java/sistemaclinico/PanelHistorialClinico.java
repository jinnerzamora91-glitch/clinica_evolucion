package sistemaclinico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHistorialClinico extends JPanel {
    private static final Color ACCENT_DARK = new Color(43, 103, 119);
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
            // replicamos el PDF (igual que antes)
            new PdfHelper().generarPDFHistorialClinico(p);
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
