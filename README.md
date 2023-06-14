# eis-app

## Pre-requisites
You have to install:
- [Maven](https://maven.apache.org/) 
- [JDK 8](https://www.oracle.com/java/technologies/downloads/)

## Usage
1. Download and extract the .zip
2. Enter the project folder ``/eis-app``
3. Build the project with
```bash
mvn package 
```
4. Run the app with
```bash 
java -jar target/eis-app-*-jar-with-dependencies.jar [options]
```

For the Javadoc, run the maven site plugin with
```bash
mvn site:run
```