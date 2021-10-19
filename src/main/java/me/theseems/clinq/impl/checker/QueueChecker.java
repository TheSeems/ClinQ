package me.theseems.clinq.impl.checker;

import lombok.Getter;
import me.theseems.clinq.api.Checker;
import me.theseems.clinq.api.MapPipe;
import me.theseems.clinq.api.OptionalPipe;
import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.api.compiler.CompiledCheck;
import me.theseems.clinq.api.compiler.TokenCompiler;
import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.impl.compiler.QueueTokenCompiler;
import me.theseems.clinq.impl.token.BlockToken;
import me.theseems.clinq.impl.token.CheckToken;
import me.theseems.clinq.impl.token.CheckerToken;
import me.theseems.clinq.impl.token.ErrorToken;
import me.theseems.clinq.impl.token.MapCheckToken;
import me.theseems.clinq.impl.token.MapToken;
import me.theseems.clinq.impl.token.PipeToken;
import me.theseems.clinq.impl.token.Token;
import me.theseems.clinq.impl.token.WhenToken;

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
	public Checker<InputType, CheckType> when(Check<CheckType> condition, Checker<CheckType, CheckType> tweak) {
		tokens.add(new WhenToken<>(new CheckToken<>(condition), new CheckerToken<>(tweak)));
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
