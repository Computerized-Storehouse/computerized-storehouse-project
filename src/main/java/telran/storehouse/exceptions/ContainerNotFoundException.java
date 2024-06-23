package telran.storehouse.exceptions;

import static telran.storehouse.exceptionMessages.ExceptionMessages.*;

@SuppressWarnings("serial")
public class ContainerNotFoundException extends NotFoundException {

	public ContainerNotFoundException() {
		super(CONTAINER_NOT_FOUND);
	}

}
