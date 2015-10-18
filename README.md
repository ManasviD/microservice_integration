# microservice_integration
## Introduction
The aim of this project to create the client for resful service (/etc) and improve the same for parallelism.

## Technology
* Build to run on JAVA 8
* The project uses Spring 4 MVC and restful API(s).
* Maven 3.2.3 is used for Build & Execution

## How To Execute 
1. Start the listing Service (/etc) ::
    java -jar listing-1.0-SNAPSHOT-jar-with-dependencies.jar
    
2. Start the search Service (/etc) ::
    java -jar search-1.0-SNAPSHOT-jar-with-dependencies.jar

3. Start the aggregrate search client.
    mvn spring-boot:run

## Agggregated Services

### Basic Search 
    aggregates the Core Services in sequential manner :http://localhost:8080/search?term=test
   ![Basic Search Call Graph](https://raw.githubusercontent.com/ManasviD/microservice_integration/master/images/basic_search.JPG)
  
### Improved Search
   Utlizes the JAVA Executor framework to induce parallelism by executing the calls to the Core Services parallely.
   http://localhost:8080/asearch?term=test
   ![Basic Search Call Graph](https://raw.githubusercontent.com/ManasviD/microservice_integration/master/images/imporved_search.JPG)
