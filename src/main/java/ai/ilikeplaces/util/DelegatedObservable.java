package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.*;
import java.util.Observable;

/**
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@ClassPreamble(
    authors = {"Ravindranath Akila"},
    description = {"Delegated Object which helpes the observer pattern"},
    extentions={},
    version=1)
public class DelegatedObservable extends Observable{

    @Override
    public void setChanged(){
        super.setChanged();
    }
    @Override
    public void clearChanged(){
        super.clearChanged();
    }
}
