package ru.yandex.fixcolor.my_lib.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.junit.Test;

public class PlotTest  {
    Thread nit = null;
    Canvas canvas = null;
    Plot plot = null;

    @Test
    public void clearPlot() {
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
        plot.newDataX((short) 1000);    plot.newDataTrend(0, (short)150);   plot.newDataTrend(1, (short)200);   plot.newDataPush();
        plot.setZoomX(-200, 1000);
        plot.rePaint();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        plot.setZoomX(-200, 3000);
        plot.rePaint();
        //
        while (nit.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
