import javax.lang.model.type.ArrayType;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UnmatchedTextArea extends JTextArea {

    String littleorbigs;

    public UnmatchedTextArea(String littleorbigs){
        super();

        this.littleorbigs = littleorbigs;
        setPreferredSize(new Dimension(200,150));
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
    }

    public void populateUnmatched(ArrayList<String> freeList){
        String unmatched = " ";
        for (String fl : freeList){
            unmatched = unmatched.concat("\n" + fl);
        }
        setText("Unmatched "+littleorbigs + ": " + unmatched);


        //TODO: Adjust the size of the box depending on the number of free bigs/littles
//        setPreferredSize(new Dimension(200,20*freeList.size()));
    }
}
