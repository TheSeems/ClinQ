package me.theseems.clinq.impl.token.special;

public class BlockToken implements SpecialToken {
	private final boolean block;

	public BlockToken(boolean block) {
		this.block = block;
	}

	@Override
	public String toString() {
		return "BlockToken{" +
			"block=" + block +
			'}';
	}
}
