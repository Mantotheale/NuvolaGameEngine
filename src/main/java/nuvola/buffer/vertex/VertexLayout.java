package nuvola.buffer.vertex;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public interface VertexLayout extends Iterable<VertexAttribute> {
    int byteSize();

    VertexLayout POSITION_3D_LAYOUT = new VertexLayout() {
        private final List<VertexAttribute> attributes = List.of(
                new VertexAttribute(0, 3, VertexPrimitiveType.FLOAT, false, byteSize(), 0)
        );

        @Override
        public int byteSize() {
            return 3 * Float.BYTES;
        }

        @Override
        public @NotNull Iterator<VertexAttribute> iterator() {
            return attributes.iterator();
        }
    };

    VertexLayout POSITION_3D_COLOR_3C_LAYOUT = new VertexLayout() {
        private final List<VertexAttribute> attributes = List.of(
                new VertexAttribute(0, 3, VertexPrimitiveType.FLOAT, false, byteSize(), 0),
                new VertexAttribute(1, 3, VertexPrimitiveType.FLOAT, false, byteSize(), 3 * Float.BYTES)
        );

        @Override
        public int byteSize() {
            return 6 * Float.BYTES;
        }

        @Override
        public @NotNull Iterator<VertexAttribute> iterator() {
            return attributes.iterator();
        }
    };

    VertexLayout POSITION_3D_TEX_COORDS_2D_LAYOUT = new VertexLayout() {
        private final List<VertexAttribute> attributes = List.of(
                new VertexAttribute(0, 3, VertexPrimitiveType.FLOAT, false, byteSize(), 0),
                new VertexAttribute(1, 2, VertexPrimitiveType.FLOAT, false, byteSize(), 3 * Float.BYTES)
        );

        @Override
        public int byteSize() {
            return 5 * Float.BYTES;
        }

        @Override
        public @NotNull Iterator<VertexAttribute> iterator() {
            return attributes.iterator();
        }
    };
}
