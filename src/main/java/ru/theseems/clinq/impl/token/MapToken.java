package ru.theseems.clinq.impl.token;

import ru.theseems.clinq.api.MapPipe;

public class MapToken<T, V> implements Token {
	private final MapPipe<T, V> mapPipe;

	public MapToken(MapPipe<T, V> mapPipe) {
		this.mapPipe = mapPipe;
	}

	public MapPipe<T, V> getMapPipe() {
		return mapPipe;
	}

	@Override
	public boolean accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return "MapToken{" +
			"mapPipe=" + mapPipe +
			'}';
	}
}
