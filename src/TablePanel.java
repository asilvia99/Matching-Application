import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class TablePanel extends JPanel{
    /**
     * An updated list of the littles who are listed on the table
     */
    ArrayList<String> tablelittles;
    /**
     * An updated list of the bigs who are paired with a little on the table
     */
    ArrayList<String> tableBigs;
    JTable table;
    Matching matching;

    /**
     * This is used to ensure that the table listener is only called when the user interacts with the table
     * Not when the table is updated from a function
     */
    boolean isHumanEvent = true;


    /**
     * Column numbers are assigned as member variables so that if more columns needed to be added in the future the columns can be moved around easily
     */
    int bigCol = 0;
    int lil1Col = 1;
    int lilRank1Col = 2;
    int bigRank1Col = 3;
    int lil2Col = 4;
    int lilRank2Col = 5;
    int bigRank2Col = 6;


    Application application;

    public TablePanel(Matching matching, Application application){
        super();
        this.application = application;
        this.matching = matching;
        this.tablelittles = new ArrayList<String>();
        initPanel();
        setActionListeners();
    }


    /**
     * Sets up tablemodel, preferred sizes, column headers etc
     * Fills the table with matching data
     */
    private void initPanel(){
        setBackground(Color.decode("#D9E4E8"));

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Big");
        model.addColumn("Little 1");
        model.addColumn("Little 1's ranking of Big");
        model.addColumn("Big's ranking of Little 1");
        model.addColumn("Little 2");
        model.addColumn("Little 2's ranking of Big");
        model.addColumn("Big's ranking of Little 2");
        model.addColumn("Satisfaction Score");
        model.setNumRows(matching.bigsPreferences.size());
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(110);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(30);
        table.getColumnModel().getColumn(6).setPreferredWidth(30);
        table.getColumnModel().getColumn(7).setPreferredWidth(30);


        table.setRowHeight(20);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(700,700));
        add(pane);
        pane.setAlignmentX(100);

        JTableHeader header  = table.getTableHeader();
        header.setDefaultRenderer(new TextAreaRenderer());
        header.setPreferredSize(new Dimension(700,50));


        add(table.getTableHeader());
        setPreferredSize(new Dimension(900,800));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20,20,20,20));

        populateTable();
    }


    /**
     * Defines the detailed logic for when the user interacts with the table
     * The user can only edit the little columns
     *
     * Possible outcomes:
     * If a valid little is typed in, the littles and scores are updated
     * If an invalid little is entered, an error box will appear
     * If a little is entered that is already on the table, an error box will appear
     */
    private void setActionListeners(){

        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (!isHumanEvent) {
                    return;
                }
                int col = e.getColumn();
                int row = e.getLastRow();

                //the little columns are the only editable columns...
                //if any other column is clicked ignore the action
//                if (col != lil1Col || col!= lil2Col){
//                    return;
//                }

                String val = table.getValueAt(row, col).toString();

                //this will remove the girl who was just removed from the list of table littles
                updateTableLittles();
                identifyUnmatchedLittles();
                identifyUnmatchedBigs();


                //if no value is entered, set scores to zero
                if (val.equals("")) {
                    isHumanEvent = false;
                    table.setValueAt("", row, col + 1);
                    table.setValueAt("", row, col + 2);
                    isHumanEvent = true;
                }
                //If this is not a valid little.. spit an error popup window
                //TODO: make this an actual error window
                else if (!matching.littlesPreferences.containsKey(val)) {
                    JFrame popup = new JFrame();
                    JPanel popupPanel = new JPanel();
                    JLabel popupError = new JLabel("This little was not found, please enter a valid little");
                    popupPanel.add(popupError);
                    popup.add(popupPanel);
                    popup.setTitle("Error");
                    popup.pack();
                    popup.setVisible(true);
                    popup.setSize(350, 200);

                    isHumanEvent = false;
                    table.setValueAt("", row, col + 1);
                    table.setValueAt("", row, col + 2);
                    isHumanEvent = true;
                }
                //if they entered a little who was already on the table, give an error box
                //TODO: make this an actual error box with buttons saying "do you want to remove ___ from ___?"
                else if (duplicateLittlesExist(val)) {
                    Frame popup = new JFrame();
                    JPanel popupPanel = new JPanel();
                    JTextArea popupError = new JTextArea(val + " now has two bigs: " + table.getValueAt(row, bigCol) + " and " + findBig(val, row) + " \nPlease remove her from one of these bigs.");
                    popupError.setLineWrap(true);
                    popupError.setPreferredSize(new Dimension(300, 200));
                    popupPanel.add(popupError);
                    popup.add(popupPanel);
                    popup.setTitle("Error");
                    popup.pack();
                    popup.setVisible(true);
                    popup.setSize(350, 200);
                    fillRowLittleRanking(row);
                    fillRowBigRanking(row);

                }
                //if they put in a free little, update scores and remove her from free littles
                else if (matching.freeLittles.contains(val)) {
                    matching.freeLittles.remove(val);
                    identifyUnmatchedLittles();
                    fillRowLittleRanking(row);
                    fillRowBigRanking(row);

                }
            }
        });
    }


    /**
     * Sets the text for the bigs name and the computed littles name for each row
     * Function is called upon startup in the populateTable function
     *
     * parameters are used in case more columns are added to the table in the future
     * @param bigCol
     * @param lil1Col
     * @param lil2Col
     * @param beginRow
     */
    private void fillWithMatches(int bigCol, int lil1Col, int lil2Col, int beginRow) {
        int row = beginRow;
        for (String big : matching.freeBigs) {
            matching.matches.put(big, null);
        }

        TreeMap<String, ArrayList<String>> sortedmatches = new TreeMap<>();
        sortedmatches.putAll(matching.matches);
        for (Map.Entry match : sortedmatches.entrySet()) {
            String big = match.getKey().toString();
            ArrayList<String> littles = (ArrayList<String>) match.getValue();

            table.setValueAt(big, row, bigCol);
            if (littles != null) {
                table.setValueAt(littles.get(0), row, lil1Col);
                tablelittles.add(littles.get(0));
                if (littles.size() > 1) {
                    table.setValueAt(littles.get(1), row, lil2Col);
                    tablelittles.add(littles.get(1));
                }
            }

            row++;
        }
    }


    /**
     * fills all rows with the ranking of what the big ranked the little
     */
    public void fillBigsRankings() {
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, bigCol) != null) {
                fillRowBigRanking(i);
            }
        }
    }


    /**
     * fills a single row with what the big ranked the little
     * if row is empty, score will be empty
     * if big did not rank the little, text will be set to "Not Ranked"
     * @param row
     */
    public void fillRowBigRanking(int row) {
        isHumanEvent = false;
        String big = table.getValueAt(row, bigCol).toString();

        if (table.getValueAt(row, lil1Col) != null && !table.getValueAt(row, lil1Col).toString().equals("")) {
            String little1 = table.getValueAt(row, lil1Col).toString();
            //TODO: input the initial string into the getBigsRanking and have that function handle null and "" values
            String ranking1 = String.valueOf(matching.getBigsRanking(big, little1));

            table.setValueAt(ranking1, row, bigRank1Col);
        }
        if (table.getValueAt(row, lil2Col) != null && !table.getValueAt(row, lil2Col).toString().equals("")) {
            String little2 = table.getValueAt(row, lil2Col).toString();
            String ranking2 = String.valueOf(matching.getBigsRanking(big, little2));

            table.setValueAt(ranking2, row, bigRank2Col);
        }
        isHumanEvent = true;
    }


    /**
     * fills all rows with the ranking of what the little ranked the big
     */
    public void fillLittleRankings() {
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, bigCol) != null) {
                fillRowLittleRanking(i);
            }
        }

    }

    /**
     * fills a single row with what the little ranked the big
     * if row is empty, score will be empty
     * if little did not rank the big, text will be set to "Not Ranked"
     * @param row
     */
    public void fillRowLittleRanking(int row) {
        isHumanEvent = false;
        String big = table.getValueAt(row, bigCol).toString();

        if (table.getValueAt(row, lil1Col) != null && !table.getValueAt(row, lil1Col).toString().equals("")) {
            String little1 = table.getValueAt(row, lil1Col).toString();
            String ranking1 = matching.getLittlesRanking(big, little1);

            table.setValueAt(ranking1, row, lilRank1Col);
        }
        if (table.getValueAt(row, lil2Col) != null && !table.getValueAt(row, lil2Col).toString().equals("")) {
            String little2 = table.getValueAt(row, lil2Col).toString();
            String ranking2 = matching.getLittlesRanking(big, little2);

            table.setValueAt(ranking2, row, lilRank2Col);
        }
        isHumanEvent = true;
    }

//
//   I think this was used for satisfaction scores
//
//    public int parseScore(int row, int col) {
//        if (table.getValueAt(row, lilRank1Col).equals("") || table.getValueAt(row, lilRank1Col) == null) {
//            return 0;
//        }
//        if (table.getValueAt(row, col).equals("Not Ranked")) {
//            return 0;
//        }
//        else {
//            return Integer.parseInt(table.getValueAt(row, lilRank1Col).toString());
//        }
//
//    }

    /**
     * Makes Big and Ranking Columns uneditable
     * calls the functions that populate the table with matches and rankings
     */
    public void populateTable() {


        //makes all columns except the little columns uneditable
        JTextField f = new JTextField();
        f.setEditable(false);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            if (i == lil1Col || i == lil2Col) {
                continue;
            }
            table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(f));
        }

        //populates table with matches and rankings
        fillWithMatches(bigCol, lil1Col, lil2Col, 0);
        fillBigsRankings();
        fillLittleRankings();

    }



    //TODO: make this return the list so you can send it to the infoPanel??

    //TODO: this seems super inefficient and not necessary... try to just compare the table littles list to the littles list and if they're not on the table littles list then add them to free
    public void identifyUnmatchedLittles(){
        for (Map.Entry little: matching.littlesPreferences.entrySet()){
            String l = little.getKey().toString();
            boolean free = true;
            for (int i = 0; i <table.getRowCount(); i++){
                String lil1 = null;
                String lil2 = null;
                if (table.getValueAt(i,lil1Col) != null){
                    lil1 = table.getValueAt(i, lil1Col).toString();
                }
                if (table.getValueAt(i,lil2Col) != null) {
                    lil2 = table.getValueAt(i, lil2Col).toString();
                }
                if (lil1 != null && lil1.equals(l)){
                    free = false;
                    break;
                }
                if (lil2 != null && lil2.equals(l)){
                    free = false;
                    break;
                }
            }
            if (free == true && !matching.freeLittles.contains(l)){
                matching.freeLittles.add(l);
            }
        }

        application.infoPanel.unmatchedLilsTextField.populateUnmatched(matching.freeLittles);
    }


    public void identifyUnmatchedBigs(){
        boolean free;
        for (int i = 0; i <table.getRowCount(); i++){
            String b = table.getValueAt(i, bigCol).toString();

            if (table.getValueAt(i,lil1Col) != null || table.getValueAt(i, lil2Col) != null){
                if (matching.littlesPreferences.containsKey(table.getValueAt(i,lil1Col).toString()) || matching.littlesPreferences.containsKey(table.getValueAt(i, lil2Col))) {

                    free = false;
                    if (matching.freeBigs.contains(b)) {
                        matching.freeBigs.remove(b);
                    }
                }
                else{
                    if (!matching.freeBigs.contains(b)) {
                        matching.freeBigs.add(b);
                    }
                }
            }
            else {
                free = true;
                if (!matching.freeBigs.contains(b)) {
                    matching.freeBigs.add(b);
                }

            }

        }
        application.infoPanel.unmatchedBigsTextField.populateUnmatched(matching.freeBigs);
    }


    /**
     * This function iterates through the little columns and populates the
     * "tablelittles" list with the names in each column
     *
     * The table littles list is an up-to-date list that tracks the names currently in the little columns
     */
    public void updateTableLittles(){
        ArrayList<String> updatedTableLittles = new ArrayList<String>();

        for (int i = 0; i<table.getRowCount(); i++) {
            if (table.getValueAt(i,lil1Col) != null && !table.getValueAt(i,lil1Col).toString().equals("")){
                String lilInCol1 = table.getValueAt(i, lil1Col).toString();
                updatedTableLittles.add(lilInCol1);
            }
            if (table.getValueAt(i,lil2Col) != null && !table.getValueAt(i,lil2Col).toString().equals("")) {
                String lilInCol2 = table.getValueAt(i, lil2Col).toString();
                updatedTableLittles.add(lilInCol2);
            }
        }
        this.tablelittles = updatedTableLittles;
    }

    /**
     * Checks if an inputted little is on the table twice
     * This is called when a new little is typed into the table
     *
     * @param little
     * @return
     */
    public boolean duplicateLittlesExist(String little){
        int count = 0;
        for (String l: tablelittles){
            if (l.equals(little)){
                count ++;
            }
        }
        if (count > 1){
            return true;
        }
        return false;
    }


    /**
     * Used when there is a little found twice in the table,
     * this will find the big for that little, other than the one in the given row
     * @param little
     * @param row
     * @return
     */
    public String findBig(String little, int row){
        for (int i = 0; i<table.getRowCount(); i++){
            if (i == row){
                continue;
            }
            if (table.getValueAt(i,lil1Col) != null && table.getValueAt(i, lil1Col).toString().equals(little))
                return (table.getValueAt(i,bigCol).toString());
            if (table.getValueAt(i,lil2Col) != null && table.getValueAt(i, lil2Col).toString().equals(little))
                return (table.getValueAt(i,bigCol).toString());

        }
        return "error finding other big";
    }

}
