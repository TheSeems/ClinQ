package me.theseems.clinq.api.check;

public interface Check<T> {
	boolean check(T value);
	default CheckSettings settings() {
		return new CheckSettings();
	}
}
