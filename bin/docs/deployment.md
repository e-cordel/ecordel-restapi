# Deployment

The e-cordel rest api is currently deployed on heroku platform. Any new commit in the `main` branch triggers a new deploy.
Read more about [Heroku Java Support.](https://devcenter.heroku.com/articles/java-support)

## Environment variables

Any configuration in the [application.yml](../src/main/resources/application.yml) may be overwritten by defining a
environment variable with the same name. [More info here.](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)

Heroku also sets the following [environment variables](https://devcenter.heroku.com/articles/java-support#environment) by default:
 - PORT
 - JDBC_DATABASE_URL
 - SPRING_DATASOURCE_URL
 - SPRING_DATASOURCE_USERNAME
 - SPRING_DATASOURCE_PASSWORD

## Important files

 - [Procfile](../Procfile) - java command to start up the server
 - [system.properties](../system.properties) - java version is specified in this file
