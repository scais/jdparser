package cz.zcu.validator.jdparser.api;

/**
 * Necessary constants for JavaDocs reading.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 12.12.2012
 */
public class Constants {

    /**
     * This String value is placed, if some element needs value, but it's value
     * was not read successfully from JavaDocs. Usually, this constant is used
     * as a fake type in method's parameters.
     */
    public static final String WRONG_READ_VALUE = "JDPARSER_WRONG_READ_VALUE";

}
