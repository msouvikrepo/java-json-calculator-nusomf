package de.jsoncalculator.application;

import de.jsoncalculator.application.model.operations.Function;
import de.jsoncalculator.application.model.operations.OperationName;
import de.jsoncalculator.application.model.operations.Operations;
import de.jsoncalculator.application.model.output.Output;
import de.jsoncalculator.application.model.output.OutputName;
import de.jsoncalculator.application.model.output.Outputs;
import de.jsoncalculator.application.model.output.RoundedValue;
import de.jsoncalculator.application.util.UtilityImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import de.jsoncalculator.application.model.data.Area;
import de.jsoncalculator.application.model.data.DataName;
import de.jsoncalculator.application.model.data.Entries;
import de.jsoncalculator.application.model.data.Entry;
import de.jsoncalculator.application.model.data.ExtendedStatistics;
import de.jsoncalculator.application.model.data.Population;
import de.jsoncalculator.application.model.operations.Field;
import de.jsoncalculator.application.model.operations.Filter;
import de.jsoncalculator.application.model.operations.Operation;
import de.jsoncalculator.application.model.operations.OperationFields;

public class UtilityImplTest {

    @Test
    public void givenFilterAndDataEntries_whenFilterDataEntriesByName_thenReturnFilteredEntries(){

        //  test if regex works for data entries
                
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

        List<Entry> expectedEntriesList = Arrays.asList(entry);

        UtilityImpl utilityImpl = new UtilityImpl();
        List<Entry> actualEntriesList = utilityImpl.filterDataEntriesByName(getFixtureDataEntries(), getFixtureFilter()).getEntriesList();

        assertThat(expectedEntriesList).usingRecursiveFieldByFieldElementComparator().isEqualTo(actualEntriesList);

    }

    @Test
    public void givenJsonObject_whenCreateDataEntriesObjectfromDataJsonObject_thenReturnDatEntriesObject(){

        List<Entry> expectedEntriesList = getFixtureDataEntries().getEntriesList();

        UtilityImpl utilityImpl = new UtilityImpl();
        List<Entry> actualEntriesList = utilityImpl.createDataEntriesObjectfromDataJsonObject(getFixtureDataJsonObject()).getEntriesList();

        assertThat(expectedEntriesList).usingRecursiveFieldByFieldElementComparator().isEqualTo(actualEntriesList);

    }

    @Test
    public void givenJsonObject_whencreateOperationsObjectFromOperationsJsonObject_thenReturnOperationsObject(){

        List<Operation> expectedOperationList = getFixtureOperationsObject().getOperationsList();
        UtilityImpl utilityImpl = new UtilityImpl();
        List<Operation> actualOperationList = utilityImpl.createOperationsObjectFromOperationsJsonObject(getFixtureOperationsJsonObject()).getOperationsList();

        assertThat(expectedOperationList).usingRecursiveFieldByFieldElementComparator().isEqualTo(actualOperationList);
    }

    @Test
    public void givenOutputsObject_whenCreateOutputsJSONArrayFromOutputsObject_thenReturnJSONArray(){

        JSONArray expectedOutputsJsonArray = getFixtureOutputsJsonArray();
        UtilityImpl utilityImpl = new UtilityImpl();
        JSONArray actualOutputsJsonArray = utilityImpl.createOutputsJSONArrayFromOutputsObject(getFixtureOutputs());

        assertThat(expectedOutputsJsonArray).usingRecursiveFieldByFieldElementComparator().isEqualTo(actualOutputsJsonArray);
    }
    
    //  used by getfilteredEntriesFieldValues
    @Test
    public void givenOperationFieldNameAndFilteredEntries_whenGetListOfFieldValues_thenReturnDoubleListOfFieldValues(){

        List<Double> expectedListOfFieldValues = getFixtureListOfFieldValues();
        UtilityImpl utilityImpl = new UtilityImpl();
        List<Double> actualListOfFieldValues = utilityImpl.getListOfFieldValues("area", getFixtureDataEntries());

        assertThat(expectedListOfFieldValues).usingRecursiveFieldByFieldElementComparator().isEqualTo(actualListOfFieldValues);
    }

    public List<Double> getFixtureListOfFieldValues(){

        //  list of areas
        List<Double> fixtureListOfFieldValues= Arrays.asList(987654.321, 6541.59, 80.76);
        return fixtureListOfFieldValues;
    }

    public Outputs getFixtureOutputs(){

        OutputName firstOutputName = new OutputName();
        firstOutputName.setName("important");
        RoundedValue firstRoundedValue = new RoundedValue();
        firstRoundedValue.setRoundedValue("4030418.67");

        Output firstOutput = new Output();
        firstOutput.setOutputName(firstOutputName);
        firstOutput.setRoundedValue(firstRoundedValue);

        Outputs outputs = new Outputs();
        outputs.setOutputsList(Arrays.asList(firstOutput));

        return outputs;
    }

    public JSONArray getFixtureOutputsJsonArray(){

        String outputsJsonArrayString = "[ {\n" + //
                "  \"name\" : \"important\",\n" + //
                "  \"roundedValue\" : \"4030418.67\"\n" + //
                "}]";

        JSONArray jsonArray = new JSONArray(outputsJsonArrayString);
        
        return jsonArray;
    }

    //  Fixture Filter
    public Filter getFixtureFilter(){

        Filter filter = new Filter();
        filter.setFilter(".*e.*n.*");

        return filter;

    }

    //  Fixture Function
    public Function getFixtureFunction(){

        Function function = new Function();
        function.setFunction("max");

        return function;

    }

    //  Fixture Operation fields
    public OperationFields getFixtureOperationFields(){

        Field firstField = new Field();
        firstField.setField("population");
        OperationFields operationFields = new OperationFields();
        operationFields.setOperationFieldsList(Arrays.asList(firstField));
        return operationFields;
    }

    //  Fixture Operation Name
    public OperationName getFixtureOperationName(){

        OperationName operationName = new OperationName();
        operationName.setName("future");
        return operationName;
    }

    //  Fixture Operations object
    public Operations getFixtureOperationsObject(){

        Operation firstOperation = new Operation();
        firstOperation.setFilter(getFixtureFilter());
        firstOperation.setFunction(getFixtureFunction());
        firstOperation.setOperationFields(getFixtureOperationFields());
        firstOperation.setOperationName(getFixtureOperationName());

        Operations operations = new Operations();
        operations.setOperationsList(Arrays.asList(firstOperation));

        return operations;
    }

    //  Fixture Operations JsonObject
    public JSONObject getFixtureOperationsJsonObject(){

        String operationsJsonString = "{\n" + //
                "  \"operations\": [\n" + //
                "    {\n" + //
                "      \"name\": \"future\",\n" + //
                "      \"function\": \"max\",\n" + //
                "      \"field\": [\n" + //
                "        \"population\"\n" + //
                "      ],\n" + //
                "      \"filter\": \".*e.*n.*\"\n" + //
                "    }]}";

        JSONObject jsonObject = new JSONObject(operationsJsonString);
        return jsonObject;
    }

    //  Fixture data JsonObject
    public JSONObject getFixtureDataJsonObject(){

        String dataJsonString = "{\n" + //
                "  \"entries\": [\n" + //
                "    {\n" + //
                "      \"name\": \"New York\",\n" + //
                "      \"population\": 123456789,\n" + //
                "      \"extendedStatistics\": {\n" + //
                "        \"area\": 987654.321\n" + //
                "      }\n" + //
                "    },\n" + //
                "    {\n" + //
                "      \"name\": \"Seattle\",\n" + //
                "      \"population\": 258741369,\n" + //
                "      \"extendedStatistics\": {\n" + //
                "        \"area\": 6541.59\n" + //
                "      }\n" + //
                "    },\n" + //
                "    {\n" + //
                "      \"name\": \"Regensburg\",\n" + //
                "      \"population\": 134218,\n" + //
                "      \"extendedStatistics\": {\n" + //
                "        \"area\": 80.76\n" + //
                "      }\n" + //
                "    }]}";

        JSONObject jsonObject = new JSONObject(dataJsonString);
        return jsonObject;
    }

    //  Fixture Data Entries
    public Entries getFixtureDataEntries(){
        
        DataName dataNameOne = new DataName();
        dataNameOne.setName("New York");
        Population populationOne = new Population();
        populationOne.setPopulation(123456789);
        Area areaOne = new Area();
        areaOne.setArea(987654.321);
        ExtendedStatistics extendedStatisticsOne = new ExtendedStatistics();
        extendedStatisticsOne.setArea(areaOne);

        Entry entryOne = new Entry();
        entryOne.setDataName(dataNameOne);
        entryOne.setPopulation(populationOne);
        entryOne.setExtendedStatistics(extendedStatisticsOne);


        DataName dataNameTwo = new DataName();
        dataNameTwo.setName("Seattle");
        Population populationTwo = new Population();
        populationTwo.setPopulation(258741369);
        Area areaTwo = new Area();
        areaTwo.setArea(6541.59);
        ExtendedStatistics extendedStatisticsTwo = new ExtendedStatistics();
        extendedStatisticsTwo.setArea(areaTwo);

        Entry entryTwo = new Entry();
        entryTwo.setDataName(dataNameTwo);
        entryTwo.setPopulation(populationTwo);
        entryTwo.setExtendedStatistics(extendedStatisticsTwo);


        DataName dataNameThree = new DataName();
        dataNameThree.setName("Regensburg");
        Population populationThree = new Population();
        populationThree.setPopulation(134218);
        Area areaThree = new Area();
        areaThree.setArea(80.76);
        ExtendedStatistics extendedStatisticsThree = new ExtendedStatistics();
        extendedStatisticsThree.setArea(areaThree);

        Entry entryThree = new Entry();
        entryThree.setDataName(dataNameThree);
        entryThree.setPopulation(populationThree);
        entryThree.setExtendedStatistics(extendedStatisticsThree);
        

        Entries entries =new Entries();
        List<Entry> dataList = Arrays.asList(entryOne, entryTwo, entryThree);
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
