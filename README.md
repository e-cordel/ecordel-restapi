# ecordel-rest-api
to run a api container:
1. docker build . -t ecordel-restapi:1
2. docker container run --name ecordel --rm -p 8080:8080 ecordel-restapi:1

tip: --rm parameter will exclude container image after execution and it cause data loss.
