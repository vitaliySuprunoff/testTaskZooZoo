public class AnimalAd {
    private String title, url, price, place, kindAnimal, type;

    public AnimalAd(String title, String url, String price, String place, String kindAnimal, String type) {
        this.title = title;
        this.url = url;
        this.price = price;
        this.place = place;
        this.kindAnimal = kindAnimal;
        this.type = type;
    }

    @Override
    public String toString() {
        return "AnimalAd{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", place='" + place + '\'' +
                ", kindAnimal='" + kindAnimal + '\'' +
                ", type='" + type + '\'' +
                '}';
    }


    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPrice() {
        return price;
    }

    public String getPlace() {
        return place;
    }

    public String getKindAnimal() {
        return kindAnimal;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.toString().equals(obj.toString())){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return title.hashCode() + place.hashCode() + price.hashCode();
    }
}