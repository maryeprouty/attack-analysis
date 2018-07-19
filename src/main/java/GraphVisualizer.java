/*
*
* This program visualizes the dependency and association graphs mapping tactics
* and technical impacts.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-07-17
*/

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;
import java.awt.Insets;

class GraphVisualizer extends JApplet {

    @Override
    public void init() {
        JGraphXAdapter<Vertex, DefaultEdge> dependencyAdapter = new
                JGraphXAdapter(TacticGraphBuilder.createDependencyGraph());
        JGraphXAdapter<Vertex, DefaultEdge> associationAdapter = new
                JGraphXAdapter<>(TacticGraphBuilder.createAssociationGraph());

        mxGraphComponent dependencyComponent = initComponent(dependencyAdapter);
        mxGraphComponent associationComponent = initComponent(associationAdapter);

        Container pane = getContentPane();
        JPanel mainPanel = new JPanel();
        pane.add(mainPanel);
        JPanel panel = new JPanel(new GridBagLayout());
        mainPanel.add(panel);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("Tactics to Technical Impacts"), c);
        c.gridy++;
        panel.add(associationComponent, c);
        c.gridx++;
        c.gridy--;
        panel.add(new JLabel("Tactic Dependencies"), c);
        c.gridy++;
        panel.add(dependencyComponent, c);


        mxHierarchicalLayout dependencyLayout = new mxHierarchicalLayout(dependencyAdapter);
        dependencyLayout.setOrientation(SwingConstants.WEST);
        dependencyLayout.execute(dependencyAdapter.getDefaultParent());

        mxHierarchicalLayout associationLayout = new mxHierarchicalLayout(associationAdapter);
        associationLayout.setOrientation(SwingConstants.WEST);
        associationLayout.execute(associationAdapter.getDefaultParent());
    }

    /**
     * Creates a component from a JGraphXAdapter and sets its properties.
     * @param graph The JGraphXAdapter created from the dependency or association JGrapht graph.
     * @return The Swing component illustrating the graph.
     */
    private mxGraphComponent initComponent(JGraphXAdapter<Vertex, DefaultEdge> graph) {
        mxGraphComponent component = new mxGraphComponent(graph);

        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        component.getGraph().setCellsEditable(false);
        component.getGraph().setCellsMovable(false);
        component.getGraph().setCellsResizable(false);
        component.getGraph().setEnabled(false);
        component.getGraph().getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");

        return component;
    }

}
