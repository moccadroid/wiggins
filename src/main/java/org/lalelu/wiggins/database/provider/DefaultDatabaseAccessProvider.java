package org.lalelu.wiggins.database.provider;

import java.util.ArrayList;
import java.util.List;

public class DefaultDatabaseAccessProvider implements DatabaseAccessProvider {


    @Override
    public void execute(String sql) {

    }

    @Override
    public List<Object[]> getResultList(String sql) {
        return new ArrayList<Object[]>();
    }
}
