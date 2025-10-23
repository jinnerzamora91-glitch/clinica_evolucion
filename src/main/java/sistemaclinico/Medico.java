/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

public class Medico extends Persona {
    private String especialidad;
    private boolean ocupado;

    public Medico(String nombre, String cedula, int edad, String especialidad) {
        super(nombre, cedula, edad);
        this.especialidad = especialidad;
        this.ocupado = false;
    }

    public String getEspecialidad() { return especialidad; }
    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }

    @Override
    public String toString() {
        return "Dr. " + nombre + " (" + especialidad + ")";
    }
}