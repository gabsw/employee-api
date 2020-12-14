# Employee REST API
## Abstract
This is a start up exercise for the BE onboarding at Talkdesk. It implements CRUD APIs for an Employee Resource.

## Prerequisites
* Maven 3.6.3
* OpenJDK 11
* Docker and docker-compose

## How to run the database
To build the containers for the first time, make sure that you are in the same folder as `docker-compose.yml` and use following command:
`docker-compose up --build`

To start and stop without rebuilding the containers, use the following commands:
`docker-compose start` or `docker-compose start -d` (run in detached mode)
`docker-compose stop` or `docker-compose stop -d` (run in detached mode)

`docker-compose down -v` will destroy the containers with all the volumes.

## Updating Postgresql Init Files

If you want to update the init sql files and you have already executed `docker-compose up --build`,
then you must delete the postgresql volume that holds the pgdata.

To do that:
1. Destroy the containers with `docker-compose rm`
2. Destroy the `postgres_data` volume with `docker volume rm postgres_data`

After that, you can simply run `docker-compose up` again.

Note that erasing the volume will remove all data.

## Tests
In order to run all the tests:
1. Start the database with `docker-compose up db`.
2. Run `mvn clean test integration-test`. Note that this will truncate tables in the database.

# Quarkus without Docker
How to run the application:
`./mvnw compile quarkus:dev`

# PostgreSQL without Docker

Create databases:
`createdb employees_db`
`createdb test_db`

Create user with password:
`psql employees_db`
`create user app_user with encrypted password 'complex_password';`

Start and stop the database server:
`pg_ctl -D  /usr/local/var/postgres start`

`pg_ctl -D  /usr/local/var/postgres stop`

How to connect to db:
`psql postgresql://app_user:complex_password@localhost:5432/employees_db`

## Author
Gabriela Santos

## Resources
* [Employee REST API documentation](http://localhost:8080/swagger-ui.html#/) (Application must be running first.)
* [Postman collection](https://github.com/gabsw/employee-api/blob/main/Employee%20Api.postman_collection.json)
