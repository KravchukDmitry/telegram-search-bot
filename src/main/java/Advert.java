import lombok.Data;

@Data

public class Advert {
    private String name;
    private String link;
    private String price;
    private String date;

    @Override
    public String toString() {
        return "Advert{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
