package nuvola.buffer.vertex;

import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;

public record Position3DTexCoords2DVertex(
        float x, float y, float z,
        float u, float v) implements Vertex {
    @Override
    public void fill(@NotNull FloatBuffer buffer) {
        buffer
                .put(x).put(y).put(z)
                .put(u).put(v);
    }

    @Override
    public @NotNull VertexLayout layout() {
        return VertexLayout.POSITION_3D_TEX_COORDS_2D_LAYOUT;
    }
}