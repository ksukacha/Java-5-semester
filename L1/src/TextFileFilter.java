import javax.swing.filechooser.FileFilter;
import java.io.File;

public class TextFileFilter extends FileFilter{
    @Override
    public boolean accept(File f) {
        if(f.isDirectory()){
            return true;
        }
        return f.getName().endsWith(".txt");
    }

    @Override
    public String getDescription() {
        return "*.txt";
    }
}
