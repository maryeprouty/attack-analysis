/*
*
* This class is a concrete implementation of a vertex to be used in graphs provided
* by the JGraphT package. It represents one of the 8 technical impacts from CWEs, which
* can be found at https://cwe.mitre.org/community/swa/detection_methods.html.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-07-11
*/

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

class TechnicalImpact extends Vertex {

    private ArrayList<String> cweList;

    private TechnicalImpact(String name) {
        super(name);
    }

    TechnicalImpact(String name, ArrayList<String> cwes) {
        this(name);
        this.cweList = cwes;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        TechnicalImpact other = (TechnicalImpact) obj;
        return Objects.equals(name, other.name) && Objects.equals(cweList, other.cweList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cweList);
    }

    /**
     * This method converts the CWEs in the cweList to a readable string of CWEs.
     * @return cweStr The string representation of CWEs related to this technical impact.
     */
    String cwesToString() {
        String cweStr = "'"+ this.toString() + "' is caused by \nCWEs ";

        int i = 0;
        for (String cwe: cweList) {
            cweStr += cwe;
            if (i < cweList.size() - 1) {
                cweStr += ", ";
            }
            i++;
        }
        cweStr += ".";

        return cweStr;
    }

    /**
     * A getter for the list of CWEs associated with this technical impact.
     * @return cweList The list of CWEs that cause this technical impact.
     */
    ArrayList<String> getCwes() {
        return cweList;
    }


    @Override
    public String toString() {
        return name;
    }

}
