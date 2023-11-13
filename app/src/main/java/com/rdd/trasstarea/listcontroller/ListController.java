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
        Stream<Task> streamlista = listTask.stream().filter(Task::isPrioritaria);
        return streamlista.collect(Collectors.toList());
    }

    private void addTask(Task task){
        listTask.add(task);
    }

    public List<Task> getListTask() {
        return listTask;
    }

    private void setListTareas() {
        // Agregar tareas a la lista
        listTask.add(new Task("Cumple adrian", true, convertirFecha("02/21/12"), Task.States.STARTED, "Hola pepe"));
        listTask.add(new Task("Hacer tareas", false, convertirFecha("02/21/12"), Task.States.FINALIZED, "Hola pepe"));
        listTask.add(new Task("Ir al gimnasio", false, convertirFecha("02/21/12"), Task.States.ADVANCED, "Hola pepe"));
        listTask.add(new Task("Leer un libro", true, convertirFecha("02/21/12"), Task.States.NOTSTARTED, "Hola pepe"));
        // Agrega más tareas según sea necesario
    }


    public static Calendar convertirFecha(String date){
        //define el formato de la fecha
        DateFormat formateador= new SimpleDateFormat("dd/M/yy");
        try {
            // convierte un String en formato fecha en una fecha real
            Date fecha= formateador.parse("10/01/2022");
            // creamos un calendario
            Calendar calendario= new GregorianCalendar();
            //hacemos calculos sobre el calendario
            assert fecha != null;
            calendario.setTime(fecha);
            //movemos el ccalendario
            calendario.add(Calendar.DATE,5);
            return calendario;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }
}
