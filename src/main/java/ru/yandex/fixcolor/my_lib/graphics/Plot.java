package ru.yandex.fixcolor.my_lib.graphics;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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
    // начало времени отображения
    private double time0 = 0.0;
    private int timeIndexBegin = 0;
    // длительность отображении времени
    private double timeLenght = 1000.0;
    private int timeIndexEnd = 0;

    // массив графиков
    private Vector<Trend>   trends = null;
    private Vector<Short>   dataX = null;
    private Short[]         newData = null;
    private Vector<Short[]> dataGraphics = null;
    private short           dataXtmp = 0;
    private double[]        trX = null;
    private Object dataSynh = null;
    private Object windSynh = null;
    private Object lockPaint = null;
    private MyPaint myPaint = null;

    public Plot(Canvas canvas, int fieldWidth, int fieldHeight) {
        this.canvas = canvas;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        myPaint = new MyPaint();
        myPaint.start();
        width = (int)canvas.getWidth();
        height = (int)canvas.getHeight();
        gc = canvas.getGraphicsContext2D();
        trends = new Vector<>();
        dataX = new Vector<>();
        //
        newData = new Short[1];
        dataGraphics = new Vector<>();
        //
        dataSynh = new Object();
        windSynh = new Object();
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
    //
    public void setZoomX(double time0, double timeLenght) {
        this.time0 = time0;
        this.timeLenght = timeLenght;
    }
    // ---------------
    private void _clearFields() {
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
        gc.stroke();;
    }
    private void _clearWindow() {
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
        gc.closePath();
        gc.stroke();
    }
    private void _paintNet() {
        gc.beginPath();
        // рисование сетки
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
        gc.closePath();
        gc.stroke();
    }
    public void clearFields() {
        myPaint.clearFields();
    }
    public void clearWindow() {
        myPaint.clearWindow();
    }
    public void paintNet() {
        myPaint.paintNet();
    }
    // ---------------
    public void removeAllTrends() {
        trends.clear();
    }
    public void addTrend(Color lineColor, double lineWidth) {
        trends.add(new Trend(lineColor, lineWidth));
        newData = new Short[trends.size() + 1];
    }
    public void clearAllTrends() {
        synchronized (dataSynh) {
            for (int i = 0; i < trends.size(); i++) {
                trends.get(i).clearData();
            }
            dataX.clear();
        }
    }
    public void newDataX(short dataX) {
        /*synchronized (dataSynh) {
            dataXtmp = dataX;
        }*/
        newData[0] = dataX;
    }
    public void newDataTrend(int n, short data) {
        /*synchronized (dataSynh) {
            trends.get(n).newData(data);
        }*/
        newData[n + 1] = data;
    }
    public void newDataPush() {
        /*synchronized (dataSynh) {
            dataX.add(dataXtmp);
            for (int i = 0; i < trends.size(); i++) {
                trends.get(i).newDataPush();
            }
        }*/
        dataGraphics.add(newData);
        newData = new Short[trends.size() + 1];
    }
    public void rePaint() {
        /*(new Thread( ()-> {
            synchronized (dataSynh) {
                Vector<Double> tmp = new Vector<>();
                // начало
                for (int i = 0; i < dataX.size(); i++) {
                    if (dataX.get(i).doubleValue() >= time0) {
                        timeIndexBegin = i;
                        break;
                    }
                }
                // конец
                timeIndexEnd = dataX.size();
                for (int i = timeIndexBegin; i < dataX.size(); i++) {
                    if (dataX.get(i).doubleValue() >= (time0 + timeLenght)) {
                        timeIndexEnd = i + 1;
                        break;
                    }
                }
                //
                double k = timeLenght / (width - fieldWidth);
                trX = new double[timeIndexEnd - timeIndexBegin];
                for (int i = 0; i < trX.length; i++) {
                    trX[i] = ((dataX.get(i + timeIndexBegin).doubleValue() - time0) / k) + fieldWidth;
                }
            }
            //
            synchronized (windSynh) {
                Platform.runLater(() -> {
                    _clearFields();
                    _clearWindow();
                    _paintNet();
                    for (int i = 0; i < trends.size(); i++) {
                        trends.get(i).rePaint();
                    }
                });
            }
        })).start();*/
        myPaint.rePaint(dataGraphics);
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
        private Color lineColor = null;
        private double lineWidth = 0.0;
        Vector<Short> data = null;
        short dataTmp = 0;

        public Trend(Color lineColor, double lineWidth) {
            this.lineColor = lineColor;
            this.lineWidth = lineWidth;
            data = new Vector<>();
        }

        public void clearData() {
            data.clear();
        }

        public void newData(short data) {
            dataTmp = data;
        }

        public void newDataPush() {
            data.add(dataTmp);
        }

        public void rePaint() {
            double[] trY = new double[trX.length];
            for (int i = 0; i < trX.length; i++) {
                trY[i] = height - (data.get(i + timeIndexBegin).doubleValue() / 1.0);
            }
            gc.beginPath();
            gc.setStroke(lineColor);
            gc.setLineWidth(lineWidth);
            gc.strokePolyline(trX, trY, trX.length);
            gc.closePath();
            gc.stroke();
        }
        public void rePaint2(double[] x, double[] y) {
            gc.beginPath();
            gc.setStroke(lineColor);
            gc.setLineWidth(lineWidth);
            gc.strokePolyline(x, y, x.length);
            gc.closePath();
            gc.stroke();
        }
    }
    // ---------------
    private class MyPaint extends Thread {
        private final BlockingQueue<DatQueue> paintQueue = new ArrayBlockingQueue<>(10);
        private Object lock = new Object();
        //
        DatQueue queueClearFields = new DatQueue(ClearFields, null);
        DatQueue queueClearWindow = new DatQueue(ClearWindow, null);
        DatQueue queuePaintNet    = new DatQueue(PaintNet,    null);
        //
        public static final int ClearFields = 0;
        public static final int ClearWindow = 1;
        public static final int PaintNet    = 2;
        public static final int RePaint     = 3;

        @Override
        public void run() {
            DatQueue datQueue = null;
            while (!isInterrupted()) {
                try {
                    datQueue = paintQueue.poll(10, TimeUnit.MILLISECONDS);
                    if (datQueue == null)   continue;
                    switch (datQueue.command) {
                        case ClearFields:
                            Platform.runLater(()->_clearFields());
                            break;
                        case ClearWindow:
                            Platform.runLater(()->_clearWindow());
                            break;
                        case PaintNet:
                            Platform.runLater(()->_paintNet());
                            break;
                        case RePaint:
                            _rePaint(datQueue.datGraph);
                            break;
                        default:
                            System.out.println("unknouw commands");
                            continue;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        // ---
        private void _rePaint(Vector<Short[]> datGraph) {
            // нахождение индексов
            int indexBegin = 0;
            int indexEnd = datGraph.size();
            // начальный индекс
            for (int i = 0; i < datGraph.size(); i++) {
                if (datGraph.get(i)[0] >= time0) {
                    indexBegin = i;
                    break;
                }
            }
            // конечный индекс
            for (int i = indexBegin; i < datGraph.size(); i++) {
                try {
                    double x = datGraph.get(i)[0];
                    if (datGraph.get(i)[0] >= (time0 + timeLenght)) {
                        indexEnd = i + i;
                        break;
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // создание массива
            int lenghtMass = datGraph.get(0).length;
            double[][] massGraphcs = new double[lenghtMass][indexEnd - indexBegin];
            // зум и оффсет
            double kX = timeLenght / (width - fieldWidth);
            double kY = 1;
            for (int i = 0; i < massGraphcs[0].length; i++) {
                Short[] tmpShort = datGraph.get(i + indexBegin);
                // ось X
                massGraphcs[0][i] = ((tmpShort[0].doubleValue() - time0) / kX) + fieldWidth;
                // ось Y
                for (int y = 1; y < lenghtMass; y++) {
                    massGraphcs[y][i] = ((tmpShort[y].doubleValue() - 0.0) / kY) + 0.0;
                }
            }
            // обрисовка
            Platform.runLater(()->{
                _clearFields();
                _clearWindow();
                _paintNet();
                for (int i = 1; i < lenghtMass; i++) {
                    trends.get(i - 1).rePaint2(massGraphcs[0], massGraphcs[i]);
                }
            });
        }
        // ---
        public void clearFields() {
            synchronized (lock) {
                paintQueue.add(queueClearFields);
            }
        }
        public void clearWindow() {
            synchronized (lock) {
                paintQueue.add(queueClearWindow);
            }
        }
        public void paintNet() {
            synchronized (lock) {
                paintQueue.add(queuePaintNet);
            }
        }
        public void rePaint(Vector<Short[]> dat) {
            synchronized (lock) {
                paintQueue.add(new DatQueue(RePaint, dat));
            }
        }
        // ---
    }
    // ---------------
    private class DatQueue {
        int command;
        Vector<Short[]> datGraph;

        public DatQueue(int command, Vector<Short[]> datGraph) {
            this.command = command;
            this.datGraph = datGraph;
        }
    }
    // ---------------
}
