package client;

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
import serialization.SaveObject;
import server.TableObject;

import javax.xml.crypto.Data;
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


    public void startClient() {
        try {
            Socket client = new Socket("localhost", 1337);
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF("Hi i'm " + client.getLocalSocketAddress());
            DataInputStream input = new DataInputStream(client.getInputStream());
            byte[] sent = input.readAllBytes();
            getDataFromClient(toObject(sent));
            client.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<SaveObject> toObject (byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            ArrayList<SaveObject> sol = (ArrayList<SaveObject>) o;
            return sol;
        } catch (IOException e) {
            e.printStackTrace();
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


    //requests list from server
    public void getDataFromClient(ArrayList<SaveObject> tol){
        tableData.clear();
        for (SaveObject so: tol)  tableData.add(new TableObject(so.getType(), so.getCustomer(), so.getPosition(), so.getSize(), so.getRadioactive(), so.getFlammable(), so.getToxic(), so.getExplosive(), so.getProperties()));
    }

    //HIER TCP
    /**
     *
     * @param tableObject
     */
    public void addData(TableObject tableObject){
        //server.getTableData().add(tableObject);
    }


    //HIER TCP
    /**
     *
     * @param tableObject
     */
    public void deleteData(TableObject tableObject) {
        //server.getTableData().remove(tableObject);
    }


    //HIER TCP
    /**
     *
     * @param actionEvent
     */
    public void addCargoToList(ActionEvent actionEvent) {
        int pos = Integer.parseInt(position.getText());
        int siz = Integer.parseInt(size.getText());
        TableObject obj = new TableObject(type.getText(), customerName.getText(), pos, siz, radioactive.isSelected(), flammable.isSelected(), toxic.isSelected(), explosive.isSelected(), pressurizedArmed() + solidArmed() + fragileArmed());
        addData(obj);
        populateTable();
    }

    //HIER TCP
    /**
     * Load a List of Cargo
     * @param actionEvent
     */
    public void openItem(ActionEvent actionEvent) {
        tableData.clear();
        ArrayList<SaveObject> saveObjects = IO.loadE("file.txt");
        for (SaveObject so: saveObjects) {
            tableData.add(new TableObject(so.getType(), so.getCustomer(), so.getPosition(), so.getSize(), so.getRadioactive(), so.getFlammable(), so.getToxic(), so.getExplosive(), so.getProperties()));
        }
        populateTable();
    }

    //HIER TCP
    /**
     * save a List of Cargo
     * @param actionEvent
     */
    public void saveItem(ActionEvent actionEvent) {
        ArrayList<SaveObject> saveObjectArrayList = new ArrayList<>();

        for (TableObject to: tableData)  saveObjectArrayList.add(new SaveObject(to.getType(), to.getCustomer(), to.getPosition(), to.getSize(), to.isRadioactive(), to.isFlammable(), to.isToxic(), to.isExplosive(), to.getProperties()));

        IO.saveE("file.txt", saveObjectArrayList);
        System.out.println("all saved");
    }

    //HIER TCP
    @FXML
    private void deleteRowFromTable(ActionEvent event){
        int index = cargoTable.getSelectionModel().getSelectedIndex();
        TableObject tableObj = (TableObject) cargoTable.getItems().get(index);
        System.out.println("Print Customer Name of Deleted: " + tableObj.getCustomer());
        cargoTable.getItems().removeAll(cargoTable.getSelectionModel().getSelectedItem());
        deleteData(tableObj);
    }

    //HIER TCP
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

        cargoTable.setItems(tableData);
    }








    /**
     * Handle action related to input (in this case specifically only responds to
     * keyboard event CTRL-A).
     *
     * @param event Input event.
     */
    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A) {
                provideAboutFunctionality();
            }
        }
    }

    /**
     * Perform functionality associated with "About" menu selection or CTRL-A.
     */
    private void provideAboutFunctionality() {
        System.out.println("You clicked on About!");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        startClient();

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













