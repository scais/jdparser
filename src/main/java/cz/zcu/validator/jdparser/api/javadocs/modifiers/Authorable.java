package cz.zcu.validator.jdparser.api.javadocs.modifiers;

import java.util.Collection;

/**
 * If JavaDoc element can have {@code author} annotation, has to implement this
 * interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public interface Authorable {

    /**
     * Returns whether or not has the JavaDoc element {@code author} annotation.
     * 
     * @return True, if has {@code author} annotation, otherwise false.
     */
    public boolean hasAuthors();

    /**
     * Returns {@code author} annotation value.
     * 
     * @return Returns the value of the {@code author} annotation.
     */
    public Collection<String> getAuthors();

    /**
     * Sets the value of the {@code author} annotation.
     * 
     * @param authors
     *            Value of {@code author} annotation to set.
     */
    public void setAuthors(Collection<String> authors);

}
