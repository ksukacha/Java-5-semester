import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class Demo extends JFrame {
    private JLabel dateLabel;
    private JLabel currencyLabel;

    public Demo() {
        LocalizationBundle.setBundle(Locale.UK);
        setBounds(new Rectangle(700, 500));
        MethodsPanel panel = new MethodsPanel();
        setContentPane(panel);
        JRadioButton eng = new JRadioButton(new ImageIcon("src/resources/en.png"));
        eng.setSelectedIcon(new ImageIcon("src/resources/enPressed.png"));
        eng.setSelected(true);
        JRadioButton bel = new JRadioButton(new ImageIcon("src/resources/bel.png"));
        bel.setSelectedIcon(new ImageIcon("src/resources/belPressed.png"));
        bel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationBundle.setBundle(new Locale("be", "BY"));
                update();
            }
        });
        eng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationBundle.setBundle(Locale.UK);
                update();
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(eng);
        group.add(bel);
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new FlowLayout());
        radioPanel.add(eng);
        radioPanel.add(bel);
        add(radioPanel, BorderLayout.NORTH);

        JPanel localInfoPanel = new JPanel();
        localInfoPanel.setLayout(new GridLayout(2,1));
        dateLabel = new JLabel();
        currencyLabel = new JLabel();
        localInfoPanel.add(dateLabel);
        localInfoPanel.add(currencyLabel);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1,2));
        northPanel.add(radioPanel);
        northPanel.add(localInfoPanel);

        add(northPanel, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        update();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void update() {
        this.setTitle(LocalizationBundle.getString("windowTitle"));
        dateLabel.setText(LocalizationBundle.getString("todayDate") + ": " +
               DateFormat.getDateInstance(DateFormat.SHORT, LocalizationBundle.getLastSelectedLocale()).format(new Date()));
       currencyLabel.setText(LocalizationBundle.getString("currency") + ": " +
               NumberFormat.getCurrencyInstance(LocalizationBundle.getLastSelectedLocale()).format(349.15));

    }

    public static void main(String[] args) {
        new Demo();
    }
}
