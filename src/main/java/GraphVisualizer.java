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
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraphView;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.util.mxPoint;

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

import java.util.ArrayList;
import java.util.Arrays;


class GraphVisualizer extends JApplet {

    @Override
    public void init() {

        //Convert JGraphT graphs to JGraphX graphs for visualization
        JGraphXAdapter<Vertex, DefaultEdge> dependencyAdapter = new
                JGraphXAdapter(TacticGraphBuilder.createDependencyGraph());
        JGraphXAdapter<Vertex, DefaultEdge> associationAdapter = new
                JGraphXAdapter<>(TacticGraphBuilder.createAssociationGraph());

        //Initialize Swing components to add to UI
        mxGraphComponent dependencyComponent = initComponent(dependencyAdapter);
        mxGraphComponent associationComponent = initComponent(associationAdapter);

        //Format components in a GridBagLayout to illustrate graphs side by side
        Container pane = getContentPane();
        JPanel mainPanel = new JPanel();
        pane.add(mainPanel);
        JPanel panel = new JPanel(new GridBagLayout());
        mainPanel.add(panel);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 20, 5, 20);
        panel.add(new JLabel("Tactics to Technical Impacts"), c);
        c.gridy++;
        panel.add(associationComponent, c);
        c.gridx++;
        c.gridy--;
        panel.add(new JLabel("Tactic Dependencies"), c);
        c.gridy++;
        panel.add(dependencyComponent, c);

        //Format the layout of each graph to be hierarchical with a west orientation
        mxHierarchicalLayout dependencyLayout = new mxHierarchicalLayout(dependencyAdapter);
        dependencyLayout.setOrientation(SwingConstants.WEST);
        dependencyLayout.setIntraCellSpacing(20);
        dependencyLayout.setInterHierarchySpacing(30);
        dependencyLayout.execute(dependencyAdapter.getDefaultParent());

        mxHierarchicalLayout associationLayout = new mxHierarchicalLayout(associationAdapter);
        associationLayout.setOrientation(SwingConstants.WEST);
        associationLayout.setIntraCellSpacing(20);
        associationLayout.setInterHierarchySpacing(30);
        associationLayout.execute(associationAdapter.getDefaultParent());
    }

    /**
     * Creates a component from a JGraphXAdapter and sets its graph's properties.
     * @param graph The JGraphXAdapter created from the dependency or association JGrapht graph.
     * @return The Swing component illustrating the graph.
     */
    private mxGraphComponent initComponent(JGraphXAdapter<Vertex, DefaultEdge> graph) {

        ArrayList<String> impacts = new ArrayList<>(Arrays.asList("Execute unauthorized code or commands",
                "Gain privileges/assume identity", "Read data", "Modify data", "DoS: unreliable execution",
                "DoS: resource consumption", "Bypass protection mechanism", "Hide activities"));

        mxGraphComponent component = new mxGraphComponent(graph);
        component.setConnectable(false);

        //Disable the user from editing the graphs
        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setCellsMovable(false);
        graph.setCellsResizable(false);
        graph.setEnabled(false);

        //Stylize the font, color, etc for vertices and edges
        mxStylesheet style = graph.getStylesheet();
        style.getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        style.getDefaultEdgeStyle().put(mxConstants.STYLE_STROKECOLOR, "black");

        style.getDefaultVertexStyle().put(mxConstants.STYLE_FONTSIZE, 11);
        style.getDefaultVertexStyle().put(mxConstants.STYLE_FONTCOLOR, "black");
        style.getDefaultVertexStyle().put(mxConstants.STYLE_FONTFAMILY, "Times New Roman");
        style.getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.getDefaultVertexStyle().put(mxConstants.STYLE_FILLCOLOR, "pink");
        style.getDefaultVertexStyle().put(mxConstants.STYLE_STROKECOLOR, "black");
        style.getDefaultVertexStyle().put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);

        //Change size of vertices to fully encase label
        Object[] cells = graph.getChildVertices(graph.getDefaultParent());
        for (Object c: cells) {
            mxCell cell = (mxCell) c;
            mxGeometry geo = cell.getGeometry();

            geo.setHeight(30);
            if (impacts.contains(cell.getValue().toString())) {
                geo.setWidth(225);
            } else {
                geo.setWidth(125);
            }
        }

        //Center the graph within a larger border
        graph.setBorder(40);
        mxGraphView view = graph.getView();
        view.setTranslate(new mxPoint(25, 25));

        return component;
    }

}
