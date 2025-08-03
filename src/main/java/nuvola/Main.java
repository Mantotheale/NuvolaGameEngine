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
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(600, 600, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> glViewport(0, 0, width, height));

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

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

        while (!glfwWindowShouldClose(window))
        {
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            shader.bind();
            shader.setUniform("texture1", texture1.boundSlot().get());
            shader.setUniform("texture2", texture2.boundSlot().get());

            vbo.bind();
            ebo.bind();
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        vbo.delete();
        ebo.delete();
        shader.delete();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}