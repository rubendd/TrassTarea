package com.rdd.trasstarea.fondopersonalizado.formas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Circulo extends Forma{
    private float radius;

    public Circulo() {
        this.radius = 50; // Radio del círculo
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.RED);

        // Dibujar el círculo con la rotación aplicada
        canvas.save();
        canvas.rotate(getRotation(), getPosX() + radius, getPosY() + radius);
        canvas.drawCircle(getPosX() + radius, getPosY() + radius, radius, paint);
        canvas.restore();
        rotate();
    }
}
