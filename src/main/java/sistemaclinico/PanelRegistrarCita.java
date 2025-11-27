package sistemaclinico;

import javax.swing.*;
import java.awt.*;

public class PanelRegistrarCita extends JPanel {
    private static final Color ACCENT_DARK = new Color(43, 103, 119);
    private JComboBox<Paciente> cbPacientes;
    private JTextField tfMotivo, tfCostoHab;
    private Sistema sistema;

    // CAMBIA COLOR DEL PANEL
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
