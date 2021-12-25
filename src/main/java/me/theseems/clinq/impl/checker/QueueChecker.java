package me.theseems.clinq.impl.checker;

import lombok.Getter;
import me.theseems.clinq.api.checker.Checker;
import me.theseems.clinq.api.MapPipe;
import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.api.compiler.CompiledCheck;
import me.theseems.clinq.api.compiler.TokenCompiler;
import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.impl.compiler.QueueTokenCompiler;
import me.theseems.clinq.impl.token.special.BlockToken;
import me.theseems.clinq.impl.token.CheckToken;
import me.theseems.clinq.impl.token.CheckerToken;
import me.theseems.clinq.impl.token.special.condition.ElseToken;
import me.theseems.clinq.impl.token.special.ErrorToken;
import me.theseems.clinq.impl.token.MapCheckToken;
import me.theseems.clinq.impl.token.MapToken;
import me.theseems.clinq.impl.token.Token;
import me.theseems.clinq.impl.token.WhenToken;
import me.theseems.clinq.impl.token.special.condition.ThenToken;
import me.theseems.clinq.utils.Checkers;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

// TODO: break down to: lexer, semanter (optional), translator
public class QueueChecker<InputType, CheckType> extends Checker<InputType, CheckType> {
	private Queue<Token> tokens = new ArrayDeque<>();
	private TokenCompiler compiler = new QueueTokenCompiler();

	// Lazy for caching if we want to reuse our checker
	@Getter(lazy = true)
	private final CompiledCheck<InputType> compiled = compile();

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

		tokens.add(new CheckerToken<>(mapped));
		return this;
	}

	@Override
	public <PipeCheckType> Checker<InputType, CheckType> mapCheck(MapPipe<CheckType, PipeCheckType> pipe,
	                                                              Check<PipeCheckType> check) {
		tokens.add(new MapCheckToken<>(new MapToken<>(pipe), new CheckToken<>(check)));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> when(Check<CheckType> condition) {
		tokens.add(new WhenToken<>(new CheckToken<>(condition)));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> when(Check<CheckType> condition, Checker<CheckType, CheckType> tweak) {
		tokens.add(new WhenToken<>(new CheckToken<>(condition), new CheckerToken<>(tweak)));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> whenCheck(Check<CheckType> condition, Check<CheckType> check) {
		return when(condition, Checkers.of(check));
	}

	@Override
	public <PipeCheckType> Checker<InputType, PipeCheckType> map(MapPipe<CheckType, PipeCheckType> pipe) {
		tokens.add(new MapToken<>(pipe));

		QueueChecker<InputType, PipeCheckType> newChecker = new QueueChecker<>();
		newChecker.tokens = tokens;
		return newChecker;
	}

	@Override
	public Checker<InputType, CheckType> error(String message) {
		tokens.add(new ErrorToken(message));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> blocking() {
		tokens.add(new BlockToken(true));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> then(Checker<InputType, CheckType> checker) {
		tokens.add(new ThenToken<>(checker));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> then(Consumer<Checker<InputType, CheckType>> tweak) {
		QueueChecker<InputType, CheckType> newChecker = new QueueChecker<>();
		tweak.accept(newChecker);

		tokens.add(new ThenToken<>(newChecker));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> thenCheck(Check<CheckType> check) {
		tokens.add(new ThenToken<>(Checkers.of(check)));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> orElseCheck(Check<CheckType> check) {
		tokens.add(new ElseToken<>(Checkers.of(check)));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> orElse(Checker<InputType, CheckType> checker) {
		tokens.add(new ElseToken<>(checker));
		return this;
	}

	@Override
	public Checker<InputType, CheckType> orElse(Consumer<Checker<InputType, CheckType>> tweak) {
		QueueChecker<InputType, CheckType> newChecker = new QueueChecker<>();
		tweak.accept(newChecker);

		tokens.add(new ElseToken<>(newChecker));
		return this;
	}

	@Override
	public boolean check(InputType value, CheckErrors errors) {
		return getCompiled().check(value, errors);
	}

	@Override
	public TokenCompiler getCompiler() {
		return compiler;
	}

	@Override
	public void setCompiler(TokenCompiler compiler) {
		this.compiler = compiler;
	}

	@Override
	public Queue<Token> getTokens() {
		return tokens;
	}

	// TODO: add propagation strategy and some more fantastic stuff
	private CompiledCheck<InputType> compile() {
		return compiler.compile(tokens);
	}
}
