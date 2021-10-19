package me.theseems.clinq.impl.token;

import me.theseems.clinq.impl.compiler.visit.VisitResult;

public interface TokenVisitor {
	<T> VisitResult visit(CheckToken<T> token);

	<T, V> VisitResult visit(PipeToken<T, V> token);

	<T, V> VisitResult visit(MapToken<T, V> token);

	<T, V> VisitResult visit(CheckerToken<T, V> token);

	<T> VisitResult visit(WhenToken<T> token);

	<T, V> VisitResult visit(MapCheckToken<T, V> token);
}
