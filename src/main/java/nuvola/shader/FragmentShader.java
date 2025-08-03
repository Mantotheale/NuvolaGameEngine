package nuvola.shader;

import nuvola.utils.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public sealed class FragmentShader implements Shader {
    public final static FragmentShader EMPTY = new EmptyFragmentShader();

    @NotNull public static FragmentShader parse(@NotNull String content) {
        return new ValuedFragmentShader(new ConcreteShader(Type.FRAGMENT_SHADER, content));
    }

    @NotNull public static FragmentShader fromFile(@NotNull Path path) {
        return new ValuedFragmentShader(new ConcreteShader(Type.FRAGMENT_SHADER, FileUtils.fileToString(path)));
    }

    @NotNull public static FragmentShader fromFile(@NotNull String pathString) {
        return new ValuedFragmentShader(new ConcreteShader(Type.FRAGMENT_SHADER, FileUtils.fileToString(pathString)));
    }

    private final static class EmptyFragmentShader extends FragmentShader { }

    private final static class ValuedFragmentShader extends FragmentShader {
        private final ConcreteShader shader;

        public ValuedFragmentShader(@NotNull ConcreteShader shader) {
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