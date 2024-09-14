package src;

import com.krab.lazy.LazyGui;
import grafica.GPoint;
import grafica.GPointsArray;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.HashMap;

import static src.Util.*;

// forum.processing.org/two/discussion/13189/a-better-way-to-fade
// Fader code by benja and not subject to license.

/**
 * Main class for creating a dynamic sine-cosine graph pattern.
 * It uses circular buffers and a LazyGui interface for user input controls.
 */
public class Sincos extends PApplet {
    final static float ppi = Util.ppi;  // Constant multiplier for calculations
    float it;  // Inner time variable (theta)
    int itStep; // Step counter, usually synced with frame count but manually controlled
    float ot; // Outer time variable
    float tm; // Time multiplier, affects the speed of drawing
    int lastTime; // Stores the last frame time in milliseconds
    int delta; // Time difference between frames
    int desiredFrameRate; // Desired frame rate for the sketch
    HashMap<String, Integer> keysPressed; // Stores pressed key states

    CircularBuffer pointsBuffer; // Buffer to store points for drawing
    LazyGui gui; // GUI for user interaction and sliders
    PGraphics mg; // Off-screen graphics object for main pattern rendering

    /**
     * Sets up the window size and renderer.
     */
    public void settings() {
        size(800, 800, P2D); // Use 800x800 window with P2D renderer
    }

    /**
     * Initializes the sketch with graphics, buffers, GUI, and initial conditions.
     */
    public void setup() {
        smooth(); // Enables anti-aliasing
        gui = new LazyGui(this); // Initialize LazyGui for user controls
        mg = createGraphics(width, height, JAVA2D); // Create an off-screen PGraphics for drawing
        mg.beginDraw();
        {
            mg.colorMode(HSB, 360, 100, 100, 100); // Use HSB color mode for hue control
            background(0); // Set background to black
            mg.strokeWeight(0.0625f); // Set very thin stroke weight
        }
        mg.endDraw();

        initDebug(this);

        // Initialize variables
        it = 0;
        itStep = 0;
        ot = 0;
        tm = 1; // Default time multiplier

        // Change the drawing mode (e.g., FAST, SLOW) using a utility method
        Util.toggleMode(mode);

        pointsBuffer = new CircularBuffer(3000); // Initialize the circular buffer with a size of 3000
        graphPointsArray = new GPointsArray(3000); // Array to hold points for the graph

        // Pre-fill the GPointsArray with default values
        for (int i = 0; i < 3000; i++) {
            graphPointsArray.add(i, 0);
        }

        // Set desired frame rate and apply it
        desiredFrameRate = 30;
        frameRate(desiredFrameRate);

        // Initialize key press tracking for arrow keys
        keysPressed = new HashMap<>();
        keysPressed.put("UP",    0);
        keysPressed.put("DOWN",  0);
        keysPressed.put("LEFT",  0);
        keysPressed.put("RIGHT", 0);

        // Start a separate thread for background updates
        UpdateLoop update = new UpdateLoop();
        update.start();
    }

    /**
     * Handles key presses, updates key states, and controls mode toggles.
     */
    public void keyPressed() {
        // Handle coded keys (e.g., arrow keys)
        if (key == CODED) {
            if (keyCode == UP) {
                keysPressed.put("UP", 1);
            }
            if (keyCode == DOWN) {
                keysPressed.put("DOWN", 1);
            }
            if (keyCode == LEFT) {
                keysPressed.put("LEFT", 1);
            }
            if (keyCode == RIGHT) {
                keysPressed.put("RIGHT", 1);
            }
        }

        // Handle other key presses for toggling specific actions or modes
        switch (key) {
            case 'f': // Enable FPS display
                Util.toggleFPS();
                break;
            case 'p': // Toggle pause
                Util.togglePause();
                break;
            case 'm': // Clear background and change drawing mode
                mg.background(0);
                Util.toggleMode();
                break;
        }
    }

    /**
     * Handles key releases, resets key states.
     */
    public void keyReleased() {
        // Reset key states when coded keys (e.g., arrow keys) are released
        if (key == CODED) {
            if (keyCode == UP) {
                keysPressed.put("UP", 0);
            }
            if (keyCode == DOWN) {
                keysPressed.put("DOWN", 0);
            }
            if (keyCode == LEFT) {
                keysPressed.put("LEFT", 0);
            }
            if (keyCode == RIGHT) {
                keysPressed.put("RIGHT", 0);
            }
        }
    }

    /**
     * Main draw loop that renders the sine-cosine pattern. Updates at each frame.
     */
    public void draw() {
        delta = millis() - lastTime; // Calculate time since last frame
        float otInc = gui.slider("otInc", 0f, 0f, 0.03f); // Slider for outer time increment
        float a = gui.slider("a", 0, 0f, 8f); // Slider for controlling parameter 'a'
        background(0,0,0,100); // Clear the screen with a transparent background
        if(mode.equals("SLOW"))
            fadeGraphics(mg,-1); // Apply fading effect in slow mode
            // TODO: Double check color settings
        // Start drawing to the off-screen graphics buffer
        mg.beginDraw();
        {
            if (!Util.paused) {
                mg.push();
                {
                    // Fast drawing mode
                    if (mode.equals("FAST")) {
                        mg.background(0);
                        mg.fill(0, 0, 0, 100); // Semi-transparent black fill for fading effect
                        mg.rect(0, 0, width, height); // Cover the entire window
                        it = 0; // Reset the inner time in fast mode
                    } else {
                        // Switch to slow drawing mode, clear background once
                        if (doModeSwitch) {
                            mg.background(0);
                        }
                    }
                    doModeSwitch = false;

                    // Translate drawing to the center of the window
                    mg.translate(width / 2f, height / 2f);

                    // Main loop to generate the spiral pattern
                    for (int i = 0; i < s; i++) {
                        float theta = it;
                        float r = Function.function(theta, ot, a); // Calculate radius using custom function
                        graphPointsArray.set(itStep % s, new GPoint(itStep % s, r)); // Update points in array

                        // Calculate x, y positions based on theta and radius
                        float x = 10 * r * cos(TAU * theta);
                        float y = 10 * r * sin(TAU * theta);

                        // Scale the points to fit the window
                        float scale = 18;
                        x *= scale;
                        y *= scale;

                        // Draw the tip point
                        mg.pushStyle();
                        {
                            mg.stroke(0, 0, 100); // White stroke
                            mg.strokeWeight(2);
                            mg.point(x, y);
                        }
                        mg.popStyle();

                        // Set stroke color for the lines
                        mg.stroke(30 * sin(it * ppi)+180, (it * 10000) % 1f * 100, 100);
                        float ic = 200; // Inner circle speed

                        // Draw circular connecting lines
                        mg.line(40 * cos(ic * it * ppi),
                                40 * sin(ic * it * ppi),
                                x, y);

                        // Increment the inner time and step counter
                        it += 0.001f * tm;
                        itStep += 1;
                    }

                    // Increment the outer time
                    ot += otInc;
                }
                mg.pop();
            }
        }
        mg.endDraw();

        // Display the off-screen graphics on the main canvas
        image(mg, 0, 0);

        // Display debug information if enabled
        Util.displayDebug(this, tm, s);

        // Update the last time to the current time
        lastTime = millis();
    }

    /**
     * Applies a fade effect to the graphics by reducing the alpha value of all pixels.
     *
     * @param g           the PGraphics to apply the fade effect on
     * @param fadeAmount  the amount to reduce the alpha per frame
     */
    void fadeGraphics(PGraphics g, int fadeAmount) {
        if (fadeAmount > 0 || fadeAmount < 0 && frameCount % -fadeAmount == 0) {
            g.beginDraw();
            g.loadPixels();

            // Iterate over all pixels
            for (int i = 0; i < g.pixels.length; i++) {

                // Get the current alpha value
                int alpha = (g.pixels[i] >> 24) & 0xFF;

                // Reduce the alpha value
                alpha = max(0, alpha - fadeAmount);

                // Update the pixel with the new alpha value
                g.pixels[i] = alpha << 24 | (g.pixels[i]) & 0xFFFFFF;
            }
            g.updatePixels();
            g.endDraw();
        }
    }

    /**
     * Background update thread that handles time-based changes and key input.
     */
    public class UpdateLoop extends Thread {
        public void run() {
            int lastTime = 0; // Initialize local lastTime for this thread
            do {
                delta = millis() - lastTime; // Calculate delta time in the thread loop

                // Handle key inputs to adjust parameters
                if (keysPressed.get("UP") >= 1) {
                    s += 1; // Increase number of steps (points)
                    keysPressed.put("UP", keysPressed.get("UP") + 1);
                }
                if (keysPressed.get("DOWN") >= 1) {
                    s = max(1, s - 1); // Decrease number of steps, minimum of 1
                    keysPressed.put("DOWN", keysPressed.get("DOWN") + 1);
                }
                if (keysPressed.get("LEFT") >= 1) {
                    tm *= 1 / 1.1f; // Decrease time multiplier
                    keysPressed.put("LEFT", keysPressed.get("LEFT") + 1);
                }
                if (keysPressed.get("RIGHT") >= 1) {
                    tm *= 1.1f; // Increase time multiplier
                    keysPressed.put("RIGHT", keysPressed.get("RIGHT") + 1);
                }

                // Sleep to maintain 60 FPS-like behavior
                try {
                    //noinspection BusyWait
                    sleep(max((1000 / 60) - delta, 0));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                lastTime = millis(); // Update last time for the thread loop
            } while (true); // Loop indefinitely
        }
    }

    /**
     * Main method to start the Processing sketch.
     */
    public static void main(String... args) {
        PApplet.main("src.Sincos");
    }
}