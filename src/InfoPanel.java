import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class InfoPanel extends JPanel{

    Matching matching;

    JButton button;
    JLabel scoresLabel;
    UnmatchedTextArea unmatchedLilsTextField;
    UnmatchedTextArea unmatchedBigsTextField;


    JLabel littlesRanksTitleLabel;
    JComboBox<String> littlesRankingsBox;
    JTextArea littlesRankingsResultsLabel;

    JLabel bigsRanksTitleLabel;
    JComboBox<String> bigsRankingsBox;
    JTextArea bigsRankingsResultsLabel;

    JLabel whoRanksLittleTitleLabel;
    JComboBox<String> whoRanksLittleBox;
    JTextArea whoRanksLittleResultsLabel;

    JLabel whoRanksBigTitleLabel;
    JComboBox<String> whoRanksBigBox;
    JTextArea whoRanksBigResultsLabel;

    public InfoPanel(Matching matching){
        super();
        this.matching = matching;
        initPanel();
        fillPanel();
        initActionListeners();


    }

    /**
     * initalizes items on the panel and adds them to the panel
     */
    private void initPanel(){
        setBackground(Color.decode("#D9E4E8"));
        unmatchedLilsTextField = new UnmatchedTextArea("littles", new JTextArea());
        unmatchedBigsTextField = new UnmatchedTextArea("bigs", new JTextArea());

        scoresLabel = new JLabel();

        littlesRanksTitleLabel = new JLabel("Select a little to see her preferences:");
        littlesRanksTitleLabel.setPreferredSize(new Dimension(400,20));
        littlesRankingsBox = new JComboBox<>();
        littlesRankingsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        littlesRankingsResultsLabel = new JTextArea();


        bigsRanksTitleLabel = new JLabel("Select a big to see her preferences:");
        bigsRanksTitleLabel.setPreferredSize(new Dimension(400,20));
        bigsRankingsBox = new JComboBox<>();
        bigsRankingsResultsLabel = new JTextArea();

        whoRanksLittleTitleLabel = new JLabel ("Select a Little to see who ranked her:");
        whoRanksLittleTitleLabel.setPreferredSize(new Dimension(400,20));
        whoRanksLittleBox = new JComboBox<String>();
        whoRanksLittleResultsLabel = new JTextArea();

        whoRanksBigTitleLabel = new JLabel ("Select a Big to see who ranked her:");
        whoRanksBigTitleLabel.setPreferredSize(new Dimension(400,20));
        whoRanksBigBox = new JComboBox<String>();
        whoRanksBigResultsLabel = new JTextArea();

//        got rid of this button for now so I could improve its functionality
//        button = new JButton("Click to display computed matches");
//        button.setPreferredSize(new Dimension(300,20));

        setLayout(new FlowLayout());
//        add(button);
        add(scoresLabel);
        add(unmatchedLilsTextField);
        add(unmatchedBigsTextField);
        add(littlesRanksTitleLabel);
        add(littlesRankingsBox);
        add(littlesRankingsResultsLabel);
        add(bigsRanksTitleLabel);
        add(bigsRankingsBox);
        add(bigsRankingsResultsLabel);
        add(whoRanksLittleTitleLabel);
        add(whoRanksLittleBox);
        add(whoRanksLittleResultsLabel);
        add(whoRanksBigTitleLabel);
        add(whoRanksBigBox);
        add(whoRanksBigResultsLabel);
        setPreferredSize(new Dimension(500,700));
    }

    /**
     * populates comboboxes with options
     */
    private void fillPanel(){
        populateLittlesComboBox(littlesRankingsBox);
        populateLittlesComboBox(whoRanksLittleBox);
        populateBigsComboBox(bigsRankingsBox);
        populateBigsComboBox(whoRanksBigBox);

    }

    /**
     * sets action listeners for all items on the panel
     */
    private void initActionListeners(){

//        button.addActionListener(new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            createMatchesFrame();
//        }
//    });

        littlesRankingsBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox littlebox = (JComboBox) e.getSource();
            Object selected = littlebox.getSelectedItem();
            if (selected.toString().equals("Clear")){
                setLinedText(littlesRankingsResultsLabel, new ArrayList<String>());
                return;
            }
            ArrayList<String> prefBigs = matching.littlesPreferences.get(selected);
//                infoPanel.littlesRankingsResultsLabel.setText(prefBigs.toString());
            setLinedText(littlesRankingsResultsLabel, prefBigs);


        }
    });
        bigsRankingsBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox bigbox = (JComboBox) e.getSource();
            Object selected = bigbox.getSelectedItem();
            if (selected.toString().equals("Clear")){
                setLinedText(bigsRankingsResultsLabel, new ArrayList<String>());
                return;
            }
            ArrayList<String> prefLittles = matching.bigsPreferences.get(selected);
//                infoPanel.bigsRankingsResultsLabel.setText(prefLittles.toString());
            setLinedText(bigsRankingsResultsLabel, prefLittles);

        }
    });

        whoRanksLittleBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox littlebox = (JComboBox) e.getSource();
            Object selected = littlebox.getSelectedItem();
            String selectedLittle = selected.toString();
            if (selected.toString().equals("Clear")){
                setLinedText(whoRanksLittleResultsLabel, new ArrayList<String>());
                return;
            }
            findBigsWhoRankedLittle(selectedLittle);

        }
    });
        whoRanksBigBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox bigbox = (JComboBox) e.getSource();
            Object selected = bigbox.getSelectedItem();
            String selectedBig = selected.toString();
            if (selected.toString().equals("Clear")){
                setLinedText(whoRanksBigResultsLabel, new ArrayList<String>());
                return;
            }
            findLittlesWhoRankedBig(selectedBig);

        }
    });
    }



    //TODO: Maybe put this logic in matching instead and do the text setting here with the returned array

    /**
     * finds out which bigs ranks a certain little, puts that list in the respective text area
     * @param selectedLittle
     */
    public void findBigsWhoRankedLittle(String selectedLittle){
        ArrayList<String> bigsWhoWant = new ArrayList<>();
        for (Map.Entry big: matching.bigsPreferences.entrySet()){
            String aBig = big.getKey().toString();
            ArrayList<String> prefLittles = matching.bigsPreferences.get(aBig);
            if (prefLittles.contains(selectedLittle)){
                bigsWhoWant.add(aBig);
            }
        }
        whoRanksLittleResultsLabel.setText(bigsWhoWant.toString());
        setLinedText(whoRanksLittleResultsLabel, bigsWhoWant);

    }

    /**
     * finds out which littles ranks a certain big, puts that list in the respective text area
     */
    public void findLittlesWhoRankedBig(String selectedBig){
        ArrayList<String> littlesWhoWant = new ArrayList<>();
        for (Map.Entry little: matching.littlesPreferences.entrySet()){
            String aLittle = little.getKey().toString();
            ArrayList<String> prefBigs = matching.littlesPreferences.get(aLittle);
            if (prefBigs.contains(selectedBig)){
                littlesWhoWant.add(aLittle);
            }
        }
        setLinedText(whoRanksBigResultsLabel, littlesWhoWant);
    }


    //TODO: fix this to make the size more accurate (just google it im sure theres a solution)

    /**
     * Sets the text area to a certain list and adjusts the size of the text area depending on the size of the list
     * Size varies a bit and needs to be adjusted
     * @param textbox
     * @param ppltolist
     */
    public void setLinedText(JTextArea textbox, ArrayList<String> ppltolist){
        String list = " ";
        for (String fl : ppltolist){
            list = list.concat( fl + "\n");
        }
        textbox.setText(list);
        textbox.setBorder(BorderFactory.createEmptyBorder(3, 5, 5, 5));
        textbox.setPreferredSize(new Dimension(400,7+ 17*ppltolist.size()));
    }


    /**
     * Adds options to the littles combo box- including a clear option
     * @param box
     */
    void populateLittlesComboBox(JComboBox box) {
        box.addItem("Clear");
        TreeMap<String, ArrayList<String>> sortedlittles = new TreeMap<>();
        sortedlittles.putAll(matching.littlesPreferences);
        for (Map.Entry lilprefs : sortedlittles.entrySet()) {
            String littleName = lilprefs.getKey().toString();
            box.addItem(littleName);
        }
    }


    /**
     * Adds options to the bigs combo box- including a clear option
     * @param box
     */
    public void populateBigsComboBox(JComboBox box){
        box.addItem("Clear");
        TreeMap<String, ArrayList<String>> sortedbigs = new TreeMap<>();
        sortedbigs.putAll(matching.bigsPreferences);
        for (Map.Entry bigprefs: sortedbigs.entrySet()){
            String bigName = bigprefs.getKey().toString();
            box.addItem(bigName);
        }
    }


    //TODO: maybe make this its own class and fix it so you compare the new matches with the originals?

    /**
     * hitting a button should open a new window that allows you to compare the computer made matches to the handmade matches
     */
    public void createMatchesFrame() {
        int numBigs = this.matching.bigsPreferences.size();
        JTable origMatchesTable = new JTable(numBigs + 1, 3);
        JScrollPane pane = new JScrollPane(origMatchesTable);
        pane.setPreferredSize(new Dimension(500, 700));
        JFrame matchFrame = new JFrame("Matches");
        JPanel matchPanel = new JPanel();

        origMatchesTable.setRowHeight(30);
        origMatchesTable.setValueAt("Big", 0, 0);
        origMatchesTable.setValueAt("Little 1", 0, 1);
        origMatchesTable.setValueAt("Little 2", 0, 2);
        origMatchesTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        origMatchesTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        origMatchesTable.getColumnModel().getColumn(2).setPreferredWidth(70);

        matchPanel.setBorder(BorderFactory.createBevelBorder(2));
        matchPanel.setLayout((new FlowLayout()));
        matchPanel.add(pane);
        matchFrame.add(matchPanel, BorderLayout.CENTER);
        matchFrame.setTitle("Big Little Matching");
        matchFrame.pack();
        matchFrame.setVisible(true);
        matchFrame.setSize(700, 700);
        matchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    }
}
