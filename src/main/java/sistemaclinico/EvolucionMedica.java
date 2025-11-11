package sistemaclinico;

import java.util.List;

public class EvolucionMedica {
    private CitaMedica cita;
    private String diagnostico;
    private String observaciones;
    private List<Medicamento> medicamentos;

    public EvolucionMedica(CitaMedica cita, String diagnostico, String observaciones, List<Medicamento> medicamentos) {
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.medicamentos = medicamentos;
    }

    public CitaMedica getCita() { return cita; }
    public String getDiagnostico() { return diagnostico; }
    public String getObservaciones() { return observaciones; }
    public List<Medicamento> getMedicamentos() { return medicamentos; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evolución - Cita #").append(cita.getId()).append("\n");
        sb.append("Motivo: ").append(cita.getMotivo()).append("\n");
        sb.append("Diagnóstico: ").append(diagnostico).append("\n");
        sb.append("Observaciones: ").append(observaciones).append("\n");
        sb.append("Medicamentos:\n");
        for (Medicamento m : medicamentos) sb.append(" - ").append(m).append("\n");
        return sb.toString();
    }
}
