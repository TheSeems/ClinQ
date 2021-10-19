package me.theseems.clinq.impl.token;

import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class MapCheckToken<CheckType, PipeCheckType> implements Token {
	private final MapToken<CheckType, PipeCheckType> pipe;
	private final CheckToken<PipeCheckType> check;

	public MapCheckToken(MapToken<CheckType, PipeCheckType> pipe, CheckToken<PipeCheckType> check) {
		this.pipe = pipe;
		this.check = check;
	}

	public CheckToken<PipeCheckType> getCheck() {
		return check;
	}

	public MapToken<CheckType, PipeCheckType> getPipe() {
		return pipe;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
