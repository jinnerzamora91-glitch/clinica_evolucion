package sistemaclinico;

import java.util.*;
import java.util.stream.Collectors;

public class Sistema {
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();
    private List<CitaMedica> citas = new ArrayList<>();
    private List<Pago> pagos = new ArrayList<>();

    public Sistema() {
        inicializarMedicos();
    }

    private void inicializarMedicos() {
        medicos.add(new Medico("Pedro García", "M001", 45, "Cardiología", 120000));
        medicos.add(new Medico("Ana Torres", "M002", 38, "Neurología", 130000));
        medicos.add(new Medico("Luis Ramírez", "M003", 42, "Pediatría", 90000));
        medicos.add(new Medico("Carolina López", "M004", 50, "Ortopedia", 100000));
        medicos.add(new Medico("Daniel Gómez", "M005", 37, "Gastroenterología", 95000));
        medicos.add(new Medico("Marta Ruiz",   "M006", 34, "Medicina General", 70000));
        medicos.add(new Medico("Roberto Díaz", "M007", 41, "Medicina General", 70000));
        medicos.add(new Medico("Sofia Méndez", "M008", 36, "Dermatología", 90000));
    }

    // Pacientes
    public void registrarPaciente(Paciente p) { pacientes.add(p); }
    public List<Paciente> getPacientes() { return Collections.unmodifiableList(pacientes); }

    // Medicos
    public List<Medico> getMedicos() { return Collections.unmodifiableList(medicos); }

    // Citas
    public void registrarCita(CitaMedica c) {
        citas.add(c);
        c.registrar();
    }
    public List<CitaMedica> getCitasActivas() {
        return citas.stream().filter(c -> !c.isFinalizada()).collect(Collectors.toList());
    }
    public List<CitaMedica> getCitasFinalizadas() {
        return citas.stream().filter(CitaMedica::isFinalizada).collect(Collectors.toList());
    }
    public List<CitaMedica> getTodasCitas() { return Collections.unmodifiableList(citas); }

    // Finalizar cita
    public void finalizarCita(CitaMedica cita, String diagnostico, String observaciones, List<Medicamento> meds) {
        cita.finalizar(diagnostico, meds);
        EvolucionMedica ev = new EvolucionMedica(cita, diagnostico, observaciones, meds);
        cita.getPaciente().getHistoriaClinica().agregarEvolucion(ev);
        Pago pago = new Pago(cita, meds);
        pagos.add(pago);
    }

    public List<Pago> getPagos() { return Collections.unmodifiableList(pagos); }

    // Asignación básica de médico por motivo
    public Medico asignarMedicoPorMotivo(String motivo) {
        String m = motivo == null ? "" : motivo.toLowerCase();
        Map<String, String> palabrasAEspecialidad = new HashMap<>();
        palabrasAEspecialidad.put("pecho", "Cardiología");
        palabrasAEspecialidad.put("cardio", "Cardiología");
        palabrasAEspecialidad.put("cabeza", "Neurología");
        palabrasAEspecialidad.put("niño", "Pediatría");
        palabrasAEspecialidad.put("nino", "Pediatría");
        palabrasAEspecialidad.put("estómago", "Gastroenterología");
        palabrasAEspecialidad.put("estomago", "Gastroenterología");
        palabrasAEspecialidad.put("hueso", "Ortopedia");
        palabrasAEspecialidad.put("piel", "Dermatología");
        palabrasAEspecialidad.put("general", "Medicina General");

        for (Map.Entry<String,String> e : palabrasAEspecialidad.entrySet()) {
            if (m.contains(e.getKey())) {
                Optional<Medico> opt = medicos.stream().filter(md -> md.getEspecialidad().equalsIgnoreCase(e.getValue()) && md.isDisponible()).findFirst();
                if (opt.isPresent()) return opt.get();
            }
        }
        Optional<Medico> general = medicos.stream().filter(md -> md.getEspecialidad().toLowerCase().contains("general") && md.isDisponible()).findFirst();
        if (general.isPresent()) return general.get();
        Optional<Medico> cualquiera = medicos.stream().filter(Medico::isDisponible).findFirst();
        return cualquiera.orElse(medicos.get(0));
    }
}
