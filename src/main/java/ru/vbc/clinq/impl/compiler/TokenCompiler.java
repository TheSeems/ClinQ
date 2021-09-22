package ru.vbc.clinq.impl.compiler;

import ru.vbc.clinq.api.Check;

import java.util.function.Consumer;

public interface TokenCompiler {
	TokenCompiler settings(Consumer<TokenCompilerSettings> settingsConsumer);

	<T> Check<T> compile();
}
