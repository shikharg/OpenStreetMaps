package project.projectfiles;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Keeps track of focus between the two panes via a HashTable
public class FocusManager extends JTabbedPane implements ChangeListener, PropertyChangeListener{
    private Hashtable tabFocus;

    public FocusManager() {
        tabFocus = new Hashtable();
        addChangeListener(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(this);
    }
    
    //PropertyChangeListener stores the focus of the last pane so we can switch back to it later
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("permanentFocusOwner".equals(e.getPropertyName())) {
            final Object value = e.getNewValue();
            if (value != null) {
                tabFocus.put(getTitleAt(getSelectedIndex()), value);
            }
        }

    }

    //ChangeListener to figure out when user switches panes, gets focus from the other pane 
    //and puts it in the current pane
    @Override
    public void stateChanged(ChangeEvent e) {
        Object value = tabFocus.get(getTitleAt(getSelectedIndex()));
        if (value != null) {
            ((Component) value).requestFocusInWindow();
        }
    }
}
