package cz.zcu.validator.jdparser.api.skeletons.modifiers;

/**
 * Represents modifier states in class. True means, that modifier has been used,
 * UNSET hasn't.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 * 
 */
public enum ModifierStates {

    /**
     * True and false values of {@code ModifierStates}.
     */
    TRUE("true"), UNSET("unset");

    /**
     * String representaiton of {@code ModifierStates}.
     */
    private String modifierString;

    private ModifierStates(String modifierString) {

        this.modifierString = modifierString;
    }

    /**
     * Returns whether the {@code ModifierStates} has been set to true or not.
     * 
     * @param state
     *            {@code ModifierStates} which value is being checked.
     * @return True, if {@code ModifierStates} has {@code TRUE} value, otherwise
     *         returns false.
     */
    public static boolean hasBeenSet(ModifierStates state) {

        if (state.equals(TRUE)) {

            return true;
        }

        else {

            return false;
        }
    }

    @Override
    public String toString() {

        return modifierString;
    }
}
