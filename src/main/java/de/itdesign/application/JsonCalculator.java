package de.itdesign.application;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonCalculator {

    public static void main(String[] args) throws JSONException, IOException {
        // Don't change this part
        if (args.length == 3) {
            // Path to the data file, e.g. data/data.xml
            final String DATA_FILE = args[0];
            // Path to the data file, e.g. operations/operations.xml
            final String OPERATIONS_FILE = args[1];
            // Path to the output file
            final String OUTPUT_FILE = args[2];

            // <my code here>
            

            //  Read Data file into Data Json Object
            JSONObject dataJsonObject = parseJsonFileIntoJsonObject(DATA_FILE);
            
            //  Fill Data Entries Object from Data Json Object
            Entries entries = createDataEntriesObjectfromDataJsonObject(dataJsonObject);

            //  Read Operations file into Operations JsonObject
            JSONObject operationsJsonObject = parseJsonFileIntoJsonObject(OPERATIONS_FILE);
            
            //  Get Operations object from Operations JsonObject
            Operations operations = createOperationsObjectFromOperationsJsonObject(operationsJsonObject);
            
            //  Get list of Operations from Operations object
            List<Operation> operationList = operations.getOperationsList();

            //  Inititialize list of Output objects
            List<Output> outputsList = new ArrayList<>();

            //  for each Operation object, perform func operation on filtered data entries object that returns an output object
            for(Operation operation : operationList){
               
                // get filtered Entries Object from current Operation filter
                Entries filteredEntries = filterDataEntriesByName(entries, operation.getFilter());
                
                //perform operation on filtered data to get output object
                Output output = performOperationOnFilteredData(filteredEntries, operation);
                
                //Add output to list of outputs object
                outputsList.add(output);
                
            }

           //  Create Outputs object from list of output objects
            Outputs outputs = new Outputs();
            outputs.setOutputsList(outputsList);

            // Create Outputs JsonArray from Outputs object
            JSONArray outputsJsonArray = createOutputsJSONArrayFromOutputsObject(outputs);

            // Create Outputs Json file from Outputs JsonArray
            createOutputFileFromOutputsJsonArray(outputsJsonArray, OUTPUT_FILE);

        } else {
            System.exit(1);
        }
    }



    public static Operations createOperationsObjectFromOperationsJsonObject(JSONObject operationsJsonObject){
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
            Name name = new Name();
            name.setName(operationJsonObject.getString("name"));
            operation.setName(name);
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

    public static JSONArray createOutputsJSONArrayFromOutputsObject(Outputs outputs){

        JSONArray outputsJsonArray = new JSONArray();
        List<Output> outputList = outputs.getOutputsList();
      
        for(Output output : outputList){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", output.getName().getName());
            jsonObject.put("roundedValue" , output.getRoundedValue().getRoundedValue());
            outputsJsonArray.put(jsonObject);

        }

        return outputsJsonArray;
    }

    public static void createOutputFileFromOutputsJsonArray(JSONArray outputsJsonArray, String outputFilePath) throws IOException{
        
        String outputsJsonArrayString = outputsJsonArray.toString();
        Files.write(Paths.get(outputFilePath), outputsJsonArrayString.getBytes());
    }

    public static Operation createOperationObjectFromOperationJsonObject(JSONObject operationJsonObject){
        
        Name name = new Name();
        Function function = new Function();
        List<Field> fieldsList = new ArrayList<>();
        OperationFields operationFields = new OperationFields();
        Filter filter = new Filter();

        name.setName(operationJsonObject.getString("name"));
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
        operation.setName(name);
        operation.setFunction(function);
        operation.setOperationFields(operationFields);
        operation.setFilter(filter);

        return operation;
    }

    public static Entries createDataEntriesObjectfromDataJsonObject(JSONObject dataJsonObject){
        
        JSONArray entriesJsonArray = dataJsonObject.getJSONArray("entries");
            List<Entry> entryList = new ArrayList<>();
            for (int i=0; i<entriesJsonArray.length();i++){

                JSONObject entryJsonObject = entriesJsonArray.getJSONObject(i);

                Name name = new Name();
                Population population = new Population();
                Area area = new Area();
                ExtendedStatistics extendedStatistics = new ExtendedStatistics();

                name.setName(entryJsonObject.getString("name"));
                population.setPopulation(entryJsonObject.getDouble("population"));
                JSONObject extendedStatisticsJsonObject = entryJsonObject.getJSONObject("extendedStatistics");
                area.setArea(extendedStatisticsJsonObject.getDouble("area"));
                extendedStatistics.setArea(area);

                Entry entry = new Entry();
                entry.setName(name);
                entry.setPopulation(population);
                entry.setExtendedStatistics(extendedStatistics);

                entryList.add(entry);

            }
            Entries entries = new Entries();
            entries.setEntriesList(entryList);

        return entries;
    }

    public static Entries filterDataEntriesByName(Entries entries, Filter filter){

        String regexFilter = filter.getFilter();
        Pattern pattern = Pattern.compile(regexFilter);

        List<Entry> entriesList = entries.getEntriesList();
        List<Entry> filteredEntriesList = new ArrayList<>();
        
        for(Entry entry : entriesList){

            String entryName = entry.getName().getName();
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

    public static Output performOperationOnFilteredData(Entries filteredEntries, Operation operation){

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

    public static Output computeAverageOfFilteredEntries(Operation operation, Entries filteredEntries){
        
        //  Unbox Operation object
        String operationName = operation.getName().getName();
        
        //  Get List of fields as Strings
        List<String> operationFieldStringsList = getOperationFieldStringsList(operation);

        //  For each field, get a list of the field values from the filtered entries
        List<Double> filteredEntriesFieldValues = new ArrayList<>();
        for (String operationFieldName : operationFieldStringsList){
            filteredEntriesFieldValues.addAll(getListOfFieldValues(operationFieldName, filteredEntries));
        }

        //  Compute the average of all the filtered Entries
        double sum = 0;
        for (Double filteredEntriesFieldValue : filteredEntriesFieldValues){
            sum += filteredEntriesFieldValue;
        }
        double average = sum / filteredEntriesFieldValues.size();

        // Result Output object
        Output output = new Output();
        Name name = new Name();
        name.setName(operationName);
        output.setName(name);
        RoundedValue roundedValue = new RoundedValue();
        roundedValue.setRoundedValue(formatDoubleToStringTwoDecimalPlaces(average));
        output.setRoundedValue(roundedValue);

        return output;
    }

    public static Output computeSumOfFilteredEntries(Operation operation, Entries filteredEntries){
        

        //  Unbox Operation object
        String operationName = operation.getName().getName();
        
        //  Get List of fields as Strings
        List<String> operationFieldStringsList = getOperationFieldStringsList(operation);

        //  For each field, get a list of the field values from the filtered entries
        List<Double> filteredEntriesFieldValues = new ArrayList<>();
        for (String operationFieldName : operationFieldStringsList){
            filteredEntriesFieldValues.addAll(getListOfFieldValues(operationFieldName, filteredEntries));
        }

        //  Compute the sum of all the filtered Entries
        double sum = 0;
        for (Double filteredEntriesFieldValue : filteredEntriesFieldValues){
            sum += filteredEntriesFieldValue;
        }
        
        // Create and return result Output object
        Output output = new Output();
        Name name = new Name();
        name.setName(operationName);
        output.setName(name);
        RoundedValue roundedValue = new RoundedValue();
        roundedValue.setRoundedValue(formatDoubleToStringTwoDecimalPlaces(sum));
        output.setRoundedValue(roundedValue);

        return output;
    }

    
    public static String formatDoubleToStringTwoDecimalPlaces(Double unformattedDoubleValue){

        // Format the average to two decimal places
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedValue = df.format(unformattedDoubleValue);

        return formattedValue;
    }

    public static List<Double> getListOfFieldValues(String operationFieldName, Entries filteredEntries){

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

    public static List<String> getOperationFieldStringsList(Operation operation){

        List<Field> operationFieldsList = operation.getOperationFields().getOperationFieldsList();
        List<String> operationFieldStringsList = new ArrayList<>();
        for (Field field : operationFieldsList){
            operationFieldStringsList.add(field.getField());
        }
        return operationFieldStringsList;
    }

    
    public static Output computeMinOfFilteredEntries(Operation operation, Entries filteredEntries){
        
        //  Unbox Operation object
        String operationName = operation.getName().getName();
        
        //  Get List of fields as Strings
        List<String> operationFieldStringsList = getOperationFieldStringsList(operation);

        //  For each field, get a list of the field values from the filtered entries
        List<Double> filteredEntriesFieldValues = new ArrayList<>();
        for (String operationFieldName : operationFieldStringsList){
            filteredEntriesFieldValues.addAll(getListOfFieldValues(operationFieldName, filteredEntries));
        }

        //  Compute the min of all the filtered Entries
        double min = Collections.min(filteredEntriesFieldValues);
        
        // Create and return result Output object
        Output output = new Output();
        Name name = new Name();
        name.setName(operationName);
        output.setName(name);
        RoundedValue roundedValue = new RoundedValue();
        roundedValue.setRoundedValue(formatDoubleToStringTwoDecimalPlaces(min));
        output.setRoundedValue(roundedValue);

        return output;
    }

    public static Output computeMaxOfFilteredEntries(Operation operation, Entries filteredEntries){

        //  Unbox Operation object
        String operationName = operation.getName().getName();
        
        //  Get List of fields as Strings
        List<String> operationFieldStringsList = getOperationFieldStringsList(operation);

        //  For each field, get a list of the field values from the filtered entries
        List<Double> filteredEntriesFieldValues = new ArrayList<>();
        for (String operationFieldName : operationFieldStringsList){
            filteredEntriesFieldValues.addAll(getListOfFieldValues(operationFieldName, filteredEntries));
        }

        //  Compute the max of all the filtered Entries
        double max = Collections.max(filteredEntriesFieldValues);
        
        // Create and return result Output object
        Output output = new Output();
        Name name = new Name();
        name.setName(operationName);
        output.setName(name);
        RoundedValue roundedValue = new RoundedValue();
        roundedValue.setRoundedValue(formatDoubleToStringTwoDecimalPlaces(max));
        output.setRoundedValue(roundedValue);

        return output;
    }

    public static JSONObject parseJsonFileIntoJsonObject(String fileName) throws JSONException, IOException{
        String jsonFileString = new String(Files.readAllBytes(Paths.get(fileName)));
        return new JSONObject(jsonFileString);
    }
    
    
}

