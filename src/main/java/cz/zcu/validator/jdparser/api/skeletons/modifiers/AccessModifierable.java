package cz.zcu.validator.jdparser.api.skeletons.modifiers;

/**
 * Represents all standard access modifiers.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz
 * 
 */
public interface AccessModifierable {

    /**
     * Gets {@code AccessModifiers} modifier of an element.
     * 
     * @return TRUE, if element is static, otherwise UNSET.
     */
    public AccessModifiers getAccessModifier();

    /**
     * Adjusts {@code AccessModifiers} modifier of an element.
     * 
     * @param modifierParam
     *            {@code AccessModifiers}, which will be set.
     */
    public void setAccessModifier(AccessModifiers modifierParam);

}
