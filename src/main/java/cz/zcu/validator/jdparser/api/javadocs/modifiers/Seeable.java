package cz.zcu.validator.jdparser.api.javadocs.modifiers;

import java.util.Collection;

/**
 * If JavaDoc element can have @code{see} annotation, has to implement this
 * interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public interface Seeable {

    /**
     * Returns whether or not has the JavaDoc element @code{see} annotation.
     * 
     * @return True, if has @code{see} annotation, otherwise false.
     */
    public boolean hasSee();

    /**
     * Returns all see references in collection.
     * 
     * @return Collection of see references.
     */
    public Collection<String> getSee();

    /**
     * Set @code{see}.
     * 
     * @param sees
     *            Collection with @code{see} to set.
     */
    public void setSee(Collection<String> sees);

}
