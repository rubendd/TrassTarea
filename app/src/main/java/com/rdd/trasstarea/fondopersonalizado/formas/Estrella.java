package com.rdd.trasstarea.fondopersonalizado.formas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Estrella extends Forma{
    private Path path;
    private int numeroPuntas = 5;

    public Estrella() {
        this.path = new Path();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.CYAN);
        path.reset();

        float radius = 50; // Radio de la circunferencia circunscrita a la estrella

        for (int i = 0; i < numeroPuntas; i++) {
            double angle = Math.toRadians(i * 144); // 360 grados divididos entre 5 puntas
            float x = (float) (getPosX() + radius * Math.cos(angle));
            float y = (float) (getPosY() + radius * Math.sin(angle));

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        path.close();

        // Dibujar la estrella con la rotación aplicada
        canvas.save();
        canvas.rotate(getRotation(), getPosX(), getPosY());
        canvas.drawPath(path, paint);
        canvas.restore();
        rotate();
    }
}

