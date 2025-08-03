package nuvola.shader;

import nuvola.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public sealed class GeometryShader implements Shader {
    public final static GeometryShader EMPTY = new EmptyGeometryShader();

    @NotNull public static GeometryShader parse(@NotNull String content) {
        return new ValuedGeometryShader(new ConcreteShader(Type.GEOMETRY_SHADER, content));
    }

    @NotNull public static GeometryShader fromFile(@NotNull Path path) {
        return new ValuedGeometryShader(new ConcreteShader(Type.GEOMETRY_SHADER, FileUtils.fileToString(path)));
    }

    @NotNull public static GeometryShader fromFile(@NotNull String pathString) {
        return new ValuedGeometryShader(new ConcreteShader(Type.GEOMETRY_SHADER, FileUtils.fileToString(pathString)));
    }

    private final static class EmptyGeometryShader extends GeometryShader { }

    private final static class ValuedGeometryShader extends GeometryShader {
        private final ConcreteShader shader;

        public ValuedGeometryShader(@NotNull ConcreteShader shader) {
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