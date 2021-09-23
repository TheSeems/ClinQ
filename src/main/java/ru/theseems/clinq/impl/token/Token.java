package ru.theseems.clinq.impl.token;

public interface Token {
	boolean accept(TokenVisitor visitor);
}
