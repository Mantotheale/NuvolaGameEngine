package nuvola.exceptions;

import nuvola.buffer.vertex.Vertex;
import nuvola.buffer.VertexBuffer;

public class DifferentVertexLayoutException extends RuntimeException {
    public DifferentVertexLayoutException(VertexBuffer buffer, Vertex vertex) {
        super("The vertex layout doesn't match the buffer layout, buffer: " + buffer + ", vertex: " + vertex);
    }
}
