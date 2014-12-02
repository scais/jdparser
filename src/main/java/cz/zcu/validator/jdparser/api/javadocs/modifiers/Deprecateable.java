package cz.zcu.validator.jdparser.api.javadocs.modifiers;

/**
 * If JavaDoc element can be @code{deprecated} annotation, has to implement this interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public interface Deprecateable {

    /**
     * If JavaDoc element is @code{deprecated}, return true, otherwise false.
     * 
     * @return True, if JavaDoc element is @code{deprecated}, otherwise false.
     */
    public boolean isDeprecated();

    /**
     * Set's JavaDoc element @code{deprecated} value.
     * 
     * @param deprecated
     *            Value of @code{deprecated}.
     */
    public void setDepreceted(boolean deprecated);

}
