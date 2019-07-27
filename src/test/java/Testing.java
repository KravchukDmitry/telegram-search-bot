import org.junit.Test;

public class Testing {

    @Test
    public void test1() {
        Parser parser = new Parser();
        parser.getAdverts("https://www.avito.ru/moskva/tovary_dlya_kompyutera?s_trg=3&bt=1&q=ryzen+3+2200");
    }

}
