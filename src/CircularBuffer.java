package src;

import processing.core.PVector;

// ChatGPT plus modifications by EmergencyTemporalShift, free to use without attribution.
@SuppressWarnings("unused")
public class CircularBuffer {
    final PVector[] buffer;
    final int maxSize;
    int currentIndex = 0;

    CircularBuffer(int size) {
        buffer = new PVector[size];
        maxSize = size;
    }
    @SuppressWarnings("unused")
    void add(float value) {
        buffer[currentIndex] = new PVector((float) currentIndex, value);
        currentIndex = (currentIndex + 1) % maxSize;
    }

    @SuppressWarnings("unused")
    PVector get(int index) {
        return buffer[(currentIndex + index) % maxSize];
    }

    @SuppressWarnings("unused")
    int size() {
        return maxSize;
    }
}
