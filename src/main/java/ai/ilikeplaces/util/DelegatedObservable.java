package ai.ilikeplaces.util;

import ai.scribble.License;
import ai.scribble._class_preamble;
import ai.scribble._note;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_class_preamble(
        authors = {"Ravindranath Akila"},
        description = {"Delegated Object which helpes the observer pattern"},
        extentions = {},
        version = 1)
public class DelegatedObservable extends Observable implements Serializable {

    public interface DelegatedObservableHelper {
        /**
         * @param o
         */
        public void addObserver(Observer o);

        /**
         * @param o
         */
        public void deleteObserver(Observer o);
    }

    @_note(note = "This override is necessary as the parent method is protected.")
    @Override
    public void setChanged() {
        super.setChanged();
    }

    @_note(note = "This override is necessary as the parent method is protected.")
    @Override
    public void clearChanged() {
        super.clearChanged();
    }
}
