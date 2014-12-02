package cz.zcu.validator.jdparser.api.javadocs.modifiers;

/**
 * If JavaDoc element can have {@code return} annotation, has to implement this
 * interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public interface Returnable {

    /**
     * Returns whether or not has the JavaDoc element @code{return} annotation.
     * 
     * @return True, if has @code{return} annotation, otherwise false.
     */
    public boolean hasReturn();

    /**
     * Returns {@code return} annotation value.
     * 
     * @return Collection of see references.
     */
    public String getReturn();

    /**
     * Set @code{return}.
     * 
     * @param returnParam
     *            Value of @code{return to set.
     */
    public void setReturn(String returnParam);

}
