package serialization;

import classes.SaveObject;

import java.io.*;
import java.util.ArrayList;

public class IO {

    public static void saveE(String filename, ArrayList<SaveObject> items){
        try (ObjectOutputStream oos=new ObjectOutputStream(
                new FileOutputStream(filename))){
            oos.writeObject(items);
        } catch (FileNotFoundException e) {}
        catch (IOException e) {}
    }


    public static ArrayList<SaveObject> loadE(String filename){
        try (ObjectInputStream ois=new ObjectInputStream(
                new FileInputStream(filename))){
            return (ArrayList<SaveObject>)ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }
        return null;
    }
}
