import com.sun.org.apache.xpath.internal.operations.Number;
import javafx.application.Application;
import java.util.LinkedHashSet;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.*;


public class BuildJavaFX extends Application  {

    private TableView<AnimalAd> table = new TableView<AnimalAd>();
    public static ObservableList<AnimalAd> data;


    public static void main (String[] args) {
        try{
            dbWorker.tryToConnect();
            LinkedHashSet<AnimalAd> animals= Parser.work();
            dbWorker.animalAdCollectionToDB(animals);
            data =  FXCollections.observableArrayList(animals);
            animals.clear();
            launch(args);
        }
        catch (Exception e){}
    }


    @Override
    public void start(Stage stage){

        Scene scene = new Scene(new Group());
        stage.setTitle("Объявления zoo-zoo.ru");
        stage.setWidth(950);
        stage.setHeight(550);

        final Label label = new Label("Количество объявлений :"+Integer.toString(data.size()));
        label.setFont(new Font("Arial", 14));
        label.setMaxWidth(600);
        table.setEditable(false);
        TilePane tilePane = new TilePane();

        TextField searchTF = new TextField();
        searchTF.setMinWidth(300);

        ObservableList<String> optionsKindAnimal =
                FXCollections.observableArrayList(
                        "все животные",
                        "собаки, щенки",
                        "кошки, котята",
                        "грызуны",
                        "птицы",
                        "рептилии",
                        "рыбки",
                        "насекомые",
                        "лошади",
                        "домашний скот",
                        "экзотические животные"
                );
        final ComboBox comboBoxKindAnimal = new ComboBox(optionsKindAnimal);
        comboBoxKindAnimal.setMinWidth(150);
        comboBoxKindAnimal.setValue("все животные");

        ObservableList<String> optionsType =
                FXCollections.observableArrayList(
                        "любой тип",
                        "продам",
                        "куплю",
                        "вязка",
                        "отдам в добрые руки"
                );
        final ComboBox comboBoxType = new ComboBox(optionsType);
        comboBoxType.setMinWidth(150);
        comboBoxType.setValue("любой тип");

        Button buttonSearch = new Button();
        buttonSearch.setText("Найти");
        buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data.clear();
                data =  FXCollections.observableArrayList(dbWorker.search(searchTF,comboBoxKindAnimal,comboBoxType));
                table.setItems(data);
                label.setText("Количество объявлений "+Integer.toString(data.size()));
            }
        });


        Button buttonOpenUrl = new Button();
        buttonOpenUrl.setText("Открыть на сайте");
        buttonOpenUrl.setMinWidth(170);
        buttonOpenUrl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try{
                    getHostServices().showDocument(table.getSelectionModel().getSelectedItem().getUrl());
                }
                catch (NullPointerException exc){
                    JOptionPane.showMessageDialog(null, "Выберите необходимое объявление!");
                }
            }
        });


        HBox searchbox = new HBox();
        searchbox.setSpacing(10);
        searchbox.getChildren().addAll(searchTF,comboBoxKindAnimal,comboBoxType,buttonSearch, buttonOpenUrl);

        data.addListener(new ListChangeListener<AnimalAd>() {
            @Override
            public void onChanged(
                    javafx.collections.ListChangeListener.Change<? extends AnimalAd> arg0) {
                // TODO Auto-generated method stub
            }
        });

        TableColumn titleCol = new TableColumn("Объявление");
        titleCol.setMinWidth(300);
        titleCol.setMaxWidth(350);
        titleCol.setCellValueFactory(
                new PropertyValueFactory<AnimalAd, String>("title"));

        TableColumn priceCol = new TableColumn("Цена");
        priceCol.setMinWidth(85);
        priceCol.setMaxWidth(100);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<AnimalAd, String>("price"));

        TableColumn placeCol = new TableColumn("Местоположение");
        placeCol.setMinWidth(300);
        placeCol.setMaxWidth(350);
        placeCol.setCellValueFactory(
                new PropertyValueFactory<AnimalAd, Number>("place"));

        TableColumn kindAnimalCol = new TableColumn("Разновидность");
        kindAnimalCol.setMinWidth(100);
        kindAnimalCol.setMaxWidth(150);
        kindAnimalCol.setCellValueFactory(
                new PropertyValueFactory<AnimalAd, String>("kindAnimal"));

        TableColumn typeCol = new TableColumn("Тип объявления");
        typeCol.setMinWidth(100);
        typeCol.setMaxWidth(150);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<AnimalAd, String>("type"));


        table.setItems(data);
        table.getColumns().addAll(titleCol,priceCol,placeCol,kindAnimalCol,typeCol);
        table.setMaxWidth(910);
        table.setMinWidth(910);



        final VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(30, 0, 0, 10));
        vbox.getChildren().addAll(searchbox,table, label);
        tilePane.getChildren().addAll(vbox);
        ((Group) scene.getRoot()).getChildren().add(tilePane);
        stage.setScene(scene);
        stage.show();
    }
}