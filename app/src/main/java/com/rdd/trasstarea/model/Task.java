package com.rdd.trasstarea.model;

import static java.time.temporal.ChronoUnit.DAYS;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Task implements Parcelable {
    private String titulo, description;
    private boolean prioritaria;
    private long daysLeft;
    private Calendar dateEnd;

    private  int progresState;

    protected Task(Parcel in) {
        titulo = in.readString();
        description = in.readString();
        prioritaria = in.readByte() != 0;
        daysLeft = in.readLong();
        progresState = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(description);
        dest.writeByte((byte) (prioritaria ? 1 : 0));
        dest.writeLong(daysLeft);
        dest.writeInt(progresState);
    }

    public enum States{
        NOTSTARTED, STARTED, ADVANCED, ALMOSFINALIZED, FINALIZED
    }


    public Task(String titulo, boolean prioritaria, Calendar dateEnd, States state, String description) {
        this.titulo = titulo;
        this.prioritaria = prioritaria;
        this.dateEnd = dateEnd;
        this.progresState = setStatesNumber(state);
        this.daysLeft = getDaysLeft();
        this.description = description;
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

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
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
        // Obtener la fecha actual
        Calendar fechaActual = Calendar.getInstance();

        // Definir una fecha futura
        Calendar fechaFutura = Calendar.getInstance();
        fechaFutura.set(getDateEnd().get(Calendar.YEAR), getDateEnd().get(Calendar.MONTH), getDateEnd().get(Calendar.DAY_OF_YEAR));  // Por ejemplo, 31 de diciembre de 2023

        // Calcular la diferencia en milisegundos
        long diferenciaEnMillis = fechaFutura.getTimeInMillis() - fechaActual.getTimeInMillis();

        // Calcular la diferencia en días
        return diferenciaEnMillis / (24 * 60 * 60 * 1000);
    }

    private String calendarToText() throws ParseException {
        Calendar cal = getDateEnd();
        cal.add(Calendar.DATE, 1);
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
