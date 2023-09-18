package de.jsoncalculator.application.model.data;

import java.util.ArrayList;
import java.util.List;

public class Entries {

    private List<Entry> entriesList = new ArrayList<>();

    public List<Entry> getEntriesList() {
        return entriesList;
    }

    public void setEntriesList(List<Entry> entriesList) {
        this.entriesList = entriesList;
    }
    
}
