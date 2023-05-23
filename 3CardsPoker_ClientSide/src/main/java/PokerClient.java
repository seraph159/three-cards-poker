import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

interface PokerInfoListener {
    void onPokerInfoReceived(PokerInfo pokerInfo);
}

public class PokerClient {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private List<PokerInfoListener> listeners = new ArrayList<>();

    public PokerClient(String host, int port) throws IOException {
        try {
            clientSocket = new Socket(host, port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new IOException("Something Went Wrong: Poker Client");
        }

        Thread listenerThread = new Thread(() -> {
            while (isConnected()) {
                PokerInfo pokerInfo = receivePokerInfo();
                if (pokerInfo != null) {
                    notifyPokerInfoListeners(pokerInfo);
                }
            }
        });
        listenerThread.start();
    }

    public void sendPokerInfo(int playerId, int anteWager, int pairPlusWager, int playWager ,String gameInfo) {
        PokerInfo pokerInfo = new PokerInfo(playerId, anteWager, pairPlusWager, playWager, gameInfo);
        try {
            out.writeObject(pokerInfo);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokerInfo receivePokerInfo() {
        try {
            return (PokerInfo) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isConnected() {
        return clientSocket != null && clientSocket.isConnected();
    }

    public void closeConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPokerInfoListener(PokerInfoListener listener) {
        listeners.add(listener);
    }

    public void removePokerInfoListener(PokerInfoListener listener) {
        listeners.remove(listener);
    }

    private void notifyPokerInfoListeners(PokerInfo pokerInfo) {
        for (PokerInfoListener listener : listeners) {
            listener.onPokerInfoReceived(pokerInfo);
        }
    }
}