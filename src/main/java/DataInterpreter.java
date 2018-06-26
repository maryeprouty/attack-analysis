/**
* This class serves to manipulate data from csv files into data structures
* that can be used to compute the Jaccard Similarity Index for tactics vs.
* attack vectors.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-06-19
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class DataInterpreter {

    /**
    * This method reads in a data file in csv format and parses it by comma to create
    * a list of bit vectors.
    * @param filename Specifies which file is being read.
    * @return tactics Returns the list of bit vectors.
    */
    public static ArrayList<ArrayList<String>> dataFileToList(String filename) {

        BufferedReader inputReader = null;
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
        String fileStr = "src/main/resources/" + filename;

        try {
            File inFile = new File(fileStr);
            inputReader = new BufferedReader(new FileReader(inFile));
            String dataStr = inputReader.readLine();
            int index = 0;

            //For each line of the csv file, create a list of bit vectors.
            while (dataStr != null) {
                if (index > 0) {
                    String[] preVector = dataStr.split(",");
                    ArrayList<String> vector = new ArrayList<String>(Arrays.asList(preVector));
                    dataList.add(vector);
                }
                dataStr = inputReader.readLine();
                index++;

            }
            inputReader.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + fileStr);
        } catch (IOException ex) {
            System.err.println("Error reading " + fileStr);
        }

        return dataList;

    }

    /**
    * This method converts an ArrayList to a Map so a classification attribute can be
    * mapped to a bit vector, ie. tactic->technique vector.
    * @param list Specifies which list is being converted to a map.
    * @return map Returns the map of the attributes to vectors.
    */
    public static Map<String, ArrayList<String>> listToMap(ArrayList<ArrayList<String>> list) {

        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        for (ArrayList<String> innerList: list) {
            String tactic = innerList.remove(innerList.size() - 1);
            map.put(tactic, innerList);
        }

        return map;

    }


}
