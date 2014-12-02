package cz.zcu.validator.jdparser.api;

import java.util.Map;

import cz.zcu.validator.jdparser.api.javadocs.JavaDocAttribute;
import cz.zcu.validator.jdparser.api.javadocs.JavaDocClass;
import cz.zcu.validator.jdparser.api.javadocs.JavaDocMethod;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonAttribute;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonClass;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonConstructor;
import cz.zcu.validator.jdparser.api.skeletons.SkeletonMethod;

/**
 * Design pattern - Messenger.
 * 
 * Representation of a class. Holds all information about class.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public class ClassRepresentation {

    /**
     * Pairs of constructors and it´s JavaDocs.
     */
    private Map<SkeletonConstructor, JavaDocMethod> constructors;

    /**
     * Pairs of attributes and it´s JavaDocs.
     */
    private Map<SkeletonAttribute, JavaDocAttribute> attributes;

    /**
     * Pairs of methods and it´s JavaDocs.
     */
    private Map<SkeletonMethod, JavaDocMethod> methods;

    /**
     * Class JavaDocs.
     */
    private JavaDocClass javaDocClass;

    /**
     * Class.
     */
    private SkeletonClass skeletonClass;

    public ClassRepresentation(Map<SkeletonConstructor, JavaDocMethod> constructorsParam, Map<SkeletonAttribute, JavaDocAttribute> attributesParam,
            Map<SkeletonMethod, JavaDocMethod> methodsParam, JavaDocClass javaDocClassParam, SkeletonClass skeletonClassParam) {

        this.constructors = constructorsParam;
        this.attributes = attributesParam;
        this.methods = methodsParam;
        this.javaDocClass = javaDocClassParam;
        this.skeletonClass = skeletonClassParam;
    }

    public Map<SkeletonConstructor, JavaDocMethod> getConstructors() {

        return constructors;
    }

    public void setConstructors(Map<SkeletonConstructor, JavaDocMethod> constructors) {

        this.constructors = constructors;
    }

    public Map<SkeletonAttribute, JavaDocAttribute> getAttributes() {

        return attributes;
    }

    public void setAttributes(Map<SkeletonAttribute, JavaDocAttribute> attributes) {

        this.attributes = attributes;
    }

    public JavaDocClass getJavaDocClass() {

        return javaDocClass;
    }

    public void setJavaDocClass(JavaDocClass javaDocClass) {

        this.javaDocClass = javaDocClass;
    }

    public Map<SkeletonMethod, JavaDocMethod> getMethods() {

        return methods;
    }

    public void setMethods(Map<SkeletonMethod, JavaDocMethod> methods) {

        this.methods = methods;
    }

    public SkeletonClass getSkeletonClass() {

        return skeletonClass;
    }

    public void setSkeletonClass(SkeletonClass skeletonClass) {

        this.skeletonClass = skeletonClass;
    }

    @Override
    public String toString() {

        String output = skeletonClass.toString() + "\n" + javaDocClass.toString();
        output += "\nCONSTRUCTORS: " + constructors.toString();
        output += "\nATTRIBUTES: " + attributes.toString();
        output += "\nMETHODS: " + methods.toString();

        return output;
    }

}
