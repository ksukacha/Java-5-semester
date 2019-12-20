import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class MethodExecutionDialog extends JDialog {

    public MethodExecutionDialog(Method m, boolean modal){
        super((Frame) null, modal);
        setTitle(LocalizationBundle.getString("dialogTitle"));
        setBounds(new Rectangle(300,300));
        int numberOfFields = m.getParameterCount();
        setLayout(new GridLayout(numberOfFields+2, 2));
        String parameterType, parameter;
        JTextField tf;
        List<JTextField> textFields = new ArrayList<>();
        for(int i = 0; i<numberOfFields; ++i){
            parameterType = m.getParameterTypes()[i].getName();
            parameter = m.getParameters()[i].getName();
            add(new JLabel(parameterType+" "+parameter));
            tf = new JTextField();
            textFields.add(tf);
            add(tf);
        }
        add(new JLabel());
        JButton button = new JButton(LocalizationBundle.getString("execute"));
        JLabel answer = new JLabel();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] params = new Object[textFields.size()];
                    for (int i = 0; i < textFields.size(); ++i) {
                        switch (m.getParameterTypes()[i].getName()) {
                            case "int":
                                params[i] = Integer.parseInt(textFields.get(i).getText().trim());
                                break;
                            case "double":
                                params[i] = Double.parseDouble(textFields.get(i).getText().trim());
                                break;
                            case "short":
                                params[i] = Short.parseShort(textFields.get(i).getText().trim());
                                break;
                            case "long":
                                params[i] = Long.parseLong(textFields.get(i).getText().trim());
                                break;
                            case "float":
                                params[i] = Float.parseFloat(textFields.get(i).getText().trim());
                                break;
                            default:
                                params[i] = textFields.get(i).getText().trim();
                                break;
                        }
                    }
                    answer.setText(m.invoke(null, params).toString());
                }catch(NumberFormatException| java.lang.IllegalAccessException|java.lang.reflect.InvocationTargetException ex){

                    Object[] options = {LocalizationBundle.getString("ok")};
                    JOptionPane.showOptionDialog(MethodExecutionDialog.this,//parent container of JOptionPane
                            ex.toString(), LocalizationBundle.getString("error"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,//do not use a custom Icon
                            options,//the titles of buttons
                    options[0]);//default button title
                }
            }
        });
        add(button);
        add(new JLabel(LocalizationBundle.getString("result")));
        add(answer);
        setLocationRelativeTo(null);
        setVisible(true);

    }

}
