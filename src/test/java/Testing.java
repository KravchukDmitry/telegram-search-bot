import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Testing {

    @Test
    @Ignore
    public void parserTest() throws IOException {
        Parser parser = new Parser();
        List<Advert> advertList = parser.getAdverts("https://www.avito.ru/");
        for (Advert a : advertList) {
            log.info(a.toString());
        }
    }

    @Test
    @Ignore
    public void guavaCacheTest() throws InterruptedException {
        LoadingCache<Long, ChatSettings> botCache = BotCacheBuilder.buildCache(2,TimeUnit.MILLISECONDS);
        ChatSettings settings = new ChatSettings();
        Assert.assertEquals(0, botCache.size());
        botCache.put(123l, settings);
        Assert.assertEquals(1, botCache.size());
        TimeUnit.SECONDS.sleep(10);
        botCache.getIfPresent(123l);
        Assert.assertEquals(0, botCache.size());
    }

    @Test
    public void whenCacheMiss__thenValueIsComputed() {
        CacheLoader<String, String> loader;
        loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache;
        cache = CacheBuilder.newBuilder().build(loader);

        Assert.assertEquals(0, cache.size());
        Assert.assertEquals("HELLO", cache.getUnchecked("hello"));
        Assert.assertEquals(1, cache.size());
    }

    @Test
    public void whenEntryIdle__thenEviction()  throws InterruptedException {
        CacheLoader<String, String> loader;
        loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache;
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2,TimeUnit.MILLISECONDS)
                .build(loader);

        cache.getUnchecked("hello");
        Assert.assertEquals(1, cache.size());

        cache.getUnchecked("hello");
        Thread.sleep(300);

        cache.getUnchecked("test");
        Assert.assertEquals(1, cache.size());
        Assert.assertNull(cache.getIfPresent("hello"));
    }
}
