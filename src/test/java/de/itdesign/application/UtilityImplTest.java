package de.itdesign.application;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.itdesign.application.pojo.data.Area;
import de.itdesign.application.pojo.data.DataName;
import de.itdesign.application.pojo.data.Entries;
import de.itdesign.application.pojo.data.Entry;
import de.itdesign.application.pojo.data.ExtendedStatistics;
import de.itdesign.application.pojo.data.Population;
import de.itdesign.application.pojo.operations.Field;
import de.itdesign.application.pojo.operations.Filter;
import de.itdesign.application.pojo.operations.Function;
import de.itdesign.application.pojo.operations.Operation;
import de.itdesign.application.pojo.operations.OperationFields;
import de.itdesign.application.pojo.operations.OperationName;
import de.itdesign.application.pojo.output.Output;
import de.itdesign.application.pojo.output.OutputName;
import de.itdesign.application.pojo.output.RoundedValue;
import de.itdesign.application.util.UtilityImpl;

public class UtilityImplTest {

    @Test
    public void givenFilterAndDataEntries_whenFilterDataEntriesByName_thenReturnFilteredEntries(){

        //  test if regex works for data entries
        Filter filter = getFixtureFilter();
        Entries entries = getFixtureDataEntries();
        
        UtilityImpl utilityImpl = new UtilityImpl();
        Entries actualEntries = utilityImpl.filterDataEntriesByName(entries, filter);

        DataName dataName = new DataName();
        dataName.setName("Regensburg");
        Population population = new Population();
        population.setPopulation(134218);
        Area area = new Area();
        area.setArea(80.76);
        ExtendedStatistics extendedStatistics = new ExtendedStatistics();
        extendedStatistics.setArea(area);

        Entry entry = new Entry();
        entry.setDataName(dataName);
        entry.setPopulation(population);
        entry.setExtendedStatistics(extendedStatistics);

        Entries expectedEntries =new Entries();
        List<Entry> dataList = Arrays.asList(entry);
        expectedEntries.setEntriesList(dataList);

        //Assertion is comparing object hash not object contents : How to compare the contents?
        Assert.assertArrayEquals("Filter should apply regex and return filtered data", expectedEntries.getEntriesList().toArray(), actualEntries.getEntriesList().toArray());
        
    }

    public void givenEntries_whenComputeSumOfEntries_thenReturnSumOfEntries(){

        Entries filteredEntries = getFixtureDataEntries();
        
        Function function = new Function();
        function.setFunction("sum");
        Operation operation = new Operation();
        Field field = new Field();
        field.setField("population");
        OperationFields operationFields = new OperationFields();
        List<Field> operationFieldsList = Arrays.asList(field);
        operationFields.setOperationFieldsList(operationFieldsList);
        OperationName operationName = new OperationName();
        operationName.setName("information");

        operation.setFunction(function);
        operation.setOperationName(operationName);

        Output expectedOutput = new Output();
        OutputName expectedOutputName = new OutputName();
        RoundedValue expectedRoundedValue = new RoundedValue();
        expectedOutputName.setName("information");
        expectedRoundedValue.setRoundedValue("836.02");
        expectedOutput.setOutputName(expectedOutputName);
        expectedOutput.setRoundedValue(expectedRoundedValue);
        

    }

    public void givenEntries_whenComputeAverageOfEntries_thenReturnAverageOfEntries(){

    }

    public void givenEntries_whenComputeMinOfEntries_thenReturnMinOfEntries(){

    }

    public void givenEntries_whenComputeMaxOfEntries_thenReturnMaxOfEntries(){

    }

    //  used by computeXyzOfFilteredEntries
    public void givenOperationAndFilteredEntries_whenGetfilteredEntriesFieldValues_thenReturnListOfDoubleFieldValues(){

    }

    //  used by getfilteredEntriesFieldValues
    public void givenOperationFieldNameAndFilteredEntries_whenGetListOfFieldValues_thenReturnDoubleListOfFieldValues(){

    }

    //  Fixture Filter
    public Filter getFixtureFilter(){

        Filter filter = new Filter();
        filter.setFilter(".*e.*n.*");

        return filter;

    }

    //  Fixture Data Entries
    public Entries getFixtureDataEntries(){
        
        DataName dataName1 = new DataName();
        dataName1.setName("New York");
        Population population1 = new Population();
        population1.setPopulation(123456789);
        Area area1 = new Area();
        area1.setArea(987654321);
        ExtendedStatistics extendedStatistics1 = new ExtendedStatistics();
        extendedStatistics1.setArea(area1);

        Entry entry1 = new Entry();
        entry1.setDataName(dataName1);
        entry1.setPopulation(population1);
        entry1.setExtendedStatistics(extendedStatistics1);


        DataName dataName2 = new DataName();
        dataName2.setName("Seattle");
        Population population2 = new Population();
        population2.setPopulation(258741369);
        Area area2 = new Area();
        area2.setArea(654159);
        ExtendedStatistics extendedStatistics2 = new ExtendedStatistics();
        extendedStatistics2.setArea(area2);

        Entry entry2 = new Entry();
        entry2.setDataName(dataName2);
        entry2.setPopulation(population2);
        entry2.setExtendedStatistics(extendedStatistics2);


        DataName dataName3 = new DataName();
        dataName3.setName("Regensburg");
        Population population3 = new Population();
        population3.setPopulation(134218);
        Area area3 = new Area();
        area3.setArea(80.76);
        ExtendedStatistics extendedStatistics3 = new ExtendedStatistics();
        extendedStatistics3.setArea(area3);

        Entry entry3 = new Entry();
        entry3.setDataName(dataName3);
        entry3.setPopulation(population3);
        entry3.setExtendedStatistics(extendedStatistics3);
        

        Entries entries =new Entries();
        List<Entry> dataList = Arrays.asList(entry1, entry2, entry3);
        entries.setEntriesList(dataList);

        return entries;
    }
  
    //  Fixture Operation
    public Operation getFixtureOperationSum(){

        Filter filter = new Filter();
        filter.setFilter(".*e.*n.*");

        return null;

    }
}
