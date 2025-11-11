package sistemaclinico;

public class Medico extends Persona implements Registrable {
    private String especialidad;
    private double tarifaConsulta;
    private boolean disponible = true;

    public Medico(String nombre, String cedula, int edad, String especialidad, double tarifaConsulta) {
        super(nombre, cedula, edad);
        this.especialidad = especialidad;
        this.tarifaConsulta = tarifaConsulta;
    }

    public String getEspecialidad() { return especialidad; }
    public double getTarifaConsulta() { return tarifaConsulta; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public void registrar() { System.out.println("Médico registrado: " + nombre); }

    @Override
    public String toString() { return nombre + " (" + especialidad + ")"; }
}
