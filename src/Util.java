package src;

import grafica.GPoint;
import grafica.GPointsArray;
import processing.core.PApplet;
import processing.core.PGraphics;

import static processing.core.PApplet.*;

class Util {
  final static float ppi = PI/5f; // Part pi
  public static int s;
  static Boolean debugEnabled = true;
  static boolean paused = false;

  //public static CopyOnWriteArrayList<GPoint> graphPointsCOW;
  public static GPointsArray graphPointsArray;
    static PGraphics debugBar;
  static int tSize = 24;
  public static String mode = "SLOW";
  public static Boolean doModeSwitch = true;
  public static Boolean doDebugClear = true;

  static void initDebug(PApplet ma) {

      debugBar = ma.createGraphics(ma.width, tSize *3, JAVA2D);
    debugBar.beginDraw();
    debugBar.textSize(tSize);
    debugBar.endDraw();
  }

  static void displayDebug(PApplet ma, float tm, float s) {
    if (Util.debugEnabled) {
      debugBar.beginDraw();
      debugBar.push();
      {
        debugBar.textSize(24);
        debugBar.colorMode(HSB, 100);
        debugBar.fill(0);
        debugBar.rect(0, 0, debugBar.width, tSize);
        debugBar.rect(0, debugBar.textSize, debugBar.width, tSize);

        debugBar.fill(100);

        debugBar.textAlign(LEFT, TOP);
        debugBar.text("FPS: " + nf(ma.frameRate, 1, 2), 0, 0);
        debugBar.text("Mode: " + mode, 0, debugBar.textSize);

        debugBar.textAlign(CENTER, TOP);
        debugBar.text("TimeStep Multiplier: " + nf(tm, 1, 2), debugBar.width / 2f, 0);

        debugBar.textAlign(RIGHT, TOP);
        debugBar.text("Steps per frame: " + nf(s, 1, 0), debugBar.width, 0);
    }
      debugBar.pop();
      debugBar.endDraw();
      ma.image(debugBar,0,0);
    }
      else { // TODO: Blank bar only once
        if(doDebugClear) {
          debugBar.rect(0, 0, debugBar.width, tSize*3);
        }
      }
      doDebugClear = false;
  }

  static void togglePause() {
    Util.paused = !Util.paused;
  }

  static void toggleDebug() {
    Util.debugEnabled = !Util.debugEnabled;
  }

  @SuppressWarnings("unused")
  static void setDebug(Boolean show) {
    Util.debugEnabled = show;
  }

  static void setMode(String lMode) { // Local mode
    if(lMode.equals("SLOW")) {
      mode = "SLOW";
      s = 1;
    } else {
      mode = "FAST";
      s = 3000;
    }
    doModeSwitch = true;
  }

  static void toggleMode() {
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
    doModeSwitch = true;
  }
}
