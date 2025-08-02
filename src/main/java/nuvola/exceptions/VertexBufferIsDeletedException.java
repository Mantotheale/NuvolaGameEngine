package nuvola.exceptions;

import nuvola.VertexBuffer;
import org.jetbrains.annotations.NotNull;

public class VertexBufferIsDeletedException extends RuntimeException {
    public VertexBufferIsDeletedException(@NotNull VertexBuffer vertexBuffer) {
        super("The selected vertex buffer has already been deleted. Buffer: " + vertexBuffer);
    }
}
