package ru.vbc.clinq.impl.token;

public interface Token {
	boolean accept(TokenVisitor visitor);
}
