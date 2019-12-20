import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodsPanel extends JPanel {
    private DefaultListModel<String> methodsModel;

    public MethodsPanel(){
        setPreferredSize(new Dimension(400,600));
        setLayout(new BorderLayout());
        methodsModel = new DefaultListModel<>();
        Method[] methods = Math.class.getMethods();
        for(Method m: methods){
            if((m.getModifiers() & Modifier.STATIC) != 0) {
                methodsModel.addElement(m.toString());
            }
        }
        JList<String> methodsList = new JList<>(methodsModel);
        methodsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2){
                    int index = methodsList.getSelectedIndex();
                    MethodExecutionDialog jDialog = new MethodExecutionDialog(methods[index], true);
                }
            }
        });

        add(new JScrollPane(methodsList,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    }
}
