Proyecto Clinica

El Sistema Clínico es una aplicación de consola desarrollada en Java que permite gestionar la información básica de una clínica.
Entre sus funciones principales se encuentran el registro de pacientes, la creación y finalización de citas médicas, y la visualización de historiales clínicos, con almacenamiento de datos mediante serialización.

El sistema implementa conceptos fundamentales de Programación Orientada a Objetos (POO) como:

Herencia (clase abstracta Persona)
Interfaz (Registrable)
Composición (EvolucionMedica → Medicamento)
Agregación (HistoriaClinica → EvolucionMedica)
Polimorfismo y encapsulamiento

Funcionalidades Principales

Registrar paciente
Permite ingresar un nuevo paciente con su nombre, cédula y edad.
Si el paciente ya existe, el sistema pregunta si desea usar el mismo historial clínico.

Crear cita médica

Muestra los pacientes registrados.
Solicita el motivo de la cita y recomienda médicos según la especialidad relacionada.
Asigna un médico disponible y agenda la cita.

Finalizar cita médica

Permite registrar diagnóstico, observaciones y medicamento recetado.
Marca la cita como finalizada y agrega la evolución médica al historial del paciente

Ver historial clínico

Muestra todas las evoluciones médicas registradas de un paciente seleccionado.
Guardar y cargar datos
Los datos de pacientes y citas se guardan automáticamente al salir.

| Clase                  | Descripción                                                                                          

| Registrable (Interfaz) | Define el método registrar() para clases que pueden registrarse (como pacientes y médicos).        
| Persona (Abstracta)    | Clase base para Medico y Paciente. Contiene datos comunes.                                       
| Medico                 | Hereda de Persona e implementa Registrable. Contiene especialidad y estado (disponible/ocupado). 
| Paciente               | Hereda de Persona e implementa Registrable. Contiene un objeto HistoriaClinica.                
| Medicamento            | Representa un medicamento con nombre y dosis.                                                        
| CitaMedica             | Relaciona un Paciente y un Medico con un motivo de consulta.                                     
| EvolucionMedica        | Representa la evolución de una cita con diagnóstico, observaciones y medicamento (composición).      
| HistoriaClinica        | Contiene una lista de evoluciones médicas (agregación).                                              
| Sistema                | Contiene el menú principal, manejo de archivos y lógica de negocio.                                  

Requerimientos funcionales

01: El sistema debe permitir registrar nuevos pacientes.

02: El sistema debe permitir registrar y administrar médicos.

03: El sistema debe guardar los datos en archivos .dat.

04: El sistema debe permitir consultar información por nombre o ID.

05: El sistema debe generar un reporte PDF con la información clínica.

06: El sistema debe permitir eliminar o modificar registros.

07: El sistema debe mostrar mensajes claros al usuario sobre las operaciones realizadas.

