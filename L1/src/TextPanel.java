import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TextPanel extends JPanel {
    private JTextArea textArea;
    private boolean textChanged;
    public TextPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Новый текстовый файл"));
        textArea = new JTextArea(25,55);
        textArea.setFont(new Font("Areal", Font.PLAIN, 20));
        textArea.setTabSize(10);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textChanged = true;
            }
        });
        add(new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
    }

    public void setTextAreaText(String text) {
        textChanged = false;
        textArea.setText(text);
    }
    public void update(String newText){
        textChanged = true;
        textArea.setText(newText);
    }

    public boolean textChanged() {
        return textChanged;
    }

    public String getText() {
        return textArea.getText();
    }

}
