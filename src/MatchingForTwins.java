import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchingForTwins extends Matching {

    ArrayList<TwinSet> twinPairs;
    int bigCounter;
    int maxBigsWithTwins;

    public MatchingForTwins(){
        super();
        twinPairs = new ArrayList<TwinSet>();

        //Big counter increases every time a new twin set is created
        bigCounter = 0;
        maxBigsWithTwins = super.littlesPreferences.size() - super.bigsPreferences.size();
        maxBigsWithTwins = 3;
    }



    @Override
    public void match() throws UglyDucklingException{
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
                for (String b: prefBigs){

                    //if their top is not yet taken... propose to them
                    if (freeBigs.contains(b)){
                        propose(l,b);
                        break;
                    }

                    //if their top is taken...
                    else{

                        //if not too many people have twins and this girl only has one little
                        if (bigCounter < maxBigsWithTwins && matches.get(b).size() == 1){
                            //make them twins
                            addTwin(l,b);
                            break;
                        }

                        //if there is already enough twin sets but this girl has one little
                        else if (bigCounter >= maxBigsWithTwins && matches.get(b).size() == 1){

                            // check if this twin set would be more optimal than other existing twin sets
                            //if it is
                            if (findWorstTwinSetScore() > getGroupScore(l, matches.get(b).get(0), b )){
                                //make them twins and disassemble the bad twin set
                                disassembleTwinSet(findWorstTwinSet());
                                addTwin(l,b);
                                break;
                            }
                            else{
                                //if its not

                                //check if big prefers this little more than their current little
                                //switch them so this little is with the big and old little is free
                                // if big prefers their current little... keep trying more bigs
                                if (likesLittleMore(b,l)) {
                                    String bigsCurrMatch = matches.get(b).get(0);
                                    switchMatch(l, bigsCurrMatch, b);
                                    break;
                                }
                            }
                        }

                        //if this big already has twins
                        else if (matches.get(b).size() == 2){

                            //check if this little would be a more optimal twin
                            //if yes, replace her as one of the twins,
                            TwinSet currentTwinSet = null;
                            for (TwinSet t: twinPairs){
                                if (t.big.equals(b)){
                                    currentTwinSet = t;
                                }
                            }
                            int currentLittle1Score = currentTwinSet.l1Score;
                            int currentLittle2Score = currentTwinSet.l2Score;
                            int thisLittleScore = getIndividualScore(l,b);

                            //if this little is better than one of the current twins
                            if (thisLittleScore < currentLittle1Score || thisLittleScore<currentLittle2Score){
                                disassembleTwinSet(currentTwinSet);
                                addTwin(l, b);
                                break;
                            }
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

    public void addTwin(String l, String b){
        String origTwin = matches.get(b).get(0);
        matches.get(b).add(l);
        twinPairs.add(new TwinSet(origTwin, l, b, getGroupScore(origTwin, l, b), getIndividualScore(origTwin, b), getIndividualScore(l, b)));
        freeLittles.remove(l);
        bigCounter++;
    }

    public int getGroupScore(String l1, String l2, String b) {
        int little1Score = getIndividualScore(l1,b);
        int little2Score = getIndividualScore(l2, b);

        return little1Score + little2Score;
    }


    /**
     * Returns the sum of what the big ranked the little, and what the little ranked the big
     * If they both ranked each other first, score will be zero
     * Lower score = like each other more
     * @param l
     * @param b
     * @return
     */
    public int getIndividualScore(String l, String b){

        ArrayList<String> thisBigsPreferences = bigsPreferences.get(b);
        ArrayList<String> lPreferences = littlesPreferences.get(l);

        /* We want to initialize the values to be the size of the list
        In this scenario a lower score means a better match...

        If the little is not on the bigs list, she won't be found and this value will not be replaced
        In that scenario, you want her value to be "last place"... not 0 (which would indicate a first choice)
         */

        //How the big ranked the little
        int littleRank = thisBigsPreferences.size();

        //How the little ranked the big
        int bigRank = lPreferences.size();


        //get ranking of the little in the big's list
        for (int i = 0; i < thisBigsPreferences.size(); i++) {
            if (thisBigsPreferences.get(i).equals(l)) {
                littleRank = i;
            }
        }

        //get ranking of the big in the little's list
        for (int i = 0; i < lPreferences.size(); i++) {
            if (lPreferences.get(i).equals(b)) {
                bigRank = i;
            }
        }

        return littleRank + bigRank ;
    }


    public int findWorstTwinSetScore(){
        int maxScore=0;
        for (TwinSet t: twinPairs){
            if (t.groupScore > maxScore){
                maxScore = t.groupScore;
            }
        }
        return maxScore;
    }

    public TwinSet findWorstTwinSet(){
        int worstScore = findWorstTwinSetScore();
        TwinSet worstSet = null;
        for (TwinSet t: twinPairs){
            if (t.groupScore == worstScore){
                worstSet = t;
            }
        }
        return worstSet;
    }


    /**
     * This removes the least wanted little from a twinset
     * @param t
     */
    public void disassembleTwinSet(TwinSet t){
        // if l1 is more unwanted than l2
        if (t.l1Score > t.l2Score){
            removeATwin(t,t.little1);
        }
        //if l2 is more unwanted than l1
        else if (t.l2Score > t.l1Score){
            removeATwin(t,t.little2);
        }
        else if (t.l2Score == t.l1Score){
            int l1RankOfBig = bigsPreferences.size();
            int l2RankOfBig = bigsPreferences.size();
            ArrayList<String> l1Ranks = littlesPreferences.get(t.little1);
            ArrayList<String> l2Ranks = littlesPreferences.get(t.little2);
            for (int i = 0; i < l1Ranks.size(); i++) {
                if (l1Ranks.get(i).equals(t.big)) {
                    l1RankOfBig = i;
                }
            }
            for (int i = 0; i < l2Ranks.size(); i++) {
                if (l2Ranks.get(i).equals(t.big)) {
                    l2RankOfBig = i;
                }
            }
            // if little 1 wants the big more
            if (l1RankOfBig < l2RankOfBig){
                removeATwin(t,t.little2);
            }
            // if little 2 wants the big more
            else if (l2RankOfBig < l1RankOfBig){
                removeATwin(t,t.little1);
            }

            /*
            We don't need to have an extra step for if each little ranked the big the same because...
            if they did, the l1Score and l2Score would be different and would depend on the bigs rankings of each little
            This would have been done earlier when we compared l1 scores to l2 scores
             */
        }
    }

    public void removeATwin(TwinSet t, String l){
        if (l.equals(t.little1)){
            freeLittles.add(t.little1);
            matches.get(t.big).remove(t.little1);
            twinPairs.remove(t);
        }
        else if (l.equals(t.little2)){
            freeLittles.add(t.little2);
            matches.get(t.big).remove(t.little2);
            twinPairs.remove(t);
        }
        else {
            System.out.println("Error occurred while trying to remove a twin");
        }
    }




    @Override
    public void switchMatch(String newLittle, String oldLittle, String big){
        ArrayList<String> newMatch = new ArrayList<String>();
        newMatch.add(0,newLittle);
        matches.replace(big, newMatch);
        freeLittles.remove(newLittle);
        freeLittles.add(oldLittle);
    }

    @Override
    boolean likesLittleMore(String b, String l){
        String currentMatch = matches.get(b).get(0);
        ArrayList<String> thisBigsPreferences = bigsPreferences.get(b);
        int currentMatchRank = bigsPreferences.size();
        int potentialLittleRank = bigsPreferences.size();

        if (thisBigsPreferences.get(0).equals(currentMatch))
            return false;
        if (thisBigsPreferences.get(0).equals(l))
            return true;

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


    @Override
    public void printResults(){
        System.out.println("Matches are: ");
        for (Map.Entry match: matches.entrySet()){
            String big = (String)match.getKey();
            System.out.println(big + " is matched with " + match.getValue() + ". ");
        }
    }


}
