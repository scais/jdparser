package cz.zcu.validator.jdparser.api.skeletons;

import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifierable;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifiers;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.Finalable;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.ModifierStates;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.Nameable;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.Staticable;

/**
 * Design pattern - Adapter.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public abstract class AbstractSkeleton implements AccessModifierable, Finalable, Nameable, Staticable {

    /**
     * Name of an element.
     */
    private String name;

    /**
     * Access modifier of an element.
     */
    private AccessModifiers accessModifier;

    /**
     * {@code final} modifier.
     */
    private ModifierStates isFinal;

    /**
     * {@code static} modifier.
     */
    private ModifierStates isStatic;

    public AbstractSkeleton(String nameParam, AccessModifiers accessModifierParam, ModifierStates isFinalParam, ModifierStates isStaticParam) {

        this.name = nameParam;
        this.accessModifier = accessModifierParam;
        this.isFinal = isFinalParam;
        this.isStatic = isStaticParam;
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
    public ModifierStates isStatic() {

        return isStatic;
    }

    @Override
    public void setStaticModifier(ModifierStates isStaticParam) {

        this.isStatic = isStaticParam;
    }

    @Override
    public ModifierStates isFinal() {

        return isFinal;
    }

    @Override
    public void setFinalModifier(ModifierStates isFinalParam) {

        this.isFinal = isFinalParam;
    }

    @Override
    public AccessModifiers getAccessModifier() {

        return accessModifier;
    }

    @Override
    public void setAccessModifier(AccessModifiers modifier) {

        this.accessModifier = modifier;
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

        final AbstractSkeleton ref = (AbstractSkeleton) obj;

        if (ref.getAccessModifier().equals(getAccessModifier()) && ref.getName().equals(getName()) && ref.isStatic().equals(isStatic())
                && ref.isFinal().equals(isFinal())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 37 * (getAccessModifier().hashCode() + getName().hashCode() + isFinal().hashCode() + isStatic().hashCode());
    }

    @Override
    public String toString() {

        return "NAME: " + getName() + " ACCESS MODIFIER: " + getAccessModifier() + " FINAL: " + isFinal() + " STATIC: " + isStatic();
    }

}
