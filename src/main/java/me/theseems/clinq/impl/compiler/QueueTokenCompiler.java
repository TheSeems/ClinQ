package me.theseems.clinq.impl.compiler;

import me.theseems.clinq.api.compiler.CompiledCheck;
import me.theseems.clinq.api.compiler.TokenCompiler;
import me.theseems.clinq.api.compiler.TokenCompilerSettings;
import me.theseems.clinq.api.compiler.error.Error;
import me.theseems.clinq.api.compiler.exception.CompileError;
import me.theseems.clinq.impl.check.ConfiguredCheck;
import me.theseems.clinq.impl.compiler.visit.VisitResult;
import me.theseems.clinq.impl.token.CheckToken;
import me.theseems.clinq.impl.token.CheckerToken;
import me.theseems.clinq.impl.token.MapCheckToken;
import me.theseems.clinq.impl.token.Token;
import me.theseems.clinq.impl.token.WhenToken;
import me.theseems.clinq.impl.token.special.BlockToken;
import me.theseems.clinq.impl.token.special.condition.ConditionToken;
import me.theseems.clinq.impl.token.special.condition.ElseToken;
import me.theseems.clinq.impl.token.special.ErrorToken;
import me.theseems.clinq.impl.token.special.SpecialToken;
import me.theseems.clinq.impl.token.special.condition.ThenToken;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class QueueTokenCompiler implements TokenCompiler {
	private final TokenCompilerSettings settings;

	public QueueTokenCompiler() {
		this.settings = new TokenCompilerSettings();
	}

	@Override
	public TokenCompiler settings(Consumer<TokenCompilerSettings> settingsConsumer) {
		settingsConsumer.accept(settings);
		return this;
	}

	private <T> CheckToken<T> attach(CheckToken<T> token, List<Token> tokens) {
		CheckToken<T> result = token;
		for (Token special : tokens) {
			if (special instanceof ErrorToken) {
				result = new CheckToken<>(ConfiguredCheck.message(result.getCheck(),
					((ErrorToken) special).getMessage()));
			} else if (special instanceof BlockToken) {
				result = new CheckToken<>(ConfiguredCheck.block(result.getCheck()));
			} else {
				throw new CompileError(
					"Special token should be either ErrorToken or BlockToken. Given: " + special);
			}
		}

		return result;
	}

	private <T, V> MapCheckToken<T, V> attach(MapCheckToken<T, V> token, List<Token> tokens) {
		return new MapCheckToken<>(token.getPipe(), attach(token.getCheck(), tokens));
	}

	private Token fetchAndAttachToCheck(CheckToken<?> checkToken, Queue<Token> tokens) {
		List<Token> additional = new ArrayList<>();
		while (!tokens.isEmpty() && tokens.element() instanceof SpecialToken) {
			additional.add(tokens.remove());
		}

		return attach(checkToken, additional);
	}

	private Token fetchAndAttachToMapCheck(MapCheckToken<?, ?> mapCheckToken, Queue<Token> tokens) {
		List<Token> additional = new ArrayList<>();
		while (!tokens.isEmpty() && tokens.element() instanceof SpecialToken) {
			additional.add(tokens.remove());
		}

		return attach(mapCheckToken, additional);
	}

	@SuppressWarnings("unchecked")
	private <T> Token fetchAndAttachToWhen(WhenToken<T> whenToken, Queue<Token> tokens) {
		List<Token> additional = new ArrayList<>();
		List<ConditionToken> conditional = new ArrayList<>();
		while (!tokens.isEmpty() && tokens.element() instanceof SpecialToken) {
			if (tokens.element() instanceof ConditionToken) {
				conditional.add((ConditionToken) tokens.remove());
			} else {
				additional.add(tokens.remove());
			}
		}

		ThenToken<T, T> thenToken = ThenToken.of(whenToken.getPassCheckerToken());
		ElseToken<T, T> elseToken = ElseToken.of(whenToken.getFailCheckerToken());

		for (ConditionToken conditionToken : conditional) {
			if (conditionToken instanceof ThenToken<?, ?>) {
				thenToken = (ThenToken<T, T>) conditionToken;
			} else if (conditionToken instanceof ElseToken<?, ?>) {
				elseToken = (ElseToken<T, T>) conditionToken;
			} else {
				throw new CompileError("Illegal conditional token found: " + conditionToken);
			}
		}

		if (thenToken == null && elseToken == null) {
			throw new CompileError("Neither 'then' nor 'else' tokens specified for 'when' token");
		}

		return new WhenToken<>(
			attach(whenToken.getCheckToken(), additional),
			thenToken != null ? new CheckerToken<>(thenToken.getChecker()) : null,
			elseToken != null ? new CheckerToken<>(elseToken.getChecker()) : null
		);
	}

	private Queue<Token> merge(Queue<Token> tokens) {
		Queue<Token> result = new ArrayDeque<>();
		while (!tokens.isEmpty()) {
			Token current = tokens.remove();
			if (current instanceof CheckToken<?>) {
				current = fetchAndAttachToCheck((CheckToken<?>) current, tokens);
			} else if (current instanceof MapCheckToken<?, ?>) {
				current = fetchAndAttachToMapCheck((MapCheckToken<?, ?>) current, tokens);
			} else if (current instanceof WhenToken<?>) {
				current = fetchAndAttachToWhen((WhenToken<?>) current, tokens);
			} else if (current instanceof SpecialToken) {
				throw new CompileError("Special token is not linked to a check");
			}

			result.add(current);
		}

		return result;
	}

	@Override
	public <T> CompiledCheck<T> compile(Queue<Token> tokens) {
		// Baking all special tokens there are
		tokens = merge(tokens);

		if (settings.isExceptionOnFail()) {
			return compileWithExceptionOnFail(tokens);
		}

		return compileStandard(tokens);
	}

	private <T> CompiledCheck<T> compileWithExceptionOnFail(Queue<Token> tokens) {
		return (value, errors) -> {
			var visitor = new QueueCheckerTokenVisitor(value);
			for (Token token : tokens) {
				VisitResult result = Objects.requireNonNull(token).accept(visitor);

				if (!result.isSuccess()) {
					result.getErrors().forEach(errors::add);
					throw new IllegalStateException(
						result.getErrors().stream()
							.map(Error::getDescription)
							.collect(Collectors.joining())
					);
				}
			}

			return true;
		};
	}

	private <T> CompiledCheck<T> compileStandard(Queue<Token> tokens) {
		return (value, errors) -> {
			var visitor = new QueueCheckerTokenVisitor(value);
			boolean overall = true;

			for (Token token : tokens) {
				VisitResult result = Objects.requireNonNull(token).accept(visitor);

				if (!result.isSuccess()) {
					result.getErrors().forEach(errors::add);
					overall = false;
				}
				if (!result.isPropagate()) {
					break;
				}
			}

			return overall;
		};
	}
}
