package com.rdd.trasstarea.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rdd.trasstarea.model.Task;

/**
 * ViewModel
 */
public class ComunicateFragments extends ViewModel {

    private final MutableLiveData<Task> taskLiveData = new MutableLiveData<>();


    /**
     * AUDIO
     */
    private final MutableLiveData<Uri> mAudio = new MutableLiveData<>();

    public LiveData<Uri> getAudio() {
        return mAudio;
    }
    public void setAudio(Uri audio){
        mAudio.setValue(audio);
    }


    public MutableLiveData<Uri> getmAudio() {
        return mAudio;
    }


    /**
     * FOTO
     */
    private final MutableLiveData<Uri> mFoto = new MutableLiveData<>();

    public LiveData<Uri> getFoto() {
        return mFoto;
    }
    public void setFoto(Uri foto){
        mFoto.setValue(foto);
    }


    /**
     * VIDEO
     */

    private final MutableLiveData<Uri> mVideo = new MutableLiveData<>();


    public LiveData<Uri> getVideo() {
        return mVideo;
    }
    public void setVideo(Uri video){
        mVideo.setValue(video);
    }

    /**
     * DOCUMENTO
     */

    private final MutableLiveData<Uri> mDocumento = new MutableLiveData<>();


    public LiveData<Uri> getDocumento() {
        return mDocumento;
    }

    public void setDocumento(Uri documento){
        mDocumento.setValue(documento);
    }




    /***********************/

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
    private final MutableLiveData<Integer> id = new MutableLiveData<>();


    private final MutableLiveData<String> description = new MutableLiveData<>();
    private final MutableLiveData<String> url_doc = new MutableLiveData<>();
    private final MutableLiveData<String> url_img = new MutableLiveData<>();
    private final MutableLiveData<String> url_video = new MutableLiveData<>();
    private final MutableLiveData<String> url_audio = new MutableLiveData<>();

    public MutableLiveData<String> getUrl_doc() {
        return url_doc;
    }

    public MutableLiveData<String> getUrl_img() {
        return url_img;
    }

    public MutableLiveData<String> getUrl_video() {
        return url_video;
    }

    public MutableLiveData<String> getUrl_audio() {
        return url_audio;
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<Integer> getId() {
        return id;
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
    public void setId(int nomb) {
        id.setValue(nomb);
    }
    public void setUrl_doc(String nomb) {
        url_doc.setValue(nomb);
    }
    public void setUrl_audio(String nomb) {
        url_audio.setValue(nomb);
    }
    public void setUrl_img(String nomb) {
        url_img.setValue(nomb);
    }
    public void setUrl_video(String nomb) {
        url_video.setValue(nomb);
    }


}
