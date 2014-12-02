package cz.zcu.validator.jdparser.api.skeletons.modifiers;

/**
 * Every element able to have @code{name} modifier must implement this interface. 
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public interface Nameable {

    /**
     * Gets name of an element.
     * 
     * @return String representation of name of an element.
     */
    public String getName();

    /**
     * Sets name of an element.
     * 
     * @param name
     *            Name of an element.
     */
    public void setName(String nameParam);

}
