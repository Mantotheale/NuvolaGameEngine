package nuvola.shader;

import nuvola.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public sealed class VertexShader implements Shader {
    public final static VertexShader EMPTY = new EmptyVertexShader();

    @NotNull public static VertexShader parse(@NotNull String content) {
        return new ValuedVertexShader(new ConcreteShader(Type.VERTEX_SHADER, content));
    }

    @NotNull public static VertexShader fromFile(@NotNull Path path) {
        return new ValuedVertexShader(new ConcreteShader(Type.VERTEX_SHADER, FileUtils.fileToString(path)));
    }

    @NotNull public static VertexShader fromFile(@NotNull String pathString) {
        return new ValuedVertexShader(new ConcreteShader(Type.VERTEX_SHADER, FileUtils.fileToString(pathString)));
    }

    private final static class EmptyVertexShader extends VertexShader { }

    private final static class ValuedVertexShader extends VertexShader {
        private final ConcreteShader shader;

        public ValuedVertexShader(@NotNull ConcreteShader shader) {
            this.shader = Objects.requireNonNull(shader);
        }

        @Override
        public void delete() {
            shader.delete();
        }

        @Override
        public void attachTo(@NotNull ShaderProgram program) {
            shader.attachTo(program);
        }

        @Override
        public void detachFrom(@NotNull ShaderProgram program) {
            shader.detachFrom(program);
        }

        @Override
        public String toString() {
            return shader.toString();
        }
    }
}