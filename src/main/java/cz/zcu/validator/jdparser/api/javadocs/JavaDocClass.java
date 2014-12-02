package cz.zcu.validator.jdparser.api.javadocs;

import java.util.Collection;

import cz.zcu.validator.jdparser.api.javadocs.modifiers.Authorable;
import cz.zcu.validator.jdparser.api.javadocs.modifiers.Versionable;

/**
 * Design pattern - Messenger.
 * 
 * Represents comment of one class. In addition to standard JavaDoc element
 * {@code JavaDocClass} can contain {@code author} and {@code version}.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public class JavaDocClass extends AbstractJavaDoc implements Authorable, Versionable {

    /**
     * Text with {@code author} annotation.
     */
    private Collection<String> authors;

    /**
     * Text with {@code version} annotation.
     */
    private String version;

    public JavaDocClass(String name, String textComment, boolean deprecated, String since, Collection<String> see, Collection<String> authors, String version) {

        super(name, textComment, deprecated, since, see);

        this.authors = authors;
        this.version = version;
    }

    @Override
    public boolean isEmpty() {

        return super.isEmpty() && authors.isEmpty() && version.isEmpty();
    }

    @Override
    public boolean hasVersion() {

        return !version.isEmpty();
    }

    @Override
    public String getVersion() {

        return version;
    }

    @Override
    public void setVersion(String version) {

        this.version = version;
    }

    @Override
    public boolean hasAuthors() {

        return !authors.isEmpty();
    }

    @Override
    public Collection<String> getAuthors() {

        return authors;
    }

    @Override
    public void setAuthors(Collection<String> authors) {

        this.authors = authors;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {

            return false;
        }
        if (obj == this) {

            return true;
        }
        if (obj.getClass() != this.getClass()) {

            return false;
        }
        if (!super.equals(obj)) {

            return false;
        }

        final JavaDocClass ref = (JavaDocClass) obj;
        if (ref.getAuthors().equals(getAuthors()) && ref.getVersion().equals(getVersion())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 17 * (getAuthors().hashCode() + getVersion().hashCode()) + super.hashCode();
    }

    @Override
    public String toString() {

        return super.toString() + " AUTHORS: " + getAuthors() + " VERSION: " + getVersion();
    }

}
