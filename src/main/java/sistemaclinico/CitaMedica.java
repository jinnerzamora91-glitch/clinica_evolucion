/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.io.Serializable;

public class CitaMedica implements Serializable {
    private Paciente paciente;
    private Medico medico;
    private String motivo;
    private boolean finalizada;

    public CitaMedica(Paciente paciente, Medico medico, String motivo) {
        this.paciente = paciente;
        this.medico = medico;
        this.motivo = motivo;
        this.finalizada = false;
    }

    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }
    public String getMotivo() { return motivo; }
    public boolean isFinalizada() { return finalizada; }
    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }

    @Override
    public String toString() {
        return "Cita médica (Motivo: " + motivo + ", Finalizada: " + finalizada + ")";
    }
}