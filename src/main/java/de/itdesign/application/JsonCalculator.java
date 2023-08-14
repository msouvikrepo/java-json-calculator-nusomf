package de.itdesign.application;

import java.util.List;

import java.io.IOException;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.itdesign.application.pojo.data.Entries;
import de.itdesign.application.pojo.operations.Operation;
import de.itdesign.application.pojo.operations.Operations;
import de.itdesign.application.pojo.output.Output;
import de.itdesign.application.pojo.output.Outputs;
import de.itdesign.application.util.UtilityImpl;

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
            
            //  Utility Class Impl object
            UtilityImpl utilityImpl = new UtilityImpl();

            //  Read Data file into Data JsonObject
            JSONObject dataJsonObject = utilityImpl.parseJsonFileIntoJsonObject(DATA_FILE);
            
            //  Create Data Entries Object from Data JsonObject
            Entries entries = utilityImpl.createDataEntriesObjectfromDataJsonObject(dataJsonObject);

            //  Read Operations file into Operations JsonObject
            JSONObject operationsJsonObject = utilityImpl.parseJsonFileIntoJsonObject(OPERATIONS_FILE);
            
            //  Get Operations object from Operations JsonObject
            Operations operations = utilityImpl.createOperationsObjectFromOperationsJsonObject(operationsJsonObject);
            
            //  Get list of Operations from Operations object
            List<Operation> operationList = operations.getOperationsList();

            //  Inititialize list of Output objects
            List<Output> outputsList = new ArrayList<>();

            //  for each Operation object, perform func operation on filtered data entries object that returns an output object
            for(Operation operation : operationList){
               
                // get filtered Entries Object from current Operation filter
                Entries filteredEntries = utilityImpl.filterDataEntriesByName(entries, operation.getFilter());
                
                //perform operation on filtered data to get output object
                Output output = utilityImpl.performOperationOnFilteredData(filteredEntries, operation);
                
                //Add output to list of outputs object
                outputsList.add(output);
                
            }

            //  Create Outputs object from list of output objects
            Outputs outputs = new Outputs();
            outputs.setOutputsList(outputsList);

            // Create Outputs JsonArray from Outputs object
            JSONArray outputsJsonArray = utilityImpl.createOutputsJSONArrayFromOutputsObject(outputs);

            // Create Outputs Json file from Outputs JsonArray
            utilityImpl.createOutputFileFromOutputsJsonArray(outputsJsonArray, OUTPUT_FILE);

        } else {
            System.exit(1);
        }
    }

}

