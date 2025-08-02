package nuvola.buffer.vertex;

public record VertexAttribute(
        int index,
        int primitiveCount,
        VertexPrimitiveType primitiveType,
        boolean isNormalized,
        int stride,
        int offset
) {}