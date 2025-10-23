/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.util.*;

public class Sistema {
    private static ArrayList<Medico> medicos = new ArrayList<>();
    private static ArrayList<Paciente> pacientes = new ArrayList<>();
    private static ArrayList<CitaMedica> citas = new ArrayList<>();
    private static ArrayList<Medicamento> medicamentos = new ArrayList<>();
    private static Map<Paciente, HistoriaClinica> historiales = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarMedicos();
        inicializarMedicamentos();
        menu();
    }

    private static void menu() {
        int opc;
        do {
            System.out.println("\n===== SISTEMA CLÍNICO =====");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Crear cita médica");
            System.out.println("3. Finalizar cita");
            System.out.println("4. Ver historial clínico");
            System.out.println("5. Ver medicamentos");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opc = sc.nextInt(); sc.nextLine();

            switch (opc) {
                case 1 -> registrarPaciente();
                case 2 -> crearCita();
                case 3 -> finalizarCita();
                case 4 -> verHistorial();
                case 5 -> verMedicamentos();
                case 6 -> System.out.println("Saliendo del sistema clínico...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opc != 6);
    }

    private static void registrarPaciente() {
        System.out.print("Ingrese nombre del paciente: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese cédula o TT: ");
        String cedula = sc.nextLine();
        System.out.print("Ingrese edad: ");
        int edad = sc.nextInt(); sc.nextLine();
        System.out.print("Ingrese síntomas o motivo de consulta: ");
        String sintomas = sc.nextLine();

        for (Paciente p : pacientes) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                System.out.print("Ya existe un paciente con ese nombre. ¿Es el mismo? (s/n): ");
                String resp = sc.nextLine();
                if (resp.equalsIgnoreCase("s")) {
                    System.out.println("Usando el historial existente del paciente.");
                    return;
                }
            }
        }

        Paciente nuevo = new Paciente(nombre, cedula, edad, sintomas);
        pacientes.add(nuevo);
        historiales.put(nuevo, new HistoriaClinica(nuevo));
        System.out.println("Paciente registrado correctamente.");
    }

    private static void crearCita() {
        if (pacientes.isEmpty()) {
            System.out.println("Debe registrar un paciente primero.");
            return;
        }

        System.out.print("Ingrese nombre del paciente: ");
        String nombre = sc.nextLine();
        Paciente paciente = buscarPaciente(nombre);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        System.out.print("Ingrese el dolor o motivo de consulta: ");
        String motivo = sc.nextLine();

        Medico recomendado = recomendarMedico(motivo);
        if (recomendado == null) {
            System.out.println("No hay médicos disponibles para ese tipo de consulta.");
            return;
        }

        if (recomendado.isOcupado()) {
            System.out.println("El " + recomendado + " está en otra cita.");
            return;
        }

        System.out.print("¿Desea asignar al " + recomendado + "? (s/n): ");
        if (!sc.nextLine().equalsIgnoreCase("s")) return;

        String habitacion = "HAB-" + (new Random().nextInt(50) + 1);
        CitaMedica cita = new CitaMedica(paciente, recomendado, motivo, habitacion);
        citas.add(cita);
        recomendado.setOcupado(true);

        System.out.println("Cita creada exitosamente.");
        System.out.println("Habitación asignada: " + habitacion);
    }

    private static void finalizarCita() {
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }

        System.out.println("\n--- LISTA DE CITAS ---");
        for (int i = 0; i < citas.size(); i++) {
            System.out.println((i + 1) + ". " + citas.get(i));
        }

        System.out.print("Seleccione el número de la cita que desea finalizar: ");
        int index = sc.nextInt() - 1; sc.nextLine();
        if (index < 0 || index >= citas.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        CitaMedica cita = citas.get(index);
        if (cita.isFinalizada()) {
            System.out.println("Esta cita ya fue finalizada anteriormente.");
            return;
        }

        cita.finalizar();
        System.out.print("Ingrese diagnóstico o evolución del paciente: ");
        String diagnostico = sc.nextLine();

        Medicamento med = medicamentos.get(new Random().nextInt(medicamentos.size()));
        EvolucionMedica evo = new EvolucionMedica(cita, diagnostico, med);
        historiales.get(cita.getPaciente()).agregarEvolucion(evo.toString());

        System.out.println("\n✅ Cita finalizada correctamente.");
        System.out.println("Evolución agregada al historial clínico del paciente.");
        System.out.println("Medicamento recomendado: " + med.getNombre());
    }

    private static void verHistorial() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }

        System.out.println("\n--- PACIENTES REGISTRADOS ---");
        for (int i = 0; i < pacientes.size(); i++) {
            System.out.println((i + 1) + ". " + pacientes.get(i).getNombre());
        }

        System.out.print("Seleccione el número del paciente: ");
        int index = sc.nextInt() - 1; sc.nextLine();
        if (index < 0 || index >= pacientes.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        Paciente p = pacientes.get(index);
        historiales.get(p).mostrarHistorial();
    }

    private static void verMedicamentos() {
        System.out.println("\n--- LISTA DE MEDICAMENTOS ---");
        for (Medicamento m : medicamentos) {
            System.out.println("- " + m.getNombre());
            System.out.println("Pacientes que lo usan:");
            for (Map.Entry<Paciente, HistoriaClinica> e : historiales.entrySet()) {
                for (String ev : e.getValue().toString().split("\n")) {
                    if (ev.contains(m.getNombre())) {
                        System.out.println("  • " + e.getKey().getNombre());
                    }
                }
            }
        }
    }

    private static Paciente buscarPaciente(String nombre) {
        for (Paciente p : pacientes)
            if (p.getNombre().equalsIgnoreCase(nombre))
                return p;
        return null;
    }

    private static Medico recomendarMedico(String motivo) {
        motivo = motivo.toLowerCase();
        for (Medico m : medicos) {
            if ((motivo.contains("pecho") && m.getEspecialidad().equalsIgnoreCase("Cardiología")) ||
                (motivo.contains("cabeza") && m.getEspecialidad().equalsIgnoreCase("Neurología")) ||
                (motivo.contains("estómago") && m.getEspecialidad().equalsIgnoreCase("Gastroenterología")) ||
                (motivo.contains("hueso") && m.getEspecialidad().equalsIgnoreCase("Ortopedia")) ||
                (motivo.contains("niño") && m.getEspecialidad().equalsIgnoreCase("Pediatría"))) {
                return m;
            }
        }
        return medicos.get(new Random().nextInt(medicos.size()));
    }

    private static void inicializarMedicos() {
        String[] especialidades = {"Cardiología", "Neurología", "Pediatría", "Ortopedia", "Gastroenterología"};
        for (int i = 1; i <= 20; i++) {
            String nombre = "Carlos " + i;
            String esp = especialidades[i % especialidades.length];
            medicos.add(new Medico(nombre, "MED" + i, 35 + i % 10, esp));
        }
    }

    private static void inicializarMedicamentos() {
        String[] nombres = {"Ibuprofeno", "Paracetamol", "Amoxicilina", "Omeprazol", "Nitroglicerina",
                "Diclofenaco", "Prednisolona", "Cefalexina", "Clonazepam", "Enalapril"};
        for (String n : nombres)
            medicamentos.add(new Medicamento(n));
    }
}