import javafx.application.Platform;
import javafx.collections.ObservableList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

public class PokerServer {
    private int port;
    private ServerSocket serverSocket;
    private List<Socket> sockets = new ArrayList<>(); // maintain a list of active sockets

    private boolean stopAccepting;
    ObservableList<String> gameInfoList;
    private AtomicInteger connectedClients = new AtomicInteger(0);

    private AtomicInteger playerIdCounter = new AtomicInteger(0);

    public PokerServer(int port, ObservableList<String> gameInfoList) throws FileNotFoundException {
        this.port = port;
        this.stopAccepting = false;
        this.gameInfoList = gameInfoList;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (!stopAccepting) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        sockets.add(clientSocket); // add the active socket to the list
                        connectedClients.incrementAndGet();
                        playerIdCounter.incrementAndGet();
                        Platform.runLater(()->{
                            gameInfoList.add("New client connected. Player ID: " + playerIdCounter.get());
                            gameInfoList.add("Total connected clients: " + connectedClients.get());
                        });
                        new Thread(new ClientHandler(clientSocket, gameInfoList, connectedClients, playerIdCounter)).start();
                    } catch (SocketException e) {
                        // to handle the SocketException error thrown by accept after closing server socket
                        // or invoking stopServer()
                        if (serverSocket.isClosed()) {
                            System.out.println("Server socket closed, stopping server.");
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopServer() {
        try {
            stopAccepting = true;
            for (Socket s : sockets) {
                s.close(); // close all active sockets
            }
            sockets.clear(); // remove all sockets from the list
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ClientHandler implements Runnable {
    private Socket clientSocket;

    private Game game;

    private AtomicInteger connectedClients;

    private AtomicInteger playerId;

    private ObservableList<String> gameInfoList;
    public ClientHandler(Socket clientSocket, ObservableList<String> gameInfoList, AtomicInteger connectedClients, AtomicInteger playerId) throws FileNotFoundException {

        this.clientSocket = clientSocket;
        this.game = new Game(playerId, gameInfoList);
        this.connectedClients = connectedClients;
        this.playerId = playerId;
        this.gameInfoList = gameInfoList;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            PokerInfo clientRequestData = new PokerInfo(0, 0,0,0, "");
            out.writeObject(clientRequestData);
            out.flush();
            while ((clientRequestData = (PokerInfo) in.readObject()) != null) {
                // Process received PokerInfo object and send a response
                clientRequestData.setPlayerId(playerId.get());
                String message = clientRequestData.getGameInfo();
                System.out.println("Received from client " + playerId + ": " + message);

                final int antWagerFinal = clientRequestData.getAnteWager();
                if(message.equals("deal")){
                    Platform.runLater(() -> {
                        gameInfoList.add("Client " + playerId + " bet: " + antWagerFinal);
                    });
                    //System.out.println("uu");
                    PokerInfo clientResponseInfo = new PokerInfo(clientRequestData.getPlayerId(), clientRequestData.getAnteWager()
                            ,clientRequestData.getPairPlusWager(),clientRequestData.getPlayWager(), "deal");
                    game.updatePlayerDealState(clientResponseInfo);
                    out.writeObject(clientResponseInfo);
                } else if(message.equals("play")){
                    PokerInfo clientResponseInfo = new PokerInfo(clientRequestData.getPlayerId(), clientRequestData.getAnteWager()
                            ,clientRequestData.getPairPlusWager(),clientRequestData.getPlayWager(), "play");
                    game.updatePlayerPlayState(clientResponseInfo);
                    out.writeObject(clientResponseInfo);
                } else if(message.equals("fold")){
                    PokerInfo clientResponseInfo = new PokerInfo(clientRequestData.getPlayerId(), clientRequestData.getAnteWager()
                            ,clientRequestData.getPairPlusWager(),clientRequestData.getPlayWager(), "fold");
                    game.updatePlayerFoldState(clientResponseInfo);
                    out.writeObject(clientResponseInfo);
                } else if (message.equals("restart")) {
                    PokerInfo clientResponseInfo = new PokerInfo(clientRequestData.getPlayerId(), clientRequestData.getAnteWager()
                            ,clientRequestData.getPairPlusWager(),clientRequestData.getPlayWager(), "restarted into a fresh game");
                    game.setTotalWin(clientResponseInfo,1000);
                    out.writeObject(clientResponseInfo);
                }
                out.flush();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            connectedClients.decrementAndGet();
            Platform.runLater(() -> gameInfoList.add("Client" + playerId + "disconnected. Total clients: " + connectedClients.get()));
        }
    }
}