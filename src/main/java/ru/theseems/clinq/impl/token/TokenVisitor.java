package ru.theseems.clinq.impl.token;

public interface TokenVisitor {
	<T> boolean visit(CheckToken<T> token);

	<T, V> boolean visit(PipeToken<T, V> token);

	<T, V> boolean visit(MapToken<T, V> token);
}
