package de.itdesign.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.itdesign.application.data.*;
import de.itdesign.application.operations.*;
import de.itdesign.application.output.*;


public class UtilityImpl implements UtilityInterface{

    public Operations createOperationsObjectFromOperationsJsonObject(JSONObject operationsJsonObject){
        
        //  Get Operations JsonArray from Operations JsonObject
        JSONArray operationsJsonArray = operationsJsonObject.getJSONArray("operations");
        
        //  Initialize list of Operations
        List<Operation> operationsList = new ArrayList<>();

        //  Get Operations Object from Operations JsonArray
        for(int l=0; l<operationsJsonArray.length();l++){
            
            //  Initialize Operation object
            Operation operation = new Operation();

            //  Get Operation JsonObject from Operation JsonArray element
            JSONObject operationJsonObject = operationsJsonArray.getJSONObject(l);

            //  Fill in Operation object elements from Operation JsonObject
            OperationName operationName = new OperationName();
            operationName.setName(operationJsonObject.getString("name"));
            operation.setOperationName(operationName);
            Function function = new Function();
            function.setFunction(operationJsonObject.getString("function"));
            operation.setFunction(function);
            Filter filter = new Filter();
            filter.setFilter(operationJsonObject.getString("filter"));
            operation.setFilter(filter);
            
            //  Get Operation Fields object from JsonArray fields
            JSONArray fieldJsonArray = operationJsonObject.getJSONArray("field");
            List<Field> fieldsList = new ArrayList<>();
            for (int m= 0; m<fieldJsonArray.length(); m++){
                Field field = new Field();
                field.setField(fieldJsonArray.getString(m));
                fieldsList.add(field);
            }
            //  Create list of Operation fields
            OperationFields operationFields = new OperationFields();
            operationFields.setOperationFieldsList(fieldsList);
            operation.setOperationFields(operationFields);
            operationsList.add(operation);
        }
        
        Operations operations = new Operations();
        operations.setOperationsList(operationsList);
        return operations;
    }

    public JSONArray createOutputsJSONArrayFromOutputsObject(Outputs outputs){

        JSONArray outputsJsonArray = new JSONArray();
        List<Output> outputList = outputs.getOutputsList();
      
        for(Output output : outputList){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", output.getOutputName().getName());
            jsonObject.put("roundedValue" , output.getRoundedValue().getRoundedValue());
            outputsJsonArray.put(jsonObject);

        }

        return outputsJsonArray;
    }

    public void createOutputFileFromOutputsJsonArray(JSONArray outputsJsonArray, String outputFilePath) throws IOException{
        
        String outputsJsonArrayString = outputsJsonArray.toString();
        Files.write(Paths.get(outputFilePath), outputsJsonArrayString.getBytes());
    }

    public Operation createOperationObjectFromOperationJsonObject(JSONObject operationJsonObject){
        
        OperationName operationName = new OperationName();
        Function function = new Function();
        List<Field> fieldsList = new ArrayList<>();
        OperationFields operationFields = new OperationFields();
        Filter filter = new Filter();

        operationName.setName(operationJsonObject.getString("name"));
        function.setFunction(operationJsonObject.getString("function"));
        filter.setFilter(operationJsonObject.getString("filter"));

        JSONArray fieldJsonArray = operationJsonObject.getJSONArray("field");
        for(int k=0; k<fieldJsonArray.length(); k++){
            Field field = new Field();
            field.setField(fieldJsonArray.getString(k));
            fieldsList.add(field);
        }

        operationFields.setOperationFieldsList(fieldsList);
    
        Operation operation = new Operation();
        operation.setOperationName(operationName);
        operation.setFunction(function);
        operation.setOperationFields(operationFields);
        operation.setFilter(filter);

        return operation;
    }

    public Entries createDataEntriesObjectfromDataJsonObject(JSONObject dataJsonObject){
        
        JSONArray entriesJsonArray = dataJsonObject.getJSONArray("entries");
            List<Entry> entryList = new ArrayList<>();
            for (int i=0; i<entriesJsonArray.length();i++){

                JSONObject entryJsonObject = entriesJsonArray.getJSONObject(i);

                DataName dataName = new DataName();                
                Population population = new Population();
                Area area = new Area();
                ExtendedStatistics extendedStatistics = new ExtendedStatistics();

                dataName.setName(entryJsonObject.getString("name"));
                population.setPopulation(entryJsonObject.getDouble("population"));
                JSONObject extendedStatisticsJsonObject = entryJsonObject.getJSONObject("extendedStatistics");
                area.setArea(extendedStatisticsJsonObject.getDouble("area"));
                extendedStatistics.setArea(area);

                Entry entry = new Entry();
                entry.setDataName(dataName);
                entry.setPopulation(population);
                entry.setExtendedStatistics(extendedStatistics);

                entryList.add(entry);

            }
            Entries entries = new Entries();
            entries.setEntriesList(entryList);

        return entries;
    }

    //  Filters the Data Entries using given regex filter
    public Entries filterDataEntriesByName(Entries entries, Filter filter){

        String regexFilter = filter.getFilter();
        Pattern pattern = Pattern.compile(regexFilter);

        List<Entry> entriesList = entries.getEntriesList();
        List<Entry> filteredEntriesList = new ArrayList<>();
        
        for(Entry entry : entriesList){

            String entryName = entry.getDataName().getName();
            Matcher matcher = pattern.matcher(entryName);
            boolean patternMatches = matcher.matches();

            if(patternMatches){
                filteredEntriesList.add(entry);
            }
                    
        }

        Entries filteredEntries = new Entries();
        filteredEntries.setEntriesList(filteredEntriesList);

        return filteredEntries;
    }

    public Output performOperationOnFilteredData(Entries filteredEntries, Operation operation){

        Output output = new Output();

        //  Perform the operation function on the filtered entries
        String operationFunction = operation.getFunction().getFunction();
        
        if (operationFunction.equals("average")){
            output = computeAverageOfFilteredEntries(operation, filteredEntries);
        }
        else if (operationFunction.equals("sum")){
            output = computeSumOfFilteredEntries(operation, filteredEntries);
        }
        else if (operationFunction.equals("max")){
            output = computeMaxOfFilteredEntries(operation, filteredEntries);
        }
        else if (operationFunction.equals("min")){
            output = computeMinOfFilteredEntries(operation, filteredEntries);
        }

        return output;
    }

    public Output computeAverageOfFilteredEntries(Operation operation, Entries filteredEntries){
        
        //  Unbox Operation object
        String operationName = operation.getOperationName().getName();
        
        //  Get a list of Field Values from Filtered Entries for the given Operation fields
        List<Double> filteredEntriesFieldValues = getfilteredEntriesFieldValues(operation, filteredEntries);

        //  Compute the average of all the filtered Entries
        double sum = 0;
        for (Double filteredEntriesFieldValue : filteredEntriesFieldValues){
            sum += filteredEntriesFieldValue;
        }
        double average = sum / filteredEntriesFieldValues.size();

        // Create and return result Output object
        Output output = createResultOutputObject(operationName, average);

        return output;
    }

    //  Get a list of Field Values for given filtered entries and Operation
    public List<Double> getfilteredEntriesFieldValues(Operation operation, Entries filteredEntries){

        //  Get List of fields as Strings
        List<String> operationFieldStringsList = getOperationFieldStringsList(operation);

        //  For each field, get a list of the field values from the filtered entries
        List<Double> filteredEntriesFieldValues = new ArrayList<>();
        for (String operationFieldName : operationFieldStringsList){
            filteredEntriesFieldValues.addAll(getListOfFieldValues(operationFieldName, filteredEntries));
        }
        return filteredEntriesFieldValues;
    }


    public Output computeSumOfFilteredEntries(Operation operation, Entries filteredEntries){
        

        //  Unbox Operation object
        String operationName = operation.getOperationName().getName();
        
        //  Get a list of Field Values from Filtered Entries for the given Operation fields
        List<Double> filteredEntriesFieldValues = getfilteredEntriesFieldValues(operation, filteredEntries);

        //  Compute the sum of all the filtered Entries
        double sum = 0;
        for (Double filteredEntriesFieldValue : filteredEntriesFieldValues){
            sum += filteredEntriesFieldValue;
        }
        
        // Create and return result Output object
        Output output = createResultOutputObject(operationName, sum);

        return output;
    }

    //  Format Double value to a String containing exactly two decimal places
    public String formatDoubleToStringTwoDecimalPlaces(Double unformattedDoubleValue){

        // Format the Double value to exactly two decimal places
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedValue = df.format(unformattedDoubleValue);

        return formattedValue;
    }

    //  Get consolidated list of all field values from the filtered data entries
    public List<Double> getListOfFieldValues(String operationFieldName, Entries filteredEntries){

        List<Double> fieldValuesList = new ArrayList<>();

        List<Entry> filteredEntriesList = filteredEntries.getEntriesList();
        for (Entry entry : filteredEntriesList){
            if(operationFieldName.equals("population")){
                fieldValuesList.add(entry.getPopulation().getPopulation());
            }
            if(operationFieldName.equals("area")){
                fieldValuesList.add(entry.getExtendedStatistics().getArea().getArea());
            }
        }

        return fieldValuesList;
    }

    //  Get a list of strings containing all operation fields
    public List<String> getOperationFieldStringsList(Operation operation){

        List<Field> operationFieldsList = operation.getOperationFields().getOperationFieldsList();
        List<String> operationFieldStringsList = new ArrayList<>();
        for (Field field : operationFieldsList){
            operationFieldStringsList.add(field.getField());
        }
        return operationFieldStringsList;
    }

    
    public Output computeMinOfFilteredEntries(Operation operation, Entries filteredEntries){
        
        //  Unbox Operation object
        String operationName = operation.getOperationName().getName();
        
        //  Get a list of Field Values from Filtered Entries for the given Operation fields
        List<Double> filteredEntriesFieldValues = getfilteredEntriesFieldValues(operation, filteredEntries);

        //  Compute the min of all the filtered Entries
        double min = Collections.min(filteredEntriesFieldValues);
        
        // Create and return result Output object
        Output output = createResultOutputObject(operationName, min);

        return output;
    }

    public Output computeMaxOfFilteredEntries(Operation operation, Entries filteredEntries){

        //  Unbox Operation object
        String operationName = operation.getOperationName().getName();
        
        //  Get a list of Field Values from Filtered Entries for the given Operation fields
        List<Double> filteredEntriesFieldValues = getfilteredEntriesFieldValues(operation, filteredEntries);

        //  Compute the max of all the filtered Entries
        double max = Collections.max(filteredEntriesFieldValues);
        
        // Create and return result Output object
        Output output = createResultOutputObject(operationName, max);

        return output;
    }

    //  Create and return result Output object for given Operation Name and Double value (after rounding it)
    public Output createResultOutputObject(String operationName, Double valueDouble){

        Output output = new Output();
        OutputName outputName = new OutputName();
        outputName.setName(operationName);
        output.setOutputName(outputName);
        RoundedValue roundedValue = new RoundedValue();
        roundedValue.setRoundedValue(formatDoubleToStringTwoDecimalPlaces(valueDouble));
        output.setRoundedValue(roundedValue);

        return output;
    }

    //  Load Json file into JsonObject
    public JSONObject parseJsonFileIntoJsonObject(String fileName) throws JSONException, IOException{
        String jsonFileString = new String(Files.readAllBytes(Paths.get(fileName)));
        return new JSONObject(jsonFileString);
    }
    
   
}
