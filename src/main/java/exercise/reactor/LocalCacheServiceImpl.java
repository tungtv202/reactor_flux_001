package exercise.reactor;

import exercise.reactor.dto.PopularPurchasesDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocalCacheServiceImpl implements CacheService {

    public static final int MAX_AGE = 30000;
    public static Map<String, CacheValue> INMEMORY_DATABASE = new ConcurrentHashMap<>();

    @Override
    public List<PopularPurchasesDto> getPopularPurchases(String username) {
        if (ObjectUtils.isEmpty(username)) return null;
        var cacheValue = INMEMORY_DATABASE.get(username);
        return cacheValue == null ? null : (System.currentTimeMillis() - cacheValue.getCommittedTime() > MAX_AGE
                ? null : cacheValue.getPopularPurchasesDto());
    }

    @Override
    public void setPopularPurchases(String username, Flux<PopularPurchasesDto> value) {
        List<PopularPurchasesDto> cacheValue = new ArrayList<>();
        value.collectList().map(cacheValue::addAll).subscribe();
        INMEMORY_DATABASE.put(username, new CacheValue(cacheValue));
    }

    @Getter
    @NoArgsConstructor
    public static class CacheValue {
        private long committedTime;
        private List<PopularPurchasesDto> popularPurchasesDto;

        public CacheValue(List<PopularPurchasesDto> popularPurchasesDto) {
            this.popularPurchasesDto = popularPurchasesDto == null ? new ArrayList<>() : popularPurchasesDto;
            this.committedTime = System.currentTimeMillis();
        }
    }
}
