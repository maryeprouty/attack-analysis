/**
* This program computes the Jaccard Similarity Index (JSI), or another statistic of
* likelihood, for an attack vector and each tactic vector in the ATT&CK matrix as
* specified on attack.mitre.org. These values are then ranked to illustrate which
* tactics are the most similar to a given attack vector.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-06-25
*/

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class RankCalculator {

    public ArrayList<ArrayList<AttackStat>> stats = new ArrayList<ArrayList<AttackStat>>();
    public static ArrayList<String> systems = new ArrayList<String>(Arrays.asList("jsi",
        "weighted", "techniques"));
    public String rankSystem;

    /**
    * No-arg constructor makes jsi the default ranking method.
    */
    public RankCalculator() {
        this("jsi");
    }

    /**
    * The constructor for a RankCalculator, providing the method of ranking.
    * @param rankSystem Specifies which of jsi, weighted, or techniques should be
    * utilized to rank tactics.
    */
    public RankCalculator(String rankSystem) {
        this.rankSystem = rankSystem;
    }

    /**
    * This method computes the Jaccard Similarity Index with the equation
    * JS(A,B) = (A intersects B)/(A union B) * 100, or a weighted index involving 0s,
    * along with the percent uncertainty based on the count of ?s in an attack vector.
    * This method also counts the number of techniques an attack employs out of the
    * techniques in a tactic.
    * @param tactic Specifies the tactic vector being compared.
    * @param attack Specifies the attack vector being compared.
    * @return stat Returns the AttackStat representing the stat for a comparison
    * between the tactic and attack vector.
    */
    public AttackStat computeStat(ArrayList<String> tactic, ArrayList<String> attack) {

        Double jSIndex = 0.0;
        Double percentOfTechniques = 0.0;
        int intersection = 0;
        int union = 0;
        int tacticOnes = 0;
        int uncertaintyCount = 0;
        int uncertaintyCountOnes = 0;

        Double weightedJSI = 0.0;
        Double weightedIntersection = 0.0;
        Double weightedUnion = 0.0;

        AttackStat myStat;

        //Ensure that vector format for comparison is correct.
        if (tactic.size() != attack.size()) {
            System.err.println("Attack vector is formatted incorrectly");
            myStat = new AttackStat();
        } else {
            //Compare the "bits" in each tactic and attack vector to compute
            //the stats for this pair of vectors.
            for (int i = 0; i < tactic.size(); i++) {
                String tacticBit = tactic.get(i);
                String attackBit = attack.get(i);
                if (tacticBit.equals("1") && attackBit.equals("1")) {
                    tacticOnes++;
                    intersection++;
                    union++;
                    weightedIntersection++;
                    weightedUnion++;
                } else if (tacticBit.equals("1") && attackBit.equals("0")) {
                    tacticOnes++;
                    union++;
                    weightedUnion++;
                } else if (tacticBit.equals("1") && attackBit.equals("?")) {
                    tacticOnes++;
                    uncertaintyCount++;
                    uncertaintyCountOnes++;
                    //union++; //To include this as part of union or not to???
                    //Clem says to ignore question marks for union.
                } else if (tacticBit.equals("0") && attackBit.equals("?")) {
                    uncertaintyCount++;
                } else if (tacticBit.equals("0") && attackBit.equals("1")) {
                    union++;
                    weightedUnion++;
                } else if (tacticBit.equals("0") && attackBit.equals("0")) {
                    weightedIntersection += 0.1;
                    weightedUnion += 0.1;
                } else {
                    System.err.println("Attack bit has invalid value");
                }
            }

            //Compute stats and add them to the stat instance
            jSIndex = ((double) intersection / (double) union) * 100;
            weightedJSI = (weightedIntersection / weightedUnion) * 100;
            percentOfTechniques = ((double) intersection / (double) tacticOnes) * 100;
            //double percentUncertainty = ((double) uncertaintyCount / (double) tactic.size()) * 100;
            Double percentUncertainty = ((double) uncertaintyCountOnes / (double) tacticOnes) * 100;
            String techniques = Integer.toString(intersection) + "/" + Integer.toString(tacticOnes);

            if (rankSystem.equals("techniques")) {
                myStat = new AttackStat(percentOfTechniques, percentUncertainty, techniques);
            } else if (rankSystem.equals("weighted")) {
                myStat = new AttackStat(weightedJSI, percentUncertainty, techniques);
            } else {
                myStat = new AttackStat(jSIndex, percentUncertainty, techniques);
            }


        }

        return myStat;

    }


    /**
    * This method computes the stats for each tactic-attack vector pair and ranks
    * the tactics by the rankSystem for each attack vector.
    * @param tactics Specifies the map of tactics to compare each attack to.
    * @param attacks Specifies the list of attack vectors being compared to tactics.
    */
    public void rankTactics(Map<String, ArrayList<String>> tactics,
        ArrayList<ArrayList<String>> attacks) {

        for (ArrayList<String> attack: attacks) {

            //For each attack vector in the attack file, compute the stats for
            //all tactics.
            ArrayList<AttackStat> thisAttackStats = new ArrayList<AttackStat>();

            for (Map.Entry<String, ArrayList<String>> entry: tactics.entrySet()) {
                String tactic = entry.getKey();
                ArrayList<String> tacticVector = entry.getValue();
                AttackStat stat = computeStat(tacticVector, attack);
                stat.setTactic(tactic);
                thisAttackStats.add(stat);

            }

            //Rank each tactic for a given attack vector according to its rank value.
            Collections.sort(thisAttackStats);
            stats.add(thisAttackStats);
        }
    }


    @Override
    public String toString() {

        int statsIndex = 1;
        String statsString = "";

           for (ArrayList<AttackStat> attackStats: stats) {
              statsString += "\nAttack " + Integer.toString(statsIndex) + ".\n\t";
              for (AttackStat stat: attackStats) {
                  statsString += stat.toString();
              }
              statsIndex++;
              statsString += "\n";
           }

        return statsString;

    }



}
