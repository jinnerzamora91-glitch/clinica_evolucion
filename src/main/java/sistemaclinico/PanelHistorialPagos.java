package sistemaclinico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHistorialPagos extends JPanel {
    private static final Color ACCENT_DARK = new Color(43, 103, 119);
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
            new PdfHelper().generarPDFHistorialPagos(p);
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
