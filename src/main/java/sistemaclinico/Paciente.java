package sistemaclinico;

public class Paciente extends Persona implements Registrable {
    private String direccion;
    private String telefono;
    private HistoriaClinica historiaClinica;

    public Paciente(String nombre, String cedula, int edad, String direccion, String telefono) {
        super(nombre, cedula, edad);
        this.direccion = direccion;
        this.telefono = telefono;
        this.historiaClinica = new HistoriaClinica(this);
    }

    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public HistoriaClinica getHistoriaClinica() { return historiaClinica; }

    @Override
    public void registrar() { System.out.println("Paciente registrado: " + nombre); }

    @Override
    public String toString() { return nombre + " - " + cedula; }
}
