package de.itdesign.application;

import java.util.List;
import java.io.IOException;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.itdesign.application.model.data.Entries;
import de.itdesign.application.model.operations.Operation;
import de.itdesign.application.model.operations.Operations;
import de.itdesign.application.model.output.Output;
import de.itdesign.application.model.output.Outputs;
import de.itdesign.application.util.UtilityImpl;


public class JsonCalculator {

    private static Logger logger = LogManager.getLogger(JsonCalculator.class);

    public static void main(String[] args){
        // Don't change this part
        if (args.length == 3) {
            // Path to the data file, e.g. data/data.xml
            final String DATA_FILE = args[0];
            // Path to the data file, e.g. operations/operations.xml
            final String OPERATIONS_FILE = args[1];
            // Path to the output file
            final String OUTPUT_FILE = args[2];

            // <my code here>
            
            UtilityImpl utilityImpl = new UtilityImpl();

            JSONObject dataJsonObject = new JSONObject();
            try {
                dataJsonObject = utilityImpl.parseJsonFileIntoJsonObject(DATA_FILE);
            } catch (JSONException e) {
                logger.error("Did not encounter valid Json in Data File", e);
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("Invalid data file name or path", e);
                e.printStackTrace();
            }
            
            Entries entries = new Entries();
            try {
        
                entries = utilityImpl.createDataEntriesObjectfromDataJsonObject(dataJsonObject);

            } catch (JSONException e) {
                logger.error("Invalid Data Json format", e);
                e.printStackTrace();
            }

            JSONObject operationsJsonObject = new JSONObject();
            try {
                operationsJsonObject = utilityImpl.parseJsonFileIntoJsonObject(OPERATIONS_FILE);
            } catch (JSONException e) {
                logger.error("Did not encounter valid Json in Operations File", e);
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("Invalid operations file name or path", e);
                e.printStackTrace();
            }
            
            Operations operations = new Operations();
            try {
                
                operations = utilityImpl.createOperationsObjectFromOperationsJsonObject(operationsJsonObject);

            } catch (JSONException e) {
                logger.error("Invalid operations Json format", e);
                e.printStackTrace();
            }
            
            List<Operation> operationList = operations.getOperationsList();

            List<Output> outputsList = new ArrayList<>();

            try {

                for(Operation operation : operationList){
                
                    Entries filteredEntries = utilityImpl.filterDataEntriesByName(entries, operation.getFilter());
                    
                    Output output = utilityImpl.performOperationOnFilteredData(filteredEntries, operation);
                    
                    outputsList.add(output);
                    }

            }catch (NullPointerException e) {
                logger.error("Objects list is empty or trying to access a null object", e);
                e.printStackTrace();
            }catch (Exception e) {
                logger.error("Error during processing data", e);
                e.printStackTrace();
            }

            Outputs outputs = new Outputs();
            outputs.setOutputsList(outputsList);

            JSONArray outputsJsonArray = utilityImpl.createOutputsJSONArrayFromOutputsObject(outputs);

            try {
                utilityImpl.createOutputFileFromOutputsJsonArray(outputsJsonArray, OUTPUT_FILE);
            } catch (IOException e) {
                logger.error("Invalid Output file path or application lacks write permission", e);
                e.printStackTrace();
            }

        } else {
            System.exit(1);
        }
    }

}

