package ru.theseems.clinq.impl.compiler;

import ru.theseems.clinq.api.Check;

import java.util.function.Consumer;

public interface TokenCompiler {
	TokenCompiler settings(Consumer<TokenCompilerSettings> settingsConsumer);

	<T> Check<T> compile();
}
