package org.lalelu.wiggins;

import org.lalelu.wiggins.data.provider.DatabaseAccessProvider;
import org.lalelu.wiggins.data.provider.DefaultDatabaseAccessProvider;

public class BrivelCentral {
    private static DatabaseAccessProvider databaseAccessProvider = null;

    public static DatabaseAccessProvider getDatabaseAccessProvider() {
        if(databaseAccessProvider == null)
            databaseAccessProvider = new DefaultDatabaseAccessProvider();

        return databaseAccessProvider;
    }

    public static void setDatabaseAccessProvider(DatabaseAccessProvider databaseAccessProvider) {
        BrivelCentral.databaseAccessProvider = databaseAccessProvider;
    }

}



/*
JsonRequest<TestObject> testRequest = new JsonRequest<TestObject>(TestObject.class);

        try {
            br = new BufferedReader(new FileReader("/Users/maximilian.uhlig/Documents/parseFile.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            Map<String, JsonRequest<?>> requestMap = new HashMap<String, JsonRequest<?>>();

            Map<String, JsonObjectModel> assemblerMap = new HashMap<String, JsonObjectModel>();
            while(line != null) {
                line = line.trim();

                if(line.startsWith("#") || line.isEmpty()) {
                    line = br.readLine();
                    continue;
                }

                if(line.equals("-object:testObject")) {
                    String[] parameters = line.split(":");
                    JsonObjectModel assembler = new JsonObjectModel(TestObject.class);
                    assemblerMap.put(parameters[1], assembler);
                    testRequest.addAssembler(assembler);

                } else if(line.equals("-object:poiEvent")) {
                    String[] parameters = line.split(":");
                    JsonObjectModel assembler = new JsonObjectModel(PoiEvent.class);
                    assemblerMap.put(parameters[1], assembler);
                    testRequest.addAssembler(assembler);

                } else if(line.startsWith("-mapping")) {

                    String[] parameters = line.split(":");
                    String mainObject = parameters[1];
                    String subObject = parameters[2];
                    String field = parameters[3];

                    JsonObjectModel assembler = assemblerMap.get(subObject);
                    assembler.setField(field);

                } else if(!line.startsWith("-")) {
                    String[] parameters = line.split(":");
                    String objectName = parameters[0];
                    String field = parameters[1];
                    String path = parameters[2];

                    JsonObjectModel assembler = assemblerMap.get(objectName);
                    assembler.addSelector(new JsonSelector(field+":"+path));
                }

                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        globalStringCount = 0;
        globalIntegerCount = 0;
        globalDoubleCount = 0;
        globalBooleanCount = 0;
        String jsonUrl = "http://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:OEFFGRUENPKTGOGD&srsName=EPSG:4326&outputFormat=json";
        //jsonUrl = "http://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:BADESTELLENOGD&srsName=EPSG:4326&outputFormat=json";
        try {
            Long before = System.currentTimeMillis();
            String jsonString = FileRetriever.retrieve(jsonUrl, "ISO-8859-1");
            Long after = System.currentTimeMillis();
            timing += "<br> Retrieving the json took: " + (after - before) + "ms...";

            before = System.currentTimeMillis();
            Gson gsonMapper = new GsonBuilder().create();
            Map<String, Object> gsonData = gsonMapper.fromJson(jsonString, Map.class);
            after = System.currentTimeMillis();
            timing += "<br> Gson took " + (after - before) + "ms to parse to a Map as gsonData";

            before = System.currentTimeMillis();
            ObjectMapper jacksonMapper = new ObjectMapper();
            Map<String, Object> jacksonData = jacksonMapper.readValue(jsonString, Map.class);
            after = System.currentTimeMillis();
            timing += "<br> Jackson took " + (after - before) + "ms to parse to a Map as jacksonData";

            before = System.currentTimeMillis();
            org.boon.json.ObjectMapper boonMapper = new ObjectMapperImpl();
            Map<String, Object> boonData = boonMapper.readValue(jsonString, Map.class);
            after = System.currentTimeMillis();
            timing += "<br> Boon took " + (after - before) + "ms to parse to a Map (lazy) as boonData";

            List<TestObject> resultList = new ArrayList<TestObject>();
            before = System.currentTimeMillis();

        testRequest.compile();
        resultList = testRequest.getResult(jacksonData);
        after = System.currentTimeMillis();
        timing +="<br> Dynamic mapping: " + (after - before) + "ms resulting in " + resultList.size() + " new TestObjects with boonData.";

 */