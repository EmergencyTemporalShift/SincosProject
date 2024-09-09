package src;

import grafica.GPlot;
import processing.core.PApplet;

class GraphWindow extends PApplet {
    //JFrame frame;
    GPlot plot;

    public GraphWindow() {
        super();
        PApplet.runSketch(new String[]{this.getClass().getName()}, this);
    }

    public void settings() {
        size(450, 300, P2D);
        smooth();
    }
    public void setup() {
        windowTitle("Graph");
        plot = new GPlot(this);
    }

    public void draw() {
        background(0);
        // Draw graph
        plot.setPos(0,0);
        plot.setPoints(Util.graphPointsArray);
        plot.defaultDraw();
    }

    public void mousePressed() {

    }

    public void mouseDragged() {

    }
}
