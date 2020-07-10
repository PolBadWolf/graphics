package ru.yandex.fixcolor.my_lib.graphics;

import javafx.scene.canvas.Canvas;
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
        plot.clearPlot();
        plot.clearFields();
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
