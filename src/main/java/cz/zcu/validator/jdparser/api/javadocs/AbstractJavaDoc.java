package cz.zcu.validator.jdparser.api.javadocs;

import java.util.Collection;

import cz.zcu.validator.jdparser.api.javadocs.modifiers.Commentable;
import cz.zcu.validator.jdparser.api.javadocs.modifiers.Deprecateable;
import cz.zcu.validator.jdparser.api.javadocs.modifiers.Seeable;
import cz.zcu.validator.jdparser.api.javadocs.modifiers.Sinceable;

/**
 * Design pattern - Adapter. Standard JavaDoc element is able to hold this
 * information.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 6.12.2012
 */
public abstract class AbstractJavaDoc implements Seeable, Sinceable, Deprecateable, Commentable {

    /**
     * Represents name of an JavaDoc element. For attribute, name is attribute
     * name, for methods, its method's name and for class it is class name.
     */
    private String name;

    /**
     * Text comment of an JavaDoc element. Contains only the core text, not
     * content of annotations.
     */
    private String textComment;

    /**
     * Holds whether the JavaDoc element has {@code deprecated} annotation.
     */
    private boolean deprecated;

    /**
     * Holds information about {@code since} annotation.
     */
    private String since;

    /**
     * Holds information about {@code see} annotation.
     */
    private Collection<String> see;

    public AbstractJavaDoc(String name, String textComment, boolean deprecated, String since, Collection<String> see) {

        this.name = name;
        this.textComment = textComment;
        this.deprecated = deprecated;
        this.since = since;
        this.see = see;
    }

    public boolean isEmpty() {

        if (textComment.isEmpty() && !deprecated && since.isEmpty() && see.isEmpty()) {

            return true;
        }

        return false;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @Override
    public boolean hasCommentText() {

        return !textComment.isEmpty();
    }

    @Override
    public String getCommentText() {

        return textComment;
    }

    @Override
    public void setCommentText(String commentText) {

        this.textComment = commentText;
    }

    @Override
    public boolean isDeprecated() {

        return deprecated;
    }

    @Override
    public void setDepreceted(boolean deprecated) {

        this.deprecated = deprecated;
    }

    @Override
    public boolean hasSince() {

        return !since.isEmpty();
    }

    @Override
    public String getSince() {

        return since;
    }

    @Override
    public void setSince(String since) {

        this.since = since;
    }

    @Override
    public boolean hasSee() {

        return !see.isEmpty();
    }

    @Override
    public Collection<String> getSee() {

        return see;
    }

    @Override
    public void setSee(Collection<String> see) {

        this.see = see;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {

            return false;
        }
        if (obj == this) {

            return true;
        }
        if (obj.getClass() != getClass()) {

            return false;
        }

        final AbstractJavaDoc ref = (AbstractJavaDoc) obj;
        if (ref.getName().equals(getName()) && ref.getCommentText().equals(getCommentText()) && ref.getSee().equals(getSee())
                && ref.getSince().equals(getSince()) && (ref.isDeprecated() == isDeprecated())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 37 * (getName().hashCode() + getSee().hashCode() + getCommentText().hashCode() + getSince().hashCode() + Boolean.toString(isDeprecated())
                .hashCode());
    }

    @Override
    public String toString() {

        return "NAME: " + getName() + " COMMENT_TEXT: " + getCommentText() + " DEPRACATED: " + isDeprecated() + " SEE: " + getSee() + " SINCE: " + getSince();
    }

}
