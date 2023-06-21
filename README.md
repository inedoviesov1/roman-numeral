# Roman Numerals

Roman Numerals is a web service that takes in a number and outputs a Roman numeral. Ancient Romans used a special method of showing numbers. See the details on Roman numerals
and transformation rules in the following article https://www.mathsisfun.com/roman-numerals.html

## Building and running the project

### Requirements

Building this project requires the following:

* Java 17+
* Maven 3.5+

### Building

Maven is used as a build tool for this project. The project can be built from source by running:

`mvn clean package`

This command will create an executable JAR file inside `roman-numeral/target` directory
called `roman-numeral-<version>.jar`.
Generated JAR file contains the compiled Java classes from our project and also all the runtime libraries that are
needed to start this Spring Boot application,
for example, an embedded tomcat library is packaged so the application can be started from this JAR file.

### Running

You can run the application using an executable JAR build in the previous step by running the following command:

```bash
java -jar target/roman-numeral-0.0.1-SNAPSHOT.jar
```  

Alternatively, you can run it from command line with Maven:

```bash
mvn spring-boot:run
```

The service should be up and running within a few seconds. Embedded server is started on port 8080, and it can be tested
by visiting http://localhost:8080/romannumeral?query=1

## Engineering and testing

Before start development I looked into the assignment to identify core problems.
The following are considered to be individual tasks and could be handled separately:

* number to Roman numeral conversion
* web service

### Number to Roman numeral conversion

This is a core logical task in this assignment. As it was not allowed to use any framework or utilities for solving this
problem I decided to start with this one.

This task can be implemented as a standalone class, which can then be used in any environment. It is straightforward to
test it as conversion of numbers to roman numerals is well documented and complete. For this particular reason I decided
to implement it using TTD.

### Web Service

I considered a couple of options for implementing this part:

* use Tomcat server and implement a webservice from scratch using standard java techniques like a servlet to implement
  an end-point
* use Spring Boot (selected solution)

[Spring Boot](https://spring.io/projects/spring-boot) has been selected as a framework to implement a webservice for
this task. Spring Boot makes it easy to create stand-alone Spring based Applications
and provides the features that help to speed up development process, testing and shipment of the application. The main
reasons to user Spring Boot in this case:

* create stand-alone Spring application
* embedded Tomcat (no need to deploy WAR files)
* "starter" dependencies to simplify build configuration
* publicly available

### Implementation details

According to Spring's approach RESTful Web Service is implemented using controller. In our case
class `RomanNumeralController` is annotated with  _@RestController_ (similar to _@SlingServlet_ in Apache Sling). This
controller handles requests to path `/romannumeral`.

`RomanNumeralServiceImpl` is a service component annotated with _@Service_ and implements an
interface `RomanNumeralService`. This service is being called from `RomanNumeralController` via interface, that in
Spring's approach it allows to replace an implementation class with another one. This is similar to Apache Felix in AEM
world.

### Testing

`RomanNumeralServiceImpl` is unit tested with JUnit 5 framework and _@ParameterizedTest_ for repeatable tests.

`RomanNumeralController` is unit tested using Spring's capabilities provided with the
dependency `spring-boot-starter-test`. We use _@WebMvcTest_ to test our controller. It will auto-configure the Spring
MVC infrastructure for our unit tests, and we can mock parts of the application that should not be part of this test,
for example we mock `RomanNumeralService` because we test is separately.

## Application packaging

To be able to start our Spring Boot application using the simple `java -jar` command, we need to build a fat JAR. The Spring Boot
Maven Plugin helps with that. This plugin is configured in `pom.xml` and made part of Maven's _package_ lifecycle.

In addition to the compiled classes and resources generated JAR file has the following characteristics:

* contains all the runtime libraries that are needed to start our Spring Boot application. For example, an embedded
  tomcat library is packaged into the `BOOT-INF/lib` directory.
* JAR file is made executable by adding required properties to `META-INF/MANIFEST.MF`

## Project dependencies

Maven is used as a build automation and dependency management tool. This project uses Spring Boot Starter to simplify
development, this is done by inheriting from parent in root `pom.xml`:

```xml

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.0</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

This allows us to reuse capabilities provided by the starter project without a need to define everything in
our `pom.xml`.

Project has the dependencies described in the table bellow.

| Dependency                                                                                                                       | Description                                                                                                                |
|----------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| `org.springframework.boot:spring-boot-starter-web`                                                                               | Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container. |
| `org.hibernate.validator:hibernate-validator`                                                                                    | Hibernate's Bean Validation (JSR-380) reference implementation.                                                            |
| `org.springframework.boot:spring-boot-devtools`                                                                                  | Spring Boot Developer Tools. In our particular case used to enable hot deployment to running server locally.               |
| `org.springframework.boot:spring-boot-starter` <br/> __exclution__: <br/> `org.springframework.boot:spring-boot-starter-logging` | Exclude Spring Boot's Default Logging                                                                                      |
| `org.springframework.boot:spring-boot-starter-log4j2`                                                                            | Log4j2 Dependency                                                                                                          |
| `org.springframework.boot:spring-boot-starter-actuator`                                                                          | Starter for using Spring Boot's Actuator (features to monitor and manage your application)                                 |
| `pl.project13.maven:git-commit-id-plugin`                                                                                        | This plugin makes basic repository information available through maven resources.                                          |
| `org.springframework.boot:spring-boot-starter-test`                                                                              | Starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito.                 |

## DevOps Capabilities

This section describes DevOps capabilities that are enabled in the project.

### Logging

The application has logging enabled using Spring's capabilities and Log4j2 logging framework. Log file location is `logs/romannumeral.log` in the directory from where the application is being started.

General log level is set to `INFO` for the whole application. Logging for package `com.inedoviesov1.romannumeral.service.impl` is set to level `DEBUG` to see the detailed process of converting integer to roman numeral.

### Monitoring

As the application is based on Spring Boot we can utilize its monitoring capabilities. Spring provides a few out-of-the-box monitoring features and in this app we use the following:
* `http://localhost:8080/actuator/health` - heath check-up provides basic information about the application’s health. It shows status `UP` as long as the application is healthy. Currently, there are no additional checks in the applications, but for example if there was a service that connects to a database we could check a connection and use it as an indicator in the health check.
* `http://localhost:8080/actuator/info` - displays information about the application. For example, we can display project name, description and version as defined in Maven `pom.xml`. In addition, we include a Git information with branch name, commit and time to quickly identify currently deployed state in case of any error.

### Metrics

Spring also provides capabilities to access application metrics, where some are provided out-of-the-box, and custom metrics can be added to the system.

Custom metric has been added to count the requests to `/romannumeral` path, including successful and failed requests. It also adds a reason of failure. See the table below describing what metrics can be explored for Roman Numerals application.

| URL                                                                                         | Description         |
|---------------------------------------------------------------------------------------------|---------------------|
| http://localhost:8080/actuator/metrics/romannumeral.requests.inttoroman                     | Total requests      |
| http://localhost:8080/actuator/metrics/romannumeral.requests.inttoroman?tag=outcome:SUCCESS | Successful requests |
| http://localhost:8080/actuator/metrics/romannumeral.requests.inttoroman?tag=outcome:ERROR   | Failed requests     |

Failed requests can be further expanded with an additional parameter value for parameter `tag`:
* `tag=cause:missing-param` - missing required parameter
* `tag=cause:type-mismatch` - wrong type of the parameter, e.g. string instead of integer
* `tag=cause:value-out-of-range` - value is out of the allowed range

Example response from http://localhost:8080/actuator/metrics/romannumeral.requests.inttoroman:
```json
{
  "name": "romannumeral.requests.inttoroman",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 10.0
    }
  ],
  "availableTags": [
    {
      "tag": "cause",
      "values": [
        "missing-param",
        "type-mismatch",
        "value-out-of-range"
      ]
    },
    {
      "tag": "outcome",
      "values": [
        "ERROR",
        "SUCCESS"
      ]
    }
  ]
}
```


## Docker

Roman Numerals application can be containerized using Docker. Docker deamon should be running on the host machine in order to be able to build an image.

Docker image can be built by running the following command:

```shell
docker build -t inedoviesov/roman-numeral .
```

This command builds the image called `inedoviesov/roman-numeral` based on the `Dockerfile` located in the root directory.

To run newly built image execute the following command:

```shell
docker run --name roman-numeral -p 8080:8080 inedoviesov/roman-numeral
```

_**Note**: after running the container with the above command you cannot run it with the same name again. To start existing container run the command:_
```shell
docker start roman-numeral 
```

You can connect to the running container via terminal by executing a command:

```shell
docker exec -ti roman-numeral /bin/sh
```

Container has a configured volume to preserve the logs between the runs. Application logs are located in the directory `/tmp/logs`.
