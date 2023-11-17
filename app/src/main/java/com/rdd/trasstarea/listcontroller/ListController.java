package com.rdd.trasstarea.listcontroller;

import com.rdd.trasstarea.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListController {

    private final List<Task> listTask = new ArrayList<>();

    public ListController() {
        setListTareas();
    }

    public List<Task> filtarLista(){
        return listTask.stream().filter(Task::isPrioritaria).collect(Collectors.toList());
    }


    public List<Task> getListTask() {
        return listTask;
    }


    public static String calendarToText(Calendar calendar) {
        Calendar cal = calendar;
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy");

        return format1.format(cal.getTime());
    }

    private void setListTareas() {
        // Agregar tareas a la lista
        listTask.add(new Task("Cumple adrian", true, convertirFecha("03/12/23"), Task.States.STARTED, "Hola adrian"));
        listTask.add(new Task("Hacer tareas", false, convertirFecha("28/7/23"), Task.States.FINALIZED, "Hola pepe"));
        listTask.add(new Task("Ir al gimnasio", false, convertirFecha("07/10/23"), Task.States.ADVANCED, "Hola pepe"));
        listTask.add(new Task("NAVIDAD", true, convertirFecha("31/12/23"), Task.States.NOTSTARTED, "Hola pepe"));
        // Agrega más tareas según sea necesario
    }


    public static Calendar convertirFecha(String date){
        DateFormat formateador = new SimpleDateFormat("dd/MM/yy");
        try {
            Date fecha = formateador.parse(date);
            Calendar calendario = new GregorianCalendar();
            assert fecha != null;
            calendario.setTime(fecha);
            return calendario;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

}
