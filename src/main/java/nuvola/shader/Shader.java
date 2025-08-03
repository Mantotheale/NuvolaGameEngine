package nuvola.shader;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public interface Shader {
    default void delete() { }
    default void attachTo(@NotNull ShaderProgram program) { }
    default void detachFrom(@NotNull ShaderProgram program) { }

    enum Type {
        VERTEX_SHADER {
            @Override
            public int glType() {
                return GL_VERTEX_SHADER;
            }
        },
        FRAGMENT_SHADER {
            @Override
            public int glType() {
                return GL_FRAGMENT_SHADER;
            }
        },
        GEOMETRY_SHADER {
            @Override
            public int glType() {
                return GL_GEOMETRY_SHADER;
            }
        };

        public abstract int glType();
    }
}