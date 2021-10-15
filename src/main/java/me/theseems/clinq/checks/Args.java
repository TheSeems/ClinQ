package me.theseems.clinq.checks;

public class Args {
	@SafeVarargs
	public static <T> boolean onlyOneNonNull(T... objs) {
		boolean hasNonNull = false;
		for (T obj : objs) {
			if (obj == null) {
				continue;
			}

			if (!hasNonNull) {
				hasNonNull = true;
				continue;
			}

			return false;
		}

		return hasNonNull;
	}
}
