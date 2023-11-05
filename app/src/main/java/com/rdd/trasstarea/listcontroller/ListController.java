package com.rdd.trasstarea.listcontroller;

import com.rdd.trasstarea.model.Task;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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
        listTask.add(new Task("Cumple adrian", true, LocalDate.of(2023, Month.DECEMBER, 3), 25));
        listTask.add(new Task("Hacer tareas", false, LocalDate.of(2023, Month.AUGUST, 3), 55));
        listTask.add(new Task("Ir al gimnasio", false, LocalDate.of(2023, Month.JULY, 15), 12));
        listTask.add(new Task("Leer un libro", true, LocalDate.of(2023, Month.OCTOBER, 10), 8));
        // Agrega más tareas según sea necesario
    }
}
