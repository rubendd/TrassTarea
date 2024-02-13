package com.rdd.trasstarea.fondopersonalizado.formas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cuadrado extends Forma{
    private float sideLength;

    public Cuadrado() {
        this.sideLength = 100; // Longitud del lado del cuadrado
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);

        // Dibujar el cuadrado con la rotación aplicada
        canvas.save();
        canvas.rotate(getRotation(), getPosX() + sideLength / 2, getPosY() + sideLength / 2);
        canvas.drawRect(getPosX(), getPosY(), getPosX() + sideLength, getPosY() + sideLength, paint);
        canvas.restore();
        rotate();
    }
}
