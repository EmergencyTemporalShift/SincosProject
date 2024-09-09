package src;

import com.krab.lazy.LazyGui;
import grafica.GPoint;
import grafica.GPointsArray;
import processing.core.PApplet;
import processing.core.PGraphics;

import static src.Util.graphPointsArray;
import static src.Util.s;
import static src.Util.mode;


public class Sincos extends PApplet {
    //PApplet graphWin;
    final static float ppi = Util.ppi;
    float it;  // Inner t
    int itStep; // Likely different from frame count
    float ot; // Outer t
    float tm; // Both t multiplier
    int lastTime;
    int delta;
    int desiredFrameRate;

    CircularBuffer pointsBuffer;

    LazyGui gui;
    PGraphics mg; // Main pattern graphics

    public void settings() {
        size(800, 800, P2D);
}

    public void setup() {
        smooth();
        gui = new LazyGui(this);
        mg = createGraphics(width, height);
        mg.beginDraw();
        {
            mg.colorMode(HSB, 360, 100, 100, 100);
            background(0);
            mg.strokeWeight(0.0625f);
        }
        mg.endDraw();
        it = 0;
        itStep = 0;
        ot = 0;
        tm = 1;
        Util.changeMode(mode);

        //lastTime = 0;
        //delta = 0;
        pointsBuffer = new CircularBuffer(3000);
        graphPointsArray = new GPointsArray(3000);
        for(int i = 0; i<3000; i++) {
            graphPointsArray.add(i,0);
        }
        desiredFrameRate = 30;
        frameRate(desiredFrameRate);

        // Create graph window
        //graphWin = new GraphWindow();
    }


    public void keyPressed() {
        // I know that I could short circuit these, but I like it better this way.
        if (key == CODED) {
            if (keyCode == UP) {
                s += 1;
            }
            if (keyCode == DOWN) {
                s -= 1;
                s = max(1, s);
            }
            //print(s);

            if (keyCode == LEFT) {
                tm *= 1 / 1.1f;
            }
            if (keyCode == RIGHT) {
                tm *= 1.1f;
            }
            //println(tm);
        }

        switch (key) {
            case 'f':
                Util.enableFPS();
                break;
            case 'p':
                Util.togglePause();
                break;
            case 'm':
                mg.background(0);
                Util.changeMode();
                break;
            //print(key);
        }
    }

    public void draw() {
        delta = millis() - lastTime;
        float otInc = gui.slider("otInc", 0.007f, 0f, .01f);
        @SuppressWarnings("unused")
        float a = gui.slider("a", 0, -.05f, .05f);

        it = 0;

        // Draw main shape
        mg.beginDraw();
        {
            if (!Util.paused) {
                mg.push();
                {
                    if (mode.equals("FAST")) {
                        mg.background(0);
                        mg.fill(0,0,0,100);
                        mg.rect(0, 0, width, height);
                    } else {
                        it = 0;
                        // Fading code TODO: FIX BROKEN
//                        if (frameCount % 2 == 0) {
//                            mg.pushStyle();
//                            {
//                                mg.blendMode(SUBTRACT);
//                                mg.noStroke();
//                                mg.fill(0, 0, 0, 1);
//                                mg.rect(0, 0, width, height);
//                            }
//                            mg.popStyle();
//                        }
                    }
                    mg.translate(width / 2f, height / 2f);
                    // The beef of this program, creates part of or the whole spiral depending on Util.mode
                    for (int i = 0; i < s; i++) {
                        float theta = it;
                        float r = Function.function(theta, ot);
                        //if()
                        //pointsBuffer.add(r);

                        graphPointsArray.set(itStep % s, new GPoint(itStep % s, r));

                        float x = 10 * r * cos(TAU * theta);
                        float y = 10 * r * sin(TAU * theta);

                        // Transform to fit window
                        float scale = 18;
                        // TODO: Auto scale
                        x = scale * x;
                        y = scale * y;


                        // Point at the tip
                        mg.pushStyle();
                        {
                            mg.stroke(0, 0, 100);
                            mg.strokeWeight(2);
                            mg.point(x, y);
                        }
                        mg.popStyle();
                        // Line color

                        //stroke(noise(10*t)*255);
                        //stroke(120,(it*10000)%1f*100,100);
                        mg.stroke(30 * sin(it * ppi), (it * 10000) % 1f * 100, 100);
                        float ic = 200; // Inner circle speed
                        /*line(width/2+40*cos(ic*it*ppi),
                        height/2+40*sin(ic*it*ppi),
                        width/2+x, height/2+y);*/

                        // Circle lines
                        mg.line(40 * cos(ic * it * ppi),
                                40 * sin(ic * it * ppi),
                                x, y);
//                        //Transform Attempt
//                        mg.pushMatrix();
//                          //translate(sin(ppi*it),0);
//                          mg.rotate(ic*it*ppi);
//                          mg.line(40,0,60,0);
//                          //PVector circleCoords = getTransformedPoint(40,0);
//                          //println(screenX(40,0));
//                        mg.popMatrix();
//                         //Flipped line
//                        mg.push();
//                            mg.rotate(PI/3);
//                            mg.scale(-1,1);
//                            mg.line(40*-cos(ic*it*ppi),
//                                 40* sin(ic*it*ppi),
//                                x, y);
//                                  mg.stroke(0,0,100);
//                            mg.strokeWeight(1);
//                            mg.point(x,y);
//                        mg.pop();

                        it += 0.001f * tm;
                        itStep += 1;
                    }
                    ot += otInc;
                }
                mg.pop();
            }
            Util.displayDebug(this, mg, tm, s);
        }
        mg.endDraw();

        image(mg, 0, 0);


        lastTime = millis();
    }



    public static void main(String... args){
        PApplet.main("src.Sincos");
    }
}
