
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import classes.SaveObject;
import classes.Warehouse;


public class Server implements Runnable {
    private Socket socket;
    private Warehouse warehouse;


    private Server(Warehouse warehouse, Socket socket){
        this.socket=socket;
        this.warehouse = warehouse;
    }


    private byte[] toBytes(ArrayList<SaveObject> obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }



    /*
    private byte[] toBytes(ArrayList<SaveObject> sol) throws IOException{

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
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
    */

    private SaveObject bytes2SaveObject (byte[] bytes) throws IOException, ClassNotFoundException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis);

        return (SaveObject) in.readObject();
    }





    @Override public void run(){



        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            char command = in.readChar();
            System.out.println(command);
            switch (command) {
                case 'A':
                    // add
                    SaveObject so = bytes2SaveObject(in.readAllBytes());
                    warehouse.getTableObjects().add(so);
                    System.out.println("Added new Object: " + so + " | List is now of size: " +  warehouse.getTableObjects().size());
                    break;
                case 'B':
                    //get server data
                    out.write(toBytes(warehouse.getTableObjects()));
                    System.out.println("Data List sent. Size: " + warehouse.getTableObjects().size());
                    break;
                case 'C':
                    //delete
                    SaveObject deleted = bytes2SaveObject(in.readAllBytes());
                    for(SaveObject m : warehouse.getTableObjects()){
                        System.out.println(warehouse.getTableObjects().indexOf(m) + " - "+ m.getCustomer() + " / " + m.getPosition());


                        if(m.getCustomer().equals(deleted.getCustomer()) && (m.getPosition().equals(deleted.getPosition()))){
                            System.out.println(warehouse.getTableObjects().contains(m));
                            System.out.println(m);
                            warehouse.getTableObjects().remove(m);
                            System.out.println(warehouse.getTableObjects().contains(m));
                        }
                    }
                    //System.out.println("Deleted the Object with Customer Name: " + deleted.getCustomer() + " | Pos:" + deleted.getPosition());

                    break;
                default:
                    System.out.println("ERROR! Something weird happened!");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse();

        try(ServerSocket serverSocket=new ServerSocket(1337)) {


            while(true){
                Socket socket = serverSocket.accept();
                Server s = new Server(warehouse,socket);
                System.out.println("Client: "+socket.getInetAddress()+":"+socket.getPort());
                Thread t = new Thread(s);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}