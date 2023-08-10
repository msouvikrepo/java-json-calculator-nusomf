package de.itdesign.application;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class JsonCalculatorSpec {
    public JsonCalculatorSpec() {
        // Default constructor
    }


    @Test
    public void testOutputsRoundedValueDecimalPLaces(){

        //Read output file into JSONArray
        JsonCalculator jsonCalculator = new JsonCalculator();
        JSONArray outputsJsonArray = jsonCalculator.readFileIntoJsonArray("");
        
        //Unpack JSONArray into Outputs object
        Outputs outputs = unpackJSONArrayIntoOutputsObject(outputsJsonArray);

       //Check assertion that each Rounded Value has exactly two decimal places
       for (Output output : outputs.getOutputsList()){

            double roundedValue = output.getRoundedValue().getRoundedDouble();
            int decimalPlaces = countDecimalPlaces(roundedValue);
            assertTrue("Rounded Value should have exactly two decimal places", decimalPlaces==2);

       } 

        
    }
    
    private Outputs unpackJSONArrayIntoOutputsObject(JSONArray outputsJsonArray){

        //Outputs List and Output Object declaration
        List<Output> outputsList = new ArrayList<>();
        Outputs outputs = new Outputs();

        //Unpack JSONArray Elements into Output List
        for (int i = 0; i < outputsJsonArray.length(); i++) {
            
            JSONObject jsonObject = outputsJsonArray.getJSONObject(i);
            Name name = new Name();
            RoundedValue roundedValue = new RoundedValue();
            Output output = new Output();
            name.setName(jsonObject.getString("name"));
            roundedValue.setRoundedDouble(jsonObject.getDouble("RoundedValue"));
            output.setName(name);
            output.setRoundedValue(roundedValue);
            
            outputsList.add(output);
        }

        outputs.setOutputsList(outputsList);

        return outputs;


    }

    //Counts number of decimal places in a given Double variable
    private int countDecimalPlaces(double value) {
        String valueString = Double.toString(value);
        int index = valueString.indexOf(".");
        
        if (index < 0) {
            return 0;
        }
        
        return valueString.length() - index - 1;
    }

}
