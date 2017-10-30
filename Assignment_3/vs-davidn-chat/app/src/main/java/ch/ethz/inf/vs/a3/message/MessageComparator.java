package ch.ethz.inf.vs.a3.message;


import java.util.Comparator;

import ch.ethz.inf.vs.a3.Task2.VectorClock;
import ch.ethz.inf.vs.a3.clock.VectorClockComparator;


/**
 * Message comparator class. Use with PriorityQueue.
 */
public class MessageComparator implements Comparator<Message> {


    @Override
    public int compare(Message lhs, Message rhs) {

        VectorClock lhsVC = new VectorClock();
        lhsVC.setClockFromString(lhs.getTimestamp());

        VectorClock rhsVC = new VectorClock();
        rhsVC.setClockFromString(rhs.getTimestamp());

        VectorClockComparator vectorClockComparator = new VectorClockComparator();

        return vectorClockComparator.compare(lhsVC,rhsVC);
    }

}
