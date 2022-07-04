# Leaderboard
A simple CLI application that calculates rankings based on the outcome of matches between teams participating in a league.

# What do I need to build it?

The application is written in Java with Maven as the dependency management and build tool. You will need:

- Maven >= 3.8.4 
- JDK >= 1.8

# How do I build it ?

After pulling the repository, navigate to the leaderboard project root directory (where the pom.xml lives) and type the following command:

`mvn clean install`

This will pull all of thr project's dependecies and build it, and run the tests.

# How do I run/use it?

The leaderboard application takes in a filepath as a comman line argument. Make sure the input is referencing a file with well formed data and it will do the rest!

Run the below command from the root directory of the repo

`mvn exec:java -Dexec.mainClass=com.span.digital.leaderboard.Leaderboard -Dexec.args="<path/to/input/file>"`

Example:

`mvn exec:java -Dexec.mainClass=com.span.digital.leaderboard.Leaderboard -Dexec.args="src/test/resources/scores_sample"`

