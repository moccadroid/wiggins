package org.lalelu.wiggins.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DefaultFileRetriever implements FileRetriever {

    // retrieves into a String object
    @Override
    public String retrieve(String urlToRetrieve, String encoding) throws MalformedURLException, IOException {
        InputStream is = prepareInputStream(urlToRetrieve);

        // "ISO-8859-1" <- just for reference
        BufferedReader in = new BufferedReader(new InputStreamReader(is , encoding));
        StringBuilder output = new StringBuilder();
        String str;
        boolean first = true;
        while ((str = in.readLine()) != null) {
            if (!first)
                output.append("\n");
            first = false;
            output.append(str);
        }
        in.close();
        return output.toString();
    }

    private InputStream prepareInputStream(String urlToRetrieve) throws IOException {
        URL url = new URL(urlToRetrieve);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        connection.connect();
        InputStream is = connection.getInputStream();

        return is;
    }
}
