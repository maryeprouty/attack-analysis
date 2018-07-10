import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by maryprouty on 7/10/18.
 */

//Move param for tactics to constructor/variable
class TacticGraphBuilder {

    private static Map<String, ArrayList<String>> tactics;

    static void setTactics(Map<String, ArrayList<String>> tacticsMap) {
        tactics = tacticsMap;
    }

    //For now, not including any dependencies - just the associations from the object diagram
    static Graph<String, DefaultEdge> createGraph() {

        Graph<String, DefaultEdge> g = new SimpleGraph(DefaultEdge.class);

        for (String tactic: tactics.keySet()) {
            g.addVertex(tactic);
        }

        g.addVertex("Execute unauthorized code or commands");
        g.addVertex("Gain privileges/assume identity");
        g.addVertex("Read data");
        g.addVertex("Modify data");
        g.addVertex("DoS: unreliable execution");
        g.addVertex("DoS: resource consumption");
        g.addVertex("Bypass protection mechanism");
        g.addVertex("Hide activities");


        g.addEdge("Initial Access", "Bypass protection mechanism");
        g.addEdge("Execution", "Execute unauthorized code or commands");
        g.addEdge("Execution", "DoS: unreliable execution");
        g.addEdge("Execution", "DoS: resource consumption");
        g.addEdge("Persistence", "Gain privileges/assume identity");
        g.addEdge("Persistence", "Bypass protection mechanism");
        g.addEdge("Privilege Escalation", "Gain privileges/assume identity");
        g.addEdge("Lateral Movement", "DoS: resource consumption");
        g.addEdge("Defense Evasion", "Bypass protection mechanism");
        g.addEdge("Defense Evasion", "Hide activities");
        g.addEdge("Credential Access", "Gain privileges/assume identity");
        g.addEdge("Collection", "Read data");
        g.addEdge("Exfiltration", "Modify data");
        g.addEdge("Command and Control", "Hide activities");


        return g;

    }

}
