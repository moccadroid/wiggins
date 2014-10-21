package org.lalelu.wiggins.requests.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.lalelu.wiggins.selectors.json.JsonSelector;

public class JsonRequestBuilder<T> {
    private Map<String, JsonObjectModel> conditions = new LinkedHashMap<String, JsonObjectModel>();
    private Class<T> klass = null;

    public JsonRequestBuilder(Class<T> klass) {
        this.klass = klass;
    }

    public void addObjectCondition(String condition, Class<T> objectKlass) {
        conditions.put("-object:"+condition, new JsonObjectModel(objectKlass));
    }

    public JsonRequest<T> buildRequestFromFile(String mappingUrl) throws IOException {

        try(BufferedReader br = new BufferedReader(new FileReader(mappingUrl))) {
	        StringBuilder mappingString = new StringBuilder();
	        String line = br.readLine();
	
	        while(line != null) {
	            line = line.trim();
	
	            if(line.startsWith("#") || line.isEmpty()) {
	                line = br.readLine();
	                continue;
	            }
	
	            mappingString.append(line + "\n");
	
	            line = br.readLine();
	        }
	        return buildRequest(mappingString.toString());
        }
    }

    public JsonRequest<T> buildRequestFromString(String mappingString) throws IOException {
        return buildRequest(mappingString);
    }

    private JsonRequest<T> buildRequest(String mappingString) throws IOException {
        JsonRequest<T> request = new JsonRequest<T>(klass);
        Map<String, JsonObjectModel> objectModelMap = new HashMap<String, JsonObjectModel>();

        BufferedReader br = new BufferedReader(new StringReader(mappingString));
        String line = br.readLine();

        while(line != null) {
            if(conditions.containsKey(line)) {
                String[] parameters = line.split(":");
                JsonObjectModel objectModel = conditions.get(line);
                objectModelMap.put(parameters[1], objectModel);
                request.addObjectModel(objectModel);
            } else if(line.startsWith("-mapping")) {
                String[] parameters = line.split(":");
                String mainObject = parameters[1];
                String subObject = parameters[2];
                String field = parameters[3];

                JsonObjectModel mainObjectModel = objectModelMap.get(mainObject);
                JsonObjectModel objectModel = objectModelMap.get(subObject);
                objectModel.setParent(field, mainObjectModel);

            } else if(!line.startsWith("-")) {
                String[] parameters = line.split(":");
                String objectName = parameters[0];
                String field = parameters[1];
                String path = parameters[2];

                JsonObjectModel objectModel = objectModelMap.get(objectName);
                objectModel.addSelector(new JsonSelector(field+":"+path));
            }
            line = br.readLine();
        }

        return request;
    }
}
