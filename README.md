# eis-app

## Pre-requisites
You have to install:
- [Maven](https://maven.apache.org/)
- [JDK 8](https://www.oracle.com/java/technologies/downloads/)

## Usage
1. Download and extract the .zip
2. Enter the project folder `eis-app`
3. Create a `.env` file in `src/main/resources`
4. If you plan to use *The Guardian* newspaper, append `THE_GUARDIAN_API_KEY=key` in `.env` file, replacing *key* with your key
5. Build the project with
```bash
mvn package 
```
6. Run the app with
```bash 
java -jar target/eis-app-*-jar-with-dependencies.jar [options]
```
