package cz.zcu.validator.jdparser.api.javadocs.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.zcu.validator.jdparser.api.Constants;
import cz.zcu.validator.jdparser.api.javadocs.JavaDocAttribute;
import cz.zcu.validator.jdparser.api.javadocs.JavaDocClass;
import cz.zcu.validator.jdparser.api.javadocs.JavaDocMethod;

/**
 * Design pattern - Factory.
 * 
 * Creates JavaDoc's elements.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 7.12.2012
 */
public class JavaDocsFactory {

    /**
     * Creates an instance of {@code JavaDocClass} from given String values. It
     * supposes special format of some parameters.
     * 
     * @param name
     *            Class name.
     * @param text
     *            JavaDoc element text.
     * @param deprecated
     *            If this String is not empty, JavaDoc class will be deprecated.
     * @param since
     *            Value of {@code since} annotation.
     * @param see
     *            Values of {@code see} annotation. This String should have been
     *            empty or containing values separated by ','.
     * @param author
     *            Values of {@code author} annotation. This String should have
     *            been empty or containing values separated by ','.
     * @param version
     *            Value of {@code version} annotation.
     * @return New instance of {@code JavaDocClass} with attributes generated
     *         from given parameters.
     */
    public JavaDocClass makeJavaDocClass(String name, String text, String deprecated, String since, String see, String author, String version) {

        String[] authorArray;
        if (author.isEmpty()) {

            authorArray = new String[0];
        }
        else {

            authorArray = author.split(",");
        }

        String[] seeArray;
        if (see.isEmpty()) {

            seeArray = new String[0];
        }
        else {

            seeArray = see.split(",");
        }

        return new JavaDocClass(name, text, !deprecated.isEmpty(), since, Arrays.asList(seeArray), Arrays.asList(authorArray), version);
    }

    /**
     * Creates an instance of {@code JavaDocAttribute} from given String values.
     * It supposes special format of some parameters.
     * 
     * @param name
     *            Class name.
     * @param comment
     *            JavaDoc element text.
     * @param deprecated
     *            If this String is not empty, JavaDoc class will be deprecated.
     * @param since
     *            Value of {@code since} annotation.
     * @param see
     *            Values of {@code see} annotation. This String should have been
     *            empty or containing values separated by ','..
     * @return New instance of {@code JavaDocAttribute} with attributes
     *         generated from given parameters.
     */
    public JavaDocAttribute makeJavaDocAttribute(String name, String comment, String deprecated, String since, String see) {

        String[] seeArray;
        if (see.isEmpty()) {

            seeArray = new String[0];
        }
        else {

            seeArray = see.split(",");
        }

        return new JavaDocAttribute(name, comment, !deprecated.isEmpty(), since, Arrays.asList(seeArray));
    }

    /**
     * Creates an instance of {@code JavaDocMethod} from given String values. It
     * supposes special format of some parameters.
     * 
     * @param name
     *            Class name.
     * @param comment
     *            JavaDoc element text.
     * @param deprecated
     *            If this String is not empty, JavaDoc class will be deprecated.
     * @param since
     *            Value of {@code since} annotation.
     * @param see
     *            Values of {@code see} annotation. This String should have been
     *            empty or containing values separated by ','.
     * @param parameters
     *            List of String representation of parameters. Every parameter
     *            is formed by parameter's name and parameter's description.
     *            These two parts are divided by char value '-'.
     * @param returns
     *            Value of {@code return} annotation.
     * @return New instance of {@code JavaDocMethod} with attributes generated
     *         from given parameters.
     */
    public JavaDocMethod makeJavaDocMethod(String name, String comment, String deprecated, String since, String see, List<String> parameters, String returns) {

        Map<String, String> paramMap = new HashMap<String, String>();
        for (String param : parameters) {

            String[] paramArray = param.split("-");

            // because parameter name can be in JavaDoc without description
            if (paramArray.length < 2) {

                paramMap.put(paramArray[0].trim(), Constants.WRONG_READ_VALUE);
            }
            else {

                paramMap.put(paramArray[0].trim(), paramArray[1].trim());
            }
        }

        String[] seeArray;
        if (see.isEmpty()) {

            seeArray = new String[0];
        }
        else {

            seeArray = see.trim().split(",");
        }

        return new JavaDocMethod(name, comment, !deprecated.isEmpty(), since, Arrays.asList(seeArray), paramMap, returns);
    }

}
