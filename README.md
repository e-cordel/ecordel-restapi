[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=e-cordel_ecordel-restapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=e-cordel_ecordel-restapi)

# e-cordel REST API

This is the REST API for e-cordel project.

In this project you can find an openapi [specification](./openapi.yaml) with all the information about the endpoints available. 

## How to build

You can build with maven

    mvn clean package

or, you can build a docker image

    docker build . -t ecordel-restapi:1

## How to run in local develop

To up postgres in a docker local

    docker-compose up -d

Add in VMArguments to run in an IDE

    -Dspring.profiles.active=local

### Development data

You can use the file [data.sql](src/test/resources/db/data/data.sql) to insert data to your local db.

### Cleaning DB

All data volume is mapped to directory `tmp`. You can delete this folder to get rid of development data.

## How to run

You can run with maven

    mvn spring-boot:run -Dspring-boot.run.profiles=local

or, you can run with docker if you already have an image

    docker container run --name ecordel --rm -p 8080:8080 ecordel-restapi:1

tip: --rm parameter will exclude container image after execution and it cause data loss.

## How to test

    mvn test
    
### Writing Tests

If you want to use db connection on your test you must extend the class `AbstractIntegrationTest`. This class will run the docker container and configure the spring datasource. 

## How to contribute

To get more help or find out how to contribute with this project please take a look at [this page](http://www.ecordel.com.br/como-contribuir).
