package sistemaclinico;

public class Medicamento {
    private String nombre;
    private String dosis;
    private double precio;

    public Medicamento(String nombre, String dosis, double precio) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public String getDosis() { return dosis; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() { return nombre + " - " + dosis + " ($" + precio + ")"; }
}
