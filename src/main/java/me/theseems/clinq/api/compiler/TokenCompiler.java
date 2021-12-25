package me.theseems.clinq.api.compiler;

import me.theseems.clinq.impl.token.Token;

import java.util.Queue;
import java.util.function.Consumer;

public interface TokenCompiler {
	/**
	 * Compile tokens into a check
	 *
	 * @param tokens to compile for
	 * @param <T>    of check
	 * @return compiled check
	 */
	<T> CompiledCheck<T> compile(Queue<Token> tokens);

	/**
	 * Settings of the compiler
	 *
	 * @param settingsConsumer settings tweaker
	 * @return compiler (chain)
	 */
	TokenCompiler settings(Consumer<TokenCompilerSettings> settingsConsumer);
}
