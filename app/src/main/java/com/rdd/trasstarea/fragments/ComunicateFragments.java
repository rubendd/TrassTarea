package com.rdd.trasstarea.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rdd.trasstarea.model.Task;

public class ComunicateFragments extends ViewModel {

    private final MutableLiveData<Task> taskLiveData = new MutableLiveData<>();

    public LiveData<Task> getTaskLiveData() {
        return taskLiveData;
    }

    public void setTask(Task task) {
        taskLiveData.setValue(task);
    }
    private final MutableLiveData<String> titulo = new MutableLiveData<>();
    private final MutableLiveData<String> date1 = new MutableLiveData<>();
    private final MutableLiveData<String> date2 = new MutableLiveData<>();
    private final MutableLiveData<String> state = new MutableLiveData<>();
    private final MutableLiveData<Boolean> prioritario = new MutableLiveData<>();

    private final MutableLiveData<String> description = new MutableLiveData<>();

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<String> getTitulo() {
        return titulo;
    }

    public MutableLiveData<String> getDate1() {
        return date1;
    }

    public MutableLiveData<String> getDate2() {
        return date2;
    }

    public MutableLiveData<String> getState() {
        return state;
    }

    public MutableLiveData<Boolean> getPrioritario() {
        return prioritario;
    }

    public void setTitulo(String nomb) {
        titulo.setValue(nomb);
    }
    public void setDate2(String nomb) {
        date2.setValue(nomb);
    }
    public void setDate1(String nomb) {
        date1.setValue(nomb);
    }
    public void setState(String nomb) {
        state.setValue(nomb);
    }
    public void setPrioritario(boolean nomb) {
        prioritario.setValue(nomb);
    }
    public void setDescription(String nomb) {
        description.setValue(nomb);
    }
}