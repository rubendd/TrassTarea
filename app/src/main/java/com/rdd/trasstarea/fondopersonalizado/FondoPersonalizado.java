package com.rdd.trasstarea.fondopersonalizado;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.rdd.trasstarea.fondopersonalizado.formas.Circulo;
import com.rdd.trasstarea.fondopersonalizado.formas.Cuadrado;
import com.rdd.trasstarea.fondopersonalizado.formas.Estrella;
import com.rdd.trasstarea.fondopersonalizado.formas.Forma;
import com.rdd.trasstarea.fondopersonalizado.formas.Triangulo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FondoPersonalizado extends View {

    private final int numeroDeObjetosPorForma = 3;

    private List<Forma> formas;
    private Paint paint;

    public FondoPersonalizado(Context context) {
        super(context);
        init();
    }

    public FondoPersonalizado(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        formas = new ArrayList<>();
        paint = new Paint();
        paint.setAntiAlias(true);

        // Crear 3 círculos, 3 cuadrados, 3 triángulos y 3 estrellas
        for (int i = 0; i < numeroDeObjetosPorForma; i++) {
            formas.add(new Circulo());
            formas.add(new Cuadrado());
            formas.add(new Triangulo());
            formas.add(new Estrella());
        }

        // Posicionar las formas aleatoriamente en la pantalla
        Random random = new Random();
        for (Forma shape : formas) {
            int width = getWidth();
            int height = getHeight();
            if (width > 0 && height > 0) {
                shape.setPosX(random.nextInt(width));
                shape.setPosY(random.nextInt(height));
                shape.setDirection(random.nextInt(360));
            }
        }

        // Iniciar el bucle de animación
        empezarAnimacion();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    public void empezarAnimacion() {
        post(new Runnable() {
            @Override
            public void run() {
                moveShapes();
                invalidate();
                postDelayed(this, 16); // Actualizar cada 16 ms (aproximadamente 60 FPS)
            }
        });
    }

    private void moveShapes() {
        for (Forma shape : formas) {
            shape.mover();
            manejarMargenes(shape);
        }
    }

    private void manejarMargenes(Forma forma) {
        int margin = 5; // Márgenes para evitar que las formas se queden atascadas en los bordes

        if (forma.getPosX() < 0 || forma.getPosX() > getWidth() - margin) {
            forma.reversarDireccionX();
        }

        if (forma.getPosY() < 0 || forma.getPosY() > getHeight() - margin) {
            forma.reverseDirectionY();
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (Forma forma : formas) {
            forma.draw(canvas, paint);
        }
    }
}
