/*
*
* This class serves to navigate the association and dependency graphs constructed
* by the TacticGraphBuilder in order to determine which CWEs are at risk given the
* highest ranked tactic for a simulated attack vector.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-07-11
*/

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Set;
import java.util.TreeSet;

class GraphNavigator {

    private static Graph<Vertex, DefaultEdge> associationGraph = TacticGraphBuilder.createAssociationGraph();
    private static SimpleDirectedGraph<Tactic, DefaultEdge> dependencyGraph = TacticGraphBuilder.createDependencyGraph();


    /**
     * Converts a String representation of a tactic to a Tactic object.
     * @param tacticStr The string representing the tactic.
     * @return The Tactic object associated with the tactic string.
     */
    private static Tactic stringToTactic(String tacticStr) {

        for (Tactic v: TacticGraphBuilder.tacticVertices) {
            if (v.toString().equals(tacticStr)) {
                return v;
            }
        }

        return null;

    }

    /**
     * This method finds the CWEs associated with a tactic by looking at which Technical Impacts
     * it's mapped to in the association graph.
     * @param tacticStr The String representing the tactic.
     * @return cweSet The set of CWEs associated with this tactic.
     */
    static Set<String> findCwes(String tacticStr) {

        Set<String> cweSet = new TreeSet<>();

        //Using the tactic string, find the vertex representing this tactic in the association graph.
        Tactic myTactic = stringToTactic(tacticStr);

        //Add the CWEs from each connected Technical Impact to the CWE set.
        if (myTactic != null) {
            Set<DefaultEdge> edges = associationGraph.edgesOf(myTactic);
            for (DefaultEdge edge: edges) {
                if (associationGraph.getEdgeTarget(edge) instanceof TechnicalImpact) {
                    TechnicalImpact impact = (TechnicalImpact) associationGraph.getEdgeTarget(edge);
                    cweSet.addAll(impact.getCwes());
                }
            }
        }

        return cweSet;
    }

    /**
     * This method uses the dependency graph to determine which tactics are dependent on the
     * input tactic by recursively adding tactics to the dependentTactics set.
     * @param tacticStr The String representing the tactic.
     * @return dependentTactics The set of tactics that are dependent on the input tactic.
     */
    static Set<String> findDependentTactics(String tacticStr) {

        Set<String> dependentTactics = new TreeSet<>();

        //Using the tactic string, find the vertex representing this tactic in the dependency graph.
        Tactic myTactic = stringToTactic(tacticStr);

        //Recursively add tactics to the dependentTactics set.
        if (myTactic != null) {
            Set<DefaultEdge> edges = dependencyGraph.incomingEdgesOf(myTactic);
            for (DefaultEdge edge: edges) {
                String dependentTactic = dependencyGraph.getEdgeSource(edge).toString();
                dependentTactics.add(dependentTactic);
                dependentTactics.addAll(findDependentTactics(dependentTactic));
            }
            
        }

        return dependentTactics;

    }

}
