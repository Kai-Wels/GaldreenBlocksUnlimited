package de.ewu2000.galdreenblocks;

import org.bukkit.Material;
import java.util.LinkedList;
import java.util.List;

public class ButtonRotationState {
    private Material mat;
    private String name;
    private List<ButtonRotationStateTransition> transitions = new LinkedList<>();


    public static List<ButtonRotationState> allButtonRotationStates = new LinkedList<>();

    ButtonRotationState(Material mat, String name) {
        this.mat = mat;
        this.name= name;
    }

    public Material getMaterial(){
        return mat;
    }
    public String getName(){
        return name;
    }

    public List<ButtonRotationStateTransition> getTransitions() {
        return transitions;
    }

    public static String allRotationStatesToString(){
        String output = "O:";
        for (ButtonRotationState rs: allButtonRotationStates) {
            output +="||" + rs.name + "," + rs.mat + ",T: ";
            for (ButtonRotationStateTransition rt: rs.transitions) {
                output += rt.toString() + "; ";
            }
        }
        return output;
    }

}
