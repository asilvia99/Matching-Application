import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


    double compSatisfScore;
    double currSatisfScore;


    public Application(Matching matching) {
        super();
        this.matching = matching;

        this.tablePanel = new TablePanel(this.matching, this);
        this.infoPanel = new InfoPanel(this.matching);
        this.paragraphPanel = new ParagraphPanel(this.matching);

//
//        computeCompSatisfScore();
//        computeCurrSatisfScore();
//

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










}
