package nuvola.texture;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int id;
    private @NotNull Optional<Integer> boundSlot;
    private boolean isDeleted;

    public Texture(@NotNull Path imagePath, @NotNull Channels desiredChannels) {
        this.boundSlot = Optional.empty();
        this.isDeleted = false;
        this.id = glGenTextures();

        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = stbi_load(imagePath.toString(), width, height, channels, 0);
            if (image == null) { throw new UnableToLoadTextureException(imagePath); }

            Channels imageChannels = Channels.fromInt(channels.get(0));

            glTexImage2D(GL_TEXTURE_2D, 0, desiredChannels.openGLType(), width.get(0), height.get(0), 0, imageChannels.openGLType(), GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            stbi_image_free(image);
        }

        unbind();
    }

    public void bind(int slot) {
        if (isDeleted) { throw new TextureIsDeletedException(this); }

        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, id);
        boundSlot = Optional.of(slot);
    }

    public void bind() {
        bind(0);
    }

    public void unbind() {
        if (isDeleted) { throw new TextureIsDeletedException(this); }

        boundSlot.ifPresent(slot -> {
            glActiveTexture(GL_TEXTURE0 + slot);
            glBindTexture(GL_TEXTURE_2D, 0);
            boundSlot = Optional.empty();
        });
    }

    public int id() {
        if (isDeleted) { throw new TextureIsDeletedException(this); }
        return id;
    }

    public @NotNull Optional<Integer> boundSlot() {
        if (isDeleted) { throw new TextureIsDeletedException(this); }
        return boundSlot;
    }

    public void delete() {
        if (isDeleted) { throw new TextureIsDeletedException(this); }
        glDeleteTextures(id);
        isDeleted = true;
    }

    public enum Channels {
        RGB {
            @Override
            public int openGLType() {
                return GL_RGB;
            }
        },
        RGBA {
            @Override
            public int openGLType() {
                return GL_RGBA;
            }
        };

        public abstract int openGLType();

        public static @NotNull Channels fromInt(int channels) {
            return switch (channels) {
                case 3 -> RGB;
                case 4 -> RGBA;
                default -> throw new UnsupportedChannelsException(channels);
            };
        }
    }
}
