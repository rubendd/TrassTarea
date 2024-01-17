package com.rdd.trasstarea.listcontroller;

import com.rdd.trasstarea.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

public class ListController {

    // Lista de tareas
    private final List<Task> listTask = new ArrayList<>();

    // Constructor de la clase ListController, inicializa la lista de tareas
    public ListController() {

    }



    // Método getter para obtener la lista completa de tareas
    public List<Task> getListTask() {
        return listTask;
    }

    // Método estático para convertir un objeto Calendar a formato de texto
    public static String calendarToText(Calendar calendar) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy");
        return format1.format(calendar.getTime());
    }

    // Método privado para inicializar la lista de tareas con algunas tareas de ejemplo

    /*
    private void setListTareas() {
        // Agregar tareas a la lista
        listTask.add(new Task("Cumpleaños de Adrian", true, "03/12/23", Task.States.STARTED, "Hola Adrian", "03/12/23"));
        listTask.add(new Task("Hacer tareas", false, "28/7/23", Task.States.FINALIZED, "Hola Pepe", "03/12/23"));
        listTask.add(new Task("Ir al gimnasio", false,"07/10/23", Task.States.ADVANCED, "Hola Pepe", "03/12/23"));
        listTask.add(new Task("NAVIDAD", true, "31/12/23", Task.States.NOTSTARTED, "Hola Pepe", "03/12/23"));
    }*/

    // Método estático para convertir una cadena de fecha a un objeto Calendar
    public static Calendar convertirFecha(String date) {
        DateFormat formateador = new SimpleDateFormat("dd/MM/yy");
        try {
            Date fecha = formateador.parse(date);
            Calendar calendario = new GregorianCalendar();
            assert fecha != null;
            calendario.setTime(fecha);
            return calendario;
        } catch (ParseException e) {
            // En caso de error de análisis, imprimir la traza de la pila y devolver la fecha actual
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

    public static List<Task> orderByAlfabetico(List<Task> listTask){
        Collections.sort(listTask, (o1, o2) -> o1.getTitulo().compareToIgnoreCase(o2.getTitulo()));
        return listTask;
    }

    public static List<Task> orderByDate(List<Task> listTask){
        Collections.sort(listTask, Comparator.comparing(Task::getFechaInicio));
        return listTask;
    }

    public static List<Task> orderByDaysLeft(List<Task> listTask){
        Collections.sort(listTask, Comparator.comparingLong(Task::getDaysLeft));
        return listTask;
    }
    public static List<Task> orderByProgress(List<Task> listTask){
        Collections.sort(listTask, Comparator.comparingLong(Task::getDaysLeft));
        return listTask;
    }

    public static List<Task> orderByAsc(List<Task> listTask, boolean asc) {
        if (!asc) {
            Collections.sort(listTask, Comparator.comparingLong(Task::getProgresState));
        }
        return listTask;
    }
}
