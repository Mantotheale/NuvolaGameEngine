package nuvola;

import nuvola.exceptions.VertexBufferIsDeletedException;
import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;

public class VertexBuffer {
    private final int id;
    private boolean isDeleted;

    public VertexBuffer(long capacity, @NotNull MemoryType type) {
        id = glGenBuffers();
        isDeleted = false;

        bind();
        glBufferData(
                GL_ARRAY_BUFFER,
                capacity,
                switch (type) {
                    case STATIC -> GL_STATIC_DRAW;
                    case DYNAMIC -> GL_DYNAMIC_DRAW;
                });
        unbind();
    }

    public void bind() {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        glBindBuffer(GL_ARRAY_BUFFER, id);
    }

    public void unbind() {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void fill(@NotNull FloatBuffer buffer) {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        bind();
        glBufferSubData(GL_ARRAY_BUFFER, 0, Objects.requireNonNull(buffer));
    }

    public void delete() {
        if (isDeleted) { throw new VertexBufferIsDeletedException(this); }
        glDeleteBuffers(id);
        isDeleted = true;
    }

    public enum MemoryType {
        STATIC,
        DYNAMIC
    }
}
