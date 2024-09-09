package src;

import com.krab.lazy.LazyGui;
import grafica.GPointsArray;
import processing.core.PApplet;
import processing.core.PGraphics;


public class Sincos extends PApplet {
    @SuppressWarnings("unused")
    PApplet graphWin;
    final static float ppi = Util.ppi;
    float it;  // Inner t
    int itStep; // Likely different from frame count
    float ot; // Outer t
    int s; // Steps;
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
        gui = new LazyGui(this);
        mg = createGraphics(width, height);
        mg.beginDraw();
        {
            mg.colorMode(HSB, 360, 100, 100, 100);
            mg.strokeWeight(0.0625f);
        }
        mg.endDraw();
        it = 0;
        itStep = 0;
        ot = 0;
        tm = 1;
        s = 3000;
        lastTime = 0;
        delta = 0;
        pointsBuffer = new CircularBuffer(3000);
        Util.graphPointsArray = new GPointsArray(3000);
        for(int i = 0; i<3000; i++) {
            Util.graphPointsArray.add(i,0);
        }
        desiredFrameRate = 30;
        frameRate(desiredFrameRate);

        // Memory LEAK
        //graphWin = new GraphWindow();
    }


    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP) {
                s += 1;
            }
            if (keyCode == DOWN) {
                s -= 1;

                s = max(1, s);
                if (s == 0)
                    frameRate(1);
                else {
                    frameRate(desiredFrameRate);
                }
            }
            if (keyCode == LEFT) {
                tm *= 1 / 1.1f;
            }
            if (keyCode == RIGHT) {
                tm *= 1.1f;
            }
            //println(tm);
        }
        //print(s);
        if (key == 'f') {
            Util.enableFPS();
        }
        if (key == 'p') {
            Util.togglePause();
        }
        //print(key);
    }

    public void draw() {
        //delta = millis() - lastTime;
        it = 0;

        // Draw main shape
        mg.beginDraw();
        {
            float slider = gui.slider("Slider",0,-1,0);
            // Fading code BROKEN
//            if (frameCount % 2 == 0) {
//                mg.pushStyle();
//                {
//                    mg.blendMode(SUBTRACT);
//                    mg.noStroke();
//                    mg.fill(0, 0, 0, 1);
//                    mg.rect(0, 0, width, height);
//                }
//                mg.popStyle();
//            }

            if (!Util.paused) {
                mg.push();
                {
                    mg.background(0);
                    mg.translate(width / 2f, height / 2f);
                    //background(0);
                    //it = 0;
                    for (int i = 0; i < s; i++) {

                        //background(0);

                        float theta = it;
                        float r = Function.function(theta, slider);
                        //if()
                        pointsBuffer.add(r);

                        //graphPointsArray.set(itStep % s, new GPoint(itStep % s, r));

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
                        //Transform Attempt
                        //mg.pushMatrix();
                        //  //translate(sin(ppi*it),0);
                        //  mg.rotate(ic*it*ppi);
                        //  mg.line(40,0,60,0);
                        //  //PVector circleCoords = getTransformedPoint(40,0);
                        //  //println(screenX(40,0));
                        //mg.popMatrix();
                        // Flipped line
                        //mg.push();
                        //    mg.rotate(PI/3);
                        //    mg.scale(-1,1);
                        //    mg.line(40*-cos(ic*it*ppi),
                        //         40* sin(ic*it*ppi),
                        //        x, y);
                        //          mg.stroke(0,0,100);
                        //    mg.strokeWeight(1);
                        //    mg.point(x,y);
                        //mg.pop();

                        it += 0.001f * tm;
                        itStep +=1;
                    }
                    //src.Util.graphPointsArray.removeRange(0,src.Util.graphPointsArray.getNPoints());
                    //print(pointsBuffer.buffer);

                    // Memory LEAK:
//                    {
//                        int i = 0;
//                        for (PVector point : pointsBuffer.buffer) {
//                            if (nonNull(point)) {
//                                Util.graphPointsArray.get(i).setY(point.y);//add((int) point.x, point);
//                                i++;
//                            } else {
//                                break;
//                            }
//                        }
//                    }
                    ot += 0.3f;
                }
                mg.pop();
            }


            Util.displayDebug(this, mg, tm, s);
        }
        mg.endDraw();

        image(mg, 0, 0);


        //lastTime = millis();
    }

    public static void main(String... args){
        PApplet.main("src.Sincos");
    }
}