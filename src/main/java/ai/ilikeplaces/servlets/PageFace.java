package ai.ilikeplaces.servlets;

import ai.scribble.License;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
interface PageFace {

    @Override
    public String toString();

    public String getURL();
}
