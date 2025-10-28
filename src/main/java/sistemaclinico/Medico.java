/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

public class Medico extends Persona implements Registrable {
    private String especialidad;
    private boolean disponible;

    public Medico(String nombre, String cedula, int edad, String especialidad, boolean disponible) {
        super(nombre, cedula, edad);
        this.especialidad = especialidad;
        this.disponible = disponible;
    }

    public String getEspecialidad() { return especialidad; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public void registrar() {
        System.out.println("Registrando médico: " + nombre);
    }

    @Override
    public String toString() {
        return super.toString() + " | Especialidad: " + especialidad + " (" + (disponible ? "Disponible" : "Ocupado") + ")";
    }
}