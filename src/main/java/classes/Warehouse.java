package classes;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {

    private ArrayList<SaveObject> tableObjects = new ArrayList<>(
                List.of(
                new SaveObject("Liquid", "Dave", 5, 10, true, true, false, true, "P--"),
                new SaveObject("Boxed", "Frank", 8, 5, false, true, false, true, "--F"),
                new SaveObject("Boxed", "Frank", 3, 15, false, true, false, true, "--F"),
                new SaveObject("Dry", "James", 6, 2, false, true, false, true, "-S-")
                )
            );


    public ArrayList<SaveObject> getTableObjects() {
        return tableObjects;
    }


    /*

    */
}
