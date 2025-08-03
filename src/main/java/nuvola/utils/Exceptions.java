package nuvola.utils;

class FailToOpenFileException extends RuntimeException {
    public FailToOpenFileException(String filePath) {
        super("Couldn't open file " + filePath);
    }
}