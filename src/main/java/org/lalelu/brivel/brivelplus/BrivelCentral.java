package org.lalelu.brivel.brivelplus;

import org.lalelu.brivel.brivelplus.database.provider.DatabaseAccessProvider;
import org.lalelu.brivel.brivelplus.database.provider.DefaultDatabaseAccessProvider;

public class BrivelCentral {
    private static DatabaseAccessProvider databaseAccessProvider = null;

    // schau
    public static DatabaseAccessProvider getDatabaseAccessProvider() {
        if(databaseAccessProvider == null)
            databaseAccessProvider = new DefaultDatabaseAccessProvider();

        return databaseAccessProvider;
    }

    public static void setDatabaseAccessProvider(DatabaseAccessProvider databaseAccessProvider) {
        BrivelCentral.databaseAccessProvider = databaseAccessProvider;
    }

}
