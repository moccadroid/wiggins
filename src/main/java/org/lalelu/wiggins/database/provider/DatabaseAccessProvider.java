package org.lalelu.wiggins.database.provider;

import java.util.List;

public interface DatabaseAccessProvider {

    public void execute(String sql);
    public List<Object[]> getResultList(String sql);
}
