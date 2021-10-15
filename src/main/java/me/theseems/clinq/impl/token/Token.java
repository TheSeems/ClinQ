package me.theseems.clinq.impl.token;

import me.theseems.clinq.impl.compiler.visit.VisitResult;

public interface Token {
	VisitResult accept(TokenVisitor visitor);
}
