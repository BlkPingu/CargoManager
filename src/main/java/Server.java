import classes.TableObject;
import classes.SaveObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


//src:https://www.youtube.com/watch?v=O7TuxKJXBII
public class Server {

    private ServerSocket server;


    public ArrayList<SaveObject> tableObjects = new ArrayList<>();




    public byte[] toBytes(ArrayList<SaveObject> sol) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(sol);
            out.flush();
            byte[] bytes = bos.toByteArray();
            return bytes;
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
    }


    public ArrayList<TableObject> toObject (byte[] bytes) throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            ArrayList<TableObject> list = (ArrayList<TableObject>) o;
            return list;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return null;
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


    public void addData(SaveObject so){
        tableObjects.add(so);
    }

    public void running(){
        while(true){
            try {
                Socket client = server.accept();
                DataInputStream inputStream = new DataInputStream(client.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());


                //adding test data
                addData(new SaveObject("Liquid", "Dave", 5, 10, true, true, false, true, "P--" ));
                addData(new SaveObject("Boxed", "Frank", 8, 5, false, true, false, true, "--F"));
                addData(new SaveObject("Boxed", "Frank", 3, 15, false, true, false, true, "--F"));
                addData(new SaveObject("Dry", "James", 6, 2, false, true, false, true, "-S-"));
                addData(new SaveObject("Dry", "James", 6, 2, false, true, false, true, "-S-"));




                outputStream.write(toBytes(tableObjects));


                //switch()
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
