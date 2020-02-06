
<p align="center"><img width=60% src="https://i.ibb.co/wh3v6Sd/scit-logo.png"></p>
<p align="center">
🔬📜 A web-based scientific paper publishing system developed during the XML and Web Services university course.</p>

## Requirements
* [Java 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/index.html)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Angular CLI](https://cli.angular.io/)
* [eXist-db](http://exist-db.org/exist/apps/homepage/index.html)
* [Apache Jena](https://jena.apache.org/)
* [Docker](https://www.docker.com/) (optional)
* [IntelliJ IDE](https://www.jetbrains.com/idea/) (optional) or [Eclipse IDE](https://www.eclipse.org/downloads/packages/release/2019-09/r/eclipse-ide-enterprise-java-developers) (optional)

## Environment setup

### For Docker
1. Make sure Docker is up and running, and set the configuration to Linux containers.
2. Open `cmd` and navigate to the backend folder where `docker-compose.yml` is located
3. Start the containers using the `docker-compose up` command.

### General
1. Make sure eXist-db is running on port `8080`
	> `username: admin`
	
	> `password: [leave blank]`
2. Go to eXist dashboard and manually create a new collection named `scit` on path `db/apps`.
3. Upload the XML resources located in `resources/data` to the previously created collection
4. Make sure Apache Jena is running on port `3030`
	> `username: admin`
	
	> `password: admin`
5. Go to Jena dashboard and create a new persistent dataset named `scit`.

## Running the application

### Backend
Start it as a [Spring Boot application](https://spring.io/projects/spring-boot)
> NOTE: For frontend to work properly, backend must run on port 8001

In case something doesn't work, make sure all the parameters in the `application.properties` file are configured properly.
### Frontend

```
npm install
```

```
#Compiles and hot-reloads for development
ng serve
```

Go to <http://localhost:4200> to use the application.

## About the Team

| [<img src="https://avatars1.githubusercontent.com/u/17569172?s=88&v=4" width="100px;"/>](https://github.com/fivkovic)<br/> [<sub>Filip Ivković</sub>](https://github.com/fivkovic) |
  [<img src="https://i.ibb.co/VmhxPnd/c38df0e9296d992a3d7e67a0eb7bb86f.png" width="100px;"/>](https://github.com/kettkitt)<br/> [<sub>Katarina Tukelić</sub>](https://github.com/kettkitt) |
  [<img src="https://avatars1.githubusercontent.com/u/30222786?s=88&v=4" width="100px;"/>](https://github.com/FilipMeng)<br/> [<sub>Filip Mladenović</sub>](https://github.com/FilipMeng) |
 | --- | --- | --- |
