package ch.ethz.inf.vs.a3.Task2;

import java.util.HashMap;
import java.util.Map;

import ch.ethz.inf.vs.a3.clock.Clock;

/**
 * Created by david on 26.10.17.
 */

 public class VectorClock implements Clock{

    private Map<Integer, Integer> vector;

    public VectorClock(){
        vector = new HashMap<>();
    }

    @Override
    public void setClock(Clock other){
        VectorClock otherVector = (VectorClock) other;
        vector.clear();
        vector.putAll(otherVector.getVector());
    }

    @Override
    public void update(Clock other) {
        VectorClock otherVector = (VectorClock) other;
        try {
            for (int pid : otherVector.getVector().keySet()) {
                if(!(this.vector.containsKey(pid))){
                    this.vector.put(pid, otherVector.getVector().get(pid));
                }
                else if (this.vector.get(pid) < otherVector.getVector().get(pid)) {
                    this.vector.remove(pid);
                    this.vector.put(pid, otherVector.getVector().get(pid));
                }
            }
        }catch (NullPointerException e){}
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
        try {
            for (int pid : otherVector.getVector().keySet()) {
                if (vector.get(pid) > otherVector.getVector().get(pid)) return false;
            }
        }catch (NullPointerException e){
            return true;
        }
        return true;
    }

    @Override
    public String toString(){
        if(vector.isEmpty()){
            return "{}";
        }else {
            StringBuilder s = new StringBuilder();
            s.append("{");
            for (Map.Entry entry : vector.entrySet()) {
                s.append("\"" + Integer.toString((int) entry.getKey()) + "\":" + Integer.toString((int) entry.getValue()) + ",");
            }
            s.deleteCharAt(s.length() - 1);
            s.append("}");
            return s.toString();
        }
    }

    @Override
    public void setClockFromString(String clock){
        Map<Integer, Integer> dummy_vector = new HashMap<>();
        dummy_vector.putAll(vector);
        vector.clear();
        if(clock.equals("{}")){
        }else {
            clock = clock.substring(1,clock.length()-1);
            String array[] = clock.split(",");
            String data[][] = new String[array.length][2];
            for (int i = 0; i < array.length; i++) {
                data[i] = array[i].split(":");
            }
            try {
                for (int i = 0; i < array.length; i++) {
                    vector.put(Integer.parseInt(data[i][0].substring(1, 2)), Integer.parseInt(data[i][1]));
                }
            } catch (NumberFormatException e) {
                vector = dummy_vector;
            }
        }
    }

    public int getTime(Integer pid){
        System.out.println(pid);
        return vector.get(pid);
    }

    public Map<Integer, Integer> getVector(){
        return this.vector;
    }

    public void addProcess(Integer pid, int time){
        vector.put(pid, time);
    }



}
