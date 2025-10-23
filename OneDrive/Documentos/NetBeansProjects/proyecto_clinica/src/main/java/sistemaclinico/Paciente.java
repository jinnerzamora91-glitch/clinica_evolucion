/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

public class Paciente extends Persona {
    private String sintomas;

    public Paciente(String nombre, String cedula, int edad, String sintomas) {
        super(nombre, cedula, edad);
        this.sintomas = sintomas;
    }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    @Override
    public String toString() {
        return nombre + " - " + sintomas;
    }
}