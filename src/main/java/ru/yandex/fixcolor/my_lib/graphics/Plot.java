package ru.yandex.fixcolor.my_lib.graphics;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Vector;

public class Plot {
    private Canvas  canvas = null;
    private GraphicsContext gc = null;
    // размер холста
    private int width = 0;
    private int height = 0;
    //  *******************************
    //              поля
    // ширина
    private int fieldWidth = 0;
    private int fieldHeight = 0;
    // цвет фона
    private Color fieldBackColor = Color.GRAY;
    // цвет рамки
    private Color fieldFrameLineColor = Color.LIGHTGREEN;
    // ширина рамки
    private double fieldFrameLineWidth = 3.0;
    //  *******************************
    //               окно
    // цвет фона
    private Color windowBackColor = Color.BLACK;
    //  *******************************
    //               сетка
    // цвет сетки
    private Color netLineColor = Color.DARKGREEN;
    // ширина линий сетки
    private double netLineWidth = 1.0;
    // количество столбцов
    private int netN_Width = 14;
    // количество строк
    private int netN_Height = 10;

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
    public void setFieldBackColor(Color color) { fieldBackColor = color; }
    public Color getFieldBackColor() { return fieldBackColor; }
    // ---------------
    public void setNetLineColor(Color color) { netLineColor = color; }
    public void setNetLineWidth(double lineWidth) { netLineWidth = lineWidth; }
    public void setNetN_Width(int N_Width) { netN_Width = N_Width; }
    public void setNetN_Height(int N_Height) { netN_Height = N_Height; }
    //
    public Color getNetLineColor() { return netLineColor; }
    public double getNetLineWidth() { return netLineWidth; }
    public int getNetN_Width() { return netN_Width; }
    public int getNetN_Height() { return netN_Height; }
    // ---------------
    public void setWindowBackColor(Color color) { windowBackColor = color; }
    public Color getWindowBackColor() { return windowBackColor; }
    // ---------------
    public void setFieldWidth(int width) { fieldWidth = width; }
    public void setFieldHeight(int height) { fieldHeight = height; }
    public void setFieldFrameLineColor(Color color) { fieldFrameLineColor = color; }
    public void setFieldFrameLineWidth(double lineWidth) { fieldFrameLineWidth = lineWidth; }
    //
    public int getFieldWidth() { return fieldWidth; }
    public int getFieldHeight() { return fieldHeight; }
    public Color getFieldFrameLineColor() { return fieldFrameLineColor; }
    public double getFieldFrameLineWidth() { return fieldFrameLineWidth; }
    // ---------------
    public void clearFields() {
        Platform.runLater(() -> {
            gc.beginPath();
            // вертикальное поле
            {
                int xB = 0;
                int xE = fieldWidth;
                int yB = 0;
                int yE = height;
                gc.setFill(fieldBackColor);
                gc.fillRect(xB, yB, xE, yE);
            }
            // горизонтальное поле
            {
                int xB = 0;
                int xE = width;
                int yB = height - fieldHeight;
                int yE = height;
                gc.setFill(fieldBackColor);
                gc.fillRect(xB, yB, xE, yE);
            }
            // линия рамки
            {
                double[] x = new double[] {
                        fieldWidth - (fieldFrameLineWidth / 2),
                        fieldWidth - (fieldFrameLineWidth / 2),
                        width - (fieldFrameLineWidth / 2)
                };
                double[] y = new double[] {
                        0,
                        height - fieldHeight + (fieldFrameLineWidth / 2),
                        height - fieldHeight + (fieldFrameLineWidth / 2)
                };
                gc.setStroke(fieldFrameLineColor);
                gc.setLineWidth(fieldFrameLineWidth);
                gc.strokePolyline(x, y, x.length);
            }
            gc.closePath();
            gc.stroke();
            //((Parent)canvas.getParent()).ch
        });
    }
    public void clearPlot() {
        Platform.runLater(() -> {
            gc.beginPath();
            // очистка окна
            {
                int xB = fieldWidth;
                int xE = width - xB;
                int yB = 0;
                int yE = height - fieldHeight;
                gc.setFill(windowBackColor);
                gc.fillRect(xB, yB, xE, yE);
            }
            // рисование сетки
            {
                gc.setStroke(netLineColor);
                gc.setLineWidth(netLineWidth);
                double poluWidth = netLineWidth / 2;
                int ySize = height - fieldHeight;
                int xSize = width - fieldWidth;
                int xN = netN_Width + 1;
                int yN = netN_Height + 1;
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
            }
            gc.closePath();
            gc.stroke();
        });
    }
    // ---------------
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
