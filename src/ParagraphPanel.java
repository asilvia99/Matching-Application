import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ParagraphPanel extends JPanel {

    Matching matching;

    JLabel lilPGLabel;
    JComboBox<String> lilPGBox;
    JComboBox<String> lilsBigsBox;
    JTextArea lilPGAnswerLabel;

    JLabel bigPGLabel;
    JComboBox<String> bigPGBox;
    JComboBox<String> bigsLilBox;
    JTextArea bigPGAnswerLabel;

    public ParagraphPanel(Matching matching){
        super();

        setBackground(Color.decode("#D9E4E8"));

        this.matching = matching;
        lilPGLabel = new JLabel("Select a little to see what she wrote:");
        lilPGLabel.setPreferredSize(new Dimension(300,20));
        lilPGBox = new JComboBox<>();
        lilPGBox.setPreferredSize(new Dimension(300,30));
        lilPGBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        lilsBigsBox = new JComboBox<>();
        lilsBigsBox.setPreferredSize(new Dimension(300,30));
        lilPGAnswerLabel = new JTextArea();
        lilPGAnswerLabel.setLineWrap(true);
        lilPGAnswerLabel.setPreferredSize(new Dimension(300,270));
        lilPGAnswerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane lilPGscroll = new JScrollPane(lilPGAnswerLabel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);




        bigPGLabel = new JLabel("Select a big to see what she wrote");
        bigPGLabel.setPreferredSize(new Dimension(300,20));
        bigPGBox = new JComboBox<>();
        bigPGBox.setPreferredSize(new Dimension(300,30));
        bigsLilBox = new JComboBox<>();
        bigsLilBox.setPreferredSize(new Dimension(300,30));
        bigPGAnswerLabel = new JTextArea();
        bigPGAnswerLabel.setLineWrap(true);
        bigPGAnswerLabel.setPreferredSize(new Dimension(300,270));
        bigPGAnswerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane bigPGscroll = new JScrollPane(bigPGAnswerLabel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        setLayout((new FlowLayout()));
        add(lilPGLabel);
        add(lilPGBox);
        add(lilsBigsBox);
        add(lilPGscroll);
        add(bigPGLabel);
        add(bigPGBox);
        add(bigsLilBox);
        add(bigPGscroll);

        populateBigsComboBox(bigPGBox);
        populateLittlesComboBox(lilPGBox);

        setPreferredSize(new Dimension(400,500));


        lilPGBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lilsBigsBox.removeAllItems();
                JComboBox littlebox = (JComboBox) e.getSource();
                Object selected = littlebox.getSelectedItem();
                ArrayList<String> prefBigs = matching.littlesPreferences.get(selected);
                for (String theirBig: prefBigs){
                    lilsBigsBox.addItem(theirBig);
                }

            }
        });

        lilsBigsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox littlebox = (JComboBox) e.getSource();
                Object selectedbig = littlebox.getSelectedItem();
                String little = lilPGBox.getSelectedItem().toString();
                if (littlebox.getItemCount() > 0) {
                    int index = littlebox.getSelectedIndex();
                    lilPGAnswerLabel.setText(matching.bigsParagraphs.get(little).get(index));
                }
            }
        });
        bigPGBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bigsLilBox.removeAllItems();
                JComboBox bigPGbox = (JComboBox) e.getSource();
                Object selected = bigPGbox.getSelectedItem();
                ArrayList<String> preflils = matching.bigsPreferences.get(selected);
                for (String theirlil: preflils){
                    bigsLilBox.addItem(theirlil);
                }

            }
        });

        bigsLilBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox bigbox = (JComboBox) e.getSource();
                Object selectedlittle = bigbox.getSelectedItem();
                String big = bigPGBox.getSelectedItem().toString();
                if (bigbox.getItemCount() > 0) {
                    int index = bigbox.getSelectedIndex();
                    bigPGAnswerLabel.setText(matching.bigsParagraphs.get(big).get(index));
                }

            }
        });
    }


    void populateLittlesComboBox(JComboBox box){
        TreeMap<String, ArrayList<String>> sortedlittles = new TreeMap<>();
        sortedlittles.putAll(matching.littlesPreferences);
        for (Map.Entry lilprefs: sortedlittles.entrySet()){
            String littleName = lilprefs.getKey().toString();
            box.addItem(littleName);
        }
    }
    void populateBigsComboBox(JComboBox box) {
        TreeMap<String, ArrayList<String>> sortedbigs = new TreeMap<>();
        sortedbigs.putAll(matching.bigsPreferences);
        for (Map.Entry bigprefs : sortedbigs.entrySet()) {
            String bigName = bigprefs.getKey().toString();
            box.addItem(bigName);
        }
    }
}