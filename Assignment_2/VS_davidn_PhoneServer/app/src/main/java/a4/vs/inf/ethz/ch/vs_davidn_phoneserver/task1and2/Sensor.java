package a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * A sensor representation that provides temperature measurements. 
 * 
 * @author Leyna Sadamori
 *
 */
public interface Sensor {
	
	/**
	 * Executes a request to fetch the temperature value.
	 *
	 * @throws Exception Any exception that could happen during the request
	 *
	 * @return Response (not parsed yet)
	 */
	public String executeRequest() throws Exception;

	/**
	 * Parse response that has been returned after sending the request.
	 *
	 * @param response Raw response
	 *
	 * @return Parsed sensor value or {@link Double#NaN} if parsing fails
     */
	public double parseResponse(String response) throws XmlPullParserException, IOException;

	/**
	 * Invoke request to fetch temperature value.
	 */
	public void getTemperature();


	/**
	 * Register a SensorListener to this sensor.
	 * @param listener The SensorListener to be registered
	 */
	public void registerListener(a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2.SensorListener listener);


	/**
	 * Unregister a SensorListener from this sensor.
	 * @param listener The SensorListener to be unregistered
	 */
	public void unregisterListener(a4.vs.inf.ethz.ch.vs_davidn_phoneserver.task1and2.SensorListener listener);
}
