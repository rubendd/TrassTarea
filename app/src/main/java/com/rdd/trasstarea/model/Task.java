package com.rdd.trasstarea.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String titulo, description;
    private boolean prioritaria;
    private int daysLeft;
    private LocalDate dateEnd;

    private int progresState;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum States{
        NOTSTARTED, STARTED, ADVANCED, ALMOSFINALIZED, FINALIZED
    };


    public Task(String titulo, boolean prioritaria, LocalDate dateEnd, States state, String description) {
        this.titulo = titulo;
        this.prioritaria = prioritaria;
        this.dateEnd = dateEnd;
        this.progresState = setStatesNumber(state);
        this.daysLeft = getDaysLeft();
        this.description = description;
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

    public int getDaysLeft() {
        return (int) DAYS.between(LocalDate.now(),getDateEnd());
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
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
}
