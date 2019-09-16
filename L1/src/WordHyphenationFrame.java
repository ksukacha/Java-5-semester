import javafx.util.Pair;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Map;
import java.util.StringTokenizer;

public class WordHyphenationFrame extends JFrame {
    private TextPanel textPanel;
    private File fileToCheck;
    private Map<String, String> dictionary;

    public WordHyphenationFrame() {
        super("Слова");
        textPanel = new TextPanel();
        setContentPane(textPanel);
        JMenuBar menuBar = createMenu();
        setJMenuBar(menuBar);
        setBounds(new Rectangle(700,500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                int optionPressed = fileSavingConfirmation();
                int fileChooserOptionPressed = 0;
                if(optionPressed == JOptionPane.OK_OPTION){
                    fileChooserOptionPressed = save();

                }
                if(optionPressed != JOptionPane.CLOSED_OPTION) {
                    if(fileChooserOptionPressed == JFileChooser.APPROVE_OPTION) {
                        dispose();
                    }
                }
            }
        });

    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Опции");
        JMenuItem openFile = new JMenuItem("Открыть файл");
        JMenuItem saveFile = new JMenuItem("Сохранить файл");
        JMenuItem analizeFile = new JMenuItem("Проверить переносы");
        JMenuItem changeDictionary = new JMenuItem("Изменить словарь");

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textPanel.textChanged()){
                    if(fileSavingConfirmation() == JOptionPane.OK_OPTION){
                        save();
                    }
                }
                JFileChooser fileChooser = setUpFileChooserForOpening();
                int result = fileChooser.showOpenDialog(WordHyphenationFrame.this);
                if (result == JFileChooser.APPROVE_OPTION ){
                    try {
                        fileToCheck = fileChooser.getSelectedFile();
                        textPanel.setTextAreaText(Utils.readFromFile(fileToCheck));
                        textPanel.setBorder(BorderFactory.createTitledBorder(fileToCheck.getName()));
                    }catch(IOException ex){
                        fileToCheck = null;
                        JOptionPane.showMessageDialog(WordHyphenationFrame.this, ex.getMessage());
                    }
                }
            }
        });

        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               save();
            }
        });

        analizeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dictionary != null) {
                    StringTokenizer st = new StringTokenizer(textPanel.getText(), "., ?!:\n\t\r", true);
                    StringBuffer sb = new StringBuffer();
                    int filledWordsLength = 0;
                    boolean rewrite = false;
                    String word;
                    while (st.hasMoreTokens()) {
                        word = st.nextToken();
                        if (word.matches("[а-яА-Я]+-[а-яА-Я]+") && !Utils.wordCorrectDivided(word, dictionary)) {
                            Pair<String, Boolean> correctWordDivision = manageWordCorrection(word, dictionary);
                            if (correctWordDivision == null) {//пользователь нажал "нет" в JOptionPane; ничего не надо менять
                                sb.append(word);
                                filledWordsLength += (word.length());
                            } else {
                                sb.append(correctWordDivision.getKey());
                                filledWordsLength += (correctWordDivision.getKey().length());
                                if (correctWordDivision.getValue()) {
                                    sb.append(textPanel.getText().substring(filledWordsLength));
                                } else {
                                    sb.append(textPanel.getText().substring(filledWordsLength + 1));
                                }
                                rewrite = true;
                            }
                        } else {
                            sb.append(word);
                            filledWordsLength += (word.length());
                        }
                        if (rewrite) {
                            rewrite = false;
                            textPanel.update(sb.toString());
                            sb = new StringBuffer(sb.substring(0, filledWordsLength));
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(WordHyphenationFrame.this, "Файл словаря не выбран", "Выберите словарь", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        changeDictionary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = setUpFileChooserForOpening();
                    int result = fileChooser.showOpenDialog(WordHyphenationFrame.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File fileDictionary = fileChooser.getSelectedFile();
                        dictionary = Utils.readDictionary(fileDictionary);
                    }
                }catch(IOException ex){
                    JOptionPane.showMessageDialog(WordHyphenationFrame.this, ex.getMessage());
                }

            }
        });
        fileMenu.add(fileMenu);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(analizeFile);
        fileMenu.add(changeDictionary);
        menuBar.add(fileMenu);

        return menuBar;
    }

    private int fileSavingConfirmation() {
        return JOptionPane.showConfirmDialog(WordHyphenationFrame.this,
                "Сохранить файл?",
                "Сохранение",
                JOptionPane.YES_NO_OPTION);
    }

    private int save() {
        JFileChooser fileChooser = new JFileChooser();
        setUpFileChooserForSaving(fileChooser);
        int result = fileChooser.showSaveDialog(WordHyphenationFrame.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                Utils.saveFile(file, textPanel.getText());
                JOptionPane.showMessageDialog(WordHyphenationFrame.this, "Файл сохранен");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(WordHyphenationFrame.this, ex.getMessage());
            }
        }
        return result;
    }

    private void setUpFileChooserForSaving(JFileChooser fileChooser){
        fileChooser.setDialogTitle("Сохранение файла");
        TextFileFilter filter = new TextFileFilter();
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
    }

    private JFileChooser setUpFileChooserForOpening(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выбор файла");
        TextFileFilter filter = new TextFileFilter();
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

    private Pair<String, Boolean> manageWordCorrection(String word, Map<String, String> dictionary){//f2
        Utils.ChangeMessage wordChangeMessage = Utils.getWordChangeMessage(word, dictionary);
        int res = JOptionPane.showConfirmDialog(WordHyphenationFrame.this, wordChangeMessage.getMessage(), "Найдена ошибка переноса", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            return new Pair<>(wordChangeMessage.getCorrectWord(), wordChangeMessage.hasDash());
        }return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WordHyphenationFrame app = new WordHyphenationFrame();
                app.setVisible(true);
            }
        });
    }
}
