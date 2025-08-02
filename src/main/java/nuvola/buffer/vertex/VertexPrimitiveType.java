package nuvola.buffer.vertex;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public enum VertexPrimitiveType {
    FLOAT {
        @Override
        public int glType() {
            return GL_FLOAT;
        }
    };

    abstract public int glType();
}
