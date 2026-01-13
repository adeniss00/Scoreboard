
# Football World Cup Score Board


A simple Java library for managing a real-time football match scoreboard. This project was developed as a technical exercise to demonstrate the application of SOLID principles, TDD, and clean code practices.


## Run Locally

Clone the project

```bash
  git clone https://github.com/adeniss00/Scoreboard
```

Go to the project directory

```bash
  cd Scoreboard-master
```

Install dependencies

```bash
  mvn clean install
```

Launch the main

```bash
  mvn package
```


## Running Tests

To run tests, run the following command

```bash
  mvn test
```


## Tech Stack

- Java 17
- Maven 3.9+

## Features

- Start a new match (initial score 0-0)
- Finish an ongoing match
- Update the score of an existing match
- Get a summary of matches, sorted by total score (descending) and then by addition time (most recent first)


## Architecture
```
football-scoreboard/
├── src/
│   ├── main/java/com/scoreboard/
│   │   ├── IScoreBoard.java
│   │   ├── ScoreBoardImpl.java
│   │   ├── model/Match.java
│   │   └── exception/ScoreBoardException.java
│   └── test/java/com/scoreboard/ScoreBoardImplTest.java
├── pom.xml
└── README.md

```