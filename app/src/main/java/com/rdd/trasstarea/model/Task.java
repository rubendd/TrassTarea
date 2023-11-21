package com.rdd.trasstarea.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Task implements Serializable {
    private String titulo, description;
    private boolean prioritaria;
    private long daysLeft;
    private Calendar dateEnd;
    private Calendar fechaInicio;

    private  int progresState;



    public String getDescription() {
        return description;
    }


    public enum States{
        NOTSTARTED, STARTED, ADVANCED, ALMOSFINALIZED, FINALIZED
    }


    public Task(String titulo, boolean prioritaria, Calendar dateEnd, States state, String description, Calendar fechaInicio) {
        this.titulo = titulo;
        this.prioritaria = prioritaria;
        this.dateEnd = dateEnd;
        this.progresState = setStatesNumber(state);
        this.daysLeft = getDaysLeft();
        this.description = description;
        this.fechaInicio = fechaInicio;
    }


    public Task() {
    }

    public int getProgresState() {
        return progresState;
    }

    public int setStatesNumber(States states) {
        if (states == States.NOTSTARTED){
            return 0;
        }
        if (states == States.STARTED){
            return 25;
        }
        if (states == States.ADVANCED){
            return 50;
        }
        if (states == States.ALMOSFINALIZED){
            return 75;
        }
        if (states == States.FINALIZED){
            return 100;
        }
        return 0;
    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }


    public long getDaysLeft() {
            return fechasDiferencia();
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public Calendar getDateEnd() {
        return dateEnd;
    }

    public String calendar() {
        try {
            return calendarToText();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDateEnd(Calendar dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return prioritaria == task.prioritaria && daysLeft == task.daysLeft && Objects.equals(titulo, task.titulo) && Objects.equals(dateEnd, task.dateEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, prioritaria, daysLeft, dateEnd);
    }


    private long fechasDiferencia(){
        // Obtain the current date
        Calendar fechaActual = Calendar.getInstance();

        // Set the time fields to 0 to compare only the dates
        fechaActual.set(Calendar.HOUR_OF_DAY, 0);
        fechaActual.set(Calendar.MINUTE, 0);
        fechaActual.set(Calendar.SECOND, 0);
        fechaActual.set(Calendar.MILLISECOND, 0);

        // Obtain the due date
        Calendar fechaFutura = getDateEnd();

        // Set the time fields to 0 to compare only the dates
        fechaFutura.set(Calendar.HOUR_OF_DAY, 0);
        fechaFutura.set(Calendar.MINUTE, 0);
        fechaFutura.set(Calendar.SECOND, 0);
        fechaFutura.set(Calendar.MILLISECOND, 0);

        // Calculate the difference in days
        long diferenciaEnMillis = fechaFutura.getTimeInMillis() - fechaActual.getTimeInMillis();
        return diferenciaEnMillis / (24 * 60 * 60 * 1000);
    }

    private String calendarToText() throws ParseException {
        Calendar cal = getFechaInicio();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        return format1.format(cal.getTime());
    }



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
