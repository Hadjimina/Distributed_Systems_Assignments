package ch.ethz.inf.vs.a3.Task2;

import java.util.Map;

import ch.ethz.inf.vs.a3.clock.Clock;

/**
 * Created by david on 26.10.17.
 */

 public class VectorClock implements Clock{

    private Map<Integer, Integer> vector;

    @Override
    public void setClock(Clock other){
        VectorClock otherVector = (VectorClock) other;
        vector.clear();
        vector.putAll(otherVector.getVector());
    }

    @Override
    public void update(Clock other) {
        VectorClock otherVector = (VectorClock) other;

        for(int pid : otherVector.getVector().keySet()){
            if(this.vector.get(pid) < otherVector.getVector().get(pid)){
                this.vector.remove(pid);
                this.vector.put(pid, otherVector.getVector().get(pid));
            }
        }
    }

    @Override
    public void tick(Integer pid){
        int time = vector.get(pid);
        vector.remove(pid);
        vector.put(pid, time+1);
    }

    @Override
    public boolean happenedBefore(Clock other){
        VectorClock otherVector = (VectorClock) other;
        if(vector.equals(otherVector.getVector())){
            return false;
        }
        for(int pid : otherVector.getVector().keySet()){
            if(vector.get(pid) > otherVector.getVector().get(pid)) return false;
        }
        return true;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("{");
        for(Map.Entry entry : vector.entrySet()){
            s.append("\""+Integer.toString((int) entry.getKey())+"\":"+Integer.toString((int) entry.getValue()) +",");
        }
        s.deleteCharAt(s.length()-1);
        s.append("}");
        return s.toString();
    }

    @Override
    public void setClockFromString(String clock){
        for(int i = 3; i <= clock.length(); i=i+6){
            vector.clear();
            vector.put( Integer.parseInt(clock.substring(i,i+1)), Integer.parseInt(clock.substring(i+3, i+4)));
        }
    }

    public int getTime(Integer pid){
        return vector.get(pid);
    }

    public Map<Integer, Integer> getVector(){
        return this.vector;
    }



}
