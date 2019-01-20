import enums.Hazard;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import serialization.IO;
import classes.SaveObject;
import classes.TableObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import java.net.*;



public class TableController implements Initializable {

    /**
     * Menu Bar
     */
    @FXML AnchorPane canvas;
    @FXML private Button addBtn;
    @FXML private Button deleteBtn;

    /**
     * Cargo Table
     */
    @FXML private TableView cargoTable;
    @FXML private TableColumn typeCol;
    @FXML private TableColumn customerCol;
    @FXML private TableColumn positionCol;
    @FXML private TableColumn sizeCol;
    @FXML private TableColumn radioactiveCol;
    @FXML private TableColumn flammableCol;
    @FXML private TableColumn toxicCol;
    @FXML private TableColumn explosiveCol;
    @FXML private TableColumn propertiesCol;
    @FXML private MenuButton type;
    @FXML private TextField customerName;
    @FXML private TextField position;
    @FXML private TextField size;
    @FXML private RadioButton pressurized;
    @FXML private RadioButton solid;
    @FXML private RadioButton fragile;
    @FXML private RadioButton radioactive;
    @FXML private RadioButton flammable;
    @FXML private RadioButton toxic;
    @FXML private RadioButton explosive;

    final ObservableList<TableObject> tableData = FXCollections.observableArrayList();
    final ArrayList<SaveObject> saveObjects = new ArrayList<>();

    public void addData(SaveObject saveObject) throws IOException {
        Socket client = new Socket("localhost", 1337);
        sendCode(client,'A');
        sendObject(client,saveObject);
        client.close();
        populateTable();

    }

    public void getServerData() throws IOException {
        Socket client = new Socket("localhost", 1337);
        sendCode(client,'B');
        populateClientList(toObject(new DataInputStream(client.getInputStream()).readAllBytes()));
        client.close();

    }

    public void deleteData(SaveObject so) throws IOException {
        Socket client = new Socket("localhost", 1337);
        sendCode(client,'C');
        sendObject(client,so);
        client.close();
        populateTable();
    }



    public void sendObject(Socket socket, SaveObject saveObject){
        try {
            new DataOutputStream(socket.getOutputStream()).write(toBytes(saveObject));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCode(Socket socket, char code){
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeChar(code);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addCargoToList(ActionEvent actionEvent) {
        int pos = Integer.parseInt(position.getText());
        int siz = Integer.parseInt(size.getText());
        SaveObject obj = new SaveObject(type.getText(), customerName.getText(), pos, siz, radioactive.isSelected(), flammable.isSelected(), toxic.isSelected(), explosive.isSelected(), pressurizedArmed() + solidArmed() + fragileArmed());

        try {
            addData(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateTable(){
        typeCol.setCellValueFactory(new PropertyValueFactory<TableObject, String>("Type"));
        customerCol.setCellValueFactory(new PropertyValueFactory<TableObject, String>("Customer"));
        positionCol.setCellValueFactory(new PropertyValueFactory<TableObject, Integer>("Position"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<TableObject, Integer>("Size"));
        radioactiveCol.setCellValueFactory(new PropertyValueFactory<TableObject, Hazard>("radioactive"));
        flammableCol.setCellValueFactory(new PropertyValueFactory<TableObject, Hazard>("flammable"));
        toxicCol.setCellValueFactory(new PropertyValueFactory<TableObject, Hazard>("toxic"));
        explosiveCol.setCellValueFactory(new PropertyValueFactory<TableObject, Hazard>("explosive"));
        propertiesCol.setCellValueFactory(new PropertyValueFactory<TableObject, String>("Properties"));

        try {
            getServerData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cargoTable.setItems(tableData);
    }

    public void populateClientList(ArrayList<SaveObject> tol){

        tableData.clear();
        saveObjects.clear();

        for (SaveObject so: tol){
            saveObjects.add(so);
            tableData.add(new TableObject(so.getType(), so.getCustomer(), so.getPosition(), so.getSize(), so.getRadioactive(), so.getFlammable(), so.getToxic(), so.getExplosive(), so.getProperties()));
        }
    }

    public ArrayList<SaveObject> toObject (byte[] bytes) throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            ArrayList<SaveObject> sol = (ArrayList<SaveObject>) o;
            return sol;

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


    public byte[] toBytes(SaveObject so) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(so);
            out.flush();
            byte[] bytes = bos.toByteArray();
            return bytes;
        }  finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
    }

    public SaveObject tableObject2SaveObject(TableObject to){
        return new SaveObject(to.getType(), to.getCustomer(), to.getPosition(), to.getSize(), to.isRadioactive(), to.isFlammable(), to.isToxic(), to.isExplosive(), to.getProperties());
    }


    public void saveItem(ActionEvent actionEvent) {
        ArrayList<SaveObject> saveObjectArrayList = new ArrayList<>();
        for (TableObject to : tableData)
            saveObjectArrayList.add(new SaveObject(to.getType(),
                    to.getCustomer(), to.getPosition(), to.getSize(), to.isRadioactive(), to.isFlammable(), to.isToxic(), to.isExplosive(), to.getProperties()));
        IO.saveE("file.txt", saveObjectArrayList);
        System.out.println("all saved");
    }
    public void openItem(ActionEvent actionEvent) {
        tableData.clear();
        ArrayList<SaveObject> saveObjects = IO.loadE("file.txt");
        for (SaveObject so: saveObjects) {
            tableData.add(new TableObject(so.getType(), so.getCustomer(), so.getPosition(),
                    so.getSize(), so.getRadioactive(), so.getFlammable(), so.getToxic(), so.getExplosive(), so.getProperties()));
        }
        populateTable();
    }


    @FXML
    private void deleteRowFromTable(ActionEvent event){
        int index = cargoTable.getSelectionModel().getSelectedIndex();
        TableObject so = (TableObject) cargoTable.getItems().get(index);
        System.out.println("Print Customer Name of Deleted: " + so.getCustomer());
        cargoTable.getItems().removeAll(cargoTable.getSelectionModel().getSelectedItem());
        try {
            deleteData(tableObject2SaveObject(so));
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateTable();
    }


    /*
    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A) {
                provideAboutFunctionality();
            }
        }
    }
    */


    /**
     * Perform functionality associated with "About" menu selection or CTRL-A.
     */
    private void provideAboutFunctionality() {
        System.out.println("You clicked on About!");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        disableAll();
        populateTable();
        bindAddButton();
        bindDeleteButton();
    }

    public void enableCommonFields(){
        customerName.setDisable(false);
        position.setDisable(false);
        size.setDisable(false);
        radioactive.setDisable(false);
        flammable.setDisable(false);
        toxic.setDisable(false);
        explosive.setDisable(false);
    }

    //src: https://stackoverflow.com/questions/23040531/how-to-disable-button-when-textfield-is-empty
    public void bindAddButton(){
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(customerName.textProperty(),
                        size.textProperty(),
                        position.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return (customerName.getText().isEmpty()
                        || size.getText().isEmpty()
                        || position.getText().isEmpty());
            }
        };
        addBtn.disableProperty().bind(bb);
    }

    public void bindDeleteButton(){
        deleteBtn.disableProperty().bind(Bindings.isEmpty(cargoTable.getSelectionModel().getSelectedItems()));
    }

    public void disableAll(){
        customerName.setDisable(true);
        customerName.setText("");
        position.setDisable(true);
        position.setText("");
        size.setDisable(true);
        size.setText("");
        radioactive.setDisable(true);
        radioactive.setSelected(false);
        flammable.setDisable(true);
        flammable.setSelected(false);
        toxic.setDisable(true);
        toxic.setSelected(false);
        explosive.setDisable(true);
        explosive.setSelected(false);
        pressurized.setDisable(true);
        pressurized.setSelected(false);
        solid.setDisable(true);
        solid.setSelected(false);
        fragile.setDisable(true);
        fragile.setSelected(false);
        textFieldtoInt(position);
        textFieldtoInt(size);
    }

    /**
     * Type
     */
    public void typeBoxedSelected(ActionEvent actionEvent) {
        type.setText("Boxed");
        disableAll();
        enableCommonFields();
        fragile.setDisable(false);
    }
    public void typeDrySelected(ActionEvent actionEvent) {
        type.setText("Dry");
        disableAll();
        enableCommonFields();
        solid.setDisable(false);
    }

    public void typeLiquidSelected(ActionEvent actionEvent) {
        type.setText("Liquid");
        disableAll();
        enableCommonFields();
        pressurized.setDisable(false);
    }

    public void typeDryBoxedSelected(ActionEvent actionEvent) {
        type.setText("Dry | Boxed");
        disableAll();
        enableCommonFields();
        solid.setDisable(false);
        fragile.setDisable(false);
    }

    public void typeDryLiquidSelected(ActionEvent actionEvent) {
        type.setText("Dry | Liquid");
        disableAll();
        enableCommonFields();
        pressurized.setDisable(false);
        solid.setDisable(false);
    }

    public void typeLiquidBoxedSelected(ActionEvent actionEvent) {
        type.setText("Liquid | Boxed");
        disableAll();
        enableCommonFields();
        pressurized.setDisable(false);
        fragile.setDisable(false);
    }

    public void typeLiquidDryBoxedSelected(ActionEvent actionEvent) {
        type.setText("Liquid | Boxed | Dry");
        disableAll();
        enableCommonFields();
        pressurized.setDisable(false);
        solid.setDisable(false);
        fragile.setDisable(false);
    }

    public String solidArmed(){
        if(solid.isSelected()) return "S";
        else return "-";
    }
    public String fragileArmed(){
        if(fragile.isSelected()) return "F";
        else return "-";
    }
    public String pressurizedArmed(){
        if(pressurized.isSelected()) return "P";
        else return "-";
    }

    //src https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
    public void textFieldtoInt(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}













