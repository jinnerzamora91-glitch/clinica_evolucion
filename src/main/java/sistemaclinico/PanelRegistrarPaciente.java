package sistemaclinico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRegistrarPaciente extends JPanel {
    private static final Color ACCENT_DARK = new Color(43, 103, 119);
    private Sistema sistema;
    private JTextField tfNombre, tfCedula, tfEdad, tfDireccion, tfTelefono;

    // CAMBIA EL COLOR DEL PANEL DEL MEDIO
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
