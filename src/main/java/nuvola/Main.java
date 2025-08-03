package nuvola;

import nuvola.buffer.IndexBuffer;
import nuvola.buffer.vertex.Position3DTexCoords2DVertex;
import nuvola.buffer.vertex.Vertex;
import nuvola.buffer.VertexBuffer;
import nuvola.buffer.vertex.VertexLayout;
import nuvola.shader.FragmentShader;
import nuvola.shader.GeometryShader;
import nuvola.shader.ShaderProgram;
import nuvola.shader.VertexShader;
import nuvola.texture.Texture;
import nuvola.window.Window;

import java.nio.file.Path;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main(String[] args) {
        Window window = new Window("Hello World!", 600, 600);

        window.setKeyCallback((_, key, _, action, _) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                window.signalClose();
            }
        });

        Path shaderPath = Path.of("src/main/resources/shaders");
        VertexShader vertexShader = VertexShader.fromFile(shaderPath.resolve("texture_vertex_shader.vert"));
        FragmentShader fragmentShader = FragmentShader.fromFile(shaderPath.resolve("texture_fragment_shader.frag"));
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader, GeometryShader.EMPTY);
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

        VertexBuffer vbo = new VertexBuffer(VertexLayout.POSITION_3D_TEX_COORDS_2D_LAYOUT, vertices, VertexBuffer.MemoryType.STATIC);
        IndexBuffer ebo = new IndexBuffer(indices);

        Path texturePath = Path.of("src/main/resources/textures");
        Texture texture1 = new Texture(texturePath.resolve("container.jpg"), Texture.Channels.RGB);
        Texture texture2 = new Texture(texturePath.resolve("awesomeface.png"), Texture.Channels.RGBA);

        texture1.bind(1);
        texture2.bind(2);

        while (!window.shouldClose()) {
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            shader.bind();
            shader.setUniform("texture1", texture1.boundSlot().get());
            shader.setUniform("texture2", texture2.boundSlot().get());

            vbo.bind();
            ebo.bind();
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            window.swapBuffers();
            glfwPollEvents();
        }

        vbo.delete();
        ebo.delete();
        shader.delete();
        window.close();
    }
}