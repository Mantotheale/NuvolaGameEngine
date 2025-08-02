package nuvola;

import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;

public record Position3DVertex(float x, float y, float z) implements Vertex {
    @Override
    public void fill(@NotNull FloatBuffer buffer) {
        buffer.put(x).put(y).put(z);
    }
}