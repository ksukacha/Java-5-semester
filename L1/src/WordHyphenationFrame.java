import javafx.util.Pair;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.StringTokenizer;

public class WordHyphenationFrame extends JFrame {
    private TextPanel textPanel;
    private File fileToCheck;
    private File fileDictionary;

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
                confirmFileSaving();
                dispose();
            }
        });
        fileDictionary = new File("C:\\Users\\DELL\\Documents\\JAVA\\5 сем лабы\\words1.txt");
        try{
            Utils.readDictionary(fileDictionary);
        }catch(IOException ex){
            JOptionPane.showMessageDialog(WordHyphenationFrame.this, ex.getMessage());
        }
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
                    confirmFileSaving();
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
               StringTokenizer st = new StringTokenizer(textPanel.getText(), "., ?!:\n\t\r", true);
               StringBuffer sb = new StringBuffer();
               int filledWordsLength = 0;
               boolean rewrite = false;
               String word;
               while(st.hasMoreTokens()){
                   word = st.nextToken();
                   if(word.matches("[а-яА-Я]+-[а-яА-Я]+") && !Utils.wordCorrectDivided(word)){
                           Pair<String, Boolean> correctWordDivision = manageWordCorrection(word);
                           if(correctWordDivision==null){//пользователь нажал "нет" в JOptionPane
                               sb.append(word);
                               filledWordsLength+=(word.length());
                           }else{
                               sb.append(correctWordDivision.getKey());
                               filledWordsLength+=(correctWordDivision.getKey().length());
                               if(correctWordDivision.getValue()){
                                   sb.append(textPanel.getText().substring(filledWordsLength));
                               }else{
                                   sb.append(textPanel.getText().substring(filledWordsLength+1));
                               }
                               rewrite = true;
                           }
                   }else{
                       sb.append(word);
                       filledWordsLength+=(word.length());
                   }
                   if(rewrite){
                       rewrite = false;
                       textPanel.update(sb.toString());
                       sb = new StringBuffer(sb.substring(0, filledWordsLength));
                   }
               }
               JOptionPane.showMessageDialog(WordHyphenationFrame.this, "Переносы корректны", "Проверка завершена", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        changeDictionary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = setUpFileChooserForOpening();
                    int result = fileChooser.showOpenDialog(WordHyphenationFrame.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        fileDictionary = fileChooser.getSelectedFile();
                        Utils.readDictionary(fileDictionary);
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

    private void confirmFileSaving() {
        int optionPressed = JOptionPane.showConfirmDialog(WordHyphenationFrame.this,
                "Сохранить файл?",
                "Сохранение",
                JOptionPane.YES_NO_OPTION);
        if(optionPressed == JOptionPane.OK_OPTION){
            save();
        }
    }

    private void save() {
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\DELL\\Documents\\JAVA\\5 сем лабы");
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
    }

    private void setUpFileChooserForSaving(JFileChooser fileChooser){
        fileChooser.setDialogTitle("Сохранение файла");
        TextFileFilter filter = new TextFileFilter();
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
    }

    private JFileChooser setUpFileChooserForOpening(){
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\DELL\\Documents\\JAVA\\5 сем лабы");
        fileChooser.setDialogTitle("Выбор файла");
        TextFileFilter filter = new TextFileFilter();
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

    private Pair<String, Boolean> manageWordCorrection(String word){//f2
        Utils.ChangeMessage wordChangeMessage = Utils.getWordChangeMessage(word);
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
