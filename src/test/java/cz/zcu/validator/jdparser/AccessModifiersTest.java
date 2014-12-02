package cz.zcu.validator.jdparser;

import org.junit.Test;

import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifiers;
import static org.junit.Assert.assertTrue;

/**
 * 
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 14.12.2012
 */
public class AccessModifiersTest {

    @Test
    public void publicTest() {

        AccessModifiers publicAM = AccessModifiers.PUBLIC;

        assertTrue("AccessModifier is not PUBLIC", publicAM.isPublic());
    }

    @Test
    public void privateTest() {

        AccessModifiers publicAM = AccessModifiers.PRIVATE;

        assertTrue("AccessModifier is not PRIVATE", publicAM.isPrivate());
    }

    @Test
    public void protectedTest() {

        AccessModifiers publicAM = AccessModifiers.PROTECTED;

        assertTrue("AccessModifier is not PROTECTED", publicAM.isProtected());
    }

    @Test
    public void withoutModifierTest() {

        AccessModifiers publicAM = AccessModifiers.WITHOUT_MODIFIER;

        assertTrue("AccessModifier is not WITHOUT_MODIFIER", publicAM.isWithoutModifier());
    }

}
