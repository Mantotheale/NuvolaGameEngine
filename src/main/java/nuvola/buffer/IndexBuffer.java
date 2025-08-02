package nuvola.buffer;

import nuvola.exceptions.buffer.IndexBufferIsDeletedException;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;

public class IndexBuffer {
    private final int id;
    private boolean isDeleted;

    public IndexBuffer(int capacity) {
        this.id = glGenBuffers();
        this.isDeleted = false;

        bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) capacity * Integer.BYTES, GL_STATIC_DRAW);
        unbind();
    }

    public IndexBuffer(@NotNull List<Integer> indices) {
        this(indices.size());
        fill(indices);
    }

    public void bind() {
        if (isDeleted) { throw new IndexBufferIsDeletedException(this); }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
    }

    public void unbind() {
        if (isDeleted) { throw new IndexBufferIsDeletedException(this); }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void fill(@NotNull IntBuffer buffer) {
        bind();
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, Objects.requireNonNull(buffer));
        unbind();
    }

    public void fill(@NotNull List<Integer> indices) {
        IntBuffer buffer = MemoryUtil.memAllocInt(indices.size());
        for (Integer i: indices) {
            buffer.put(i);
        }

        fill(buffer.flip());
        MemoryUtil.memFree(buffer);
    }

    public void delete() {
        if (isDeleted) { throw new IndexBufferIsDeletedException(this); }
        glDeleteBuffers(id);
        isDeleted = true;
    }
}
