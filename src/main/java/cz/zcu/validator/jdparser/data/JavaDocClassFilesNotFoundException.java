package cz.zcu.validator.jdparser.data;

/**
 * This exception is thrown if some class is mentioned in JavaDocs, but it´s own
 * JavaDoc class is missing.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 3.12.2012
 */
public class JavaDocClassFilesNotFoundException extends JavaDocParsingException {

    /**
     * 
     */
    private static final long serialVersionUID = -5569240331906765929L;

    public JavaDocClassFilesNotFoundException() {

        super();
    }

    public JavaDocClassFilesNotFoundException(String message) {

        super(message);
    }

    public JavaDocClassFilesNotFoundException(String message, Throwable cause) {

        super(message, cause);
    }

    public JavaDocClassFilesNotFoundException(Throwable cause) {

        super(cause);
    }

}
