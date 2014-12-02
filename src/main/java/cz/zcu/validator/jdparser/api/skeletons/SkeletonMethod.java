package cz.zcu.validator.jdparser.api.skeletons;

import java.util.Map;

import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifiers;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.HasParameters;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.ModifierStates;

/**
 * Design pattern - Messenger.
 * 
 * Represents one method of an class.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public class SkeletonMethod extends AbstractSkeleton implements HasParameters {

    /**
     * {@code return} type.
     */
    private String returnType;

    /**
     * {@code abstract} modifier.
     */
    private ModifierStates isAbstract;

    /**
     * All {@code parameters} of the method. Every parameter is represented as a
     * type and name, both represented as a String and seved in map.
     */
    private Map<String, String> params;

    public SkeletonMethod(String nameParam, String returnType, AccessModifiers accessModifierParam, ModifierStates isFinalParam, ModifierStates isStaticParam,
            ModifierStates isAbstractParam, Map<String, String> paramsParam) {

        super(nameParam, accessModifierParam, isFinalParam, isStaticParam);

        this.returnType = returnType;
        this.isAbstract = isAbstractParam;
        this.params = paramsParam;
    }

    public String getReturnType() {

        return returnType;
    }

    public void setReturnType(String returnType) {

        this.returnType = returnType;
    }

    public ModifierStates getIsAbstract() {

        return isAbstract;
    }

    public void setIsAbstract(ModifierStates isAbstract) {

        this.isAbstract = isAbstract;
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
        if (!super.equals(obj)) {

            return false;
        }

        final SkeletonMethod ref = (SkeletonMethod) obj;
        if (ref.getReturnType().equals(getReturnType()) && ref.getIsAbstract().equals(getIsAbstract()) && ref.getParameters().equals(getParameters())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 17 * (getReturnType().hashCode() + getIsAbstract().hashCode() + +getParameters().hashCode()) + super.hashCode();
    }

    @Override
    public String toString() {

        return super.toString() + " RETURN: " + getReturnType() + " ABSTRACT: " + getIsAbstract() + " PARAMETERS: " + getParameters();
    }

}
