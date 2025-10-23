/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

public abstract class Persona {
    protected String nombre;
    protected String cedula;
    protected int edad;

    public Persona(String nombre, String cedula, int edad) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public String getCedula() { return cedula; }
    public int getEdad() { return edad; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setEdad(int edad) { this.edad = edad; }

    @Override
    public String toString() {
        return nombre + " (Cédula: " + cedula + ", Edad: " + edad + ")";
    }
}