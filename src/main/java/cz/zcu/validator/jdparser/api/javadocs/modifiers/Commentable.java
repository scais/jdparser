package cz.zcu.validator.jdparser.api.javadocs.modifiers;

/**
 * Every standard elements should be able go give it's comment's text. Comment
 * text is the core text, which should be in every JavaDoc element. This text
 * does not contain information from annotations.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public interface Commentable {

    /**
     * Returns whether the JavaDoc element contains comment text.
     * 
     * @return True, if comment contains some text, otherwise false.
     */
    public boolean hasCommentText();

    /**
     * Gets the comment's main text. This method return only the core text of
     * the comment, not all comment's parts. This method doesn't return
     * parameters, version and other annotation's texts.
     * 
     * @return Text of the comment.
     */
    public String getCommentText();

    /**
     * Sets the comment's main text.
     * 
     * @param commentText
     *            Comment text to set.
     */
    public void setCommentText(String commentText);

}
