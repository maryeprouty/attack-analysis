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
import com.mxgraph.util.mxHtmlColor;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;


class GraphVisualizer extends JApplet {

    private Color powderblue = new Color(185, 230, 236);
    private Color darkpowder = new Color(140, 200, 206);
    private Color beige = new Color(250, 250, 245);
    private Color darkbeige = new Color(230, 230, 220);

    @Override
    public void init() {

        //Convert JGraphT graphs to JGraphX graphs for visualization
        JGraphXAdapter<? extends Vertex, DefaultEdge> dependencyAdapter = new
                JGraphXAdapter<>(TacticGraphBuilder.createDependencyGraph());
        JGraphXAdapter<? extends Vertex, DefaultEdge> associationAdapter = new
                JGraphXAdapter<>(TacticGraphBuilder.createAssociationGraph());

        //Initialize Swing components to add to UI
        mxGraphComponent dependencyComponent = initComponent(dependencyAdapter);
        mxGraphComponent associationComponent = initComponent(associationAdapter);

        //Format components in a GridBagLayout to illustrate graphs side by side
        Container pane = getContentPane();
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(darkbeige);
        pane.add(mainPanel);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(darkbeige);
        mainPanel.add(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 20, 5, 20);
        JLabel mapLabel = new JLabel("Tactics to Technical Impacts");
        mapLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        panel.add(mapLabel, c);
        c.gridy++;
        panel.add(associationComponent, c);
        c.gridx++;
        c.gridy--;
        JLabel depLabel = new JLabel("Tactic Dependencies");
        depLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        panel.add(depLabel, c);
        c.gridy++;
        panel.add(dependencyComponent, c);

        //Illustrate the color coding system and give references to mitre
        JPanel colorBackground = new JPanel();
        colorBackground.setBackground(beige);
        JPanel colorCode = new JPanel(new GridBagLayout());
        colorCode.setBackground(beige);
        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx = 0;
        cc.gridy = 0;
        cc.insets = new Insets(5, 5, 5, 5);
        JPanel atPanel = new JPanel();
        atPanel.setBackground(darkpowder);
        atPanel.add(new JLabel("\t\t"));
        colorCode.add(atPanel, cc);
        cc.gridy++;
        JPanel tiPanel = new JPanel();
        tiPanel.setBackground(powderblue);
        tiPanel.add(new JLabel("\t\t"));
        colorCode.add(tiPanel, cc);
        cc.gridx++;
        cc.gridy--;
        JLabel atLabel = new JLabel("Attack Tactic");
        Font atFont = new Font("Times New Roman", Font.BOLD, 14);
        atLabel.setFont(atFont);
        colorCode.add(atLabel, cc);
        cc.gridy++;
        JLabel tiLabel = new JLabel("Technical Impact");
        tiLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        colorCode.add(tiLabel, cc);
        cc.gridx++;
        colorCode.add(new JLabel("https://cwe.mitre.org/community/swa/detection_methods.html"), cc);
        cc.gridy--;
        colorCode.add(new JLabel("https://attack.mitre.org/wiki/Main_Page"), cc);
        c.gridy++;
        colorBackground.add(colorCode);
        panel.add(colorBackground, c);


    }

    /**
     * Creates a component from a JGraphXAdapter and sets its graph's properties.
     *
     * @param graph The JGraphXAdapter created from the dependency or association JGrapht graph.
     * @return The Swing component illustrating the graph.
     */
    private mxGraphComponent initComponent(JGraphXAdapter<? extends Vertex, DefaultEdge> graph) {

        final mxGraphComponent component = new mxGraphComponent(graph);
        component.setConnectable(false);
        component.getViewport().setBackground(beige);

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
        style.getDefaultVertexStyle().put(mxConstants.STYLE_FILLCOLOR, mxHtmlColor.hexString(darkpowder));
        style.getDefaultVertexStyle().put(mxConstants.STYLE_STROKECOLOR, "black");
        style.getDefaultVertexStyle().put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);

        //Change size of vertices to fully encase label and determine which cells are Technical Impacts
        Object[] cells = graph.getChildVertices(graph.getDefaultParent());
        final Object[] impactCells = new Object[8];
        int i = 0;
        for (Object c : cells) {
            mxCell cell = (mxCell) c;
            mxGeometry geo = cell.getGeometry();

            geo.setHeight(30);
            TechnicalImpact impact = TacticGraphBuilder.cellToImpact(cell);
            if (impact != null) {
                geo.setWidth(225);
                impactCells[i++] = c;
            } else {
                geo.setWidth(125);
            }
        }

        //Set the color of Technical Impacts to be darker blue
        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxHtmlColor.hexString(powderblue), impactCells);

        //Allow vertices to be clickable and show CWE details in an alert dialog.
        component.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mxCell cell =(mxCell) component.getCellAt(e.getX(), e.getY());
                if (cell != null) {
                    TechnicalImpact impact = TacticGraphBuilder.cellToImpact(cell);
                    if (impact != null) {
                        JOptionPane.showMessageDialog(component, impact.cwesToString());
                    } else {
                        GraphNavigator g = new GraphNavigator(cell.getValue().toString());
                        JOptionPane.showMessageDialog(component, g.toString());
                    }
                }
            }
        });

        //Center the graph within a larger border
        graph.setBorder(30);
        mxGraphView view = graph.getView();
        view.setTranslate(new mxPoint(20, 20));

        //Format the layout of each graph to be hierarchical with a west orientation
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.WEST);
        layout.setIntraCellSpacing(20);
        layout.setInterHierarchySpacing(30);
        layout.execute(graph.getDefaultParent());

        return component;
    }


}
