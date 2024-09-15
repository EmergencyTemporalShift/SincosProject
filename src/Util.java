package src;

import grafica.GPoint;
import grafica.GPointsArray;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static processing.core.PApplet.*;

@SuppressWarnings("preview")
public class Util {
  final static float PPI = PI / 5f; // Part pi
  public static int s;
  static Boolean debugEnabled = true;
  static boolean paused = false;

  //public static CopyOnWriteArrayList<GPoint> graphPointsCOW;
  public static GPointsArray graphPointsArray;
  static PGraphics debugBar;
  static final int T_SIZE = 24;
  public static String mode = "SLOW";
  public static Boolean doModeSwitchAction = true;
  public static Boolean doDebugClear = true;

  static void initDebug(PApplet ma) {

    debugBar = ma.createGraphics(ma.width, T_SIZE * 3, JAVA2D);
    debugBar.beginDraw();
    debugBar.textSize(T_SIZE);
    debugBar.endDraw();
  }

  static void displayDebug(PApplet ma, @SuppressWarnings("unused") float tm, @SuppressWarnings("unused") float s) {
    if (Util.debugEnabled) {
      debugBar.beginDraw();
      debugBar.push();
      {
        debugBar.textSize(T_SIZE);
        debugBar.colorMode(HSB, 100);
        debugBar.fill(0);
        debugBar.rect(0, 0, debugBar.width, T_SIZE);
        debugBar.rect(0, debugBar.textSize, debugBar.width, T_SIZE);

        debugBar.fill(100);

        debugBar.textAlign(LEFT, TOP);
        debugBar.text(STR."FPS: \{nf(ma.frameRate, 1, 2)}", 0, 0);
        debugBar.text(STR."Mode: \{mode}", 0, debugBar.textSize);

        debugBar.textAlign(CENTER, TOP);
        debugBar.text(STR."TimeStep Multiplier: \{nf(tm, 1, 2)}", debugBar.width / 2f, 0);

        debugBar.textAlign(RIGHT, TOP);
        debugBar.text(STR."Steps per frame: \{nf(s, 1, 0)}", debugBar.width, 0);
    }
      debugBar.pop();
      debugBar.endDraw();
      ma.image(debugBar,0,0);
    }
      else {
        if(doDebugClear) {
          debugBar.rect(0, 0, debugBar.width, T_SIZE * 3);
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
    doModeSwitchAction = true;
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
    doModeSwitchAction = true;
  }

  /**
   * Applies a fade effect to the graphics by reducing the alpha value of all pixels.
   *
   * @param g           the PGraphics to apply the fade effect on
   * @param fadeAmount  the amount to reduce the alpha per frame
   */
  public static void fadeGraphics(PApplet ma, PGraphics g, int fadeAmount) {
    if (fadeAmount > 0 || fadeAmount < 0 && ma.frameCount % -fadeAmount == 0) {

      g.loadPixels();
      // Iterate over all pixels
      for (int i = 0; i < g.pixels.length; i++) {
        // Get the current alpha value
        int alpha = (g.pixels[i] >> 24) & 0xFF;
        // Reduce the alpha value
        alpha = max(0, alpha - ((fadeAmount > 0) ? fadeAmount : 1));

        // Update the pixel with the new alpha value
        g.pixels[i] = alpha << 24 | (g.pixels[i]) & 0xFFFFFF;
      }
      g.updatePixels();
    }
  }

  /**
   * Generates an ISO-8601 formatted timestamp down to the second.
   *
   * @return A string representation of the current timestamp in ISO-8601 format suitable for filenames.
   */
  @SuppressWarnings("unused")
  public static String getIsoTimestamp() {
    ZonedDateTime now = ZonedDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
    return formatter.format(now);
  }

  /**
   * Saves the PGraphics buffer to disk as an image file in the project's 'graphic_out' directory.
   *
   * @param ma        the PApplet object for context
   * @param buffer    the PGraphics buffer to save
   * @param filename  the filename to save the image as
   */
  public static void saveScreenshot(PApplet ma, PGraphics buffer, @SuppressWarnings("unused") String filename) {
    String projectPath = ma.sketchPath();
    String outputDir = STR."\{projectPath}/graphic_out";

    // Create the 'graphic_out' directory if it doesn't exist
    File dir = new File(outputDir);
    if (!dir.exists()) {
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
    }

    String fullPath = STR."\{outputDir}/\{filename}";
    buffer.save(fullPath);
  }

}
