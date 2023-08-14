package de.itdesign.application;

import de.itdesign.application.pojo.data.Entries;
import de.itdesign.application.pojo.operations.Filter;

public class UtilityImplTest {

    //  test if regex works for data entries
    public void givenFilterAndDataEntries_whenFilterDataEntriesByName_thenReturnFilteredEntries(){

        //  Fixture Filter
        Filter filter = new Filter();
        filter.setFilter(".*e.*n.*");

        //  Fixture Data Entries
        Entries entries = new Entries();

    }

    public void givenFilteredEntries_whenComputeSumOfFilteredEntries_thenReturnSumOfFilteredEntries(){

    }

    public void givenFilteredEntries_whenComputeAverageOfFilteredEntries_thenReturnAverageOfFilteredEntries(){

    }

    public void givenFilteredEntries_whenComputeMinOfFilteredEntries_thenReturnMinOfFilteredEntries(){

    }

    public void givenFilteredEntries_whenComputeMaxOfFilteredEntries_thenReturnMaxOfFilteredEntries(){

    }

    //  used by computeXyzOfFilteredEntries
    public void givenOperationAndFilteredEntries_whenGetfilteredEntriesFieldValues_thenReturnListOfDoubleFieldValues(){

    }

    //  used by getfilteredEntriesFieldValues
    public void givenOperationFieldNameAndFilteredEntries_whenGetListOfFieldValues_thenReturnDoubleListOfFieldValues(){

    }
  
}
