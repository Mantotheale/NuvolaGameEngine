package nuvola;

import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;

public record Position3DColor3CVertex(
        float x, float y, float z,
        float r, float g, float b) implements Vertex {
    @Override
    public void fill(@NotNull FloatBuffer buffer) {
        buffer
                .put(x).put(y).put(z)
                .put(r).put(g).put(b);
    }
}