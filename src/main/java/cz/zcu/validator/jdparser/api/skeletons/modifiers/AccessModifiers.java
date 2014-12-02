package cz.zcu.validator.jdparser.api.skeletons.modifiers;

/**
 * Possible types of access modifiers.
 * 
 * TODO: To change all application to use String instead of this enum (this
 * solution is an overkill).
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public enum AccessModifiers {

    /**
     * All standard access modifiers.
     */
    PUBLIC("public"), WITHOUT_MODIFIER(""), PROTECTED("protected"), PRIVATE("private");

    /**
     * String representation of access modifier.
     */
    private final String accessModifierString;

    private AccessModifiers(String accessModifierString) {

        this.accessModifierString = accessModifierString;
    }

    /**
     * Return correspond {@code access modifier} from String with all modifiers.
     * 
     * @param accessModifiers
     *            Modifier's String containing all modifiers.
     * @return {@code Access modifier} found in String with modifiers or null,
     *         if {@code accessModifiers} parameter is in bad format.
     */
    public static AccessModifiers getCorrespondAcessModifierFromModifiersString(String accessModifiers) {

        String[] modifiers = accessModifiers.split(" ");

        for (String modifier : modifiers) {

            if (modifier.equals(AccessModifiers.PRIVATE.toString())) {

                return AccessModifiers.PRIVATE;
            }
            else if (modifier.equals(AccessModifiers.PUBLIC.toString())) {

                return AccessModifiers.PUBLIC;
            }
            else if (modifier.equals(AccessModifiers.PROTECTED.toString())) {

                return AccessModifiers.PROTECTED;
            }
        }

        return AccessModifiers.WITHOUT_MODIFIER;
    }

    public boolean isPublic() {

        if (PUBLIC.equals(this)) {

            return true;
        }

        return false;
    }
    
    public boolean isPrivate() {

        if (PRIVATE.equals(this)) {

            return true;
        }

        return false;
    }
    
    public boolean isProtected() {

        if (PROTECTED.equals(this)) {

            return true;
        }

        return false;
    }
    
    public boolean isWithoutModifier() {

        if (WITHOUT_MODIFIER.equals(this)) {

            return true;
        }

        return false;
    }

    @Override
    public String toString() {

        return accessModifierString;
    }

}
