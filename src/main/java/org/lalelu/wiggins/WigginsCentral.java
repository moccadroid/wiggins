package org.lalelu.wiggins;

import org.lalelu.wiggins.data.jsonparser.DefaultJsonParser;
import org.lalelu.wiggins.data.jsonparser.JsonParser;
import org.lalelu.wiggins.data.provider.DatabaseAccessProvider;
import org.lalelu.wiggins.data.provider.DefaultDatabaseAccessProvider;

public class WigginsCentral {
    private static DatabaseAccessProvider databaseAccessProvider = null;
    private static JsonParser jsonParser = null;

    public static DatabaseAccessProvider getDatabaseAccessProvider() {
        if(WigginsCentral.databaseAccessProvider == null)
            WigginsCentral.databaseAccessProvider = new DefaultDatabaseAccessProvider();

        return WigginsCentral.databaseAccessProvider;
    }

    public static void setDatabaseAccessProvider(DatabaseAccessProvider databaseAccessProvider) {
        WigginsCentral.databaseAccessProvider = databaseAccessProvider;
    }

    public static void setJsonParser(JsonParser jsonParser) {
        WigginsCentral.jsonParser = jsonParser;
    }

    public static JsonParser getJsonParser() {
        if(WigginsCentral.jsonParser == null) {
            WigginsCentral.jsonParser = new DefaultJsonParser();
        }
        return WigginsCentral.jsonParser;
    }
}
