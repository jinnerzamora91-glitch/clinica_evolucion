package sistemaclinico;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class SistemaClinicoGUI_Backup extends JFrame {

    private Sistema sistema;
    private JPanel contentPanel;
    private BufferedImage logoImage;
    private static final Color ACCENT = new Color(82, 171, 152);
    private static final Color ACCENT_DARK = new Color(43, 103, 119);

    public SistemaClinicoGUI_Backup() {
        sistema = new Sistema();
        loadLogo();
        initUI();
    }

    /**
     * Carga el logo desde recursos (dentro del proyecto o ruta absoluta).
     */
    private void loadLogo() {
        // intenta recurso empaquetado (resources/logo.png)
        try (InputStream is = getClass().getResourceAsStream("/logo.png")) {
            if (is != null) {
                logoImage = ImageIO.read(is);
                return;
            }
        } catch (Exception ignored) {}

        // ruta donde esta el logo
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
        // NUEVO: Historial Médicos
        addMenuButton(lateral, "Historial Médicos", e -> showPanel(new PanelHistorialMedicos(sistema)));

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
      //cambiar color del panel del medio
    private void showWelcome() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("<html><center><span style='font-size:22px;font-weight:700'>Bienvenido a la Clinica Zamora</span><br><span style='font-size:12px;color:#2B6777'>Seleccione una opción del menú</span></center></html>", SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        showPanel(p);
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaClinicoGUI_Backup gui = new SistemaClinicoGUI_Backup();
            gui.setVisible(true);
        });
    }
}
