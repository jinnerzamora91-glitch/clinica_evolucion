/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CitaMedica {
    private Paciente paciente;
    private Medico medico;
    private String motivo;
    private LocalDateTime fecha;
    private boolean finalizada;
    private String habitacion;

    public CitaMedica(Paciente paciente, Medico medico, String motivo, String habitacion) {
        this.paciente = paciente;
        this.medico = medico;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();
        this.finalizada = false;
        this.habitacion = habitacion;
    }

    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFecha() { return fecha; }
    public boolean isFinalizada() { return finalizada; }
    public String getHabitacion() { return habitacion; }

    public void finalizar() {
        finalizada = true;
        medico.setOcupado(false);
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return paciente.getNombre() + " - " + medico + " | Motivo: " + motivo +
                " | Fecha: " + fecha.format(f) + " | Habitación: " + habitacion;
    }
}