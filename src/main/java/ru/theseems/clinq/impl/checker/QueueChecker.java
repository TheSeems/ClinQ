package ru.theseems.clinq.impl.checker;

import lombok.Getter;
import ru.theseems.clinq.impl.compiler.QueueTokenCompiler;
import ru.theseems.clinq.impl.compiler.TokenCompiler;
import ru.theseems.clinq.impl.token.MapToken;
import ru.theseems.clinq.api.Check;
import ru.theseems.clinq.api.Checker;
import ru.theseems.clinq.api.MapPipe;
import ru.theseems.clinq.api.OptionalPipe;
import ru.theseems.clinq.impl.token.CheckToken;
import ru.theseems.clinq.impl.token.PipeToken;
import ru.theseems.clinq.impl.token.Token;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

// TODO: break down to: lexer, semanter (optional), translator
public class QueueChecker<InputType, CheckType> extends Checker<InputType, CheckType> {
	private Queue<Token> tokens = new ArrayDeque<>();

	// Lazy for caching if we want to reuse our checker
	@Getter(lazy = true)
	private final Check<InputType> compiled = compile();

	@Override
	public Checker<InputType, CheckType> with(Check<CheckType> check) {
		tokens.add(new CheckToken<>(check));
		return this;
	}

	@Override
	public <PipeCheckType> Checker<InputType, CheckType> with(MapPipe<CheckType, PipeCheckType> pipe,
	                                                          Consumer<Checker<InputType, PipeCheckType>> tweak) {
		QueueChecker<InputType, CheckType> newChecker = new QueueChecker<>();
		var mapped = newChecker.map(pipe);
		tweak.accept(mapped);

		tokens.add(new CheckToken<>(mapped::check));
		return this;
	}

	@Override
	public <PipeCheckType> Checker<InputType, CheckType> mapCheck(MapPipe<CheckType, PipeCheckType> pipe,
	                                                              Check<PipeCheckType> check) {
		QueueChecker<InputType, CheckType> newChecker = new QueueChecker<>();
		var mapped = newChecker.map(pipe).with(check);

		tokens.add(new CheckToken<>(mapped::check));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> when(Check<CheckType> condition, Checker<CheckType, CheckType> tweak) {
		tokens.add(new CheckToken<CheckType>((value) -> !condition.check(value) || tweak.check(value)));
		return this;
	}

	@Override
	public <PipeCheckType> Checker<InputType, PipeCheckType> map(MapPipe<CheckType, PipeCheckType> pipe) {
		tokens.add(new MapToken<>(pipe));

		QueueChecker<InputType, PipeCheckType> newChecker = new QueueChecker<>();
		newChecker.tokens = tokens;
		return newChecker;
	}

	public <PipeCheckType> Checker<InputType, PipeCheckType> pipe(OptionalPipe<CheckType, PipeCheckType> pipe) {
		tokens.add(new PipeToken<>(pipe));

		QueueChecker<InputType, PipeCheckType> newChecker = new QueueChecker<>();
		newChecker.tokens = tokens;
		return newChecker;
	}

	@Override
	public boolean check(InputType value) {
		return getCompiled().check(value);
	}

	@Override
	public TokenCompiler compiler() {
		return null;
	}

	// TODO: add propagation strategy and some more fantastic stuff
	private Check<InputType> compile() {
		return QueueTokenCompiler.of(tokens)
			.settings(it -> it.tokenCountLimit(1000))
			.compile();
	}
}
