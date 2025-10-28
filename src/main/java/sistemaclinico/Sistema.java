/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.io.*;
import java.util.*;

public class Sistema {
    private static ArrayList<Medico> medicos = new ArrayList<>();
    private static ArrayList<Paciente> pacientes = new ArrayList<>();
    private static ArrayList<CitaMedica> citas = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        cargarDatos();
        inicializarMedicos();
        menu();
        guardarDatos();
    }

    private static void menu() {
        int opc = 0;
        do {
            System.out.println("\n=== SISTEMA DE CLÍNICA ===");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Crear cita médica");
            System.out.println("3. Finalizar cita médica");
            System.out.println("4. Ver historial clínico");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1 -> registrarPaciente();
                    case 2 -> crearCita();
                    case 3 -> finalizarCita();
                    case 4 -> verHistorial();
                    case 5 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        } while (opc != 5);
    }

    private static void registrarPaciente() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Cédula o TI: ");
        String cedula = sc.nextLine();
        System.out.print("Edad: ");
        int edad = Integer.parseInt(sc.nextLine());

        for (Paciente p : pacientes) {
            if (p.getCedula().equalsIgnoreCase(cedula) || p.getNombre().equalsIgnoreCase(nombre)) {
                System.out.print("Ya existe un paciente con ese nombre o cédula. ¿Es el mismo? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    System.out.println("Se usará el mismo historial clínico.");
                    return;
                }
            }
        }

        Paciente nuevo = new Paciente(nombre, cedula, edad);
        pacientes.add(nuevo);
        System.out.println("Paciente registrado correctamente.");
    }

    private static void crearCita() {
        if (pacientes.isEmpty()) {
            System.out.println("Debe registrar un paciente primero.");
            return;
        }

        System.out.println("\nPacientes registrados:");
        for (int i = 0; i < pacientes.size(); i++) {
            System.out.println((i + 1) + ". " + pacientes.get(i).getNombre());
        }

        System.out.print("Seleccione el número del paciente: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        if (index < 0 || index >= pacientes.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        Paciente paciente = pacientes.get(index);
        System.out.print("Motivo de la consulta: ");
        String motivo = sc.nextLine();

        List<Medico> recomendados = recomendarMedicos(motivo);
        if (recomendados.isEmpty()) {
            System.out.println("No se encontraron médicos disponibles.");
            return;
        }

        System.out.println("\nMédicos recomendados:");
        for (int i = 0; i < recomendados.size(); i++) {
            System.out.println((i + 1) + ". " + recomendados.get(i));
        }

        System.out.print("Seleccione el médico: ");
        int medIndex = Integer.parseInt(sc.nextLine()) - 1;
        if (medIndex < 0 || medIndex >= recomendados.size()) return;

        Medico medico = recomendados.get(medIndex);
        System.out.print("¿Desea agendar la cita? (s/n): ");
        if (!sc.nextLine().equalsIgnoreCase("s")) return;

        CitaMedica cita = new CitaMedica(paciente, medico, motivo);
        citas.add(cita);
        medico.setDisponible(false);
        System.out.println("✅ Cita agendada correctamente.");
    }

    private static void finalizarCita() {
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }

        System.out.println("\nCitas activas:");
        for (int i = 0; i < citas.size(); i++) {
            System.out.println((i + 1) + ". " + citas.get(i));
        }

        System.out.print("Seleccione la cita a finalizar: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        if (index < 0 || index >= citas.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        CitaMedica cita = citas.get(index);
        if (cita.isFinalizada()) {
            System.out.println("Esta cita ya fue finalizada.");
            return;
        }

        System.out.print("Ingrese diagnóstico: ");
        String diagnostico = sc.nextLine();
        System.out.print("Ingrese observaciones: ");
        String observaciones = sc.nextLine();
        System.out.print("Ingrese nombre del medicamento: ");
        String nombreMed = sc.nextLine();
        System.out.print("Ingrese dosis: ");
        String dosis = sc.nextLine();

        EvolucionMedica evolucion = new EvolucionMedica(cita, diagnostico, observaciones, nombreMed, dosis);
        cita.getPaciente().getHistoriaClinica().agregarEvolucion(evolucion);
        cita.setFinalizada(true);
        cita.getMedico().setDisponible(true);

        System.out.println("\n✅ Cita finalizada correctamente.");
    }

    private static void verHistorial() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }

        System.out.println("\nPacientes registrados:");
        for (int i = 0; i < pacientes.size(); i++) {
            System.out.println((i + 1) + ". " + pacientes.get(i).getNombre());
        }

        System.out.print("Seleccione el número del paciente: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        if (index < 0 || index >= pacientes.size()) {
            System.out.println("Selección inválida.");
            return;
        }

        pacientes.get(index).getHistoriaClinica().mostrarHistorial();
    }

    private static List<Medico> recomendarMedicos(String motivo) {
        motivo = motivo.toLowerCase();
        List<Medico> recomendados = new ArrayList<>();
        for (Medico m : medicos) {
            if ((motivo.contains("pecho") && m.getEspecialidad().equalsIgnoreCase("Cardiología")) ||
                (motivo.contains("cabeza") && m.getEspecialidad().equalsIgnoreCase("Neurología")) ||
                (motivo.contains("estómago") && m.getEspecialidad().equalsIgnoreCase("Gastroenterología")) ||
                (motivo.contains("hueso") && m.getEspecialidad().equalsIgnoreCase("Ortopedia")) ||
                (motivo.contains("niño") && m.getEspecialidad().equalsIgnoreCase("Pediatría"))) {
                recomendados.add(m);
            }
        }
        return recomendados;
    }

    private static void inicializarMedicos() {
        if (!medicos.isEmpty()) return;
        medicos.add(new Medico("Pedro García", "1001", 45, "Cardiología", true));
        medicos.add(new Medico("Ana Torres", "1002", 38, "Neurología", true));
        medicos.add(new Medico("Luis Ramírez", "1003", 42, "Pediatría", true));
        medicos.add(new Medico("Carolina López", "1004", 50, "Ortopedia", true));
        medicos.add(new Medico("Daniel Gómez", "1005", 37, "Gastroenterología", true));
    }

    private static void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datosClinica.dat"))) {
            oos.writeObject(pacientes);
            oos.writeObject(citas);
            System.out.println("✅ Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos.");
        }
    }

    private static void cargarDatos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("datosClinica.dat"))) {
            pacientes = (ArrayList<Paciente>) ois.readObject();
            citas = (ArrayList<CitaMedica>) ois.readObject();
            System.out.println("📂 Datos cargados correctamente.");
        } catch (Exception e) {
            System.out.println("No hay datos previos guardados.");
        }
    }
}