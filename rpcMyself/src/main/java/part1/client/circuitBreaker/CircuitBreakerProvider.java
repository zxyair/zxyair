package part1.client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

/*
    @author 张星宇
 */
public class CircuitBreakerProvider {
    private Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();
    public CircuitBreaker getCircuitBreaker(String serviceName)
    {
        CircuitBreaker circuitBreaker;
        if(circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker = circuitBreakerMap.get(serviceName);
        }else {
            circuitBreaker = new CircuitBreaker(5,0.5,1000);
            circuitBreakerMap.put(serviceName,circuitBreaker);
        }
        return circuitBreaker;
    }
}
