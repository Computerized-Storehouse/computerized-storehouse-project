package telran.storehouse.exceptions;

@SuppressWarnings("serial")
public class SensorNotFoundException extends NotFoundException {

	public SensorNotFoundException(long key) {
		super(String.format("Sensor %s not found", key));
	}

}
