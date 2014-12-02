package cz.zcu.validator.jdparser.api.javadocs;

import java.util.Collection;

/**
 * Design pattern - Messenger.
 * 
 * Represents comment of one attribute.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public class JavaDocAttribute extends AbstractJavaDoc {

    public JavaDocAttribute(String name, String textComment, boolean deprecated, String since, Collection<String> see) {

        super(name, textComment, deprecated, since, see);
    }

}
