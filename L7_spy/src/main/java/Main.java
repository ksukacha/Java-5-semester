import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main extends JFrame {
    private boolean directoryChosen = false;
    private Spy spy;
    public Main(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 560);
        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton analizeDirectory = new JButton("Analyze chosen directory");
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> jList = new JList<>(listModel);
        mainPanel.add(jList, BorderLayout.CENTER);
        setJMenuBar(createMenu());
        spy = new Spy();
        spy.addListener(new ForbiddenWordListener() {
            @Override
            public void forbiddenWordFound(ForbiddenWordEvent event) {
                listModel.addElement("Forbidden word - " + event.getWord() + " - found in file: " + event.getFileName());
            }
        });
        analizeDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(directoryChosen) {
                    Main.this.spy.fillForbiddenWords();
                    spy.run();
                }else {
                    JOptionPane.showMessageDialog(Main.this, "Choose file directory!", "Directory not chosen", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(analizeDirectory, BorderLayout.NORTH);
        this.add(mainPanel);
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JMenuBar createMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenu optionsMenu = new JMenu("Database options");
        JMenuItem item = new JMenuItem("Open");
        JMenuItem addItem = new JMenuItem("Add forbidden word");
        JMenuItem deleteItem = new JMenuItem("Clear database");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();
                f.setCurrentDirectory(new java.io.File("."));
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.showDialog(null, "Выбрать папку");
                File file = f.getSelectedFile();
                spy.setFiles(file.listFiles());
                directoryChosen = true;
            }
        });
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog(Main.this, true);
                jDialog.setTitle("Add new word");
                jDialog.setSize(new Dimension(300,300));
                jDialog.setLocationRelativeTo(null);
                JLabel jLabel = new JLabel("Enter forbidden words delimited by space:");
                JTextField jTextField = new JTextField();
                JButton jButton = new JButton("Add");
                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(jTextField.getText().length()!=0) {
                            String[] words = jTextField.getText().split(" ");
                            DatabaseUtils.writeToDB(words);
                            jDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(jDialog, "Enter at least one word!");
                        }
                    }
                });
                jDialog.setLayout(new GridLayout(3, 1));
                jDialog.add(jLabel);
                jDialog.add(jTextField);
                jDialog.add(jButton);
                jDialog.setVisible(true);
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseUtils.clearDB();
                Main.this.spy.setForbiddenWords(null);
                JOptionPane.showMessageDialog(Main.this, "Database cleared");
            }
        });
        menu.add(item);
        optionsMenu.add(addItem);
        optionsMenu.add(deleteItem);
        jMenuBar.add(menu);
        jMenuBar.add(optionsMenu);
        return  jMenuBar;
    }

    public static void main(String[] args) {
        new Main();
    }
}
