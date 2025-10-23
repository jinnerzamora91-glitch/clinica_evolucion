Clases Principales

Persona (abstracta) → Superclase de Medico y Paciente.  
Medico → Contiene especialidad, teléfono e ID.  
Paciente → Posee dirección, teléfono e historia clínica.  
HistoriaClinica → Contiene los antecedentes y evoluciones médicas.  
EvolucionMedica → Registra diagnóstico, tratamiento, observaciones y medicamentos.  
Medicamento → Representa el nombre, dosis y frecuencia del tratamiento.  
CitaMedica → Gestiona fecha, hora, motivo, médico y paciente.  
SistemaClinico → Clase principal que contiene los menús y listas de entidades.  
Registrable (Interfaz) → Define métodos para guardar y cargar datos.

Requerimientos Funcionales

1. Registro de pacientes y médicos: 
   - El sistema debe permitir ingresar los datos de nuevos pacientes y médicos por consola.
   - Los datos deben incluir nombre, edad, documento y otros atributos definidos.

2. Gestión de historias clínicas: 
   - Cada paciente debe tener una o varias historias clínicas.
   - Cada historia debe contener una lista de consultas médicas asociadas.

3. Registro de consultas: 
   - El médico podrá registrar consultas con información sobre fecha, motivo, diagnóstico y tratamiento.

4. Búsqueda de pacientes:
   - El sistema permitirá buscar pacientes por su número de documento.

5. Visualización de historial clínico:  
   - El usuario podrá consultar todas las historias y consultas asociadas a un paciente.

6. Persistencia de datos:  
   - La información de pacientes, médicos e hist
