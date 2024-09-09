package src;

import grafica.GPoint;
import grafica.GPointsArray;
import processing.core.PApplet;
import processing.core.PGraphics;

import static processing.core.PApplet.*;

class Util {
  final static float ppi = PI/5f; // Part pi
  public static int s;
  static Boolean fpsEnabled = true;
  static boolean paused = false;

  public static GPointsArray graphPointsArray;
  public static String mode = "FAST";

  static void displayDebug(PApplet p, PGraphics mg, float tm, float s) {
    if (Util.fpsEnabled) {
      mg.push();
      {
        mg.textSize(24);
        mg.colorMode(HSB, 100);
        mg.fill(0);
        mg.rect(0, 0, mg.width, 24);
        mg.rect(0, mg.textSize, mg.width, 24);

        mg.fill(100);

        mg.textAlign(LEFT, TOP);
        mg.text("FPS: " + nf(p.frameRate, 1, 2), 0, 0);
        mg.text("Mode: " + mode, 0, mg.textSize);

        mg.textAlign(CENTER, TOP);
        mg.text("TimeStep Multiplier: " + nf(tm, 1, 2), mg.width / 2f, 0);

        mg.textAlign(RIGHT, TOP);
        mg.text("Steps per frame: " + nf(s, 1, 0), mg.width, 0);
    }
    mg.pop();
    }

  }

  static void togglePause() {
    Util.paused = !Util.paused;
  }

  static void enableFPS() {
    Util.fpsEnabled = !Util.fpsEnabled;
  }

  @SuppressWarnings("unused")
  static void enableFPS(Boolean show) {
    Util.fpsEnabled = show;
  }

  static void changeMode(String lMode) { // Local mode
    if(lMode.equals("SLOW")) {
      mode = "SLOW";
      s = 1;
    } else {
      mode = "FAST";
      s = 3000;
    }
  }

  static void changeMode() {

    if (mode.equals("SLOW")) {
      mode = "FAST";
      s = 3000;
    } else {
      mode = "SLOW";
      s = 1;
      for(int i = 0; i<3000; i++) {
        graphPointsArray.set(i,new GPoint(i,0));
      }
    }
  }
}
