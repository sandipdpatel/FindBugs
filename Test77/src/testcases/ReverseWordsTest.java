package testcases;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utilities.BuildURL;
import utilities.ClientUtil;

@RunWith(Parameterized.class)
public class ReverseWordsTest extends BaseTest {

    private String input;
    private final String queryParam = "string";
    private final String fieldName = "string";
    private final String path = "/reversewords";

    public ReverseWordsTest(String input) {
        this.input = input;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "The Quick Brown Fox" }, { "The Quick \"Brown\" Fox" }, { "the quick brown fox" }, { " " }, { " hello " }, { " hello  hi  " },
                { "hello & hi" },
                { "a, b" },
                { "" } });
    }

    @Test
    public void testGetReverseWords() throws ClientProtocolException, IOException {
        BuildURL builder = new BuildURL(BASE_URL);
        String url = builder.addPathParam(path).addQueryParam(queryParam, input).build();
        String response = new ClientUtil().getRequest(url);
        StringBuffer buffer = new StringBuffer();
        try {
            Assert.assertTrue("Missing starting Quote", response.startsWith("\""));
            Assert.assertTrue("Missing ending Quote", response.endsWith("\""));
            Assert.assertTrue("String mismatch, input:" + input + " response:" + response, compareSentences(input, response));
        } catch (Exception e) {
            buffer.append(e.getMessage());
        }

        if (buffer.length() > 0) {
            Assert.fail(buffer.toString());
        }
    }

    @Test
    public void testPostReverseWords() throws ClientProtocolException, IOException {
        BuildURL builder = new BuildURL(BASE_URL);
        String url = builder.addPathParam(path).build();
        String response = new ClientUtil().postRequest(url, fieldName, input);
        StringBuffer buffer = new StringBuffer();
        try {
            Assert.assertEquals("Quotes missing in response, " + response, 2, response.length() - response.replaceAll("\"", "").length());
            Assert.assertTrue("String mismatch. input:" + input + " response:" + response, compareSentences(input, response));
        } catch (Exception e) {
            buffer.append(e.getMessage());
        }

        if (buffer.length() > 0) {
            Assert.fail(buffer.toString());
        }
    }

    private Boolean compareSentences (String expected, String actual) {
        actual = actual.substring(1, actual.length() - 1);
        if (expected.length() != actual.length()) {
            return false;
        }

        for (int i = 0; i < expected.length(); i++) {
            if (expected.charAt(i) == ' ') {
                if (expected.charAt(i) != actual.charAt(i)) {
                    return false;
                }
            } else {
                int wEnd = expected.indexOf(' ', i);
                if (wEnd < 0) {
                    wEnd = expected.length();
                }
                String word = expected.substring(i, wEnd);
                int wLen = word.length();
                for (int j = 0; j < wLen; j++) {
                    if (word.charAt(j) != actual.charAt(i + wLen - j - 1)) {
                        return false;
                    }
                }
                i += word.length();
            }
        }
        return true;
    }
}
