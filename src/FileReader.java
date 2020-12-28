import java.io.*;
import java.util.*;

// "C:\\Users\\allis\\Documents\\WPI 2019-2020\\BigRankings.csv"
public class FileReader {

    public Matching myMatching;

    //TODO: adjust file reader to be able to specify which columns are which... if someone didn't follow my column setup the app should still work

    /**
     *  This importer should take in two CSV files "BigRankings" and "Little Rankings"
     *
     *  I replaced all commas in the excel sheet to "|" and replaced all new lines with "++" in order to properly parse the csv
     *
     *  This will only work if column setup is as followed
     *  Big Rankings has big name in column 1 and the variable number of their prefered littles and paragraphs in the following columns
     *  Example:
     *  Col1: Big name
     *  Col2: 1st choice for little
     *  Col3: Why they want that little
     *  Col4: 2nd choice for little
     *  Col5: why they want that little
     *  etc...
     *
     *  ^ same with LittleRankings where little is in column 1 and ranked bigs follow.
     * @throws Exception for invalid File in scanner object
     */
    public void importer(File bigfile, File littlefile) throws Exception {


        //Turns bigFile into a list "bigData", a list of long strings for each Big entry
        //each of these entries will later be separated
//        Scanner scBig = new Scanner(new File("C:\\Users\\allis\\Documents\\bigfile.csv"));
        Scanner scBig = new Scanner(bigfile);
        scBig.useDelimiter("\\n");
        ArrayList<String> bigData = new ArrayList<>();
        while (scBig.hasNext()) {
            bigData.add(scBig.next());
        }

        //Turns littleFile into list "littleData", a list of long strings for each Little entry
        //each of these entries will later be separated
//        Scanner scLittle = new Scanner(new File("C:\\Users\\allis\\Documents\\littlefile.csv"));
        Scanner scLittle = new Scanner(littlefile);
        scLittle.useDelimiter("\\n");
        ArrayList<String> littleData = new ArrayList<>();
        while (scLittle.hasNext()) {
            littleData.add(scLittle.next());
        }

        //If there are more littles, create a MatchingForTwins object
        //Otherwise, create a regular matching object
        if (littleData.size() > bigData.size()) {
            myMatching = new MatchingForTwins();
        }
        else {
            myMatching = new Matching();
        }


        //Populate the big field in the Matching object with bigData
        for (String s : bigData) {
            List<String> rowdata = Arrays.asList(s.split(","));

            //first column of the file will be the name of the big
            String big = rowdata.get(0).trim();

            //the other columns will be the name of the little they ranked,
            //followed by the paragraph they wrote about them
            //the following two loops separate this data into the appropriate objects

            //adds name of preferred littles to list
            ArrayList<String> littles = new ArrayList<>();
            for (int i = 1; i < rowdata.size(); i=i+2) {
                String str = rowdata.get(i).trim();
                if (!str.isEmpty()) {
                    littles.add(str);
                }
            }
            //add paragraphs to PG list
            ArrayList<String> littlePGs = new ArrayList<>();
            for (int i = 2; i < rowdata.size(); i=i+2) {
                String str = rowdata.get(i).trim();
                if (!str.isEmpty()) {
                    str = str.replace("|", ",");
                    littlePGs.add(str);
                }
            }

            myMatching.bigsParagraphs.put(big, littlePGs);
            myMatching.bigsPreferences.put(big, littles);
        }

        //populate the little field in the Matching object with littleData
        for (String s: littleData){
            List<String> rowdata = Arrays.asList(s.split(","));

            //first column will be the name of the little
            String little = rowdata.get(0).trim();

            //the other columns will be the name of the bigs they ranked,
            //followed by the paragraph they wrote about them
            //the following two loops separate this data into the appropriate objects

            //adds name of preferred bigs to list
            ArrayList<String> bigs = new ArrayList<>();
            for (int i = 1; i<rowdata.size(); i = i+2){
                String str = rowdata.get(i).trim();

                if (!str.isEmpty()){
                    bigs.add(str);
                }
            }
            //add paragraphs to PG list
            ArrayList<String> littlePGs = new ArrayList<>();
            for (int i = 2; i < rowdata.size(); i=i+2) {
                String str = rowdata.get(i).trim();
                if (!str.isEmpty()) {
                    str = str.replace("|", ",");
                    str = str.replace("++", "\n");
                    littlePGs.add(str);
                }
            }

            myMatching.bigsParagraphs.put(little, littlePGs);
            myMatching.littlesPreferences.put(little, bigs );
        }


    }


//
//    public static void main(String[] args) throws Exception {
//        FileReader fr = new FileReader();
//        try {
//            fr.importer();
//        }
//        catch (Exception e){
//            System.out.println("Exception thrown");
//        }
//        fr.myMatching.prepareToMatch();
//
//        fr.myMatching.attemptMatching(fr);
//
////        fr.myMatching.printResults();
//        new Application((fr.myMatching) );
//
//
//    }
//

}
