# Server

Server Part In C&C Application

# Compile & Run

1. Download or clone all 4 projects - server, cli, client and common.
2. Open the projects in your Java IDE.
3. For the client project, enable multiple instances. In Intellij, go to Edit Configurations -> Build and run -> modify
   options -> allow multiple instances. You don't have to do it, but it recommended running multiple clients. 
4. Start the server first, then the client(s) and the cli.
5. Check the logs for any errors. If there are no errors, you should see the available commands in the CLI.
6. From now on focus only on the cli.

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
    done 
when asked to add java code.
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