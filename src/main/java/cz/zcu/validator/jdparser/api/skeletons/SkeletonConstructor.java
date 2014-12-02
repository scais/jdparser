package cz.zcu.validator.jdparser.api.skeletons;

import java.util.Map;

import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifierable;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifiers;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.HasParameters;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.Nameable;

/**
 * Design pattern - Messenger.
 * 
 * Represents one constructor of an class.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 5.12.2012
 */
public class SkeletonConstructor implements Nameable, AccessModifierable, HasParameters {

    /**
     * Name of an element.
     */
    private String name;

    /**
     * Access modifier of an element.
     */
    private AccessModifiers accessModifier;

    /**
     * All {@code parameters} of the constructor. Every parameter is represented
     * as a type and name, both represented as a String and saved in a map.
     */
    private Map<String, String> params;

    public SkeletonConstructor(String name, AccessModifiers accessModifier, Map<String, String> params) {

        this.name = name;
        this.accessModifier = accessModifier;
        this.params = params;
    }

    @Override
    public AccessModifiers getAccessModifier() {

        return accessModifier;
    }

    @Override
    public void setAccessModifier(AccessModifiers modifierParam) {

        this.accessModifier = modifierParam;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public void setName(String nameParam) {

        this.name = nameParam;
    }

    @Override
    public Map<String, String> getParameters() {

        return params;
    }

    @Override
    public void setParameters(Map<String, String> params) {

        this.params = params;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {

            return false;
        }
        if (obj == this) {

            return true;
        }
        if (obj.getClass() != this.getClass()) {

            return false;
        }

        final SkeletonConstructor ref = (SkeletonConstructor) obj;
        if (ref.getName().equals(getName()) && ref.getAccessModifier().equals(getAccessModifier()) && ref.getParameters().equals(getParameters())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 17 * (getName().hashCode() + getAccessModifier().hashCode() + getParameters().hashCode());
    }

    @Override
    public String toString() {

        return "NAME: " + getName() + " ACCESS MODIFIER: " + getAccessModifier() + " PARAMETERS: " + getParameters();
    }
}
