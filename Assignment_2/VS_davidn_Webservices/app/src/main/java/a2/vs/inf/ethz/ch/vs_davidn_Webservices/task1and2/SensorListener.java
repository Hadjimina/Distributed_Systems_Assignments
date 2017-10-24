package a2.vs.inf.ethz.ch.vs_davidn_Webservices.task1and2;

/**
 * A SensorListener offers callback functions to get notifications as soon as new sensor value arrive.
 * 
 * @author Leyna Sadamori
 *
 */
public interface SensorListener {
	/**
	 * Callback function for receiving sensor values.
	 * @param value A sensor value
	 */
	public void onReceiveSensorValue(double value);
	
	/**
	 * Callback function for receiving messages.
	 * Could be useful for debugging.
	 *
	 * @param message Message
	 */
	public void onReceiveMessage(String message);
}
