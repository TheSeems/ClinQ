package ru.theseems.clinq.impl.checker;

import lombok.Getter;
import ru.theseems.clinq.impl.compiler.SimpleTokenCompiler;
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
public class SimpleChecker<InputType, CheckType> extends Checker<InputType, CheckType> {
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
	public <PipeCheckType> Checker<InputType, CheckType> with(MapPipe<CheckType, PipeCheckType> pipe, Consumer<Checker<InputType, PipeCheckType>> tweak) {
		SimpleChecker<InputType, CheckType> newChecker = new SimpleChecker<>();
		var mapped = newChecker.map(pipe);
		tweak.accept(mapped);

		tokens.add(new CheckToken<>(mapped::check));
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

	@Override
	public boolean check(InputType value) {
		return getCompiled().check(value);
	}

	// TODO: add propagation strategy and some more fantastic stuff
	private Check<InputType> compile() {
		return SimpleTokenCompiler.of(tokens)
			.settings(settings -> settings.tokenCountLimit(1000))
			.compile();
	}
}
