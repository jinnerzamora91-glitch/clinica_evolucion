/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaclinico;

import java.io.Serializable;

public class EvolucionMedica implements Serializable {
    private CitaMedica cita;
    private String diagnostico;
    private String observaciones;
    private Medicamento medicamento; // Composición

    public EvolucionMedica(CitaMedica cita, String diagnostico, String observaciones, String nombreMed, String dosis) {
        this.cita = cita;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.medicamento = new Medicamento(nombreMed, dosis); // Composición
    }

    @Override
    public String toString() {
        return "\nEvolución Médica:" +
                "\nMotivo: " + cita.getMotivo() +
                "\nDiagnóstico: " + diagnostico +
                "\nObservaciones: " + observaciones +
                "\nMedicamento: " + medicamento;
    }
}