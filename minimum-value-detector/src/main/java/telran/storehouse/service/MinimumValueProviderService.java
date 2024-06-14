package telran.storehouse.service;

public interface MinimumValueProviderService {
	
	double DEFAULT_TRESHOLD_VALUE = 30;

	Double getValue(long sensorId);

}
