import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class Testing {

    @Test
    public void test1() throws IOException {
        Parser parser = new Parser();
        ArrayList<Advert> advertList = parser.getAdverts("https://www.avito.ru/moskva/tovary_dlya_kompyutera?s_trg=3&bt=1&q=ryzen+3+2200");
        for (Advert a : advertList) {
            System.out.println(a);
        }
    }

}
