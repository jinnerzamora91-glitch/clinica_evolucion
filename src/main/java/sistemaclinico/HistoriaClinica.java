package sistemaclinico;

import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica {
    private Paciente paciente;
    private List<EvolucionMedica> evoluciones = new ArrayList<>();

    public HistoriaClinica(Paciente paciente) {
        this.paciente = paciente;
    }

    public void agregarEvolucion(EvolucionMedica e) { evoluciones.add(e); }
    public List<EvolucionMedica> getEvoluciones() { return evoluciones; }

    @Override
    public String toString() {
        return "Historia de " + paciente.getNombre() + " - " + evoluciones.size() + " evoluciones";
    }
}
