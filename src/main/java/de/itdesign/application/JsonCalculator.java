package de.itdesign.application;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonCalculator {

    public static void main(String[] args) {
        // Don't change this part
        if (args.length == 3) {
            // Path to the data file, e.g. data/data.xml
            final String DATA_FILE = args[0];
            // Path to the data file, e.g. operations/operations.xml
            final String OPERATIONS_FILE = args[1];
            // Path to the output file
            final String OUTPUT_FILE = args[2];

            // <your code here>

        } else {
            System.exit(1);
        }
    }

    public JSONObject readFileIntoJsonObject(String fileName){

        return null;

    }

    public JSONArray readFileIntoJsonArray(String fileName){

        return null;

    }

    public JSONObject generateOutputJsonObject(JSONObject dataJsonObject, JSONObject outputsJsonObject){


        return null;
    }

    
    
}

