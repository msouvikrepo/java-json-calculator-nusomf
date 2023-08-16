package de.itdesign.application.util;

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

import de.itdesign.application.model.data.*;
import de.itdesign.application.model.operations.*;
import de.itdesign.application.model.output.*;


public class UtilityImpl implements Utility{

    /***
     * Creates an object of type Operations from the type JSONObject loaded from the Operations json file
     * @param operationsJsonObject JSONObject representing operation elements
     * @return Object Operations, representing a list of operation objects
     */
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

    /***
     * Generates the JSON Array representation of the output json file
     * @param outputs Object of type Outputs representing a list of output objects
     * @return JSONArray representing the output file
     */
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

    /***
     * Writes final output file containing an array of output elements into the sys path
     * @param outputsJsonArray JSONArray representing the output file
     * @param outputFilePath String representing the sys path to write output json file to
     * @throws IOException in case of non-existent (path or access privileges) exception is moved up the chain to be caught and logged
     */
    public void createOutputFileFromOutputsJsonArray(JSONArray outputsJsonArray, String outputFilePath) throws IOException{
        
        String outputsJsonArrayString = outputsJsonArray.toString();
        Files.write(Paths.get(outputFilePath), outputsJsonArrayString.getBytes());
    }

    /***
     * Create an object of type Operation containing the representation of an operation from the corresponding Json object
     * @param operationJsonObject JSONObject representing a single operation
     * @return Object of type Operation
     */
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

    /***
     * Creates an object representing list of data entries
     * @param dataJsonObject JSONObject representing the list of data entries
     * @return Object of type Entries representing the given data entries 
     */
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

    /***
     * Filters the Data Entries using a regular expression
     * @param entries Object of type Entries representing the list of data entries, the names on which to apply the filter
     * @param filter Object of type Filter representing the regex to be applied
     * @return Object of type Entries representing a list of the filtered data entry elements
     */
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

    /***
     * Calculates sum, average, max and min over filtered data entries
     * @param filteredEntries Object of type entries containing list of filtered data entries
     * @param operation Operation object specifying the operation name identifier, operation function and data fields
     * @return Object of type output containing the name identifier and the result, rounded off to two decimal places
     */
    public Output performOperationOnFilteredData(Entries filteredEntries, Operation operation){

        Output output = new Output();

        //  Perform the operation function on the filtered entries
        String operationFunction = operation.getFunction().getFunction();
  
        // The aggregation of the list of field double values is done within the compute functions to allow future groupBy operations at the cost of code repition
        
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

    /***
     * Calculates average of filtered data entries
     * @param filteredEntries Object of type entries containing list of filtered data entries
     * @param operation Operation object specifying the operation name identifier
     * @return Object of type output containing the name identifier and the average, rounded off to two decimal places
     */
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

    /***
     * Get a consolidated list of Field Values for given filtered entries and Operation, for operations that require aggregation of all field values
     * @param operation Operation object specifying the operation fields
     * @param filteredEntries Object representing the list of filtered data entries
     * @return list of double field values
     */
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

    /***
     * Calculates sum of filtered data entries
     * @param filteredEntries Object of type entries containing list of filtered data entries
     * @param operation Operation object specifying the operation name identifier
     * @return Object of type output containing the name identifier and the sum, rounded off to two decimal places
     */
    public Output computeSumOfFilteredEntries(Operation operation, Entries filteredEntries){
        

        //  Unbox Operation object to get Operation Name
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

    /***
     * Format Double value to a String containing exactly two decimal places
     * @param unformattedDoubleValue the computed result double value
     * @return string represention of computed result to two decimal places
     */
    public String formatDoubleToStringTwoDecimalPlaces(Double unformattedDoubleValue){

        // Format the Double value to exactly two decimal places
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedValue = df.format(unformattedDoubleValue);

        return formattedValue;
    }

    /***
     * Get list of field values from the filtered data entries
     * @param operationFieldName the field to access the values of
     * @param filteredEntries object representing list of filtered data entries
     * @return list of field values
     */
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


    /***
     * Get a list of strings containing all operation fields
     * @param operation object representing the operation
     * @return list of data fields
     */
    public List<String> getOperationFieldStringsList(Operation operation){

        List<Field> operationFieldsList = operation.getOperationFields().getOperationFieldsList();
        List<String> operationFieldStringsList = new ArrayList<>();
        for (Field field : operationFieldsList){
            operationFieldStringsList.add(field.getField());
        }
        return operationFieldStringsList;
    }

    /***
     * Calculates min of filtered data entries
     * @param filteredEntries object of type entries containing list of filtered data entries
     * @param operation the operation object specifying the operation name identifier
     * @return object of type output containing the name identifier and the min, rounded off to two decimal places
     */
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

    /***
     * Calculates max of filtered data entries
     * @param filteredEntries object of type entries containing list of filtered data entries
     * @param operation the operation object specifying the operation name identifier
     * @return object of type output containing the name identifier and the max, rounded off to two decimal places
     */
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

    /***
     * Creates singular result Output object
     * @param operationName Operation name identifier
     * @param valueDouble Double value of the result
     * @return Object of type output representing a sinle output element
     */
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

    /***
     * Load Json file into JsonObject
     * @param fileName String path+file name
     * @return JSONObject Loaded from file
     * @exception IOException in case of sys access errors, exception is to be carried up to caller
     * @exception JSONException in case of broken or invalid json format, exception is to be carried up to caller
     */
    public JSONObject parseJsonFileIntoJsonObject(String fileName) throws JSONException, IOException{
        String jsonFileString = new String(Files.readAllBytes(Paths.get(fileName)));
        return new JSONObject(jsonFileString);
    }
    
   
}
