# Server

Server Part In C&C Application

# Compile & Run

1. Download or clone all 4 projects - server, cli, client and common.
2. Open them in java IDE.
3. In the client project, allow multiple instance:
   In Intellij go Edit Configurations -> Build and run -> modify options -> allow multiple instances.
4. Run the server first, then the client(s) and the cli.
5. Now you need to see some logs, if there are no errors you need to see in the cli the available commands.
6. From now we will focus only in the cli.

# Sending A payload

Imagine we want to send a simple payload which runs and prints "Hello world" on the screen.
<ol>
    <li>In the cli, choose "Send Payload Command"</li>
    <li>Output: "Enter the file name". Type: Main</li>
    <li>Output: "Enter The Java Code" Type:

    public class Main {
        public static void main(String[] args) {
            System.out.println("Hello World");
        }
    }
    done 
</li>
    <li>Output: "Do you want to add another file? (y/n)". Type: 'n'</li>
    <li>Output: "Do you want to add program arguments? (y/n)". Type: 'n'</li>
    <li>
        Output: "Enter a client number (-1 for all clients) or type 'done' to finish: ".
        Type: 0 or -1 to send broadcast.
    </li>
    <li>
        Output: "Enter a client number (-1 for all clients) or type 'done' to finish: ".
        Type: "done".
    </li>
    <li>Output: "Sent payload numbers [0] to clients: [0]". </li>

</ol>

# See The Results

<ol>
    <li>choose "Display Command Result"</li>
    <li>
        Output: "Enter payload ides (-1 for all) numbers or type 'done' to finish: ".
        Type: '0'
    </li>
    <li>
        Output: "Enter payload ides (-1 for all) numbers or type 'done' to finish: ".
        Type: "done".
    </li>
    <li>Now you can see the complete process that your payload has beet throw and see the "Hello World".</li>
</ol>