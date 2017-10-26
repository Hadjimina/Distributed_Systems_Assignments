package ch.ethz.inf.vs.a3.Task2;

import ch.ethz.inf.vs.a3.clock.Clock;


/**
 * Created by david on 26.10.17.
 */

public class LamportClock implements Clock{

    private int time;

    @Override
    public void update(Clock other) {
        LamportClock otherLamport = (LamportClock) other;
        this.time = Math.max(otherLamport.getTime(), this.time);
    }

    @Override
    public void setClock(Clock other) {
        LamportClock otherLamport = (LamportClock) other;
        this.time = otherLamport.getTime();
    }

    @Override
    public void tick(Integer pid) {
        this.time++;
    }

    @Override
    public boolean happenedBefore(Clock other) {
        LamportClock otherLamport = (LamportClock) other;
        if(this.time < otherLamport.getTime()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(this.time);
    }

    @Override
    public void setClockFromString(String clock) {
        this.time = Integer.parseInt(clock);
    }

    public void setTime(int time){
        this.time = time;
    }



    public int getTime() {
        return this.time;
    }



}
