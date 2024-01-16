package com.rdd.trasstarea.activities.estadisticas;

import com.rdd.trasstarea.model.Task;

import java.util.List;

public class Estadisticas {
    public static String mostrarEstadisticas(List<Task> listTareas){
        int numTareas = listTareas.size();
        int progresoMedio = (int) calcularProgresoMedio(listTareas);
        int diasRestantesMedios = (int) calcularDiasRestantesMedios(listTareas);

        // Formatear la cadena con los resultados
        String format = String.format("Número de tareas: %d\n" +
                "Progreso medio: %d\n" +
                "Días restantes de media: %d", numTareas, progresoMedio, diasRestantesMedios);

        return format;
    }

    private static long calcularProgresoMedio(List<Task> listTareas) {
        long numeroTotal = listTareas.stream().mapToLong(Task::getProgresState).sum();
        return numeroTotal / listTareas.size();

    }

    private static long calcularDiasRestantesMedios(List<Task> listTareas) {
        long numeroTotal = listTareas.stream().mapToLong(Task::getDaysLeft).sum();
        return numeroTotal / listTareas.size();
    }
}
