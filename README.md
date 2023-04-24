# Server

This is the server component of the C&C application.

# Projects Goal

The main goal of this project is to provide a user-friendly interface for the operator to remotely control the devices
and execute payloads on remote devices. <br>
The operator can also remove clients and retrieve data about payloads results. <br>
Additionally, the operator can easily add more abilities to the remote clients or the server.

# How it works

The project consists of three main components and one shared files component.

<ul>
   <li>
      Server Component: The server is responsible for establishing communication over sockets, 
      managing remote clients, and storing data about payloads sent to the clients.
      It returns the results to the CLI if asked.
   </li>

   <li>
      Client component: The client is the remote server. <br>
      The client connects to the server, and waits for incoming messages for execution. <br>
      Note: The client component is designed to handle multiple messages in parallel,
      allowing it to efficiently process incoming requests without waiting for the completion of previous jobs. 
   </li>

   <li>
      CLI component: The CLI is responsible for managing communication with the operator.
      The CLI sends the desired commands to the server, and prints the server's responses to the console.
   </li>

   <li>
      Shared Files Component: This component contains the shared files and configurations used by all other components.
   </li>
</ul>

# Links

<ul>
   <li>
      Client component: <a href="https://github.com/YinonTzo/client.git"> https://github.com/YinonTzo/client.git</a>
   </li>

   <li>
      CLI component: <a href="https://github.com/YinonTzo/cli.git"> https://github.com/YinonTzo/cli.git</a>
   </li>

   <li>
      Shared Files Component: <a href="https://github.com/YinonTzo/common.git">https://github.com/YinonTzo/common.git</a>
   </li>
</ul>

# Prerequisites

To compile and run the project, make sure you have PostgreSQL and H2 installed.

# Compile & Run

1. Download or clone all 4 projects - server, cli, client and common.
2. Open the projects in your Java IDE.
3. For the client project, enable multiple instances. In Intellij, go to Edit Configurations -> Build and run -> modify
   options -> allow multiple instances. You don't have to do it, but it recommended running multiple clients.
4. This project uses Maven to handle dependencies, so make sure that you can package Maven first in each component
   before compiling and running the code.
5. Create in your postgreSQL new database named "app".
6. Start the server first, then the client(s) and the cli.
7. Check the logs for any errors. If there are no errors, you should see the available commands in the CLI.
8. From now on focus only on the cli.

# Sending A payload

Follow the steps below to send a simple payload that runs and prints "Hello World" on the screen:
<ol>
    <li>In the cli, select the "Send Payload Command" option.</li>
    <li>Type: "Main" when asked to add file name.</li>
    <li>copy and paste:

    public class Main {
        public static void main(String[] args) {
            System.out.println("Hello World");
        }
    }

then type "done" when asked to add java code.
</li>
    <li>Type 'n' when asked if you want to add another file.</li>
    <li>Type 'n' when asked if you want to add program arguments.</li>
    <li>Type: '0' or '-1' to send broadcast when asked to choose client number.</li>
    <li>Type: "done" when asked to choose another clients.</li>
    <li>You should see this output: "Sent payload numbers [0] to clients: [0]".</li>
</ol>

# Viewing Results

Follow the steps below to view the results of the payload:
<ol>
    <li>Select the "Display Command Result" option in the CLI.</li>
    <li>Type: '0' when asked to insert payload ides.</li>
    <li>Type: "done".</li>
    <li>You should now be able to see the complete process that your payload went through and see the
         "Hello World" message.
    </li>
    <li>The other options are easy to understand and follow.</li>
</ol>

<h2>Another Example</h2>

<h4>
   In this example we will send a payload that receives 2 arguments,
   creates a Calculator and prints the result on the screen
</h4>

<ol>
    <li>Select the "Send Payload Command" option.</li>
    <li>Type: "Main" when asked to add file name.</li>
    <li>copy and paste:

    public class Main {
        public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Usage: Main val1 val2");

            System.exit(1);
        }
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);

        Calculator calculator = new Calculator();
        int sum = calculator.add(a, b);
        int difference = calculator.subtract(a, b);
        int product = calculator.multiply(a, b);
        int quotient = calculator.divide(a, b);

        System.out.println("Sum: " + sum);
        System.out.println("Difference: " + difference);
        System.out.println("Product: " + product);
        System.out.println("Quotient: " + quotient);
        }
    }

then type "done" when asked to add java code.
</li>
    <li>Type 'y' when asked if you want to add another file.</li>
    <li>Type: "Calculator" when asked to add file name.</li>
    <li>copy and paste:

    public class Calculator {
        public int add(int a, int b) {
            return a + b;
        }

        public int subtract(int a, int b) {
            return a - b;
        }
   
        public int multiply(int a, int b) {
            return a * b;
        }
   
        public int divide(int a, int b) {
            if (b == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            return a / b;
        }
    }

then type "done" when asked to add java code.
</li>
    <li>Type 'n' when asked if you want to add another file.</li>
    <li>Type 'y' when asked if you want to add program arguments.</li>
    <li>Type '4' when asked to enter an argument.</li>
    <li>Type 'y' when asked if you want to add program arguments.</li>
    <li>Type '2' when asked to enter an argument.</li>
    <li>Type 'n' when asked if you want to add program arguments.</li>
    <li>Type: '0' or '-1' to send broadcast when asked to choose client number.</li>
    <li>Type: "done" when asked to choose another clients.</li>
    <li>You should see this output: "Sent payload numbers [0] to clients: [0]".</li>
    <li>To see the results, you can follow the steps in the "viewing results" section above.</li>
</ol>

# Data Structures

The project primarily uses three data structures:
<ul>
   <li>
      ClientManager: This structure is used to store information about connected clients.
      It assigns a unique ID to each client and provides method to retrieve data about clients.
   </li>

   <li>
      Execution Results: This structure contains information about the payloads sent to clients,
      including their status and results.
   </li>

   <li>
      Menu: Each component has a menu that contains the available commands.
   </li>
</ul>

# Sending Message Protocol

This project consists of two types of operations: <br>
requesting data (about clients or payloads), or sending a payload to a client for execution. <br>

For each operation there is a specific command in the CLI that creates the command and processes the response from the
server. A corresponding command also exists on the server side to process the message and return the response to the
CLI. Additionally, if it is a payload command, there will be a command on the client side.

To create the message in the CLI, the command creates the desired message type which must contain String type and list
of integers represents the desired IDs for clients or payloads.

On the server side, the server's menu identifies the desired command by the message type. The command then processes the
message as required, and returns the response to the CLI.<br>
If it is a payload message, the server saves it in the database, assign it an ID, and sends the payload with the ID to
the specified clients.

On the client side, the client also identifies the desired command by the message type, processes the message
accordingly, and returns the response to the server. The server's database will then locate the appropriate payload
using the ID and add the client's result to the database.

This protocol allows the operator to easily add more functionality to the project. <br>
Each command in each component must create and process the message accordingly. <br>
If there is no appropriate message available, the operator can inherit from the base messages and create their own
message.

# Third-Party Libraries And Tests

In this project I used in Maven, Java 17, Lombok, Junit5, PostgreSQL, H2 and logback.

# Limitation

To simplify, the programs that send to the clients needs to be Java only. <br>
Additionally, There are no packages. You can see in the run examples how to copy the code (without packages). <br>
Comments in the code must be in this format: // (and not /**/). <br>
There isn't any checking about the code. Any code that compiles and runs outside the program, will also compile and run
in the program. <br>
The main class name must be "Main". <br>
The compilation command is:  
"javac -d " + outputDirectory + " " + initializedCodeDirectory + "/*.java"; <br>
and the run command is:
"java -cp " + path + "/out Main " + arguments; <br>
Where the "outputDirectory", "initializedCodeDirectory" and "path" are the dirs where the code placed. <br>