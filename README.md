# PostgreSQL Branch
This branch contains the same project as the main branch but uses PostgreSQL to store data and H2 for tests instead of in-memory data structures.

# Prerequisites
To compile and run the project, make sure you have PostgreSQL and H2 installed.

# Compile & Run

1. Download or clone all 4 projects - server, cli, client and common.
2. Open the projects in your Java IDE.
3. For the client project, enable multiple instances. In Intellij, go to Edit Configurations -> Build and run -> modify
   options -> allow multiple instances. You don't have to do it, but it recommended running multiple clients.
4. This project uses Maven to handle dependencies, so make sure that you can package Maven first in each component
   before compiling and running the code.
5. create in your postgreSQL new Database named "app".
6. Start the server first, then the client(s) and the cli.
7. Check the logs for any errors. If there are no errors, you should see the available commands in the CLI.
8. From now on focus only on the cli.

Now you can follow the same instruction from the main branch.