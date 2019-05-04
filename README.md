ee8security
===========

This is an experimental implementation which aims to study how to use Java EE 8 Security API (JSR 375) for situations where there are REST API clients, a token and TOTP (time-based one time password) based authentication is needed.

There are two endpoints:

### /api/non-secured/create-token

The endpoint accepts a POST request. A user can get their access token with this. The user has to supply their email and password in the JSON format in the request body. It should be something like this:

    {"email": "jane.doe@example.com", "password": "jane.doe.pw"}

The user will get a JWT signed access token:

    {"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLmRvZUBleGFtcGxlLmNvbSJ9.oLhIIqa_XN83EfQOT8oBcCc75LCDKjLzJ-EN7M18Vbs"}

But if the user turns on TOTP, the endpoint responds with a 400 error:

    TOTP is needed; Try again with your TOTP

In this case, the user has to supply their TOTP as well:

    {"email": "john.doe@example.com", "password": "john.doe.pw", "totp": "123456"}

[The shared key for the account above can be found here](https://github.com/nuzayats/ee8security/blob/851489fc47069b4846dcb5e2532dd58819db6488/src/main/java/ee8security/MockUserService.java#L25) . Put the key into Google Authenticator's "Manual entry" screen and you can get a TOTP.

### /api/secured/greet:

The endpoint accepts a GET request. The user must supply an Authorization header which contains the access token that the user got from the other endpoint. It should look like this:

    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.oLhIIqa_XN83EfQOT8oBcCc75LCDKjLzJ-EN7M18Vbs

If everything is fine, the user gets a greeting as follows:

    Hello jane.doe@example.com

Tested on WildFly 16.

The project contains [some E2E test cases](https://github.com/nuzayats/ee8security/tree/master/src/test/java/ee8security) which can be run with the following command:

    ./mvnw clean verify

Note that:

* It will automatically download and extract the WildFly 16 zip file which is about 173MB, so it will take some bandwidth and time
* It will listen 8080, 8443 and 9990 ports so make sure those are not used
