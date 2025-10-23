/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.util.ArrayList;

public class HistoriaClinica {
    private Paciente paciente;
    private ArrayList<String> evoluciones;

    public HistoriaClinica(Paciente paciente) {
        this.paciente = paciente;
        this.evoluciones = new ArrayList<>();
    }

    public void agregarEvolucion(String evolucion) {
        evoluciones.add(evolucion);
    }

    public void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL CLÍNICO DE " + paciente.getNombre().toUpperCase() + " ---");
        if (evoluciones.isEmpty()) {
            System.out.println("No hay evoluciones registradas.");
        } else {
            for (String e : evoluciones) {
                System.out.println(e);
                System.out.println("--------------------------------------");
            }
        }
    }
}