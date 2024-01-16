package com.rdd.trasstarea.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.rdd.trasstarea.listcontroller.ListController;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Entity
public class Task implements Serializable {
    // Definición de la clase Task
    @PrimaryKey(autoGenerate = true)// Campo estático para rastrear el próximo ID disponible
    @NonNull
    private int id = 0;
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "description")
    private String description; // Título y descripción de la tarea
    @ColumnInfo(name = "prioritaria")
    private boolean prioritaria; // Estado de prioridad de la tarea
    @ColumnInfo(name = "daysLeft")
    private long daysLeft; // Número de días restantes para completar la tarea


    private String dateEnd; // Fecha límite para la tarea

    private String fechaInicio; // Fecha de inicio de la tarea

    @ColumnInfo(name = "progresState")
    private int progresState; // Estado de progreso de la tarea
    @ColumnInfo(name = "URL_doc")
    private String URL_doc;

    @ColumnInfo(name = "URL_img")
    private String URL_img;

    @ColumnInfo(name = "URL_aud")
    private String URL_aud;

    @ColumnInfo(name = "URL_vid")
    private String URL_vid;
    @Ignore
    private static int nextId = 0;

    public String getURL_doc() {
        return URL_doc;
    }

    public String getURL_img() {
        return URL_img;
    }

    public String getURL_aud() {
        return URL_aud;
    }

    public String getURL_vid() {
        return URL_vid;
    }

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }

    public void setURL_doc(String URL_doc) {
        this.URL_doc = URL_doc;
    }

    public void setURL_img(String URL_img) {
        this.URL_img = URL_img;
    }

    public void setURL_aud(String URL_aud) {
        this.URL_aud = URL_aud;
    }

    public void setURL_vid(String URL_vid) {
        this.URL_vid = URL_vid;
    }

    // Método getter para la descripción
    public String getDescription() {
        return description;
    }

    // Enumeración para los diferentes estados en los que puede estar una tarea
    public enum States {
        NOTSTARTED, STARTED, ADVANCED, ALMOSFINALIZED, FINALIZED
    }

    // Constructor para la clase Task
    public Task(String titulo, boolean prioritaria, String dateEnd, States state, String description, String fechaInicio) {
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

    public Task(int id, String titulo, String description,
                boolean prioritaria, long daysLeft,
                String dateEnd, String fechaInicio,
                int progresState, String URL_doc,
                String URL_img, String URL_aud, String URL_vid) {

        this.id = nextId;
        nextId++;
        this.titulo = titulo;
        this.description = description;
        this.prioritaria = prioritaria;
        this.daysLeft = daysLeft;
        this.dateEnd = dateEnd;
        this.fechaInicio = fechaInicio;
        this.progresState = progresState;
        this.URL_doc = URL_doc;
        this.URL_img = URL_img;
        this.URL_aud = URL_aud;
        this.URL_vid = URL_vid;
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

    public void setFechaInicio(String fechaInicio) {
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
    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    // Método para convertir el calendario a formato de texto


    // Método setter para la fecha límite
    public void setDateEnd(String dateEnd) {
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
        Calendar fechaFutura = ListController.convertirFecha(getDateEnd());

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
