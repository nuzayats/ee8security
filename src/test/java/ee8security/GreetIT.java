package ee8security;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GreetIT {

    @Test
    public void returnsUnauthorizedIfThereIsNoToken() throws IOException {
        HttpGet request = new HttpGet("http://localhost:8080/ee8security/api/secured/greet");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(response.getStatusLine().getStatusCode(), is(401));
    }

    @Test
    public void returnsUnauthorizedIfInvalidTokenIsSupplied() throws IOException {
        HttpGet request = new HttpGet("http://localhost:8080/ee8security/api/secured/greet");
        request.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.oLhIIqa_XN83EfQOT8oBcCc75LCDKjLzJ-EN7M18Vbs");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(response.getStatusLine().getStatusCode(), is(401));
    }

    @Test
    public void saysHelloIfValidTokenIsSupplied() throws IOException {
        HttpGet request = new HttpGet("http://localhost:8080/ee8security/api/secured/greet");
        request.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLmRvZUBleGFtcGxlLmNvbSJ9.oLhIIqa_XN83EfQOT8oBcCc75LCDKjLzJ-EN7M18Vbs");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertThat(EntityUtils.toString(response.getEntity()), is("Hello jane.doe@example.com"));
        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }


}
