/**
* This class holds the statistics relating a tactic vector to an attack vector,
* which can be used to rank how similar each tactic is to an attack vector.
*
* @author Mary Prouty
* @version 1.0
* @since 2018-06-22
*/

import java.lang.Comparable;

public class AttackStat implements Comparable<AttackStat> {

    private String tactic;
    private Double rankValue;
    private Double uncertainty;
    private String techniques;

    /**
    * No-args constructor for AttackStat.
    */
    public AttackStat() {
        this(0.0, 0.0, "0");
    }

    /**
    * A constructor allowing creation without knowledge of the tactic yet.
    * @param rankValue Specifies the value used to rank the AttackStat.
    * @param uncertainty Specifies the percent uncertainty associated with this stat.
    * @param techniques Specifies how many techniques this attack employs out of
    * all techniques in a tactic.
    */
    public AttackStat(Double rankValue, Double uncertainty, String techniques) {
        this.tactic = "Unknown Tactic";
        this.rankValue = rankValue;
        this.uncertainty = uncertainty;
        this.techniques = techniques;

    }

    /**
    * A setter for the tactic associated with this AttackStat.
    * @param tacticName Specifies the name of the tactic to be set.
    */
    public void setTactic(String tacticName) {
        this.tactic = tacticName;
    }

    /**
    * A getter for the rankValue associated with this AttackStat.
    * @return double Returns the numerical rank value.
    */
    public double getRankValue() {
        return this.rankValue;
    }


    public int compareTo(AttackStat other) {
        Double otherRankValue = ((AttackStat) other).getRankValue();
        return otherRankValue.compareTo(this.rankValue);
    }

    @Override
    public String toString() {
        return this.tactic + "\n\t\t" + String.format("%.2f", rankValue) + "% likelihood\n\t\t" +
            this.techniques + " techniques used\n\t";
    }

}
