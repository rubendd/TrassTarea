package com.rdd.trasstarea.fondopersonalizado.formas;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Forma {

    private float posX;
    private float posY;
    private final float speed;
    private float direction;
    private float rotationSpeed;
    private float rotation;

    Forma() {
        this.speed = 5; // Velocidad de movimiento
        this.rotationSpeed = 10.0f; // Ajusta la velocidad de rotación según sea necesario
        this.rotation = 45.0f; // Ajusta el ángulo inicial según sea necesario
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void reversarDireccionX() {
        direction = (180 - direction) % 360;
    }

    public void reverseDirectionY() {
        direction = (-direction) % 360;
    }

    public void move() {
        float radians = (float) Math.toRadians(direction);
        posX += speed * Math.cos(radians);
        posY += speed * Math.sin(radians);
    }


    public void rotate() {
        // Actualizar la rotación basada en la velocidad de rotación
        setRotation(getRotation() + rotationSpeed);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public abstract void draw(Canvas canvas, Paint paint);
}

