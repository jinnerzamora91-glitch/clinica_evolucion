/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.time.format.DateTimeFormatter;

public class EvolucionMedica {
    private CitaMedica cita;
    private String diagnostico;
    private Medicamento medicamento;

    public EvolucionMedica(CitaMedica cita, String diagnostico, Medicamento medicamento) {
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.medicamento = medicamento;
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Fecha: " + cita.getFecha().format(f) +
                "\nMédico: " + cita.getMedico() +
                "\nMotivo: " + cita.getMotivo() +
                "\nDiagnóstico: " + diagnostico +
                "\nMedicamento: " + medicamento.getNombre();
    }
}