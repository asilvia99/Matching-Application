import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class TextAreaRenderer extends JTextArea implements TableCellRenderer {

    public TextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((String)obj);
        return this;
    }
}
