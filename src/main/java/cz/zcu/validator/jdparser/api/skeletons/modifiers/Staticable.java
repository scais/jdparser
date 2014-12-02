package cz.zcu.validator.jdparser.api.skeletons.modifiers;

/**
 * Every element able to have @code{static} modifier must implement this
 * interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public interface Staticable {

    /**
     * Gets {@code static} modifier.
     * 
     * @return TRUE, if element is static, otherwise UNSET.
     */
    public ModifierStates isStatic();

    /**
     * Adjusts {@code static} modifier.
     * 
     * @param isStaticParam
     *            {@code static} modifier which will be set.
     */
    public void setStaticModifier(ModifierStates isStaticParam);

}
