package src;

import grafica.GPointsArray;
import processing.core.PApplet;
import processing.core.PGraphics;

import static processing.core.PApplet.*;

class Util {
  final static float ppi = PI/5f; // Part pi
  static Boolean fpsEnabled = true;
  static boolean paused = false;

  public static GPointsArray graphPointsArray;

  static void displayDebug(PApplet p, PGraphics mg, float tm, float s) {
    if (Util.fpsEnabled) {
      mg.push();
      mg.colorMode(HSB, 100);
      mg.fill(0);
      mg.rect(0, 0, mg.width, 24);
      mg.fill(100);
      mg.textSize(24);
      mg.textAlign(LEFT, TOP);
      mg.text("FPS: " + nf(p.frameRate, 1, 2), 0, 0);
      mg.textAlign(CENTER, TOP);
      mg.text("TimeStep Multiplier: " + nf(tm, 1, 2), mg.width / 2f, 0);
      mg.textAlign(RIGHT, TOP);
      mg.text("Steps per frame: " + nf(s, 1, 0), mg.width, 0);
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
}
