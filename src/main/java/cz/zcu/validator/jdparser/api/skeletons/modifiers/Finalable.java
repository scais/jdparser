package cz.zcu.validator.jdparser.api.skeletons.modifiers;

/**
 * Every element able to have @code{final} modifier must implement this
 * interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public interface Finalable {

    /**
     * Gets {@code final} modifier.
     * 
     * @return TRUE, if element is final, otherwise UNSET.
     */
    public ModifierStates isFinal();

    /**
     * Adjusts {@code final} modifier.
     * 
     * @param isFinalParam
     *            {@code final} modifier which will be set.
     */
    public void setFinalModifier(ModifierStates isFinalParam);

}
