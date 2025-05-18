package part1.client.circuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

/*
    @author 张星宇
 */
public class CircuitBreaker {
    private CircuitBreakerState state = CircuitBreakerState.CLOSED;
    private AtomicInteger failCount = new AtomicInteger(0);
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicInteger requestCount = new AtomicInteger(0);
    private final int failurethreshold;
    private final double haleOpenStateRate;
    private final long retrieTimeperiod;
    private long lastFailTime = 0;

    public CircuitBreaker(int failurethreshold, double haleOpenStateRate,
                          long retrieTimeperiod) {
        this.failurethreshold = failurethreshold;
        this.haleOpenStateRate = haleOpenStateRate;
        this.retrieTimeperiod = retrieTimeperiod;
    }
    public synchronized boolean allowRequest(){
        long currentTime = System.currentTimeMillis();
        switch (state){
            case OPEN:
                if(currentTime-lastFailTime>=retrieTimeperiod){
                    state = CircuitBreakerState.HALF_OPEN;
                    return true;
                }
                return false;
            case HALF_OPEN:
                requestCount.incrementAndGet();
                return true;
            case CLOSED:
            default:
                return true;
        }
    }
    public synchronized void recordSuccess(){
        if(state == CircuitBreakerState.HALF_OPEN){
            successCount.incrementAndGet();
            if(successCount.get()>=requestCount.get()*haleOpenStateRate){
                state = CircuitBreakerState.CLOSED;
                resetCount();
            }
            }
            else{
                resetCount();
        }
    }
    public synchronized void recordFail(){
        failCount.incrementAndGet();
        lastFailTime = System.currentTimeMillis();
        if(failCount.get()>=failurethreshold){
            state = CircuitBreakerState.OPEN;
        }else if(failCount.get()>=failurethreshold){
            state = CircuitBreakerState.CLOSED;
        }
    }

    private void resetCount() {
        requestCount.set(0);
        successCount.set(0);
        failCount.set(0);
    }
    public CircuitBreakerState getState(){
        return state;
    }


}
