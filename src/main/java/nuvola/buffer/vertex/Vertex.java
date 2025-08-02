package nuvola.buffer.vertex;

import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;

public interface Vertex {
    void fill(@NotNull FloatBuffer buffer);
    @NotNull VertexLayout layout();
}
