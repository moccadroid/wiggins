package org.lalelu.wiggins;

import org.lalelu.wiggins.database.provider.DatabaseAccessProvider;
import org.lalelu.wiggins.database.provider.DefaultDatabaseAccessProvider;

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
