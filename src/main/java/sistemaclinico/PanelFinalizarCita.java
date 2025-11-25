package sistemaclinico;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelFinalizarCita extends JPanel {
    private static final Color ACCENT_DARK = new Color(43, 103, 119);
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
