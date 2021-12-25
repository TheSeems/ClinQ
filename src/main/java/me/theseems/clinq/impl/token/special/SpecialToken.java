package me.theseems.clinq.impl.token.special;

import me.theseems.clinq.impl.compiler.visit.VisitResult;
import me.theseems.clinq.impl.token.Token;
import me.theseems.clinq.impl.token.TokenVisitor;

public interface SpecialToken extends Token {
	@Override
	default VisitResult accept(TokenVisitor visitor) {
		throw new IllegalStateException("Visitor should not visit special tokens." +
			" Tried to visit " + this.getClass().getName());
	}
}
