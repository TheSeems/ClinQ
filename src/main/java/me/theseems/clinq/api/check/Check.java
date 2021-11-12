package me.theseems.clinq.api.check;

public interface Check<T> {
	/**
	 * Check the value given
	 *
	 * @param value to check
	 * @return check's verdict
	 */
	boolean check(T value);

	/**
	 * Get check settings
	 *
	 * @return settings
	 */
	default CheckSettings settings() {
		return new CheckSettings();
	}
}
