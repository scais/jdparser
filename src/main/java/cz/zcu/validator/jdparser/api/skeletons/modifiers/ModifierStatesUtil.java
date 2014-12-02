package cz.zcu.validator.jdparser.api.skeletons.modifiers;

/**
 * Design pattern - Library class.
 * 
 * Util class for gathering modifiers from String. Every modifier (except access
 * modifier - they have their gathering methods inside enum class) has here
 * method for checking, if String contains it's String representation.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 5.12.2012
 */
public class ModifierStatesUtil {

    /**
     * String representation of {@code static} modifier.
     */
    public static final String STATIC_MODIFIER = "static";

    /**
     * String representation of {@code final} modifier.
     */
    public static final String FINAL_MODIFIER = "final";

    /**
     * String representation of {@code abstract} modifier.
     */
    public static final String ABSTRACT_MODIFIER = "abstract";

    /**
     * String representation of {@code volatile} modifier.
     */
    public static final String VOLATILE_MODIFIER = "volatile";

    /**
     * String representation of {@code transient} modifier.
     */
    public static final String TRANSIENT_MODIFIER = "transient";

    /**
     * String representation of {@code synchronized} modifier.
     */
    public static final String SYNCHRONIZED_MODIFIER = "synchronized";

    /**
     * String representation of {@code native} modifier.
     */
    public static final String NATIVE_MODIFIER = "native";

    /**
     * String representation of {@code strictfp} modifier.
     */
    public static final String STRICTFP_MODIFIER = "strictfp";

    /**
     * Checks if String with modifiers contains {@code STATIC_MODIFIER} and
     * returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code STATIC_MODIFIER} or {@code ModifierStates.UNSET} if not.
     */
    public static ModifierStates isStatic(String modifiers) {

        if (modifiers.contains(STATIC_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

    /**
     * Checks if String with modifiers contains {@code FINAL_MODIFIER} and
     * returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code FINAL_MODIFIER} or {@code ModifierStates.UNSET} if not.
     */
    public static ModifierStates isFinal(String modifiers) {

        if (modifiers.contains(FINAL_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

    /**
     * Checks if String with modifiers contains {@code VOLATILE_MODIFIER} and
     * returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code VOLATILE_MODIFIER} or {@code ModifierStates.UNSET} if not.
     */
    public static ModifierStates isVolatile(String modifiers) {

        if (modifiers.contains(VOLATILE_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

    /**
     * Checks if String with modifiers contains {@code ABSTRACT_MODIFIER} and
     * returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code ABSTRACT_MODIFIER} or {@code ModifierStates.UNSET} if not.
     */
    public static ModifierStates isAbstract(String modifiers) {

        if (modifiers.contains(ABSTRACT_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

    /**
     * Checks if String with modifiers contains {@code SYNCHRONIZED_MODIFIER}
     * and returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code SYNCHRONIZED_MODIFIER} or {@code ModifierStates.UNSET} if
     *         not.
     */
    public static ModifierStates isSynchronized(String modifiers) {

        if (modifiers.contains(SYNCHRONIZED_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

    /**
     * Checks if String with modifiers contains {@code NATIVE_MODIFIER} and
     * returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code NATIVE_MODIFIER} or {@code ModifierStates.UNSET} if not.
     */
    public static ModifierStates isNative(String modifiers) {

        if (modifiers.contains(NATIVE_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

    /**
     * Checks if String with modifiers contains {@code STRICTFP_MODIFIER} and
     * returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code STRICTFP_MODIFIER} or {@code ModifierStates.UNSET} if not.
     */
    public static ModifierStates isStrictfp(String modifiers) {

        if (modifiers.contains(STRICTFP_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

    /**
     * Checks if String with modifiers contains {@code TRANSIENT_MODIFIER} and
     * returns {@code ModifierStates.TRUE} if String contains this String or
     * {@code ModifierStates.UNSET} if not.
     * 
     * @param modifiers
     *            String with modifiers.
     * @return {@code ModifierStates.TRUE} if String contains
     *         {@code TRANSIENT_MODIFIER} or {@code ModifierStates.UNSET} if
     *         not.
     */
    public static ModifierStates isTransient(String modifiers) {

        if (modifiers.contains(TRANSIENT_MODIFIER)) {

            return ModifierStates.TRUE;
        }

        return ModifierStates.UNSET;
    }

}
