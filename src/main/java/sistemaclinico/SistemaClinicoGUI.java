package sistemaclinico;

import javax.swing.*;
import java.awt.*;

public class SistemaClinicoGUI extends JFrame {

    public SistemaClinicoGUI() {
        setTitle("Clínica Vida Sana");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 250, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //logo, formato png
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("logo.png"));
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(img));
            lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblLogo);
        } catch (Exception e) {
            JLabel lblNoLogo = new JLabel("🏥");
            lblNoLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
            lblNoLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblNoLogo);
        }

        // titulo o nombre del sistema clinico
        JLabel lblTitulo = new JLabel("Clínica Vida Sana");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setForeground(new Color(20, 60, 120));
        panel.add(lblTitulo);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] opciones = {
            "Registrar Paciente",
            "Crear Cita Médica",
            "Finalizar Cita Médica",
            "Ver Historial Clínico",
            "Salir"
        };

        //botones 
        for (String texto : opciones) {
            JButton boton = new JButton(texto);
            boton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            boton.setFocusPainted(false);
            boton.setBackground(new Color(200, 230, 255));
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(250, 40));

            boton.addActionListener(e -> {
                if (texto.equals("Salir")) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Funcionalidad de '" + texto + "' en desarrollo.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

            panel.add(boton);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(panel); // agrega panel
        setVisible(true); //muestra la ventana
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaClinicoGUI::new);
    }
}
