package ru.theseems.clinq.impl.compiler;

import ru.theseems.clinq.impl.checker.SimpleCheckerTokenVisitor;
import ru.theseems.clinq.api.Check;
import ru.theseems.clinq.impl.token.Token;

import java.util.Queue;
import java.util.function.Consumer;

public class SimpleTokenCompiler implements TokenCompiler {
	private final Queue<Token> tokenQueue;
	private final TokenCompilerSettings settings;

	private SimpleTokenCompiler(Queue<Token> tokenQueue) {
		this.tokenQueue = tokenQueue;
		this.settings = new TokenCompilerSettings();
	}

	public static SimpleTokenCompiler of(Queue<Token> tokens) {
		return new SimpleTokenCompiler(tokens);
	}

	@Override
	public TokenCompiler settings(Consumer<TokenCompilerSettings> settingsConsumer) {
		settingsConsumer.accept(settings);
		return this;
	}

	@Override
	public <T> Check<T> compile() {
		return value -> {
			var visitor = new SimpleCheckerTokenVisitor(value);
			return tokenQueue.stream()
				.limit(settings.tokenCountLimit)
				.allMatch(token -> token.accept(visitor));
		};
	}
}
