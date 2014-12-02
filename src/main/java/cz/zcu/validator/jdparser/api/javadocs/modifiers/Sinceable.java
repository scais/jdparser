package cz.zcu.validator.jdparser.api.javadocs.modifiers;

/**
 * If JavaDoc element can have {@code since} annotation, has to implement this
 * interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public interface Sinceable {

    /**
     * Returns whether or not has the JavaDoc element {@code since} annotation.
     * 
     * @return True, if has {@code since} annotation, otherwise false.
     */
    public boolean hasSince();

    /**
     * Returns {@code since} annotation value.
     * 
     * @return Returns the value of the {@code since} annotation.
     */
    public String getSince();

    /**
     * Sets the value of the {@code since} annotation.
     * 
     * @param since
     *            Value of {@code since} annotation to set.
     */
    public void setSince(String since);
}
