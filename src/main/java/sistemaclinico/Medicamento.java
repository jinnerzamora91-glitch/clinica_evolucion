/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private String nombre;
    private String dosis;

    public Medicamento(String nombre, String dosis) {
        this.nombre = nombre;
        this.dosis = dosis;
    }

    public String getNombre() { return nombre; }
    public String getDosis() { return dosis; }

    @Override
    public String toString() {
        return nombre + " - Dosis: " + dosis;
    }
}