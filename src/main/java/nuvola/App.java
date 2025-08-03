package nuvola;

import nuvola.buffer.IndexBuffer;
import nuvola.buffer.VertexBuffer;
import nuvola.buffer.vertex.Position3DTexCoords2DVertex;
import nuvola.buffer.vertex.Vertex;
import nuvola.buffer.vertex.VertexLayout;
import nuvola.shader.FragmentShader;
import nuvola.shader.GeometryShader;
import nuvola.shader.ShaderProgram;
import nuvola.shader.VertexShader;
import nuvola.texture.Texture;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

public class App extends Nuvola {
    private final @NotNull ShaderProgram shader;
    private final @NotNull VertexBuffer vertexBuffer;
    private final @NotNull IndexBuffer indexBuffer;
    private final @NotNull Texture texture1;
    private final @NotNull Texture texture2;

    public App(@NotNull String windowTitle, int windowWidth, int windowHeight) {
        super(windowTitle, windowWidth, windowHeight, 60);

        Path shaderPath = Path.of("src/main/resources/shaders");
        VertexShader vertexShader = VertexShader.fromFile(shaderPath.resolve("texture_vertex_shader.vert"));
        FragmentShader fragmentShader = FragmentShader.fromFile(shaderPath.resolve("texture_fragment_shader.frag"));

        this.shader = new ShaderProgram(vertexShader, fragmentShader, GeometryShader.EMPTY);
        vertexShader.delete();
        fragmentShader.delete();

        List<Vertex> vertices = List.of(
                new Position3DTexCoords2DVertex(0.5f,  0.5f, 0.0f, 1.0f, 1.0f),  // top right
                new Position3DTexCoords2DVertex(0.5f, -0.5f, 0.0f, 1.0f, 0.0f),  // bottom right
                new Position3DTexCoords2DVertex(-0.5f, -0.5f, 0.0f, 0.0f, 0.0f),  // bottom left
                new Position3DTexCoords2DVertex(-0.5f,  0.5f, 0.0f, 0.0f, 1.0f)   // top left
        );
        List<Integer> indices = List.of(
                0, 1, 3,  // first Triangle
                1, 2, 3   // second Triangle
        );

        this.vertexBuffer = new VertexBuffer(VertexLayout.POSITION_3D_TEX_COORDS_2D_LAYOUT, vertices, VertexBuffer.MemoryType.STATIC);
        this.indexBuffer = new IndexBuffer(indices);

        Path texturePath = Path.of("src/main/resources/textures");
        this.texture1 = new Texture(texturePath.resolve("container.jpg"), Texture.Channels.RGB);
        this.texture2 = new Texture(texturePath.resolve("awesomeface.png"), Texture.Channels.RGBA);

        texture1.bind(1);
        texture2.bind(2);
    }

    private int frames = 0;
    private int updates = 0;

    @Override
    public void update() {
        updates++;
    }

    @Override
    protected void oneSecUpdate() {
        System.out.println("FPS: " + frames + "\nUPS: " + updates);
        updates = 0;
        frames = 0;
    }

    @Override
    public void render() {
        frames++;

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        shader.bind();
        shader.setUniform("texture1", texture1.boundSlot().get());
        shader.setUniform("texture2", texture2.boundSlot().get());

        vertexBuffer.bind();
        indexBuffer.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void terminate() {
        vertexBuffer.delete();
        indexBuffer.delete();
        shader.delete();
        texture1.delete();
        texture2.delete();
    }
}
