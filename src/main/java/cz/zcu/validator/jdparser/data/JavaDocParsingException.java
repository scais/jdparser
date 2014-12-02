package cz.zcu.validator.jdparser.data;

/**
 * This exception (and it´s children) could be throw during JavaDoc parsing.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 3.12.2012
 */
public class JavaDocParsingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -345959178906044537L;

    public JavaDocParsingException() {

        super();
    }

    public JavaDocParsingException(String message) {

        super(message);
    }

    public JavaDocParsingException(String message, Throwable cause) {

        super(message, cause);
    }

    public JavaDocParsingException(Throwable cause) {

        super(cause);
    }

}
