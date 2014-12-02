package cz.zcu.validator.jdparser.api.skeletons.modifiers;

import java.util.Map;

/**
 * Every element able to have {@code parameters} must implement this interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 5.12.2012
 */
public interface HasParameters {

    /**
     * Returns all parameters in Map. Keys are names and values are types.
     * 
     * @return Map with parameters.
     */
    public Map<String, String> getParameters();

    /**
     * Setter for parameters.
     * 
     * @param params
     *            Parameters which will be set.
     */
    public void setParameters(Map<String, String> params);

}
