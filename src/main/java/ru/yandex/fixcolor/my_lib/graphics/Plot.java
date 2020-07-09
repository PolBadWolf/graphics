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
    private Color colorWindowNet = Color.DARKGREEN;
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
            gc.setStroke(colorWindowNet);
            gc.setLineWidth(1);
            double poluWidth = gc.getLineWidth() / 2;
            int ySize = height - fieldHeight;
            int xSize = width - fieldWidth;
            int yN = 10, xN = 12;
            int y, x;
            for (int i = 1; i < (yN - 1); i++) {
                y = (i * ySize / (yN - 1)) + fieldHeight;
                moveTo(fieldWidth + poluWidth, y);
                lineTo(width - poluWidth, y);
            }
            for (int i = 1; i < (xN - 1); i++) {
                x = (i * xSize / (xN - 1)) + fieldWidth;
                moveTo(x, fieldHeight + poluWidth);
                lineTo(x, fieldHeight + ySize - poluWidth);
            }
            gc.stroke();
        }
    }
    private void moveTo(double x, double y) {
        gc.moveTo(x, height - y);
    }
    private void lineTo(double x, double y) {
        gc.lineTo(x, height - y);
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
