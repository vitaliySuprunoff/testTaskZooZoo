import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.ConnectException;
import java.util.LinkedHashSet;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class Parser {


    public static LinkedHashSet<AnimalAd> work(){
            try{
                LinkedHashSet<AnimalAd> animals = new LinkedHashSet<>();
                getAnimalsByType(animals,"dogs","sale");
                getAnimalsByType(animals,"dogs","buy");
                getAnimalsByType(animals,"dogs","love");
                getAnimalsByType(animals,"dogs","free");
                getAnimalsByType(animals,"cats","sale");
                getAnimalsByType(animals,"cats","buy");
                getAnimalsByType(animals,"cats","love");
                getAnimalsByType(animals,"cats","free");
                getAnimalsByType(animals,"rodents","sale");
                getAnimalsByType(animals,"rodents","buy");
                getAnimalsByType(animals,"rodents","love");
                getAnimalsByType(animals,"rodents","free");
                getAnimalsByType(animals,"birds","sale");
                getAnimalsByType(animals,"birds","buy");
                getAnimalsByType(animals,"reptile","sale");
                getAnimalsByType(animals,"reptile","buy");
                getAnimalsByType(animals,"fish","sale");
                getAnimalsByType(animals,"fish","buy");
                getAnimalsByType(animals,"insects","sale");
                getAnimalsByType(animals,"insects","buy");
                getAnimalsByType(animals,"horses","sale");
                getAnimalsByType(animals,"horses","buy");
                getAnimalsByType(animals,"Домашний-скот","Продам");
                getAnimalsByType(animals,"Домашний-скот","Куплю");
                getAnimalsByType(animals,"exotic","sale");
                getAnimalsByType(animals,"exotic","buy");
                return animals;
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Не удалось подключиться к сайту zoo-zoo.ru!" +
                        " Проверьте Ваше интернет соединение! ");
                return null;
            }
    }


    private static void getAnimalsByType (LinkedHashSet<AnimalAd> animals, String animal, String type)
                                                                                    throws UnknownHostException, ConnectException{
        int page = 1;
        String link ="http://www.zoo-zoo.ru/"+animal+"/"+type+"/page"+page+".html?";
        while (parseOnePage(link,animals, animal, type)){
            page++;
            link ="http://www.zoo-zoo.ru/"+animal+"/"+type+"/page"+page+".html?";
        }
    }


    private static boolean parseOnePage(String link, LinkedHashSet<AnimalAd> animals, String animal, String type)             throws UnknownHostException, ConnectException{

        int count = animals.size();

        try{
            Document jD = Jsoup.connect(link).get();
            Elements elements = jD.getElementsByAttributeValue("class","media-wrapper ");

            for (Element e : elements){
                    Element nameUrlElement = e.child(0).child(1).child(0).child(0).child(0);
                    Element priceElement = e.child(0).child(2).child(0);
                    Element placeElement = e.child(0).child(1).child(0).child(1).child(0);

                    String price = priceElement.text();

                    String url = nameUrlElement.attr("href");
                    url = "http://www.zoo-zoo.ru"+ url;

                    String title = nameUrlElement.attr("title");
                    title = title.substring(22);

                    String place="";
                    try {
                        String country = placeElement.child(0).text();
                        place = country;
                        String region = placeElement.child(1).text();
                        place = place + " / " + region;
                        String city = placeElement.child(2).text();
                        place = place + " / " + city;
                    }
                    catch (IndexOutOfBoundsException ex){
                    }

                    String kindAnimal="";

                    switch (animal){
                        case "dogs": kindAnimal = "собаки, щенки"; break;
                        case "cats": kindAnimal = "кошки, котята"; break;
                        case "rodents": kindAnimal = "грызуны"; break;
                        case "birds": kindAnimal = "птицы"; break;
                        case "reptile": kindAnimal = "рептилии"; break;
                        case "fish": kindAnimal = "рыбки"; break;
                        case "insects": kindAnimal = "насекомые"; break;
                        case "horses": kindAnimal = "лошади"; break;
                        case "Домашний-скот": kindAnimal = "домашний скот"; break;
                        case "exotic": kindAnimal = "экзотические животные"; break;
                    }

                    String typeAd="";

                    switch(type){
                        case "sale": typeAd = "продам"; break;
                        case "buy": typeAd = "куплю"; break;
                        case "love": typeAd = "вязка"; break;
                        case "free": typeAd = "отдам в добрые руки"; break;
                        case "Продам": typeAd = "продам"; break;
                        case "Куплю": typeAd = "куплю"; break;

                    }

                    animals.add(new AnimalAd(title,url,price,place,kindAnimal,typeAd));

                    }
        }
        catch (UnknownHostException e){throw new UnknownHostException();}
        catch (ConnectException e){throw new ConnectException();}
        catch (IOException e){
            e.printStackTrace();
        }


        if (count< animals.size()){
            return true;
        }
        else {
            return false;
        }
    }
}