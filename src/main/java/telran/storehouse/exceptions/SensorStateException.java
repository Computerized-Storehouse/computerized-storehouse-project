package telran.storehouse.exceptions;

@SuppressWarnings("serial")
public class SensorStateException extends IllegalStateException {
	public SensorStateException(long key) {
		super(String.format("Sensor %s already exists", key));
	}
}
