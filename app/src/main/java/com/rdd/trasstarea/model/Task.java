package com.rdd.trasstarea.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Task implements Serializable {
    // Definición de la clase Task
    private String titulo, description; // Título y descripción de la tarea
    private boolean prioritaria; // Estado de prioridad de la tarea
    private long daysLeft; // Número de días restantes para completar la tarea
    private Calendar dateEnd; // Fecha límite para la tarea
    private Calendar fechaInicio; // Fecha de inicio de la tarea
    private int progresState; // Estado de progreso de la tarea
    private static int nextId = 0;  // Campo estático para rastrear el próximo ID disponible
    private int id = 0;

    // Método getter para la descripción
    public String getDescription() {
        return description;
    }

    // Enumeración para los diferentes estados en los que puede estar una tarea
    public enum States {
        NOTSTARTED, STARTED, ADVANCED, ALMOSFINALIZED, FINALIZED
    }

    // Constructor para la clase Task
    public Task(String titulo, boolean prioritaria, Calendar dateEnd, States state, String description, Calendar fechaInicio) {
        this.id = nextId;
        nextId++;
        this.titulo = titulo;
        this.prioritaria = prioritaria;
        this.dateEnd = dateEnd;
        this.progresState = setStatesNumber(state); // Establecer el estado de progreso según el estado proporcionado
        this.daysLeft = getDaysLeft(); // Calcular el número de días restantes para la tarea
        this.description = description;
        this.fechaInicio = fechaInicio;
    }

    // Constructor por defecto para Task
    public Task() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    // Método getter para el estado de progreso
    public int getProgresState() {
        return progresState;
    }

    public void setProgresState(int progresState) {
        this.progresState = progresState;
    }

    // Método para establecer valores numéricos para diferentes estados
    public int setStatesNumber(States states) {
        // Asociar cada estado a un valor numérico
        if (states == States.NOTSTARTED) {
            return 0;
        }
        if (states == States.STARTED) {
            return 25;
        }
        if (states == States.ADVANCED) {
            return 50;
        }
        if (states == States.ALMOSFINALIZED) {
            return 75;
        }
        if (states == States.FINALIZED) {
            return 100;
        }
        return 0;
    }

    public static int setSpinnerEnum(int progresState){
        if (progresState == 0){
            return 0;
        }
        if (progresState == 25){
            return 1;
        }
        if (progresState == 50){
            return 2;
        }
        if (progresState == 75){
            return 3;
        }
        if (progresState == 100){
            return 4;
        }
        return 0;
    }

    // Métodos getter y setter para el título
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Métodos getter y setter para el estado de prioridad
    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    // Método getter para el número de días restantes para la tarea
    public long getDaysLeft() {
        return fechasDiferencia();
    }

    // Métodos getter para la fecha de inicio y la fecha límite
    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public Calendar getDateEnd() {
        return dateEnd;
    }

    // Método para convertir el calendario a formato de texto
    public String calendar() {
        try {
            return calendarToText();
        } catch (ParseException e) {
            // Lanzar una excepción de tiempo de ejecución si hay una excepción de análisis
            throw new RuntimeException(e);
        }
    }

    // Método setter para la fecha límite
    public void setDateEnd(Calendar dateEnd) {
        this.dateEnd = dateEnd;
    }

    // Método equals para verificar la igualdad basada en ciertos campos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return prioritaria == task.prioritaria && daysLeft == task.daysLeft && Objects.equals(titulo, task.titulo) && Objects.equals(dateEnd, task.dateEnd);
    }

    // Método hashCode para generar un código hash basado en ciertos campos
    @Override
    public int hashCode() {
        return Objects.hash(titulo, prioritaria, daysLeft, dateEnd);
    }

    // Método privado para calcular la diferencia en días entre la fecha actual y la fecha límite
    private long fechasDiferencia() {
        // Obtener la fecha actual
        Calendar fechaActual = Calendar.getInstance();

        // Establecer los campos de tiempo a 0 para comparar solo las fechas
        fechaActual.set(Calendar.HOUR_OF_DAY, 0);
        fechaActual.set(Calendar.MINUTE, 0);
        fechaActual.set(Calendar.SECOND, 0);
        fechaActual.set(Calendar.MILLISECOND, 0);

        // Obtener la fecha de vencimiento
        Calendar fechaFutura = getDateEnd();

        // Establecer los campos de tiempo a 0 para comparar solo las fechas
        fechaFutura.set(Calendar.HOUR_OF_DAY, 0);
        fechaFutura.set(Calendar.MINUTE, 0);
        fechaFutura.set(Calendar.SECOND, 0);
        fechaFutura.set(Calendar.MILLISECOND, 0);

        // Calcular la diferencia en días
        long diferenciaEnMillis = fechaFutura.getTimeInMillis() - fechaActual.getTimeInMillis();
        return diferenciaEnMillis / (24 * 60 * 60 * 1000);
    }

    // Método privado para convertir el calendario a formato de texto
    private String calendarToText() throws ParseException {
        Calendar cal = getFechaInicio();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        return format1.format(cal.getTime());
    }

    // Método toString para representar la tarea como una cadena
    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "titulo='" + titulo + '\'' +
                ", description='" + description + '\'' +
                ", prioritaria=" + prioritaria +
                ", daysLeft=" + daysLeft +
                ", dateEnd=" + dateEnd +
                ", progresState=" + progresState +
                '}';
    }

}
