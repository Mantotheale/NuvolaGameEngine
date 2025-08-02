package nuvola.exceptions;

import nuvola.buffer.IndexBuffer;
import org.jetbrains.annotations.NotNull;

public class IndexBufferIsDeletedException extends RuntimeException {
    public IndexBufferIsDeletedException(@NotNull IndexBuffer indexBuffer) {
        super("The selected index buffer has already been deleted. Buffer: " + indexBuffer);
    }
}
