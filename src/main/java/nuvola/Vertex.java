package nuvola;

import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;

public interface Vertex {
    void fill(@NotNull FloatBuffer buffer);
}
