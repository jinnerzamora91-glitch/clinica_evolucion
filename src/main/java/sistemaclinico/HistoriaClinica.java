/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoriaClinica implements Serializable {
    private ArrayList<EvolucionMedica> evoluciones = new ArrayList<>();

    public void agregarEvolucion(EvolucionMedica evolucion) {
        evoluciones.add(evolucion);
    }

    public void mostrarHistorial() {
        System.out.println("\nEvoluciones registradas:");
        if (evoluciones.isEmpty()) {
            System.out.println("No hay evoluciones médicas registradas.");
        } else {
            for (EvolucionMedica e : evoluciones) {
                System.out.println("-------------------------------------");
                System.out.println(e);
            }
        }
    }
}