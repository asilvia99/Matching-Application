import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


public class Application extends JFrame {
    Matching matching;
    TablePanel tablePanel;
    InfoPanel infoPanel;
    ParagraphPanel paragraphPanel;


    JFileChooser fileChooser;

    File littlefile = null;
    File bigfile = null;

    //just test for commit i guess

    double compSatisfScore;
    double currSatisfScore;



    public static void main(String[] args) throws Exception {
        Application app = new Application();

    }


    public Application() {
        super();


        openFileReader(false);
        openFileReader(true);
        inputFiles();

        this.tablePanel = new TablePanel(this.matching, this);
        this.infoPanel = new InfoPanel(this.matching);
        this.paragraphPanel = new ParagraphPanel(this.matching);

        //identifying unmatched littles must come after table is populated
        tablePanel.identifyUnmatchedLittles();
        tablePanel.identifyUnmatchedBigs();


        setLayout(new GridLayout());
        add(tablePanel);
        add(infoPanel);
        add(paragraphPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Big Little Matching");
        pack();
        setVisible(true);
        setSize(1400, 800);



    }


    /**
     *
     * @param littlefiletrue input true if the file is the little file
     *                  and false if the file is the big file
     */
    private void openFileReader(boolean littlefiletrue){

        fileChooser = new JFileChooser();
        FileNameExtensionFilter csv = new FileNameExtensionFilter("csv","csv");
        fileChooser.setFileFilter(csv);

//        add(fileChooser);


        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            if (littlefiletrue){
                littlefile = fileChooser.getSelectedFile();
            }
            else{
                bigfile = fileChooser.getSelectedFile();
            }

        }
    }

    private void inputFiles(){


        FileReader fr = new FileReader();
        try {
            fr.importer(bigfile, littlefile);
        }
        catch (Exception e){
            System.out.println("Exception thrown");
        }
        fr.myMatching.prepareToMatch();

        fr.myMatching.attemptMatching(fr);

        this.matching = fr.myMatching;
    }





}
