package part1.server.ratelimit.impl;

import part1.server.ratelimit.RateLimit;

/*
    @author 张星宇
 */
public class TokenBucketRateLimtImpl implements RateLimit {
    private static int CAPACITY;
    private static int RATE;
    private  volatile int curCapacity;
    private volatile long timestamp = System.currentTimeMillis();
    public TokenBucketRateLimtImpl(int capacity,int rate){
        CAPACITY = capacity;
        RATE = rate;
        curCapacity = capacity;
    }

    @Override
    public synchronized boolean getToken() {
        if(curCapacity>0){
            curCapacity--;
            return true;
        }
        long current = System.currentTimeMillis();
        long interval = current - timestamp;
        if(current-timestamp>=RATE){
            if(current-timestamp>=RATE*2){
                curCapacity +=(int)(current-timestamp)/RATE-1;
            }
            if(curCapacity>CAPACITY) curCapacity = CAPACITY;
            timestamp = current;
            return true;
        }
        return false;
    }
}

