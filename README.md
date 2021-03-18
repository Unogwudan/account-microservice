# Account MicroService

This is the first of the 3 MicroServices for the Reloadly Code Challenge.

It is responsible for Account Management and Authentication. Customers can create 
new accounts, update their accounts, view their account info and Sign In to generate 
a JWT which can then be passed as an Authorization header to the Transaction 
MicroService as users must be authenticated and authorized before they can perform 
transactions.

Upon registering an API call is made to the Notification MicroService to send welcome 
email notification to the newly registered user.

## Running the project locally
To run the project locally you need to:
 
1. Have mysql installed and running;

2. Create the db "reloadly" on mysql.

3. Update the dev.properties file with your mysql credentials

4. Set spring.profiles.active=dev in the application.properties file

5. Run the project

6. Open your browser and go to "http://localhost:8082/swagger-ui.html" to access the 
swagger doc from where you can test the endpoints.


## Executing the Unit Tests
To execute the unit tests simply run "mvn clean package" on the terminal and allow 
it to execute or you can navigate to the test package and execute the tests from there.

## Test Coverage
Jacoco is used for test coverage.

To see the test coverage, run "mvn clean package". When it's done packaging, navigate 
to the target folder, inside the target folder, navigate to the "site" folder, inside
the site folder, open the jacoco folder you will see an index.html file. Open the html
page in your browser to see the test coverage.


## Deployment
This MicroService is deployed to Heroku.

Use the below url to access the swagger ui on Heroku for your testing pleasure.

https://reloadly-account-services.herokuapp.com/swagger-ui.html


Please reach out to me for any question or clarification (Daniel Unogwu).