import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class Testing {

    @Test
    @Ignore
    public void test1() throws IOException {
        Parser parser = new Parser();
        ArrayList<Advert> advertList = parser.getAdverts("https://www.avito.ru/");
        for (Advert a : advertList) {
            System.out.println(a);
        }
    }
}
