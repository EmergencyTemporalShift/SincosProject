package src;

import processing.core.PVector;

// ChatGPT plus modifications by me
public class CircularBuffer {
    PVector[] buffer;
    int maxSize;
    int currentIndex = 0;

    CircularBuffer(int size) {
        buffer = new PVector[size];
        maxSize = size;
    }

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
