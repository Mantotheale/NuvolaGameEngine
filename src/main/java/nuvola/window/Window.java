package nuvola.window;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Window {
    private final long id;
    private final @NotNull String title;
    private int width;
    private int height;

    public Window(@NotNull String title, int width, int height) {
        if (width <= 0 || height <= 0) { throw new InvalidWindowSizeException(width, height); }

        this.title = Objects.requireNonNull(title);
        this.width = width;
        this.height = height;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) { throw new GLFWInitializationException(); }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        this.id = glfwCreateWindow(width, height, title, 0, 0);
        if (id == 0) { throw new GLFWFailedToOpenWindowException(); }

        glfwMakeContextCurrent(id);

        GL.createCapabilities();
        glViewport(0, 0, width, height);

        setFramebufferSizeCallback((_, w, h) -> {
            this.width = w;
            this.height = h;
            glViewport(0, 0, w, h);
        });
    }

    public void enableVsync() {
        glfwSwapInterval(1);
    }

    public void disableVsync() {
        glfwSwapInterval(0);
    }

    public void swapBuffers() {
        glfwSwapBuffers(id);
    }

    public void close() {
        glfwFreeCallbacks(id);
        glfwDestroyWindow(id);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void setFramebufferSizeCallback(@NotNull GLFWFramebufferSizeCallbackI callback) {
        glfwSetFramebufferSizeCallback(id, Objects.requireNonNull(callback));
    }

    public void freeFramebufferSizeCallback() {
        glfwSetFramebufferSizeCallback(id, null);
    }

    public void setKeyCallback(@NotNull GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(id, Objects.requireNonNull(callback));
    }

    public void freeKeyCallback() {
        glfwSetKeyCallback(id, null);
    }
}
