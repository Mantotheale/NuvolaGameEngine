package nuvola.shader;

import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int id;
    private final @NotNull String content;
    private boolean isDeleted;

    public ShaderProgram(
            @NotNull VertexShader vertexShader,
            @NotNull FragmentShader fragmentShader,
            @NotNull GeometryShader geometryShader) {
        this.id = glCreateProgram();

        vertexShader.attachTo(this);
        fragmentShader.attachTo(this);
        geometryShader.attachTo(this);
        this.content = "VERTEX SHADER:\n" + vertexShader + "\n\nFRAGMENT SHADER:\n" + fragmentShader + "\n\nGEOMETRY SHADER:\n" + geometryShader;

        glLinkProgram(id);
        if(glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE)
            throw new ShaderProgramLinkingException(this, glGetProgramInfoLog(id));

        vertexShader.detachFrom(this);
        fragmentShader.detachFrom(this);
        geometryShader.detachFrom(this);

        this.isDeleted = false;
    }

    public int id() {
        if (isDeleted) { throw new ShaderProgramIsDeletedException(this); }
        return id;
    }

    public void bind() {
        if (isDeleted) { throw new ShaderProgramIsDeletedException(this); }
        glUseProgram(id);
    }

    public void unbind() {
        if (isDeleted) { throw new ShaderProgramIsDeletedException(this); }
        glUseProgram(0);
    }

    public void delete() {
        if (isDeleted) { throw new ShaderProgramIsDeletedException(this); }
        glDeleteProgram(id);
        isDeleted = true;
    }

    @Override
    public String toString() {
        return "id: " + id + "\ncontent:\n" + content;
    }
}
