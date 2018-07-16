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

    private Tactic thisTactic;
    private Set<String> cweSet;
    private Set<String> cwesFromDependencies;

    /**
     * Constructor that converts tactic string to a Tactic object and finds CWEs associated with the tactic.
     * @param tacticStr A string representation of a tactic.
     */
    GraphNavigator(String tacticStr) {
        this.thisTactic = stringToTactic(tacticStr);
        this.cweSet = findCwes(thisTactic);
        this.cwesFromDependencies = findCwesFromDependencies();
    }

    /**
     * Converts a String representation of a tactic to a Tactic object.
     * @param tacticStr The string representing the tactic.
     * @return The Tactic object associated with the tactic string.
     */
    private Tactic stringToTactic(String tacticStr) {

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
     * @param myTactic The tactic associated with the CWEs.
     * @return cweSet The set of CWEs associated with this tactic.
     */
    private Set<String> findCwes(Tactic myTactic) {

        Set<String> myCweSet = new TreeSet<>();

        //Add the CWEs from each connected Technical Impact to the CWE set.
        if (myTactic != null) {
            Set<DefaultEdge> edges = associationGraph.edgesOf(myTactic);
            for (DefaultEdge edge: edges) {
                if (associationGraph.getEdgeTarget(edge) instanceof TechnicalImpact) {
                    TechnicalImpact impact = (TechnicalImpact) associationGraph.getEdgeTarget(edge);
                    myCweSet.addAll(impact.getCwes());
                }
            }
        }

        return myCweSet;
    }

    /**
     * This method uses the dependency graph to determine which tactics are dependent on the
     * input tactic by recursively adding tactics to the dependentTactics set.
     * @param myTactic The tactic being depended on.
     * @return dependentTactics The set of tactics that are dependent on the input tactic.
     */
    private Set<String> findDependentTactics(Tactic myTactic) {

        Set<String> dependentTactics = new TreeSet<>();

        //Recursively add tactics to the dependentTactics set.
        if (myTactic != null) {
            Set<DefaultEdge> edges = dependencyGraph.incomingEdgesOf(myTactic);
            for (DefaultEdge edge: edges) {
                Tactic dependentTactic = dependencyGraph.getEdgeSource(edge);
                dependentTactics.add(dependentTactic.toString());
                dependentTactics.addAll(findDependentTactics(dependentTactic));
            }

        }

        return dependentTactics;

    }

    /**
     * This method finds CWEs associated with tactics that are dependent on the main tactic.
     * @return The set of CWEs at risk through dependencies.
     */
    private Set<String> findCwesFromDependencies() {
        Set<String> cwesFromDependencies = new TreeSet<>();
        Set<String> dependentTactics = findDependentTactics(thisTactic);
        for (String dependentTacticStr: dependentTactics) {
            Tactic dependentTactic = stringToTactic(dependentTacticStr);
            cwesFromDependencies.addAll(findCwes(dependentTactic));
        }
        return cwesFromDependencies;
    }

    @Override
    public String toString() {
        String cweStr = thisTactic.toString() + ":\n\tdirectly associated with ";
        if (!cweSet.isEmpty()) {
            cweStr += "CWEs ";
        } else {
            cweStr += "no CWEs;\n";
        }

        int i = 0;
        for (String cwe: cweSet) {
            cweStr += cwe;
            if (i != cweSet.size() - 1) {
                cweStr += ", ";
            } else {
                cweStr += ";\n";
            }
            i++;
        }

        if (!cwesFromDependencies.isEmpty()) {
            cweStr += "\tdependently associated with CWEs ";
        } else {
            cweStr += "\tno CWEs from dependencies.\n";
        }

        int j = 0;
        for (String dcwe: cwesFromDependencies) {
            cweStr += dcwe;
            if (j != cwesFromDependencies.size() - 1) {
                cweStr += ", ";
            } else {
                cweStr += ".\n";
            }
            j++;
        }

        return cweStr;
    }

}
