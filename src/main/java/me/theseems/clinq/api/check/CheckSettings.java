package me.theseems.clinq.api.check;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CheckSettings {
	String errorMessage;
	boolean propagate = true;

	public static CheckSettings merge(CheckSettings weak, CheckSettings strong) {
		CheckSettings result = new CheckSettings();
		if (weak.errorMessage == null) {
			result.errorMessage = strong.errorMessage;
		}

		result.propagate = strong.propagate;
		return result;
	}
}
