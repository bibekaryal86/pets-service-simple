# pets-service-layer-simple

This is a simple app which provides the business logic for Personal Expenses Tracking System application. This app is a scaled down version
of `pets-service-layer` app found here: https://github.com/bibekaryal86/pets-service-layer. The other app uses Spring
Boot with RestTemplate framework to do the exact same function as this app - `pets-service-layer-simple`. However,
this `simple` app does not use any kind of Spring or database frameworks. The web application framework is provided by
Jetty server with Java Servlets providing the endpoints. Interactions with other REST services are done by Java native HttpClient.

Because of absence of any frameworks, the footprint of this app is very grounded (~6 MB jar archive and ~100 MB runtime
JVM memory) as opposed to when using Spring Boot (~45 MB archive and ~350 MB memory). And, as a result, the app can be
deployed and continuously run 24/7 on Google Cloud Platform App Engine's free tier.

To run the app, we need to supply the following environment variables:

* Port
  * This is optional, and if it is not provided port defaults to 8080
* Time Zone
  * TZ: Preferred time zone, eg: America/Denver
* Profile
  * SPRING_PROFILES_ACTIVE (development, docker, production)
* PETS Database Security Details:
  * BASIC_AUTH_USR_PETSDATABASE (auth username of pets-database)
  * BASIC_AUTH_PWD_PETSDATABASE (auth password of pets-database)
* Authentication header for simple app security
  * BASIC_AUTH_USR: auth username
  * BASIC_AUTH_PWD: auth password

The app has been deployed to GCP:

* https://pets-service.appspot.com/pets-service/tests/ping
