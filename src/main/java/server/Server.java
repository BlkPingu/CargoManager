package server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;



//src:https://www.youtube.com/watch?v=O7TuxKJXBII
public class Server {

    final ObservableList<TableObject> tableData = FXCollections.observableArrayList();
    private ServerSocket server;
    public ObservableList<TableObject> getTableData() {
        return tableData;
    }




    public Server(int port){
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(100000);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("client disconnected");
        }
    }

    public void running(){
        while(true){
            try {
                System.out.println("waiting for client" + server.getLocalPort());
                Socket client = server.accept();
                DataInputStream inputStream = new DataInputStream(client.getInputStream());
                System.out.println(inputStream.readUTF());
                System.out.println(client.getRemoteSocketAddress());
                DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
                outputStream.writeUTF("Greetings!");
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        Server s = new Server(1337);
        s.running();
    }
}
