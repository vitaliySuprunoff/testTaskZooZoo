import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.*;
import java.sql.*;
import java.util.LinkedHashSet;


class dbWorker {

    private final static String URL =
            "jdbc:mysql://localhost:3306/zoo_zoodb?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
                    "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String NAME = "root";
    private final static String PASS = "root";


    static void tryToConnect() throws SQLException{
        try{
            Connection connection = DriverManager.getConnection(URL,NAME,PASS);
            connection.close();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Не удалось подключиться базе данных!");
            throw new SQLException();
        }
    }


    static void animalAdCollectionToDB(LinkedHashSet<AnimalAd> animals){
        try{
            Connection connection = DriverManager.getConnection(URL,NAME,PASS);
            Statement statement = connection.createStatement();
            statement.addBatch("SET SQL_SAFE_UPDATES = 0;");
            statement.addBatch("delete from animalad");
            statement.executeBatch();
            for (AnimalAd animal:
                 animals) {
                String query = "insert into animalad values('" + animal.getTitle()
                        + "', '"+animal.getUrl()
                        + "', '"+animal.getPrice()
                        + "', '"+animal.getPlace()
                        + "', '"+animal.getKindAnimal()
                        + "', '"+animal.getType() +"');";
                statement.execute(query);
            }
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    static LinkedHashSet<AnimalAd> getLinkedHashSetFromDBbyQuery(String query){
        LinkedHashSet<AnimalAd> animals = new LinkedHashSet<>();
        try{
            Connection connection = DriverManager.getConnection(URL,NAME,PASS);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                animals.add(new AnimalAd(
                   resultSet.getString("title"),
                   resultSet.getString("url"),
                   resultSet.getString("price"),
                   resultSet.getString("place"),
                   resultSet.getString("kindAnimal"),
                   resultSet.getString("type")));
            }
            connection.close();
            return animals;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    private static String getQueryBySearch (TextField searchTF, ComboBox kindAnimal, ComboBox type){
        boolean condition1 = !searchTF.getText().equals("");
        boolean condition2 = !kindAnimal.getValue().equals("все животные");
        boolean condition3 = !type.getValue().equals("любой тип");

        String query = "select * from animalad";

        if (condition1){
            query = query + " where title like '%" + searchTF.getText()+ "%'";

            if (condition2){
                query = query + " and kindAnimal ='" + kindAnimal.getValue() + "'";
            }

            if (condition3){
                query = query + " and type ='" + type.getValue() + "'";
            }
        }
        else {
            if (condition2){
                query = query + " where kindAnimal ='" + kindAnimal.getValue() + "'";
                if (condition3){
                    query = query + " and type ='" + type.getValue() + "'";
                }
            }
            else {
                if (condition3){
                    query = query + " where type ='" + type.getValue() + "'";
                }
            }
        }

        return query + ";";
    }


    static LinkedHashSet<AnimalAd> search(TextField searchTF, ComboBox kindAnimal, ComboBox type){
        return getLinkedHashSetFromDBbyQuery(getQueryBySearch(searchTF,kindAnimal,type));
    }
}