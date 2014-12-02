package cz.zcu.validator.jdparser.api.javadocs.modifiers;

/**
 * If JavaDoc element can have {@code version} annotation, has to implement this
 * interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public interface Versionable {
    
    /**
     * Returns whether or not has the JavaDoc element {@code version}
     * annotation.
     * 
     * @return True, if has {@code version} annotation, otherwise false.
     */
    public boolean hasVersion();

    /**
     * Returns {@code version} annotation value.
     * 
     * @return Returns the value of the {@code version} annotation.
     */
    public String getVersion();

    /**
     * Sets the value of the {@code version} annotation.
     * 
     * @param version
     *            Value of {@code version} annotation to set.
     */
    public void setVersion(String version);

}
