package utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BuildURL {

    private final String baseURL;
    private String pathParam;
    private String query;

    public BuildURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public BuildURL addPathParam(String pathParam) {
        if (this.pathParam == null) {
            this.pathParam = "";
        }
        this.pathParam += pathParam;
        return this;
    }

    public BuildURL addQueryParam(String queryParam, String value) throws UnsupportedEncodingException {
        if (this.query == null) {
            this.query = "";
        }
        this.query += "?" + queryParam + "=" + URLEncoder.encode(value, "UTF-8");
        return this;
    }

    public String build() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder(baseURL);

        if (pathParam != null) {
            builder.append(pathParam);
        }

        if (query != null) {
            builder.append(query);
        }

        return builder.toString();
    }
}
