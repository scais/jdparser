package cz.zcu.validator.jdparser.api.skeletons;

import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifiers;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.ModifierStates;

/**
 * Design pattern - Messenger.
 * 
 * Represents one attribute of a class.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public class SkeletonAttribute extends AbstractSkeleton {

    /**
     * The type of an attribute.
     */
    private String type;

    /**
     * {@code volatile} modifier.
     */
    private ModifierStates isVolatile;

    /**
     * {@code transient} modifier.
     */
    private ModifierStates isTransient;

    public SkeletonAttribute(String nameParam, String typeParam, AccessModifiers accessModifierParam, ModifierStates isFinalParam,
            ModifierStates isStaticParam, ModifierStates isVolatileParam, ModifierStates isTransientParam) {

        super(nameParam, accessModifierParam, isFinalParam, isStaticParam);

        this.isTransient = isTransientParam;
        this.isVolatile = isVolatileParam;
        this.type = typeParam;
    }

    public ModifierStates getIsTransient() {

        return isTransient;
    }

    public void setIsTransient(ModifierStates isTransient) {

        this.isTransient = isTransient;
    }

    public ModifierStates getIsVolatile() {

        return isVolatile;
    }

    public void setIsVolatile(ModifierStates isVolatile) {

        this.isVolatile = isVolatile;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
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

        final SkeletonAttribute ref = (SkeletonAttribute) obj;
        if (ref.getType().equals(getType()) && ref.getIsVolatile().equals(getIsVolatile()) && ref.getIsTransient().equals(getIsTransient())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 17 * (getType().hashCode() + getIsVolatile().hashCode() + getIsTransient().hashCode()) + super.hashCode();
    }

    @Override
    public String toString() {

        return super.toString() + " TYPE: " + getType() + " VOLATILE: " + getIsVolatile() + " TRANSIENT: " + getIsTransient();
    }

}
