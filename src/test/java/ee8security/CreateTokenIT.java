package ee8security;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Note that to use these tests, the application must run with the system property -Dee8security.fixedUnixTime=1556712000
 */
public class CreateTokenIT {

    private static final String ENDPOINT = "http://localhost:8080/ee8security/api/non-secured/create-token";

    @Test
    public void returnsTokenIfTotpIsNotEnabled() throws IOException {
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"jane.doe@example.com\", \"password\": \"jane.doe.pw\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(EntityUtils.toString(response.getEntity()), is("{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLmRvZUBleGFtcGxlLmNvbSIsImV4cCI6MTU1NjcxMzgwMH0.SABuKUbnMwFXVbOVy4RkJ1aAM4Q6RU37XRXoiS9EYA0\"}"));
        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void returnsErrorIfPasswordIsWrong() throws IOException {
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"jane.doe@example.com\", \"password\": \"wrong\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(response.getStatusLine().getStatusCode(), is(400));
        assertThat(EntityUtils.toString(response.getEntity()), is("Bad credential"));
    }

    @Test
    public void askForTotpIfMissing() throws IOException {
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"john.doe@example.com\", \"password\": \"john.doe.pw\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(response.getStatusLine().getStatusCode(), is(400));
        assertThat(EntityUtils.toString(response.getEntity()), is("TOTP is needed; Try again with your TOTP"));
    }

    @Test
    public void returnsTokenIfValidTotpIsGiven() throws IOException, InvalidKeyException {
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"john.doe@example.com\", \"password\": \"john.doe.pw\", \"totp\": \"879955\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(EntityUtils.toString(response.getEntity()), is("{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImV4cCI6MTU1NjcxMzgwMH0.ei11yP0Ki94lKfP-7-YLa_GyPTVeooWieZgZKgd1mCQ\"}"));
        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void returnsErrorIfTotpIsInvalid() throws IOException {
        String totp = "123456";
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"john.doe@example.com\", \"password\": \"john.doe.pw\", \"totp\": \"" + totp + "\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(EntityUtils.toString(response.getEntity()), is("Bad credential"));
        assertThat(response.getStatusLine().getStatusCode(), is(400));
    }
}
