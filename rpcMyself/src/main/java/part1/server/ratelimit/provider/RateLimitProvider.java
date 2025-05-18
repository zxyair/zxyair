package part1.server.ratelimit.provider;

import part1.server.ratelimit.RateLimit;
import part1.server.ratelimit.impl.TokenBucketRateLimtImpl;

import java.util.HashMap;
import java.util.Map;

/*
    @author 张星宇
 */
public class RateLimitProvider {
    private Map<String, RateLimit> map=new HashMap<>();
    public RateLimit getRateLimit(String interfaceName) {
        if(!map.containsKey(interfaceName)){
            RateLimit rateLimit=new TokenBucketRateLimtImpl(10,100);
            map.put(interfaceName,rateLimit);
            return rateLimit;
        }
        return map.get(interfaceName);
    }
}
