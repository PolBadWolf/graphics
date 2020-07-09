package ru.yandex.fixcolor.my_lib.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Vector;

public class Plot {
    private Canvas  canvas = null;
    private GraphicsContext gc = null;
    // ширина полей
    private int fieldWidth = 0;
    private int fieldHeight = 0;
    // размер холста
    private int width = 0;
    private int height = 0;
    // цвет: полей, окно для графика
    private Color colorField = Color.BLACK;
    private Color colorWindow = Color.BLACK;
    private Color colorWindowNet = Color.GREEN;
    // массив графиков
    private Vector<Trend>   trends = null;

    public Plot(Canvas canvas, int fieldWidth, int fieldHeight) {
        this.canvas = canvas;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        width = (int)canvas.getWidth();
        height = (int)canvas.getHeight();
        gc = canvas.getGraphicsContext2D();
        trends = new Vector<>();
    }
    // ---------------
    // ---------------
    public void clearPlot() {
        // очистка окна
        {
            int xB = fieldWidth;
            int xE = width - xB;
            int yB = 0;
            int yE = height - fieldHeight;
            gc.setFill(colorWindow);
            gc.fillRect(xB, yB, xE, yE);
        }
        // рисование сетки
        {

        }
    }
    // ---------------
    private class Trend {
        private Color colorTrend = null;

        public Trend(Color colorTrend) {
            this.colorTrend = colorTrend;
        }
    }
    // ---------------
}
