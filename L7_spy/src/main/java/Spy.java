import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spy extends Thread {
    private File[] files;
    private List<String> forbiddenWords;
    private List<ForbiddenWordListener> listeners = new ArrayList<>();

    public void addListener(ForbiddenWordListener listener) {
        listeners.add(listener);
    }


    public Spy() {
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public void fillForbiddenWords() {
        forbiddenWords = DatabaseUtils.readFromDB(DatabaseUtils.getDatabaseConnection());
    }

    public List<String> getForbiddenWords() {
        return forbiddenWords;
    }

    public void setForbiddenWords(List<String> forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }

    @Override
    public void run() {
        try {
            checkFilesOnForbiddenWords(findTxtFiles(files));
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public List<File> findTxtFiles(File[] f) {
        List<File> filesTxt = new ArrayList<>();

        for (int i = 0; i < f.length; i++) {
            String text = f[i].toString();
            Pattern pattern = Pattern.compile("([\\S\\s]+?)\\.txt");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                filesTxt.add(f[i]);
            }
        }
        return filesTxt;
    }

    public String getTextFromFile(File f) throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line;
        while ((line = reader.readLine()) != null) {
            s.append(line);
            s.append("\n");
        }

        return s.toString();
    }

    public void checkFilesOnForbiddenWords(List<File> filesList) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String word : forbiddenWords) {
            sb.append(word);
            sb.append("|");
        }
        if (sb.length() != 0) {
            for (File f : filesList) {
                String text = getTextFromFile(f);
                Pattern pattern = Pattern.compile(sb.substring(0, sb.length() - 1));
                Matcher matcher = pattern.matcher(text);
                StringBuilder s = new StringBuilder();
                while (matcher.find()) {
                    ForbiddenWordEvent event = new ForbiddenWordEvent(this, text.substring(matcher.start(), matcher.end()), f.getName());
                    for (ForbiddenWordListener listener : listeners) {
                        listener.forbiddenWordFound(event);
                    }
                }
            }
        }
    }
}
