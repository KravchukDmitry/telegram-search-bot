import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class Testing {

    @Test
    @Ignore
    public void test1() throws IOException {
        Parser parser = new Parser();
        List<Advert> advertList = parser.getAdverts("https://www.avito.ru/");
        for (Advert a : advertList) {
            System.out.println(a);
        }
    }
}
