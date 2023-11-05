package com.rdd.trasstarea.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String titulo;
    private boolean prioritaria;
    private int daysLeft;
    private LocalDate dateEnd;
    private int state;

    public Task(String titulo, boolean prioritaria, LocalDate dateEnd, int state) {
        this.titulo = titulo;
        this.prioritaria = prioritaria;
        this.dateEnd = dateEnd;
        this.state = state;
        this.daysLeft = getDaysLeft();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
