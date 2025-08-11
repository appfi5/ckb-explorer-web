# CKB Explorer Web API

This is a REST API service for CKB Explorer, built with Spring Boot and Maven.

## Technologies Used
- Java 21
- Spring Boot 3.2.10
- Maven

## Project Structure
- `src/main/java`: Java source code
- `src/main/resources`: Configuration files
- `src/test/java`: Test code
- `pom.xml`: Maven configuration file

## API Endpoints
- `GET /api/hello`: Simple hello endpoint
- `GET /api/blocks/{blockHash}`: Get block by hash
- `GET /api/blocks/latest`: Get latest block

## How to Run
1. Make sure you have Java 21 installed
2. Build the project with Maven:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```
4. Access the API at `http://localhost:8081/api/v1/swagger-ui/index.html`