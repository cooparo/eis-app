# eis-app

## Pre-requisites
You have to install:
- [Maven](https://maven.apache.org/) 
- [JDK 8](https://www.oracle.com/java/technologies/downloads/)

## Usage
1. Download and extract the .zip
2. Enter the project folder ``/eis-app``
3. Add file named ``env`` with inside ``THE_GUARDIAN_API_KEY="your_api"``, in the ``Resources`` directory (both /test/resources and /main/resources)
4. Build the project with
```bash
mvn package 
```
4. Run the app with
```bash 
java -jar target/eis-app-*-jar-with-dependencies.jar [options]
```
