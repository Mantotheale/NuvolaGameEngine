package nuvola.buffer;

import nuvola.buffer.vertex.Vertex;
import nuvola.buffer.vertex.VertexAttribute;
import nuvola.buffer.vertex.VertexLayout;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class VertexBuffer {
    private final int vao_id;
    private final int vbo_id;
    private final @NotNull VertexLayout layout;
    private boolean isDeleted;

    public VertexBuffer(@NotNull VertexLayout layout, int vertexCapacity, @NotNull MemoryType type) {
        this.vao_id = glGenVertexArrays();
        this.vbo_id = glGenBuffers();
        this.layout = Objects.requireNonNull(layout);
        this.isDeleted = false;

        glBindVertexArray(vao_id);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
        glBufferData(
                GL_ARRAY_BUFFER,
                (long) vertexCapacity * layout.byteSize(),
                switch (type) {
                    case STATIC -> GL_STATIC_DRAW;
                    case DYNAMIC -> GL_DYNAMIC_DRAW;
                });

        for (VertexAttribute attribute: layout) {
            glVertexAttribPointer(
                    attribute.index(),
                    attribute.primitiveCount(),
                    attribute.primitiveType().glType(),
                    attribute.isNormalized(),
                    attribute.stride(),
                    attribute.offset()
            );
            glEnableVertexAttribArray(attribute.index());
        }

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public VertexBuffer(@NotNull VertexLayout layout, @NotNull List<Vertex> vertices, @NotNull MemoryType type) {
        this(layout, vertices.size(), type);
        fill(vertices);
    }

    public void bind() {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        glBindVertexArray(vao_id);
    }

    public void unbind() {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        glBindVertexArray(0);
    }

    public void fill(@NotNull FloatBuffer buffer) {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Objects.requireNonNull(buffer));
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void fill(@NotNull List<Vertex> vertices) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.size() * layout.byteSize() / Float.BYTES);
        for (Vertex v: vertices) {
            if (!v.layout().equals(layout)) { throw new DifferentVertexLayoutException(this, v); }
            v.fill(buffer);
        }

        fill(buffer.flip());
        MemoryUtil.memFree(buffer);
    }

    public void delete() {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        glDeleteBuffers(vbo_id);
        glDeleteVertexArrays(vao_id);
        isDeleted = true;
    }

    public enum MemoryType {
        STATIC,
        DYNAMIC
    }
}
