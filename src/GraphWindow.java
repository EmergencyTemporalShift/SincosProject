package src;

import grafica.GPlot;
import processing.core.PApplet;

@SuppressWarnings("unused")
class GraphWindow extends PApplet {

    GPlot plot;

    public GraphWindow() {
        super();
        PApplet.runSketch(new String[]{this.getClass().getName()}, this);
    }
/**
 * Initialize window size and renderer settings.
 */
 public void settings() {
        size(450, 300, P2D);
        smooth();
    }
    public void setup() {
        windowTitle("Graph");
        plot = new GPlot(this);
        plot.setBgColor(0x80);
        plot.setBoxBgColor(0xA0);
        frameRate(10);
    }

    public void draw() {
        background(0);
        // Draw graph
        plot.setPos(0,0);
        plot.setPoints(Util.graphPointsArray);
        plot.defaultDraw();
    }

    public void mousePressed() {

    } //beginning

    public void mouseDragged() {

    }
}
