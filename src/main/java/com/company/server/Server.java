package com.company.server;

import com.company.commands.CommandLoader;
import com.company.commands.menu.Menu;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.services.map.ClientsManager;
import com.company.services.map.ExecutionResult;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.common.statuses.ClientAndServerStatus;
import com.company.common.statuses.ExecutionStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The "Server" class implements a simple server application.
 * It uses sockets to accept incoming connections from clients
 * and provides a service to execute commands submitted by clients.
 * <p>
 * The Server class contains two nested classes: "CLIHandler" and "ClientHandler".
 * <p>
 * The "CLIHandler" class is responsible for handling incoming messages from the Command Line Interface (CLI)
 * and executing the requested commands.
 * It holds a menu of available commands and uses this menu to handle requests from the CLI.
 * <p>
 * The "ClientHandler" class is responsible for handling incoming messages from the clients.
 * It receives ExecutionData objects from the clients and passes them to the ExecutionResultService for storage.
 * <p>
 * The Server class uses two sockets, one to listen for connections from clients (clientSocket)
 * and the other to listen for connection from the CLI (cliSocket).
 * When a new client connects to the server, the waitToNewClient() method is called,
 * which creates a new ClientHandler object to handle the client's requests.
 * <p>
 * Similarly, when the CLI connects to the server, a new CLIHandler object is created to handle the CLI's requests.
 * The CLIHandler and ClientHandler classes both run in separate threads to allow the server to handle multiple clients
 * and CLI requests simultaneously.
 * <p>
 * Overall, the "server" class is responsible for handling connections,
 * by creating, stopping, receiving and sending messages to connected clients.
 */

@Slf4j
public class Server {

    private ServerSocket clientSocket;
    private ServerSocket cliSocket;

    private final ClientManagerService clientManagerService;
    private final ExecutionResultService executionResultService;

    public Server() {
        this.clientManagerService = new ClientsManager();
        this.executionResultService = new ExecutionResult(clientManagerService);
    }

    public void start(int serverClientsPort, int serverCliPort) {
        try {
            this.clientSocket = new ServerSocket(serverClientsPort);
            this.cliSocket = new ServerSocket(serverCliPort);
            log.info("listening to {} and {}", serverClientsPort, serverCliPort);

            Socket cliSocket = this.cliSocket.accept();
            log.info("CLI has been connected");
            new Thread(new CLIHandler(cliSocket)).start();

            while (true) {
                waitToNewClient();
                log.info("new client has been connected");
            }
        } catch (Exception ignored) {}
        log.info("Main server has been finished.");
    }

    private void waitToNewClient() throws IOException {
        Socket clientSocket = this.clientSocket.accept();
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clientManagerService.addClient(clientHandler);
        new Thread(clientHandler).start();
    }

    private class CLIHandler implements Runnable {
        private final Socket cliSocket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private final Menu menu;

        public CLIHandler(Socket socket) throws IOException {
            this.cliSocket = socket;
            this.out = new ObjectOutputStream(cliSocket.getOutputStream());
            this.in = new ObjectInputStream(cliSocket.getInputStream());

            this.menu = new Menu(CommandLoader.loadCommands(clientManagerService, executionResultService));
        }

        @Override
        public void run() {
            try {
                BaseCLIToServer cliRequest;
                do {
                    cliRequest = this.receiveMessage();
                    log.info("CLI request: " + cliRequest.toString());
                    BaseServerToCLI response = menu.execute(cliRequest);
                    log.info("server response: " + response.toString());
                    sendMessage(response);
                } while (menu.isRun());
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                try {
                    log.info("Closing CLI and client sockets.");
                    stopConnection();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        private BaseCLIToServer receiveMessage() throws Exception {
            return (BaseCLIToServer) in.readObject();
        }

        private void sendMessage(BaseServerToCLI response) throws IOException {
            out.writeObject(response);
        }

        public void stopConnection() throws IOException {
            cliSocket.close();
            clientSocket.close();
        }
    }

    public class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ObjectOutputStream out;
        private final ObjectInputStream in;

        private int clientId;
        private ClientAndServerStatus status;
        private int removeMessageId;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            status = ClientAndServerStatus.AVAILABLE;
            removeMessageId = -1;
        }

        @Override
        public void run() {
            try {
                ExecutionData result;
                do {
                    result = receiveMessage();
                    log.info("result from client: " + result);

                    executionResultService.addResult(result);
                } while (isClientStillWorking(result));
            } catch (IOException | ClassNotFoundException e) {
                log.error(e.getMessage());
            } finally {
                try {
                    log.info("Closing the client socket.");
                    stopConnection();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        public void sendMessage(BaseServerToClient message) throws IOException {
            out.writeObject(message);
        }

        private ExecutionData receiveMessage() throws IOException, ClassNotFoundException {
            return (ExecutionData) in.readObject();
        }

        public void setRemoveMessageId(int removeMessageId) {
            this.removeMessageId = removeMessageId;
        }

        public int getClientId() {
            return clientId;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public ClientAndServerStatus getStatus() {
            return status;
        }

        public Boolean isAvailable() {
            return status == ClientAndServerStatus.AVAILABLE;
        }

        public void stopConnection() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }

        public void setUnavailable() {
            status = ClientAndServerStatus.UNAVAILABLE;
        }

        public boolean isClientStillWorking(ExecutionData result) {
            if (removeMessageId == result.getMessageId() &&
                    (result.getStatus().equals(ExecutionStatus.FINISHED) ||
                            result.getStatus().equals(ExecutionStatus.ERROR)))
                return false;
            return true;
        }
    }
}