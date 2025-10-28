/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

public class Paciente extends Persona implements Registrable {
    private HistoriaClinica historiaClinica;

    public Paciente(String nombre, String cedula, int edad) {
        super(nombre, cedula, edad);
        this.historiaClinica = new HistoriaClinica();
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    @Override
    public void registrar() {
        System.out.println("Registrando paciente: " + nombre);
    }

    @Override
    public String toString() {
        return "Paciente: " + super.toString();
    }
}