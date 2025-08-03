package nuvola.shader;

import org.jetbrains.annotations.NotNull;

class ShaderCompilationException extends RuntimeException {
    public ShaderCompilationException(ConcreteShader shader, String infoLog) {
        super("Couldn't compile shader " + shader + "\nError: " + infoLog);
    }

    public ShaderCompilationException(ConcreteShader shader) {
        super("Couldn't compile shader " + shader);
    }

    public ShaderCompilationException(String infoLog) {
        super("Couldn't compile shader. Error: " + infoLog);
    }
}

class ShaderIsDeletedException extends RuntimeException {
    public ShaderIsDeletedException(@NotNull ConcreteShader shader) {
        super("The selected shader has already been deleted. Shader: " + shader);
    }
}

class ShaderProgramLinkingException extends RuntimeException {
    public ShaderProgramLinkingException(ShaderProgram shaderProgram, String infoLog) {
        super("Couldn't link shader program:\n" + shaderProgram + "\nError: " + infoLog);
    }

    public ShaderProgramLinkingException(ShaderProgram shaderProgram) {
        super("Couldn't link shader program:\n" + shaderProgram);
    }

    public ShaderProgramLinkingException(String infoLog) {
        super("Couldn't link shader program, error: " + infoLog);
    }
}

class ShaderProgramIsDeletedException extends RuntimeException {
    public ShaderProgramIsDeletedException(@NotNull ShaderProgram shader) {
        super("The selected shader program has already been deleted. Program: " + shader);
    }
}

class ShaderUniformNotFoundException extends RuntimeException {
    public ShaderUniformNotFoundException(ShaderProgram shaderProgram, String uniformName) {
        super("An uniform with name " + uniformName + " doesn't exist in shader:\n" + shaderProgram);
    }
}