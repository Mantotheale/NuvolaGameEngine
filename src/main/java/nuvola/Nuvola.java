package nuvola;

import nuvola.window.Window;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public abstract class Nuvola {
    private final static double ONE_SEC_TIME = 1;

    private final @NotNull Window window;
    private final int FPS;
    private final double UPDATE_TIME;
    private boolean shouldClose;

    public Nuvola(@NotNull String windowTitle, int windowWidth, int windowHeight, int fps) {
        this.window = new Window(Objects.requireNonNull(windowTitle), windowWidth, windowHeight);
        window.enableVsync();

        this.FPS = fps;
        this.UPDATE_TIME = (double) 1 / fps;
        this.shouldClose = false;

        window.setKeyCallback((_, key, _, action, _) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                signalClose();
            }
        });
    }

    public final void run() {
        double currentTime =glfwGetTime();
        double nextUpdateTime = currentTime + UPDATE_TIME;
        double nextOneSecTime = currentTime + ONE_SEC_TIME;

        while (!shouldClose) {
            glfwPollEvents();

            currentTime = glfwGetTime();
            while (currentTime >= nextUpdateTime) {
                update();
                nextUpdateTime += UPDATE_TIME;
            }

            render();
            window.swapBuffers();

            while (currentTime >= nextOneSecTime) {
                oneSecUpdate();

                nextOneSecTime += ONE_SEC_TIME;
            }
        }

        terminate();
    }

    protected abstract void update();

    protected abstract void oneSecUpdate();

    protected abstract void render();

    protected abstract void terminate();

    public void signalClose() {
        shouldClose = true;
    }
}