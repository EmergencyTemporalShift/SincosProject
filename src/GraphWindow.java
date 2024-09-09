package src;

import grafica.GPlot;
import processing.core.PApplet;

class GraphWindow extends PApplet {
    //JFrame frame;

    public GraphWindow() {
        super();
        PApplet.runSketch(new String[]{this.getClass().getName()}, this);
    }

    public void settings() {
        size(450, 300, P3D);
        smooth();
    }
    public void setup() {
        windowTitle("Graph");

    }

    public void draw() {
        background(0);
        // Draw graph
        GPlot plot = new GPlot(this);
        plot.setPos(0,0);
        //plot.setPoints(Util.graphPointsArray);
        plot.defaultDraw();
    }

    public void mousePressed() {

    }

    public void mouseDragged() {

    }
}