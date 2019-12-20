import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationBundle {
    private static ResourceBundle lastSelectedBundle;
    private static Locale lastSelectedLocale;
    static {
        lastSelectedBundle = ResourceBundle.getBundle("lang", Locale.ROOT);
        lastSelectedLocale = Locale.ROOT;
    }
    private LocalizationBundle(){}

    public static String getString(String key){
        return lastSelectedBundle.getString(key);
    }

    public static Locale getLastSelectedLocale() {
        return lastSelectedLocale;
    }

    public static void setBundle(Locale locale){
        lastSelectedBundle = ResourceBundle.getBundle("lang", locale);
        lastSelectedLocale = locale;
    }
}