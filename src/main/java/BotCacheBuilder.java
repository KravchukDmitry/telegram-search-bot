import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class BotCacheBuilder {

    public static LoadingCache<Long, ChatSettings> buildCache(int lifeTime, TimeUnit timeUnit){
        CacheLoader<Long, ChatSettings> loader = new CacheLoader<Long, ChatSettings>() {
            @Override
            public ChatSettings load(Long aLong) throws Exception {
                return new ChatSettings();
            }
        };

        return CacheBuilder.newBuilder().expireAfterWrite(lifeTime, TimeUnit.MINUTES).build(loader);
    }
}
