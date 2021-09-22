package ru.vbc.clinq.impl.token;

import ru.vbc.clinq.api.MapPipe;

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
}
