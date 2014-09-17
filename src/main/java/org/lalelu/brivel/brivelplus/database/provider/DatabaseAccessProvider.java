package org.lalelu.brivel.brivelplus.database.provider;

import java.util.List;

public interface DatabaseAccessProvider {

    public void execute(String sql);
    public List<Object[]> getResultList(String sql);
}
