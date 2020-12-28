//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class TableData {
//    ArrayList<String> tableBigs;
//    ArrayList<String> tableLittles;
//
//    /**
//     * Initializes the tabledata with the originally computed matches
//     * @param matches
//     */
//    public TableData(HashMap<String, ArrayList<String>> matches){
//        this.tableBigs = new ArrayList<>();
//        this.tableLittles = new ArrayList<>();
//
//        for (Map.Entry match: matches.entrySet()) {
//            String big = match.getKey().toString();
//            tableBigs.add(big);
//            if (match.getValue() != null) {
//                ArrayList<String> little = (ArrayList<String>) match.getValue();
//                String little1 = little.get(0);
//                tableLittles.add(little1);
//                if (little.size() > 1) {
//                    String little2 = little.get(1);
//                    tableLittles.add(little2);
//                }
//            }
//        }
//    }
//}
