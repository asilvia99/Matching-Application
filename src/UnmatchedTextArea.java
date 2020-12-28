import javax.lang.model.type.ArrayType;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UnmatchedTextArea extends JTextArea {

    public UnmatchedTextArea(){
        super();

        setPreferredSize(new Dimension(200,50));
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
    }

    public void populateUnmatched(ArrayList<String> freeList){
        String unmatched = " ";
        for (String fl : freeList){
            unmatched = unmatched.concat("\n" + fl);
        }
        setText("Unmatched littles: " + unmatched);
        setPreferredSize(new Dimension(200,20*freeList.size()));
    }
}
