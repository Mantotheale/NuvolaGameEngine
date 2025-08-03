package nuvola.shader;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ConcreteShader implements Shader {
    private final int id;
    private final @NotNull String content;
    private boolean isDeleted;

    protected ConcreteShader(@NotNull Shader.Type type, @NotNull String content) {
        this.content = Objects.requireNonNull(content);

        this.id = glCreateShader(type.glType());
        glShaderSource(id, content);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
            throw new ShaderCompilationException(this, glGetShaderInfoLog(id));
        this.isDeleted = false;
    }

    public int id() {
        if (isDeleted) {
            throw new ShaderIsDeletedException(this);
        }
        return id;
    }

    @Override
    public void delete() {
        if (isDeleted) {
            throw new ShaderIsDeletedException(this);
        }
        glDeleteShader(id);
        isDeleted = true;
    }

    @Override
    public void attachTo(@NotNull ShaderProgram program) {
        if (isDeleted) {
            throw new ShaderIsDeletedException(this);
        }
        glAttachShader(program.id(), id);
    }

    @Override
    public void detachFrom(@NotNull ShaderProgram program) {
        if (isDeleted) {
            throw new ShaderIsDeletedException(this);
        }
        glDetachShader(program.id(), id);
    }

    @Override
    public String toString() {
        return "id: " + id + "\ncontent:\n" + content;
    }
}
