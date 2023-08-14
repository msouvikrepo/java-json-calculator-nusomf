package de.itdesign.application;

import de.itdesign.application.data.Entries;
import de.itdesign.application.operations.Filter;
import de.itdesign.application.operations.Operation;
import de.itdesign.application.operations.Operations;
import de.itdesign.application.output.Output;
import de.itdesign.application.output.Outputs;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface UtilityInterface {


    //  JsonObject to POJO
    public Operations createOperationsObjectFromOperationsJsonObject(JSONObject operationsJsonObject);

    public Operation createOperationObjectFromOperationJsonObject(JSONObject operationJsonObject);
    
    public Entries createDataEntriesObjectfromDataJsonObject(JSONObject dataJsonObject);

    
    //  POJO to JsonArray
    public JSONArray createOutputsJSONArrayFromOutputsObject(Outputs outputs);

    //  JsonArray to POJO
    public void createOutputFileFromOutputsJsonArray(JSONArray outputsJsonArray, String outputFilePath) throws IOException;
    
    //  Json file to JsonObject
    public JSONObject parseJsonFileIntoJsonObject(String fileName) throws JSONException, IOException;


    //  Operations on Data Entries
    public Entries filterDataEntriesByName(Entries entries, Filter filter);

    public Output performOperationOnFilteredData(Entries filteredEntries, Operation operation);

    public Output computeAverageOfFilteredEntries(Operation operation, Entries filteredEntries);

    public List<Double> getfilteredEntriesFieldValues(Operation operation, Entries filteredEntries);

    public Output computeSumOfFilteredEntries(Operation operation, Entries filteredEntries);

    public String formatDoubleToStringTwoDecimalPlaces(Double unformattedDoubleValue);

    public List<Double> getListOfFieldValues(String operationFieldName, Entries filteredEntries);

    public List<String> getOperationFieldStringsList(Operation operation);

    public Output computeMinOfFilteredEntries(Operation operation, Entries filteredEntries);

    public Output computeMaxOfFilteredEntries(Operation operation, Entries filteredEntries);

    public Output createResultOutputObject(String operationName, Double valueDouble);
 
}
