
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import classes.SaveObject;


    public class Server implements Runnable {
        private Socket socket;
        private Server(Socket socket){ this.socket=socket; }

        private ArrayList<SaveObject> tableObjects = new ArrayList<>();
                /*
                List.of(new SaveObject("Liquid", "Dave", 5, 10, true, true, false, true, "P--"),
                        new SaveObject("Boxed", "Frank", 8, 5, false, true, false, true, "--F"),
                        new SaveObject("Boxed", "Frank", 3, 15, false, true, false, true, "--F"),
                        new SaveObject("Dry", "James", 6, 2, false, true, false, true, "-S-")));
                */
        private byte[] toBytes(ArrayList<SaveObject> sol) throws IOException{
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

        private SaveObject bytes2SaveObject (byte[] bytes) throws IOException, ClassNotFoundException{
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = new ObjectInputStream(bis);

            return (SaveObject) in.readObject();
        }




        @Override public void run(){
            try (DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                switch (in.readChar()) {
                    case 'A':
                        // add
                        System.out.println("Case A: Listsize old > " + tableObjects.size());
                        SaveObject so = bytes2SaveObject(in.readAllBytes()); //tut was
                        tableObjects.add(so); //tut was
                        System.out.println("Case A: New SO revieved: " + so);
                        System.out.println("Case A: List >" + tableObjects.toString());
                        System.out.println("Case A: Listsize new > " + tableObjects.size());
                        System.out.println("----------");
                        break;
                    case 'B':
                        //get server data
                        System.out.println("Case B: List >" + tableObjects.toString());
                        System.out.println("Case B: Sending List of size > " + tableObjects.size());
                        out.write(toBytes(tableObjects));
                        System.out.println("----------");
                        break;
                    case 'C':
                        //delete
                        tableObjects.remove(bytes2SaveObject(in.readAllBytes()));
                        System.out.println("Case C: removed object");
                        System.out.println("----------");
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
            try(ServerSocket serverSocket=new ServerSocket(1337)) {
                while(true){
                    Socket socket=serverSocket.accept();
                    Server s = new Server(socket);
                    System.out.println("Client: "+socket.getInetAddress()+":"+socket.getPort());
                    new Thread(s).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
