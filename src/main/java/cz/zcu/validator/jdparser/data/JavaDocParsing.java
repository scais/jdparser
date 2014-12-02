package cz.zcu.validator.jdparser.data;

import java.io.File;
import java.util.Collection;

import cz.zcu.validator.jdparser.api.ClassRepresentation;

/**
 * Interface for collecting classes from JavaDocs. Every class enable to get
 * class representation from JavaDocs has to implement this interface.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 3.12.2012
 */
public interface JavaDocParsing {

    /**
     * Collects data from JavaDocs saved in given folder and returns it's
     * representation.
     * 
     * @param javaDocFolder
     *            Folder where are saved JavaDocs. This folder is the folder
     *            described with @code{-d} parameter when using @code{javadoc}
     *            command in command line.
     * @return Mirrors of all classes described in JavaDocs, represented as
     *         instances of ClassRepresentation.
     */
    public Collection<? extends ClassRepresentation> parseDataFromJavaDoc(File javaDocFolder) throws JavaDocParsingException;

}
