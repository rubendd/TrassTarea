package com.rdd.trasstarea.fondopersonalizado.formas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Triangulo extends Forma{
    private Path path;

    public Triangulo() {
        this.path = new Path();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.GREEN);
        path.reset();
        path.moveTo(getPosX(), getPosY());
        path.lineTo(getPosX() + 100, getPosY());
        path.lineTo(getPosX() + 50, getPosY() - 100);
        path.close();

        // Dibujar el triángulo con la rotación aplicada
        canvas.save();
        canvas.rotate(getRotation(), getPosX(), getPosY());
        canvas.drawPath(path, paint);
        canvas.restore();
        rotate();
    }
}
