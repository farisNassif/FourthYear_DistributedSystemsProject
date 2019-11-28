package ie.gmit.part_two;

import com.codahale.metrics.health.HealthCheck;

public class UserHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
