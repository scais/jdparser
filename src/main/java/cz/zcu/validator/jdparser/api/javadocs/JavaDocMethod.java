package cz.zcu.validator.jdparser.api.javadocs;

import java.util.Collection;
import java.util.Map;

import cz.zcu.validator.jdparser.api.javadocs.modifiers.Paramable;
import cz.zcu.validator.jdparser.api.javadocs.modifiers.Returnable;

/**
 * Design pattern - Messenger.
 * 
 * Represents one comment of a method.
 * 
 * @author Stepan Cais, scais@kiv.zcu.cz, 30.11.2012
 */
public class JavaDocMethod extends AbstractJavaDoc implements Returnable, Paramable {

    /**
     * Texts with {@code param} annotation. Because there could be more
     * {@code param} annotations and every param annotation has two parts (type
     * of parameter and it´s description), text are saved in map inside list.
     */
    private Map<String, String> params;

    /**
     * Text with {@code return} annotation.
     */
    private String returns;

    public JavaDocMethod(String name, String textComment, boolean deprecated, String since, Collection<String> see, Map<String, String> params, String returns) {

        super(name, textComment, deprecated, since, see);

        this.params = params;
        this.returns = returns;
    }

    @Override
    public boolean isEmpty() {

        return super.isEmpty() && params.isEmpty() && returns.isEmpty();
    }

    @Override
    public boolean hasParametersUsed() {

        return !params.isEmpty();
    }

    @Override
    public boolean allParametersHaveDescription() {

        if (!hasParametersUsed()) {

            return false;
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {

            if (entry.getValue().isEmpty()) {

                return false;
            }
        }

        return true;
    }

    @Override
    public Map<String, String> getParameters() {

        return params;
    }

    @Override
    public void setParameters(Map<String, String> parameters) {

        this.params = parameters;
    }

    @Override
    public boolean hasReturn() {

        return !returns.isEmpty();
    }

    @Override
    public String getReturn() {

        return returns;
    }

    @Override
    public void setReturn(String returnParam) {

        this.returns = returnParam;
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

        final JavaDocMethod ref = (JavaDocMethod) obj;
        if (ref.getParameters().equals(getParameters()) && ref.getReturn().equals(getReturn())) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return 17 * (getParameters().hashCode() + getReturn().hashCode()) + super.hashCode();
    }

    @Override
    public String toString() {

        return super.toString() + " PARAMETERS: " + getParameters() + " RETURNS: " + getReturn();
    }

}
