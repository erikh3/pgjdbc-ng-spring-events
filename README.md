# PostgreSQL LISTEN/NOTIFY <-> Spring events

## Description
Application demonstrates LISTEN/NOTIFY feature found
in PostgreSQL database. pgjdbc-ng driver is used with
addition of Spring asynchronous events. Spring events
can be used to send messages to database server. Also
new messages are received from database server and
translated to asynchronous events.

This functionality is combined with Spring Data JPA
to show seamless integration with at least hacks as
possible.

Multiple clients (applications) can be running at once
and every message will be received by all of them.

## Functionality
Application assigns you random nickname and lets you
type any message. Every connected application will
receive message. Press enter (empty message) to quit.

## Configuration
Database connection properties can be edited in
`src/main/resources/application.properties` file,
or inside override configuration (in standard spring way).
Database must exist and user must have permissions, no other
operations are needed (Hibernate will create or update tables
when needed).

## How to compile & run
This project required java 11.
To compile application simply run `./mvnw clean package`,
final runnable `.jar` file will be located at `target/`.
You can start application by running `./mvnw spring-boot:start`
or `java -jar target/spring-event-notify-demo.jar`.

## Running in docker
If you have docker installed there is easier way of running
this application. First build image `docker build -t spring-demo .`
Then run it with interactive shell, (and access to database)
`docker run -it --network host spring-demo`.