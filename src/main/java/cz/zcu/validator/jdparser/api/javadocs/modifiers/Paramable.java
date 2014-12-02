package cz.zcu.validator.jdparser.api.javadocs.modifiers;

import java.util.Map;

/**
 * If JavaDoc element can have parameters, has to implement this interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public interface Paramable {

    /**
     * Returns whether or not has the JavaDoc element parameters. This means,
     * that element has used the {@code param} annotation for it's parameters.
     * This method does not control, whether the description of parameters
     * exist, i.e. if element used {@code param} annotation without parameters
     * description, this method will still return true.
     * 
     * @return True, if has parameters, otherwise false.
     */
    public boolean hasParametersUsed();

    /**
     * Check whether all parameters contain description. Firstly, this method
     * should call {@code hasParametersUsed()} and then checks whether all
     * parameters have description.
     * 
     * @return True, if all parameters have description, otherwise false.
     */
    public boolean allParametersHaveDescription();

    /**
     * Returns parameters.
     * 
     * @return Returns parameters.
     */
    public Map<String, String> getParameters();

    /**
     * Sets parameters.
     * 
     * @param parameters
     *            Parameters to set.
     */
    public void setParameters(Map<String, String> parameters);

}
