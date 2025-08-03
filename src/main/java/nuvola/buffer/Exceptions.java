package nuvola.buffer;

import nuvola.buffer.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

class DifferentVertexLayoutException extends RuntimeException {
    public DifferentVertexLayoutException(VertexBuffer buffer, Vertex vertex) {
        super("The vertex layout doesn't match the buffer layout, buffer: " + buffer + ", vertex: " + vertex);
    }
}

class VertexBufferIsDeletedException extends RuntimeException {
    public VertexBufferIsDeletedException(@NotNull VertexBuffer vertexBuffer) {
        super("The selected vertex buffer has already been deleted. Buffer: " + vertexBuffer);
    }
}

class IndexBufferIsDeletedException extends RuntimeException {
    public IndexBufferIsDeletedException(@NotNull IndexBuffer indexBuffer) {
        super("The selected index buffer has already been deleted. Buffer: " + indexBuffer);
    }
}