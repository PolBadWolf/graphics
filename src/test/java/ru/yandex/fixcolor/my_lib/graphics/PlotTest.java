package ru.yandex.fixcolor.my_lib.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PlotTest  {
    Thread nit = null;
    Canvas canvas = null;
    Plot plot = null;
    public static PlotTest plotTest = null;

    AtomicInteger thrN = null;

    public static void main(String[] args) {
        plotTest = new PlotTest();
        plotTest.clearPlot();
    }

    public static PlotTest getPlotTest() {
        return plotTest;
    }

    @Override
    protected void finalize() throws Throwable {
        plot.finalize();
        super.finalize();
    }

    @Test
    public void clearPlot() {
        if (plotTest == null) plotTest = this;
        // ===========
        final BlockingQueue<ArrayList<Short[]>> paintQueue = new ArrayBlockingQueue<>(10);
        Short[] sh = null;
        Short[] sh0 = null;
        Short[] sh1 = null;
        ArrayList<Short[]> massVec = null;
        //
        massVec = new ArrayList<>();
        sh = new Short[] {0, 1, 2, 3};  massVec.add(sh);
        sh = new Short[] {2, 3, 4, 5};  massVec.add(sh);
        paintQueue.add(massVec);
        massVec = new ArrayList<>();
        sh = new Short[] {4, 5, 6, 7};  massVec.add(sh);
        sh = new Short[] {9, 8, 6, 5};  massVec.add(sh);
        paintQueue.add(massVec);
        double[][] grOsY = new double[3][2];
        grOsY[0] = new double[2];
        grOsY[1] = new double[3];
        grOsY[2] = new double[5];

        double[] p = grOsY[1];
        int l = grOsY.length;

        //
        try {
            massVec = paintQueue.poll(1, TimeUnit.SECONDS);
            sh0 = massVec.get(0); sh1 = massVec.get(1);
            massVec = paintQueue.poll(1, TimeUnit.SECONDS);
            sh0 = massVec.get(0); sh1 = massVec.get(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sh0 = massVec.get(0);
        sh1 = massVec.get(1);


        // ===========
        nit = new Thread( ()->{
            FxClass.main(new String[0]);
        });
        nit.start();
        while (FxClass.fxClass == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while ((canvas = FxClass.fxClass.getCanvas()) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //
        plot = new Plot(canvas, 100, 50);
        plot.clearFields();
        plot.clearWindow();
        //
        plot.addTrend(Color.YELLOW, 2);
        plot.addTrend(Color.WHITE, 2);
        plot.newDataX((short) 0);       plot.newDataTrend(0, (short)0);     plot.newDataTrend(1, (short)500);   plot.newDataPush();
        plot.newDataX((short) 100);     plot.newDataTrend(0, (short)400);   plot.newDataTrend(1, (short)100);   plot.newDataPush();
        plot.newDataX((short) 500);     plot.newDataTrend(0, (short)500);   plot.newDataTrend(1, (short)50);    plot.newDataPush();
        plot.newDataX((short) 900);     plot.newDataTrend(0, (short)300);   plot.newDataTrend(1, (short)550);   plot.newDataPush();
        plot.newDataX((short) 1000);    plot.newDataTrend(0, (short)150);   plot.newDataTrend(1, (short)0);     plot.newDataPush();
        while (nit.isAlive()) {
            for (int kX = 0; kX < 5; kX++) {
                plot.setZoomX(0, kX * 500 + 1000);
                plot.rePaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //
            for (int kX = 4; kX >= 0; kX--) {
                plot.setZoomX(0, kX * 500 + 1000);
                plot.rePaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
