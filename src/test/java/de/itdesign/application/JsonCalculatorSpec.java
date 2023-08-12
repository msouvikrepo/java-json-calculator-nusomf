package de.itdesign.application;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonCalculatorSpec {

    public JsonCalculatorSpec() {
        // Default constructor
    }


    @Test
    public void testOutputsRoundedValueDecimalPLaces() throws JSONException, IOException {

        //Read output file into JSONArray
        JSONArray outputsJsonArray = parseJsonFileIntoJsonArray("");

        //Unpack JSONArray into Outputs object
        Outputs outputs = unpackJSONArrayIntoOutputsObject(outputsJsonArray);

        //Check assertion that each Rounded Value has exactly two decimal places
        for (Output output: outputs.getOutputsList()) {

            String roundedValue = output.getRoundedValue().getRoundedValue();
            int decimalPlaces = countDecimalPlaces(roundedValue);
            assertTrue("Rounded Value should have exactly two decimal places", decimalPlaces == 2);

        }

    }

    @Test
    public void testValidOperation() throws JSONException, IOException {

        //Read Operations file into Operations JSONArray
        JSONObject operationsJsonObject = JsonCalculator.parseJsonFileIntoJsonObject("");
        JSONArray operationsJsonArray = operationsJsonObject.getJSONArray("operations");
        
        Operations operations = new Operations();
        operations.setOperationsList(unpackJSONArrayIntoOperationsObject(operationsJsonArray).getOperationsList());

        List<Function> functions = new ArrayList<>();
        for (Operation operation : operations.getOperationsList()){

            OperationFields operationFields = new OperationFields();
            operationFields.setOperationFieldsList(operation.getOperationFields().getOperationFieldsList());

            Function function = new Function();
            function.setFunction(operation.getFunction().getFunction());

            functions.add(function);
        }

        Boolean isValidOperation = checkForValidOperationFunction(functions);
        assertTrue("Function should be a valid operation", isValidOperation); 

    }

    //This function takes a list of functions and checks if they are all valid operations
    private Boolean checkForValidOperationFunction(List<Function> functionList){

        String[] validOperations = {"average", "sum", "min", "max"};
        Boolean checkValidOperation = false;

        for(Function function : functionList){
            String functionName = function.getFunction();
            if(Arrays.asList(validOperations).contains(functionName))
                checkValidOperation = true;
            else
                checkValidOperation = false;
        }

        return checkValidOperation;
    }


    public static JSONArray readFileIntoJsonArray(String fileName){

        return null;

    }

    private Operations unpackJSONArrayIntoOperationsObject(JSONArray operationsJsonArray) {

        List<Operation> operationsList = new ArrayList<>();

        for (int i = 0; i < operationsJsonArray.length(); i++) {

            JSONObject operationJsonObject = operationsJsonArray.getJSONObject(i);

            Name name = new Name();
            Function function = new Function();
            Filter filter = new Filter();
            OperationFields operationFields = new OperationFields();

            name.setName(operationJsonObject.getString("Name"));

            function.setFunction(operationJsonObject.getString("function"));
            JSONArray fieldsJsonArray = operationJsonObject.getJSONArray("field");
            
            List<Field> operationFieldsList = new ArrayList<>();

            for (int j = 0; j < fieldsJsonArray.length(); j++){
                Field field = new Field();
                field.setField(fieldsJsonArray.getString(i));
                operationFieldsList.add(field);
            }

            operationFields.setOperationFieldsList(operationFieldsList);
            filter.setFilter(operationJsonObject.getString("filter"));

            Operation operation = new Operation();
            operation.setName(name);
            operation.setFunction(function);
            operation.setFilter(filter);
            operation.setOperationFields(operationFields);

            operationsList.add(operation);
        }
        
       Operations operations = new Operations();
       operations.setOperationsList(operationsList);

        return operations;
    }

    private Outputs unpackJSONArrayIntoOutputsObject(JSONArray outputsJsonArray) {

        //Outputs List and Output Object declaration
        List < Output > outputsList = new ArrayList < > ();
        Outputs outputs = new Outputs();

        //Unpack JSONArray Elements into Output List
        for (int i = 0; i < outputsJsonArray.length(); i++) {

            JSONObject jsonObject = outputsJsonArray.getJSONObject(i);
            Name name = new Name();
            RoundedValue roundedValue = new RoundedValue();
            Output output = new Output();
            name.setName(jsonObject.getString("name"));
            roundedValue.setRoundedValue(jsonObject.getString("RoundedValue"));
            output.setName(name);
            output.setRoundedValue(roundedValue);

            outputsList.add(output);
        }

        outputs.setOutputsList(outputsList);

        return outputs;


    }

    private JSONArray parseJsonFileIntoJsonArray(String fileName) throws JSONException, IOException{
        String jsonArrayString = new String(Files.readAllBytes(Paths.get(fileName)));
        return new JSONArray(jsonArrayString);
    }

    //Counts number of decimal places in a given Double value
    private int countDecimalPlaces(String valueString) {
        
        int index = valueString.indexOf(".");

        if (index < 0) {
            return 0;
        }

        return valueString.length() - index - 1;
    }

}