package cz.zcu.validator.jdparser.api.skeletons;

import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifiers;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.ModifierStates;

/**
 * Design pattern - Messenger.
 * 
 * Represent one class.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public class SkeletonClass extends AbstractSkeleton {

    /**
     * {@code abstract} modifier.
     */
    private ModifierStates isAbstract;

    public SkeletonClass(String nameParam, AccessModifiers accessModifierParam, ModifierStates isFinalParam, ModifierStates isStaticParam,
            ModifierStates isAbstract) {

        super(nameParam, accessModifierParam, isFinalParam, isStaticParam);
        this.isAbstract = isAbstract;
    }

    public ModifierStates getIsAbstract() {

        return isAbstract;
    }

    public void setIsAbstract(ModifierStates isAbstract) {

        this.isAbstract = isAbstract;
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

        if (((SkeletonClass) obj).getIsAbstract().equals(getIsAbstract())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 17 * getIsAbstract().hashCode() + super.hashCode();
    }

    @Override
    public String toString() {

        return super.toString() + " ABSTRACT: " + getIsAbstract();
    }
    
}
