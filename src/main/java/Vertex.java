/**
 * Created by maryprouty on 7/10/18.
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
