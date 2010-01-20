package ai.ilikeplaces.util;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 10:41:07 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class RefString implements RefStringFace{

    private String string;

    public RefString(final String str) {
        this.string = str;
    }

    public String getString() {
        return string;
    }

    public void setString(final String string) {
        this.string = string;
    }
}