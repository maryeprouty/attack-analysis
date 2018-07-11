/*
*
* This program contains the main method for analyzing attack behaviors.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-06-25
*/

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.Map;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Set;

public class AttackAnalysis {

    private static ArrayList<RankCalculator> rankers = new ArrayList<>();

    /**
    * The main method interprets the data from two csv files of tactics and attacks in
    * order to determine which tactic each attack most closely resembles using either the
    * Jaccard Similarity Index, ratio with weighted zeros, or the percent of techniques an
    * attack uses out of those in a given tactic. The two files should be supplied by the
    * user when running the program with the tactics file first followed by the attack file,
    * and the third argument should be one of three ranking systems: jsi, weighted,
    * techniques, or all. Without the third argument, the default is jsi. The output is written
    * to a text file with the name of the ranking system.
    * ie. java AttackAnalysis tactics.csv attackvectors.csv weighted
    * @param args args[0] is the tactics file, args[1] is the attack file, and args[2]
    * is the optional ranking method.
    */
    public static void main(String[] args) {

        //Parse command-line arguments to ensure proper formatting with csv files
        //for comparison.
        if (args.length < 2) {
            System.err.println("Please input two csv files");
            System.exit(1);
        } else if (!args[0].contains(".csv") || !args[1].contains(".csv")) {
            System.err.println("Please only use csv files");
            System.exit(1);
        }

        //Instantiate new RankCalculator(s) with the rank system specified as the third
        //argument on the command-line. If there is no third argument, use default jsi.
        if (args.length >= 3) {
            if (RankCalculator.systems.contains(args[2])) {
                rankers.add(new RankCalculator(args[2]));
            } else if ("all".equals(args[2])) {
                for (String system: RankCalculator.systems) {
                    rankers.add(new RankCalculator(system));
                }
            }
        } else {
            rankers.add(new RankCalculator());
        }

        //Convert csv files from command-line into comparable data structures.
        ArrayList<ArrayList<String>> tacticsList = DataInterpreter.dataFileToList(args[0]);
        Map<String, ArrayList<String>> tacticsMap = DataInterpreter.listToMap(tacticsList);

        ArrayList<ArrayList<String>> attackList = DataInterpreter.dataFileToList(args[1]);

        //Using the specified ranking system(s), rank the tactics for each attack
        //in the attackList.
        for (RankCalculator ranker: rankers) {

            //Compare tactics and attack vectors to distinguish which tactic each
            //attack most closely matches according to the JSI/ranking value and
            //write the results to the appropriate file.
            ranker.rankTactics(tacticsMap, attackList);
            String myStats = ranker.toString();

            ArrayList<AttackStat> highestRankedTactics = ranker.getHighestRankedTactics();
            for (AttackStat stat: highestRankedTactics) {
                Set<String> cweSet = GraphNavigator.findCwes(stat.getTactic());
                System.out.println(cweSet);
            }


            try (PrintWriter out = new PrintWriter(ranker.rankSystem + ".txt")) {
                out.println(myStats);
            } catch (FileNotFoundException e) {
                System.err.println("File not found");
            }
        }

        //Build association and dependency graphs to determine which CWEs are at risk given a certain
        //attack vector.
        //Graph<Vertex, DefaultEdge> associationGraph = TacticGraphBuilder.createAssociationGraph();
        //SimpleDirectedGraph<Tactic, DefaultEdge> dependencyGraph = TacticGraphBuilder.createDependencyGraph();
        //System.out.println(associationGraph.edgesOf(TacticGraphBuilder.p));
        //System.out.println(dependencyGraph.incomingEdgesOf(TacticGraphBuilder.ia));


    }

}
