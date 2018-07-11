/*
*
* This class is a concrete implementation of a vertex to be used in graphs provided
* by the JGraphT package. It represents one of the 11 attack tactics from attack.mitre.org.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-07-11
*/

import java.util.Objects;

class Tactic extends Vertex {

    Tactic(String name) {
       super(name);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Tactic other = (Tactic) obj;
        return Objects.equals(name, other.name);

    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
