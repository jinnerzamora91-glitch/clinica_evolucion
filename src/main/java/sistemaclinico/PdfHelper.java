package sistemaclinico;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase auxiliar para evitar duplicar código de generación de PDF
 * (historial clínico y recibo de pago). Implementada con iText 5.
 */
public class PdfHelper {

    // Si quieres agregar logo desde un BufferedImage, podrías extender este helper.
    public void generarPDFHistorialClinico(Paciente paciente) {
        String fileName = "HistorialClinico_" + paciente.getNombre().replaceAll("\\s+","_") + ".pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, new BaseColor(43,103,119));
            Paragraph title = new Paragraph("Clínica Zamora\nHistorial Clínico", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            com.itextpdf.text.Font bold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
            Paragraph datos = new Paragraph();
            datos.add(new Paragraph("Paciente: " + paciente.getNombre(), bold));
            datos.add(new Paragraph("Cédula: " + paciente.getCedula(), bold));
            datos.add(new Paragraph("Edad: " + paciente.getEdad(), bold));
            document.add(datos);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{1.2f,1.6f,2f,2f,2.5f,2.5f});
            table.setWidthPercentage(100);

            addCellHeader(table, "Cita #");
            addCellHeader(table, "Fecha");
            addCellHeader(table, "Médico");
            addCellHeader(table, "Motivo");
            addCellHeader(table, "Diagnóstico");
            addCellHeader(table, "Medicamentos / Observaciones");

            for (EvolucionMedica e : paciente.getHistoriaClinica().getEvoluciones()) {
                CitaMedica c = e.getCita();
                table.addCell(String.valueOf(c.getId()));
                table.addCell(c.getFechaFormatted());
                table.addCell(c.getMedico().getNombre());
                table.addCell(c.getMotivo());
                table.addCell(e.getDiagnostico());
                StringBuilder medsObs = new StringBuilder();
                if (!e.getMedicamentos().isEmpty()) {
                    for (Medicamento m : e.getMedicamentos()) medsObs.append(m.getNombre()).append(" (").append(m.getDosis()).append(") $").append(m.getPrecio()).append("; ");
                }
                if (e.getObservaciones() != null && !e.getObservaciones().isEmpty()) medsObs.append("\nObs: ").append(e.getObservaciones());
                table.addCell(medsObs.toString());
            }

            document.add(table);

            document.add(new Paragraph("\nClínica Zamora — Confianza y bienestar"));
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            document.add(new Paragraph("Generado el: " + fecha));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            document.close();
        }
    }

    public void generarPDFHistorialPagos(Pago pago) {
        String fileName = "HistorialPago_Cita_" + pago.getCita().getId() + ".pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, new BaseColor(43,103,119));
            Paragraph title = new Paragraph("Clínica Zamora\nRecibo de Pago", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            CitaMedica c = pago.getCita();
            com.itextpdf.text.Font bold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
            Paragraph datos = new Paragraph();
            datos.add(new Paragraph("Cita #: " + c.getId(), bold));
            datos.add(new Paragraph("Paciente: " + c.getPaciente().getNombre(), bold));
            datos.add(new Paragraph("Cédula: " + c.getPaciente().getCedula(), bold));
            datos.add(new Paragraph("Médico: " + c.getMedico().getNombre(), bold));
            datos.add(new Paragraph("Fecha: " + c.getFechaFormatted(), bold));
            document.add(datos);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{4f,2f});
            table.setWidthPercentage(100);
            addCellHeader(table, "Concepto");
            addCellHeader(table, "Valor");

            table.addCell("Consulta");
            table.addCell("$" + c.getCostoConsulta());
            table.addCell("Habitación");
            table.addCell("$" + c.getCostoHabitacion());

            if (!c.getMedicamentos().isEmpty()) {
                for (Medicamento m : c.getMedicamentos()) {
                    table.addCell("Medicamento: " + m.getNombre() + " (" + m.getDosis() + ")");
                    table.addCell("$" + m.getPrecio());
                }
            }

            PdfPCell totalCell = new PdfPCell(new Paragraph("TOTAL", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD)));
            totalCell.setBackgroundColor(new BaseColor(220,245,240));
            table.addCell(totalCell);
            PdfPCell valCell = new PdfPCell(new Paragraph("$" + pago.getTotal(), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD)));
            table.addCell(valCell);

            document.add(table);

            document.add(new Paragraph("\nClínica Zamora — Confianza y bienestar"));
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            document.add(new Paragraph("Generado el: " + fecha));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            document.close();
        }
    }

    private void addCellHeader(PdfPTable table, String text) {
        PdfPCell h = new PdfPCell(new Paragraph(text, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD)));
        h.setBackgroundColor(new BaseColor(220,245,240));
        h.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(h);
    }
}
