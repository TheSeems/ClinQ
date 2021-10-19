package me.theseems.clinq.impl.compiler;

import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.impl.compiler.error.ListCheckErrors;
import me.theseems.clinq.impl.compiler.error.SimpleError;
import me.theseems.clinq.impl.compiler.visit.VisitResult;
import me.theseems.clinq.impl.token.CheckToken;
import me.theseems.clinq.impl.token.CheckerToken;
import me.theseems.clinq.impl.token.MapCheckToken;
import me.theseems.clinq.impl.token.PipeToken;
import me.theseems.clinq.impl.token.WhenToken;
import me.theseems.clinq.impl.token.MapToken;
import me.theseems.clinq.impl.token.TokenVisitor;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class QueueCheckerTokenVisitor implements TokenVisitor {
	private Object currentValue;

	public QueueCheckerTokenVisitor(Object currentValue) {
		this.currentValue = currentValue;
	}

	@Override
	public <T> VisitResult visit(CheckToken<T> token) {
		T value = (T) currentValue;
		Check<T> check = token.getCheck();

		boolean success = check.check(value);
		String errorMessage = success ? null : check.settings().getErrorMessage();

		return VisitResult.builder()
				.success(success)
				.propagate(success || check.settings().isPropagate())
				.errors(List.of(SimpleError.of(errorMessage)))
				.build();
	}

	@Override
	public <T, V> VisitResult visit(PipeToken<T, V> token) {
		Optional<V> valueOptional = token.getPipe().produce((T) currentValue);

		boolean success = valueOptional.isPresent();
		String errorMessage = success ? null : token.getPipe().settings().getErrorMessage();

		if (success) {
			currentValue = valueOptional.get();
		}

		return VisitResult.builder()
				.success(success)
				.propagate(success)
				.errors(List.of(SimpleError.of(errorMessage)))
				.build();
	}

	@Override
	public <T, V> VisitResult visit(MapToken<T, V> token) {
		currentValue = token.getMapPipe().produce((T) currentValue);
		return VisitResult.builder()
				.success(true)
				.build();
	}

	@Override
	public <T, V> VisitResult visit(CheckerToken<T, V> token) {
		ListCheckErrors listCheckErrors = new ListCheckErrors();
		token.getChecker().check((T) currentValue, listCheckErrors);
		return VisitResult.builder()
				.success(listCheckErrors.getErrors().isEmpty())
				.errors(listCheckErrors.getErrors())
				.build();
	}

	@Override
	public <T> VisitResult visit(WhenToken<T> token) {
		VisitResult checkResult = visit(token.getCheckToken());
		if (!checkResult.isSuccess() && !checkResult.isPropagate()) {
			return checkResult;
		}

		if (checkResult.isSuccess()) {
			return visit(token.getCheckerToken());
		}

		return VisitResult.builder().success(true).build();
	}

	@Override
	public <T, V> VisitResult visit(MapCheckToken<T, V> token) {
		Object temp = currentValue;
		visit(token.getPipe());
		VisitResult checkResult = visit(token.getCheck());
		currentValue = temp;

		return checkResult;
	}
}
