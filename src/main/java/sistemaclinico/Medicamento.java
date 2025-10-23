/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

public class Medicamento {
    private String nombre;

    public Medicamento(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}