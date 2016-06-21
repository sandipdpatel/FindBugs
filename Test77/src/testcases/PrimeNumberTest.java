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
public class PrimeNumberTest extends BaseTest {

    private int input;
    private final String queryParam = "number";
    private final String fieldName = "number";
    private final String path = "/primenumbers";

    private int[] primeNums = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131,
            137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293,
            307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479,
            487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673,
            677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881,
            883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997 };

    public PrimeNumberTest(int input) {
        this.input = input;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { 1 }, { 2 }, { 3 }, { 199 }, { 991 }, { 997 }, { 998 }, { 1000 }, { 1001 }, { 0 }, { -3 } });
    }

    @Test
    public void testGetPrimeNumber() throws ClientProtocolException, IOException {
        BuildURL builder = new BuildURL(BASE_URL);
        String url = builder.addPathParam(path).addQueryParam(queryParam, String.valueOf(input)).build();
        String response = new ClientUtil().getRequest(url);
        StringBuffer buffer = new StringBuffer();
        try {
            Assert.assertEquals("Invalid response, input:" + input + " response:" + response, isPrime(input), Boolean.parseBoolean(response));
        } catch (Exception e) {
            buffer.append(e.getMessage());
        }

        if (buffer.length() > 0) {
            Assert.fail(buffer.toString());
        }
    }

    @Test
    public void testPostPrimeNumber() throws ClientProtocolException, IOException {
        BuildURL builder = new BuildURL(BASE_URL);
        String url = builder.addPathParam(path).build();
        String response = new ClientUtil().postRequest(url, fieldName, String.valueOf(input));
        StringBuffer buffer = new StringBuffer();
        try {
            Assert.assertEquals("Invalid response, input:" + input + " response:" + response, isPrime(input), Boolean.parseBoolean(response));
        } catch (Exception e) {
            buffer.append(e.getMessage());
        }

        if (buffer.length() > 0) {
            Assert.fail(buffer.toString());
        }
    }

    private Boolean isPrime(int num) {
        return Arrays.binarySearch(primeNums, num) < 0 ? false : true;
    }

}
