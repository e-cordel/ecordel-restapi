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

Add in VMArguments to run in a IDE

    -Dspring.profiles.active=local

## How to run

You can run with maven

    mvn spring-boot:run

or, you can run with docker if you already hava a image

    docker container run --name ecordel --rm -p 8080:8080 ecordel-restapi:1

tip: --rm parameter will exclude container image after execution and it cause data loss.

## How to test

    mvn test

## How to contribute

To get more help or find out how to contribute with this project please take a look at [this page](http://www.ecordel.com.br/como-contribuir).