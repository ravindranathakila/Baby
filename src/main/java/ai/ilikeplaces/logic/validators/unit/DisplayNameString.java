package ai.ilikeplaces.logic.validators.unit;

import ai.reaver.RefObj;
import ai.scribble.License;
import net.sf.oval.configuration.annotation.IsInvariant;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 21, 2010
 * Time: 8:58:01 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class DisplayNameString extends RefObj<String> {
// --------------------------- CONSTRUCTORS ---------------------------

    public DisplayNameString() {
    }

    /**
     * @param displayNameToBeSet
     */
    public DisplayNameString(final String displayNameToBeSet) {
        this.obj = displayNameToBeSet;
    }

// ------------------------ ACCESSORS / MUTATORS ------------------------

    @IsInvariant
    @NotNull(message = "Sorry! You must enter a value.")
    @Length(min = 2, max = 255)
    @NotBlank
    public String getObj() {
        return obj;
    }
}
