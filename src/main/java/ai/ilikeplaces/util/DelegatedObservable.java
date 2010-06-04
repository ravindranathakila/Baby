package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.ClassPreamble;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;

import java.io.Serializable;
import java.util.Observable;

/**
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@ClassPreamble(
    authors = {"Ravindranath Akila"},
    description = {"Delegated Object which helpes the observer pattern"},
    extentions={},
    version=1)
public class DelegatedObservable extends Observable implements Serializable{

    @NOTE(note = "This override is necessary as the parent method is protected.")
    @Override
    public void setChanged(){
        super.setChanged();
    }
    
    @NOTE(note = "This override is necessary as the parent method is protected.")
    @Override
    public void clearChanged(){
        super.clearChanged();
    }
}
