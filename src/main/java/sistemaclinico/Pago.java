package sistemaclinico;

import java.util.List;

public class Pago {
    private CitaMedica cita;
    private double totalMedicamentos;
    private double total;

    public Pago(CitaMedica cita, List<Medicamento> medicamentos) {
        this.cita = cita;
        this.totalMedicamentos = medicamentos.stream().mapToDouble(Medicamento::getPrecio).sum();
        this.total = cita.getCostoConsulta() + cita.getCostoHabitacion() + totalMedicamentos;
    }

    public CitaMedica getCita() { return cita; }
    public double getTotalMedicamentos() { return totalMedicamentos; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return "Pago - Cita #" + cita.getId() + " - Paciente: " + cita.getPaciente().getNombre() + " - Total: $" + total;
    }
}
