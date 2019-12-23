import java.util.EventObject;

public class ForbiddenWordEvent extends EventObject {
    private String word;
    private String fileName;

    public ForbiddenWordEvent(Object source, String word, String fileName) {
        super(source);
        this.word = word;
        this.fileName = fileName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
