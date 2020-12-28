import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Matching {

    ArrayList<String> freeLittles;
    ArrayList<String> freeBigs;

    //matches <key: big, value: little>
    HashMap<String, ArrayList<String>> matches;

    HashMap<String, ArrayList<String>> bigsPreferences;
    HashMap<String, ArrayList<String>> littlesPreferences;

    HashMap<String, ArrayList<String>> bigsParagraphs;
    HashMap<String, ArrayList<String>> littlesParagraphs;


    public Matching(){

        freeLittles = new ArrayList<String>();
        freeBigs = new ArrayList<String>();
        matches = new HashMap<String, ArrayList<String>>();
        bigsPreferences = new  HashMap<String, ArrayList<String>>();
        littlesPreferences = new HashMap<String, ArrayList<String>>();
        bigsParagraphs = new  HashMap<String, ArrayList<String>>();
        littlesParagraphs = new HashMap<String, ArrayList<String>>();
    }


    /**
     * File reader creates the BigPreferences and LittlePreferences Hashmaps
     * This sets up the list of free bigs and littles
     */
    void prepareToMatch(){
        for (Map.Entry little: littlesPreferences.entrySet()) {
            freeLittles.add(little.getKey().toString());
        }
        for (Map.Entry big: bigsPreferences.entrySet()) {
            freeBigs.add(big.getKey().toString());
        }
    }


    /**
     * creates matches based on the given rankings
     * @throws UglyDucklingException is thrown when a little cannot be matched
     * (if each big that she ranked is happier with her current partner)
     */
    void match() throws UglyDucklingException{
        //While there are still littles who need to be matched
        while (freeLittles.size() > 0){
            //for each little

            for (Map.Entry little: littlesPreferences.entrySet()) {
                String l = little.getKey().toString();
                ArrayList<String> prefBigs = (ArrayList<String>)little.getValue();


                //if they are not yet matched
                //if they are matched... skip them
                if (!freeLittles.contains(l))
                    continue;

                //for each big in their list
                for (String b: prefBigs) {
                    //if their top is not yet taken... propose to them
                    if (freeBigs.contains(b)){
                        propose(l,b);
                        break;
                    }
                    //if their top is taken...
                    else{
                        //if big prefers this little more than their current little
                        //switch them so this little is with the big and old little is free
                        // if big prefers their current little... keep trying more bigs
                        if (likesLittleMore(b,l)){
                            String bigsCurrMatch = matches.get(b).get(0);
                            switchMatch(l, bigsCurrMatch, b);
                            break;
                        }
                    }

                    //if this is the last big on her list and the little is still free
                    //(there are no bigs who would be happier with this little... then throw exception)
                    if (prefBigs.get(prefBigs.size()-1) == b && freeLittles.contains(l) ){
                        freeLittles.remove(l);
                        throw new UglyDucklingException(l);
                    }
                }

            }

        }
    }

    /**
     * edits fields accordingly to create a match
     * @param l
     * @param b
     */
    void propose(String l, String b){
        ArrayList<String> match = new ArrayList<String>();
        match.add(l);
        matches.put(b,match);
        freeLittles.remove(l);
        freeBigs.remove(b);
    }

    /**
     * changes fields to replace an old little with a new little
     * @param newLittle
     * @param oldLittle
     * @param big
     */
    void switchMatch(String newLittle, String oldLittle, String big){

        ArrayList<String> newMatch = new ArrayList<String>();
        newMatch.add(0,newLittle);
        matches.replace(big, newMatch);

        freeLittles.remove(newLittle);
        freeLittles.add(oldLittle);
    }

    /**
     * returns a boolean to check if a big likes the input little more than her current match
     * @param b
     * @param l
     * @return
     */
    boolean likesLittleMore(String b, String l){
        String currentMatch = matches.get(b).get(0);
        ArrayList<String> thisBigsPreferences = bigsPreferences.get(b);
        int currentMatchRank = thisBigsPreferences.size();
        int potentialLittleRank = thisBigsPreferences.size();
//
//        if (thisBigsPreferences.get(0).equals(currentMatch))
//            return false;
//        if (thisBigsPreferences.get(0).equals(l))
//            return true;

        for (int i = 0; i <thisBigsPreferences.size(); i++){
            if (thisBigsPreferences.get(i).equals(currentMatch)){
                currentMatchRank = i;
            }
            if (thisBigsPreferences.get(i).equals(l)){
                potentialLittleRank = i;
            }
        }
        return potentialLittleRank < currentMatchRank;
    }



    //TODO: perhaps change this to account for the null and "" strings
    // so that there are less if statements in the table panel
    /**
     * returns what number the big ranked the little as
     * if the little was not ranked, returns "Not Ranked"
     *
     * @param b
     * @param l
     * @return
     */
    public String getBigsRanking(String b, String l) {
        String ranking = "Not Ranked";
        ArrayList<String> littles = bigsPreferences.get(b);
        for (String little: littles) {
            if (little.equals(l)){
                ranking = String.valueOf(littles.indexOf(little) + 1);
            }

        }
        return ranking;
    }


    /**
     * returns what number the little ranked the big as
     *      * if the big was not ranked, returns "Not Ranked"
     * @param b
     * @param l
     * @return
     */
    public String getLittlesRanking(String b, String l) {
        String ranking = "Not Ranked";
        ArrayList<String> bigs = littlesPreferences.get(l);
        for (String big: bigs) {
            if (big.equals(b)){
                ranking = String.valueOf(bigs.indexOf(b) + 1);
            }
        }
        return ranking;
    }

    /**
     * attempts to run the matching algorithm,
     * if exception is thrown, it removes the "unmatchable little" and tries again...
     * this will continue until valid matches are found
     * @param fr
     */
    public void attemptMatching(FileReader fr){
        try {
            fr.myMatching.match();
        }
        catch (UglyDucklingException u){
            System.out.println( u.nameOfUnmatchedLittle + " could not be matched");
            attemptMatching(fr);

        }
    }



    /**
     * this was used prior to the creation of the GUI,
     * would print the matches in the console
     */
    public void printResults(){
        System.out.println("Matches are: ");
        for (Map.Entry match: matches.entrySet()){
            String big = (String)match.getKey();
            System.out.println(big + " is matched with " + match.getValue() + ". ");
        }
    }



}
