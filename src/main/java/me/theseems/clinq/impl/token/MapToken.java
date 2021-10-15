package me.theseems.clinq.impl.token;

import me.theseems.clinq.api.MapPipe;
import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class MapToken<T, V> implements Token {
	private final MapPipe<T, V> mapPipe;

	public MapToken(MapPipe<T, V> mapPipe) {
		this.mapPipe = mapPipe;
	}

	public MapPipe<T, V> getMapPipe() {
		return mapPipe;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return "MapToken{" +
			"mapPipe=" + mapPipe +
			'}';
	}
}
