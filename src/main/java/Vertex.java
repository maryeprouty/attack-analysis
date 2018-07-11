/*
*
* This class is an abstract representation of a vertex to be used in graphs
* provided in the JGraphT package. Its subclasses include Tactic and TechnicalImpact.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-07-11
*/

abstract class Vertex {

    String name;

    Vertex(String name) {
        this.name = name;
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();


}
