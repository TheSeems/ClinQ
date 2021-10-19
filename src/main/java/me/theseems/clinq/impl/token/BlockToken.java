package me.theseems.clinq.impl.token;

import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class BlockToken implements Token {
	private final boolean block;

	public BlockToken(boolean block) {
		this.block = block;
	}

	public boolean isBlocking() {
		return block;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		throw new IllegalStateException("Visitor should not visit special tokens");
	}

	@Override
	public String toString() {
		return "BlockToken{" +
			"block=" + block +
			'}';
	}
}
