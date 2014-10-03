package org.lalelu.wiggins.net;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileRetriever {
    public String retrieve(String url, String encoding) throws MalformedURLException, IOException;
}
