package part1.client.circuitBreaker;

/*
    @author 张星宇
 */
public enum CircuitBreakerState {
    CLOSED,
    OPEN,
    HALF_OPEN
}
