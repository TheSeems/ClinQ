package ru.theseems.clinq.impl.compiler;

import ru.theseems.clinq.impl.checker.QueueCheckerTokenVisitor;
import ru.theseems.clinq.api.Check;
import ru.theseems.clinq.impl.token.CheckToken;
import ru.theseems.clinq.impl.token.MapToken;
import ru.theseems.clinq.impl.token.PipeToken;
import ru.theseems.clinq.impl.token.Token;
import ru.theseems.clinq.impl.token.TokenVisitor;

import java.util.Queue;
import java.util.function.Consumer;

public class QueueTokenCompiler implements TokenCompiler {
	private final Queue<Token> tokenQueue;
	private final TokenCompilerSettings settings;

	private QueueTokenCompiler(Queue<Token> tokenQueue) {
		this.tokenQueue = tokenQueue;
		this.settings = new TokenCompilerSettings();
	}

	public static QueueTokenCompiler of(Queue<Token> tokens) {
		return new QueueTokenCompiler(tokens);
	}

	@Override
	public TokenCompiler settings(Consumer<TokenCompilerSettings> settingsConsumer) {
		settingsConsumer.accept(settings);
		return this;
	}

	private <T> Check<T> compileWithExceptionOnFail() {
		return value -> {
			var visitor = new QueueCheckerTokenVisitor(value);
			tokenQueue.stream()
				.limit(settings.tokenCountLimit)
				.forEach(token -> {
					if (!token.accept(visitor)) {
						throw new IllegalStateException("Check failed");
					}
				});
			return true;
		};
	}

	private <T> Check<T> compileStandard() {
		return value -> {
			var visitor = new QueueCheckerTokenVisitor(value);
			return tokenQueue.stream()
				.limit(settings.tokenCountLimit)
				.allMatch(token -> token.accept(visitor));
		};
	}

	@Override
	public <T> Check<T> compile() {
		if (settings.enableExceptionOnFail) {
			return compileWithExceptionOnFail();
		}

		return compileStandard();
	}
}
