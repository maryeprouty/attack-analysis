/**
 * Created by maryprouty on 7/10/18.
 */
class Tactic extends Vertex {

    Tactic(String name) {
       super(name);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    String getName() {
        return name;
    }
}
