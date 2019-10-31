# BookingGo Challenge

## Project details
- coded in Java and built using Maven and Intellij

## Setup
`git clone https://github.com/vladmihai98/challenge-booking-go.git`

`cd challenge-booking-go`

`mvn clean install`

## Run console app
- before running, dependencies need to be downloaded; in the parent folder with the pom.xml file run:

`mvn install dependency:copy-dependencies`

`cd target/classes`

- for Dave run:

`java -cp .;../dependency/* Rideways.RidewaysConsoleApplication Dave "pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533"`

- the result in my case is: EXECUTIVE - 892875, PEOPLE_CARRIER - 734671, LUXURY_PEOPLE_CARRIER - 38874

##

- for cheapest cars run:

`java -cp .;../dependency/* Rideways.RidewaysConsoleApplication cheapest "pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533" 5`

- result in this case: PEOPLE_CARRIER - Jeff - 238371, MINIBUS - Eric - 541881, LUXURY_PEOPLE_CARRIER - Jeff - 443941

#### once result is displayed press any of CTRL + C, CTRL + X, CTRL + Z since for some reason the app waits about 10s before it completes.


## Run API
- run the following to fire up the API:

`java -jar target/booking-go-challenge-1.0.0.jar`

- to make a request to dave's taxis, access the following link:

http://localhost:8080/api/dave/?coordinates=pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533

- to make a request for the cheapest taxis, access the following link:

http://localhost:8080/api/cheapest/?pickup=51.470020,-0.454295&dropoff=3.410632,-2.157533&passengers=5
