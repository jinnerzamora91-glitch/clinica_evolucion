package sistemaclinico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CitaMedica implements Registrable {
    private static int COUNTER = 1;
    private final int id;
    private Paciente paciente;
    private Medico medico;
    private String motivo;
    private LocalDateTime fecha;
    private boolean finalizada;
    private double costoConsulta;
    private double costoHabitacion;
    private String diagnostico;
    private List<Medicamento> medicamentos = new ArrayList<>();

    public CitaMedica(Paciente paciente, Medico medico, String motivo, double costoConsulta, double costoHabitacion) {
        this.id = COUNTER++;
        this.paciente = paciente;
        this.medico = medico;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();
        this.costoConsulta = costoConsulta;
        this.costoHabitacion = costoHabitacion;
        this.finalizada = false;
    }

    public int getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }
    public String getMotivo() { return motivo; }
    public String getFechaFormatted() { return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")); }
    public boolean isFinalizada() { return finalizada; }
    public double getCostoConsulta() { return costoConsulta; }
    public double getCostoHabitacion() { return costoHabitacion; }
    public String getDiagnostico() { return diagnostico; }
    public List<Medicamento> getMedicamentos() { return medicamentos; }

    public void finalizar(String diagnostico, List<Medicamento> meds) {
        this.diagnostico = diagnostico;
        this.medicamentos = new ArrayList<>(meds);
        this.finalizada = true;
        if (medico != null) medico.setDisponible(true);
    }

    public double calcularTotal() {
        double total = costoConsulta + costoHabitacion;
        for (Medicamento m : medicamentos) total += m.getPrecio();
        return total;
    }

    @Override
    public void registrar() { if (medico != null) medico.setDisponible(false); }

    @Override
    public String toString() { return "Cita #" + id + " - " + paciente.getNombre() + " | " + motivo + " | " + getFechaFormatted() + (finalizada ? " (Finalizada)" : ""); }
}
