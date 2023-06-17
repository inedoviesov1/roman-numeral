# Roman numerals

Ancient Romans used a special method of showing numbers. See the details on Roman numerals
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

To start our Spring Boot application using the simple `java -jar` command, we need to build a fat JAR. The Spring Boot
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

| Dependency                | Description|
|---------------------------|------------|
| `org.springframework.boot:spring-boot-starter-web` | Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container. |
| `org.hibernate.validator:hibernate-validator` | Hibernate's Bean Validation (JSR-380) reference implementation.|
| `org.springframework.boot:spring-boot-devtools` | Spring Boot Developer Tools. In our particular case used to enable hot deployment to running server locally.              |
| `org.springframework.boot:spring-boot-starter-test` | Starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito.                |

