[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=e-cordel_ecordel-restapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=e-cordel_ecordel-restapi)

# e-cordel REST API

This is the REST API for e-cordel project.

In this project you can find an openapi [specification](./openapi.yaml) with all the information about the endpoints available. 

## Dependencies
You need
- java 11 installed
- docker 20+  installed
- maven 3.8.5+ installed

## How to build locally

You can build with maven

    mvn clean package

### Dependency check 

Dependency check is done automatically during `verify` phase.

To suppress false positives include the proper information in the file [dependency-check-suppressions.xml](dependency-check-suppressions.xml)

More info about this:

https://jeremylong.github.io/DependencyCheck/general/suppression.html

## How to run in local develop

To up postgres in a docker local

    docker compose up -d

Add in VMArguments to run in an IDE

    -Dspring.profiles.active=local

### Development data

You can use the file [data.sql](src/test/resources/db/data/data.sql) to insert data to your local db.

### Cleaning DB

All data volume is mapped to directory `tmp`. You can delete this folder to get rid of development data.

## How to run

You can run with maven

    mvn spring-boot:run -Dspring-boot.run.profiles=local

## How to test

    mvn test
    
### Writing Tests

If you want to use db connection on your test you must extend the class `AbstractIntegrationTest`. This class will run the docker container and configure the spring datasource. 

## How to contribute

To get more help or find out how to contribute with this project please take a look at [this page](http://www.ecordel.com.br/como-contribuir).

## Documentation

See other doc files [here.](./docs/README.md)
