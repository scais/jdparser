package cz.zcu.validator.jdparser.api.skeletons.factory;

import java.util.HashMap;
import java.util.Map;

import cz.zcu.validator.jdparser.api.Constants;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonAttribute;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonClass;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonConstructor;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonMethod;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.AccessModifiers;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.ModifierStates;
import cz.zcu.validator.jdparser.api.skeletons.modifiers.ModifierStatesUtil;

/**
 * Design pattern - Factory.
 * 
 * Creates skeleton's classes.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 5.12.2012
 */
public class SkeletonFactory {

    public SkeletonFactory() {

    }

    /**
     * Makes {@code SkeletonClass} from given attributes.
     * 
     * @param className
     *            Name of creating skeleton class.
     * @param modifiers
     *            All modifiers of skeleton class.
     * @return New instance of {@code SkeletonClass} with attributes extracted
     *         from String parameters.
     */
    public SkeletonClass makeSkeletonClass(String className, String modifiers) {

        AccessModifiers accessModifier = AccessModifiers.getCorrespondAcessModifierFromModifiersString(modifiers);

        ModifierStates isStatic = ModifierStatesUtil.isStatic(modifiers);

        ModifierStates isFinal = ModifierStatesUtil.isFinal(modifiers);

        ModifierStates isAbstract = ModifierStatesUtil.isAbstract(modifiers);

        return new SkeletonClass(className, accessModifier, isFinal, isStatic, isAbstract);
    }

    /**
     * Makes {@code SkeletonConstructor} from given attributes.
     * 
     * @param constructorText
     *            Text containing all necessary information for creating an
     *            instance of {@code SkeletonConstructor}.
     * @return New instance of {@code SkeletonConstructor}.
     */
    public SkeletonConstructor makeSkeletonConstructor(String constructorText) {

        // in JavaDocs is the space before attribute name created as '&nbsp;'
        constructorText = constructorText.replaceAll("\u00a0", " ");

        AccessModifiers accessModifier = AccessModifiers.getCorrespondAcessModifierFromModifiersString(constructorText);

        String name = constructorText.substring(accessModifier.toString().length(), constructorText.indexOf("("));

        Map<String, String> parametersMap = null;

        // constructor has parameters
        if (constructorText.substring(constructorText.indexOf("(") + 1, constructorText.indexOf(")")).length() > 0) {

            parametersMap = gatherParametersFromString(constructorText);
        }
        else {

            parametersMap = new HashMap<String, String>(0);
        }

        return new SkeletonConstructor(name, accessModifier, parametersMap);
    }

    /**
     * Makes {@code SkeletonAttribute} from given attributes.
     * 
     * @param attributeText
     *            Text containing all necessary information for creating an
     *            instance of {@code SkeletonAttribute}.
     * @return New instance of {@code SkeletonAttribute}.
     */
    public SkeletonAttribute makeSkeletonAttribute(String attributeText) {

        // in JavaDocs is the space before attribute name created as '&nbsp;'
        attributeText = attributeText.replaceAll("\u00a0", " ");

        String[] attributePart = attributeText.split(" ");

        // minimally the name of an attribute and it's type is required
        if (attributePart.length < 2) {

            return null;
        }

        String name = attributePart[attributePart.length - 1];
        String type = attributePart[attributePart.length - 2];

        AccessModifiers accessModifier = AccessModifiers.getCorrespondAcessModifierFromModifiersString(attributeText);

        ModifierStates isStatic = ModifierStatesUtil.isStatic(attributeText);

        ModifierStates isFinal = ModifierStatesUtil.isFinal(attributeText);

        ModifierStates isVolatile = ModifierStatesUtil.isVolatile(attributeText);

        ModifierStates isTransient = ModifierStatesUtil.isTransient(attributeText);

        return new SkeletonAttribute(name, type, accessModifier, isFinal, isStatic, isVolatile, isTransient);
    }

    /**
     * Makes {@code SkeletonMethod} from given attributes.
     * 
     * @param methodText
     *            Text containing all necessary information for creating an
     *            instance of {@code SkeletonMethod}.
     * @return New instance of {@code SkeletonMethod}.
     */
    public SkeletonMethod makeSkeletonMethod(String methodText) {

        // in JavaDocs is the space before attribute name created as '&nbsp;'
        methodText = methodText.replaceAll("\u00a0", " ");

        String toName = methodText.substring(0, methodText.indexOf("(")).trim();

        String name = toName.substring(toName.lastIndexOf(" ")).trim();

        String toType = toName.substring(0, toName.lastIndexOf(" ")).trim();

        String returnType = "";
        if (toType.contains(" ")) {

            returnType = toType.substring(toType.lastIndexOf(" ")).trim();
        }

        AccessModifiers accessModifier = AccessModifiers.getCorrespondAcessModifierFromModifiersString(methodText);

        ModifierStates isStatic = ModifierStatesUtil.isStatic(methodText);

        // because final can be also parameters
        ModifierStates isFinal = ModifierStatesUtil.isFinal(methodText.substring(0, methodText.indexOf("(")));

        ModifierStates isAbstract = ModifierStatesUtil.isAbstract(methodText);

        Map<String, String> parametersMap = null;
        // method has parameters
        if (methodText.substring(methodText.indexOf("(") + 1, methodText.indexOf(")")).length() > 0) {

            parametersMap = gatherParametersFromString(methodText);
        }
        else {

            parametersMap = new HashMap<String, String>(0);
        }

        return new SkeletonMethod(name, returnType, accessModifier, isFinal, isStatic, isAbstract, parametersMap);
    }

    /**
     * Makes {@code Map<String, String>} of parameters from given String, which
     * contains information about all parameters.
     * 
     * @param paramsText
     *            Text containing information about parameters.
     * @return Map with all parameters, where keys are names of parameters and
     *         values are their types.
     */
    private Map<String, String> gatherParametersFromString(String paramsText) {

        String parameters[] = paramsText.substring(paramsText.indexOf("(") + 1, paramsText.indexOf(")")).split("\n");
        Map<String, String> parametersMap = new HashMap<String, String>(parameters.length);

        for (int i = 0; i < parameters.length; i++) {

            // if does not contain space, parameter has wrong format
            if (parameters[i].trim().contains(" ")) {

                String[] splitParam = parameters[i].trim().split(" ");

                // if there are more params, they are separeted by ','
                if (splitParam[1].contains(",")) {

                    splitParam[1] = splitParam[1].replace(",", "");
                }

                // it has to be hashed according to attribute name
                parametersMap.put(splitParam[1], splitParam[0]);
            }
            else {

                parametersMap.put(parameters[i].trim(), Constants.WRONG_READ_VALUE);
            }
        }

        return parametersMap;
    }

}
