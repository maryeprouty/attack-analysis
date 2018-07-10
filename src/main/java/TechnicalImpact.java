/**
 * Created by maryprouty on 7/10/18.
 */

import java.util.ArrayList;

class TechnicalImpact extends Vertex {

    private ArrayList<String> cweList;

    TechnicalImpact(String name) {
        super(name);
    }

    TechnicalImpact(String name, ArrayList<String> cwes) {
        this(name);
        this.cweList = cwes;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }


    @Override
    public String toString() {
        String cweStr = "CWEs: ";

        int i = 0;
        for (String cwe: cweList) {
            cweStr += cwe;
            if (i < cweList.size() - 1) {
                cweStr += ", ";
            }
            i++;
        }

        return cweStr;
    }

}
