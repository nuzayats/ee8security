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

public class CreateTokenIT {

    private static final String ENDPOINT = "http://localhost:8080/ee8security/api/non-secured/create-token";

    @Test
    public void returnsTokenIfTotpIsNotEnabled() throws IOException {
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"jane.doe@example.com\", \"password\": \"jane.doe.pw\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        String payload = EntityUtils.toString(response.getEntity());
        assertThat(payload, is("{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLmRvZUBleGFtcGxlLmNvbSJ9.oLhIIqa_XN83EfQOT8oBcCc75LCDKjLzJ-EN7M18Vbs\"}"));
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
        // TODO: this test case might fail due to latency; Somehow any way to fix current timestamp is needed
        String totp = new TotpService().create(MockUserService.JOHN_SHARED_KEY_FOR_TOTP);
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"john.doe@example.com\", \"password\": \"john.doe.pw\", \"totp\": \"" + totp + "\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(EntityUtils.toString(response.getEntity()), is("{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.g7j0rDvlyVk7ZLZD4ZOE84zVakGs6uXtT9u2YHM26FM\"}"));
        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void returnsErrorIfTotpIsInvalid() throws IOException, InvalidKeyException {
        String totp = "123456";
        HttpPost request = new HttpPost(ENDPOINT);
        request.setEntity(new StringEntity("{\"email\": \"john.doe@example.com\", \"password\": \"john.doe.pw\", \"totp\": \"" + totp + "\"}"));
        request.setHeader("Content-type", "application/json");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(EntityUtils.toString(response.getEntity()), is("Bad credential"));
        assertThat(response.getStatusLine().getStatusCode(), is(400));
    }
}
