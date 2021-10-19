package me.theseems.clinq.api.compiler;

import me.theseems.clinq.impl.token.Token;

import java.util.Queue;
import java.util.function.Consumer;

public interface TokenCompiler {
	TokenCompiler settings(Consumer<TokenCompilerSettings> settingsConsumer);

	<T> CompiledCheck<T> compile(Queue<Token> tokens);
}
