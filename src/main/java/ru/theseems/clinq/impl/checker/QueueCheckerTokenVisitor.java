package ru.theseems.clinq.impl.checker;

import ru.theseems.clinq.impl.token.CheckToken;
import ru.theseems.clinq.impl.token.MapToken;
import ru.theseems.clinq.impl.token.PipeToken;
import ru.theseems.clinq.impl.token.TokenVisitor;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class QueueCheckerTokenVisitor implements TokenVisitor {
	private Object currentValue;

	public QueueCheckerTokenVisitor(Object currentValue) {
		this.currentValue = currentValue;
	}

	@Override
	public <T> boolean visit(CheckToken<T> token) {
		return token.getCheck().check((T) currentValue);
	}

	@Override
	public <T, V> boolean visit(PipeToken<T, V> token) {
		Optional<V> valueOptional = token.getPipe().produce((T) currentValue);
		if (valueOptional.isEmpty()) {
			return false;
		}

		currentValue = valueOptional.get();
		return true;
	}

	@Override
	public <T, V> boolean visit(MapToken<T, V> token) {
		currentValue = token.getMapPipe().produce((T) currentValue);
		return true;
	}
}
