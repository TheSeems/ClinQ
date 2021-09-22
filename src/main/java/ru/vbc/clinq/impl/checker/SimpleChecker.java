package ru.vbc.clinq.impl.checker;

import ru.vbc.clinq.api.Check;
import ru.vbc.clinq.api.Checker;
import ru.vbc.clinq.api.MapPipe;
import ru.vbc.clinq.api.OptionalPipe;
import ru.vbc.clinq.impl.token.CheckToken;
import ru.vbc.clinq.impl.token.MapToken;
import ru.vbc.clinq.impl.token.PipeToken;
import ru.vbc.clinq.impl.token.Token;

import java.util.ArrayDeque;
import java.util.Queue;

public class SimpleChecker<InputType, CheckType> extends Checker<InputType, CheckType> {
	private Queue<Token> tokens = new ArrayDeque<>();

	@Override
	public Checker<InputType, CheckType> with(Check<CheckType> check) {
		tokens.add(new CheckToken<>(check));
		return this;
	}

	@Override
	public <PipeCheckType> Checker<InputType, PipeCheckType> map(MapPipe<CheckType, PipeCheckType> pipe) {
		tokens.add(new MapToken<>(pipe));

		SimpleChecker<InputType, PipeCheckType> newChecker = new SimpleChecker<>();
		newChecker.tokens = tokens;
		return newChecker;
	}

	public <PipeCheckType> Checker<InputType, PipeCheckType> pipe(OptionalPipe<CheckType, PipeCheckType> pipe) {
		tokens.add(new PipeToken<>(pipe));

		SimpleChecker<InputType, PipeCheckType> newChecker = new SimpleChecker<>();
		newChecker.tokens = tokens;
		return newChecker;
	}

	// TODO: add propagation strategy and some more fantastic stuff
	@Override
	public boolean check(InputType value) {
		var visitor = new SimpleCheckerTokenVisitor(value);
		return tokens.stream().allMatch(token -> token.accept(visitor));
	}
}
