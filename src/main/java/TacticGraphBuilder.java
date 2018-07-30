/*
*
* This class serves to build the association and dependency graphs connecting attack tactics
* to technical impacts from CWEs that are detectable through static analysis.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-07-11
*/

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import com.mxgraph.model.mxCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class TacticGraphBuilder {

    //The attack tactics from attack.mitre.org that are being mapped to technical impacts.
    private static Tactic ia = new Tactic("Initial Access");
    private static Tactic xc = new Tactic("Execution");
    private static Tactic lm = new Tactic("Lateral Movement");
    private static Tactic p = new Tactic("Persistence");
    private static Tactic pe = new Tactic("Privilege Escalation");
    private static Tactic de = new Tactic("Defense Evasion");
    private static Tactic ca = new Tactic("Credential Access");
    private static Tactic xf = new Tactic("Exfiltration");
    private static Tactic c = new Tactic("Collection");
    private static Tactic ds = new Tactic("Discovery");
    private static Tactic cc = new Tactic("Command and Control");

    static ArrayList<Tactic> tacticVertices = new ArrayList<>(Arrays.asList(ia, xc, lm, p,
            pe, de, ca, xf, c, ds, cc));

    //The technical impacts from CWEs being mapped to the attack tactics above.
    private static TechnicalImpact unauth = new TechnicalImpact("Execute unauthorized code or commands",
            new ArrayList<>(Arrays.asList("78", "79", "98", "120", "129", "131", "134", "190",
                    "426", "798", "805")));
    private static TechnicalImpact gain = new TechnicalImpact("Gain privileges/assume identity", new
            ArrayList<>(Arrays.asList("306", "352", "426", "601", "798", "807")));
    private static TechnicalImpact read = new TechnicalImpact("Read data", new ArrayList<>(Arrays.asList(
            "78", "79", "89", "129", "131", "134", "352", "426", "798")));
    private static TechnicalImpact modify = new TechnicalImpact("Modify data", new ArrayList<>(
            Arrays.asList("78", "89", "129", "131", "190", "352")));
    private static TechnicalImpact dosEx = new TechnicalImpact("DoS: unreliable execution", new
            ArrayList<>(Arrays.asList("78", "120", "129", "131", "190", "352", "400",
                    "426", "805")));
    private static TechnicalImpact dosRsrc = new TechnicalImpact("DoS: resource consumption", new
           ArrayList<>(Arrays.asList("120", "190", "400", "770", "805")));
    private static TechnicalImpact bypass = new TechnicalImpact("Bypass protection mechanism", new
           ArrayList<>(Arrays.asList("79", "89", "190", "352", "400", "601", "798", "807")));
    private static TechnicalImpact hide = new TechnicalImpact("Hide activities", new ArrayList<>(
           Collections.singletonList("78")));

    private static ArrayList<TechnicalImpact> impactVertices = new ArrayList<>(Arrays.asList(unauth, gain,
            read, modify, dosEx, dosRsrc, bypass, hide));


    /**
     * This method creates the association graph, mapping attack tactics to technical
     * impacts.
     * @return g The graph mapping tactics to technical impacts.
     */
    static Graph<Vertex, DefaultEdge> createAssociationGraph() {

        Graph<Vertex, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        g.addVertex(ia);
        g.addVertex(xc);
        g.addVertex(lm);
        g.addVertex(p);
        g.addVertex(pe);
        g.addVertex(de);
        g.addVertex(ca);
        g.addVertex(xf);
        g.addVertex(c);
        g.addVertex(ds);
        g.addVertex(cc);

        g.addVertex(unauth);
        g.addVertex(gain);
        g.addVertex(read);
        g.addVertex(modify);
        g.addVertex(dosEx);
        g.addVertex(dosRsrc);
        g.addVertex(bypass);
        g.addVertex(hide);


        g.addEdge(ia, bypass);
        g.addEdge(xc, unauth);
        g.addEdge(xc, dosEx);
        g.addEdge(xc, dosRsrc);
        g.addEdge(p, bypass);
        g.addEdge(pe, gain);
        g.addEdge(lm, dosRsrc);
        g.addEdge(de, bypass);
        g.addEdge(de, hide);
        g.addEdge(ca, gain);
        g.addEdge(c, read);
        g.addEdge(xf, modify);
        g.addEdge(cc, hide);


        return g;

    }

    /**
     * This method creates the dependency graph, illustrating how some attack tactics
     * depend on one another. The direction of the edges represents tactic A depending on
     * tactic B.
     * @return d The dependency graph mapping tactics to other tactics.
     */
    static SimpleDirectedGraph<Tactic, DefaultEdge> createDependencyGraph() {

        SimpleDirectedGraph<Tactic, DefaultEdge> d = new SimpleDirectedGraph<>(DefaultEdge.class);

        d.addVertex(ia);
        d.addVertex(xc);
        d.addVertex(lm);
        d.addVertex(p);
        d.addVertex(pe);
        d.addVertex(de);
        d.addVertex(ca);
        d.addVertex(xf);
        d.addVertex(c);
        d.addVertex(ds);
        d.addVertex(cc);

        d.addEdge(c, ds);
        d.addEdge(xf, c);
        d.addEdge(xc, ia);
        d.addEdge(xc, lm);
        d.addEdge(pe, lm);

        d.addEdge(p, ca);
        d.addEdge(cc, ds);


        return d;

    }

    /**
     * This method converts an mxCell to a Technical Impact.
     * @param cell The mxgraph cell being considered or selected.
     * @return The Technical Impact associated with the cell, or null if it's another type of Vertex.
     */
    static TechnicalImpact cellToImpact(mxCell cell) {
        for (TechnicalImpact impact: impactVertices) {
            if (impact.toString().equals(cell.getValue().toString())) {
                return impact;
            }
        }
        return null;
    }

}
