package de.itdesign.application;

import java.util.List;
import java.util.ArrayList;

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

            //  Read data file into JSON Object
            JSONObject dataJsonObject = readFileIntoJsonObject(DATA_FILE);
            //  Create Data POJO
            JSONArray entriesJsonArray = dataJsonObject.getJSONArray("entries");
            List<Entry> entryList = new ArrayList<>();
            for (int i=0; i<entriesJsonArray.length();i++){

                JSONObject entryJsonObject = entriesJsonArray.getJSONObject(i);

                Name name = new Name();
                Population population = new Population();
                Area area = new Area();
                ExtendedStatistics extendedStatistics = new ExtendedStatistics();

                name.setName(entryJsonObject.getString("name"));
                population.setPopulation(entryJsonObject.getInt("population"));
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

            //  Read operations file into JSON object
            //  Create Operation POJO
                //  for each operation, perform func operation that returns an output object
            //  add output to outputs POJO
            // Create output json file from outputs

        } else {
            System.exit(1);
        }
    }



    public static JSONObject readFileIntoJsonObject(String fileName){

        return null;

    }

    public static JSONArray readFileIntoJsonArray(String fileName){

        return null;

    }

    public static JSONObject generateOutputJsonObject(JSONObject dataJsonObject, JSONObject outputsJsonObject){


        return null;
    }

    
    
}

