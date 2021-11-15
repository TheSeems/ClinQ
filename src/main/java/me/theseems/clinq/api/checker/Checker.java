package me.theseems.clinq.api.checker;

import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.api.MapPipe;
import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.api.check.CheckSettings;
import me.theseems.clinq.api.compiler.TokenCompiler;
import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.impl.check.ConfiguredCheck;
import me.theseems.clinq.impl.compiler.error.IgnoreCheckErrors;
import me.theseems.clinq.impl.token.Token;
import me.theseems.clinq.utils.Checks;

import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

public abstract class Checker<InputType, CheckType> {
	/** Simple single check */
	public abstract Checker<InputType, CheckType> with(Check<CheckType> check);

	public Checker<InputType, CheckType> with(String message, Check<CheckType> check) {
		return with(ConfiguredCheck.configured(check, new CheckSettings().setErrorMessage(message)));
	}

	public Checker<InputType, CheckType> withBlocking(String message, Check<CheckType> check) {
		return with(ConfiguredCheck.configured(check, new CheckSettings().setErrorMessage(message).setPropagate(false)));
	}

	public abstract <PipeCheckType> Checker<InputType, CheckType>
	with(MapPipe<CheckType, PipeCheckType> pipe, Consumer<Checker<InputType, PipeCheckType>> tweak);

	/** Map then check */
	public abstract <PipeCheckType> Checker<InputType, CheckType>
	mapCheck(MapPipe<CheckType, PipeCheckType> pipe, Check<PipeCheckType> check);

	public <PipeCheckType> Checker<InputType, CheckType>
	mapCheck(String message, MapPipe<CheckType, PipeCheckType> pipe, Check<PipeCheckType> check) {
		return mapCheck(pipe, ConfiguredCheck.message(check, message));
	}

	public <PipeCheckType> Checker<InputType, CheckType>
	mapCheckBlocking(MapPipe<CheckType, PipeCheckType> pipe, Check<PipeCheckType> check) {
		return mapCheck(pipe, ConfiguredCheck.block(check));
	}

	public <PipeCheckType> Checker<InputType, CheckType>
	mapCheckBlocking(String message, MapPipe<CheckType, PipeCheckType> pipe, Check<PipeCheckType> check) {
		return mapCheck(pipe, ConfiguredCheck.blockMessage(check, message));
	}

	public abstract Checker<InputType, CheckType>
	when(Check<CheckType> condition);

	public abstract Checker<InputType, CheckType>
	when(Check<CheckType> condition, Checker<CheckType, CheckType> tweak);

	public abstract Checker<InputType, CheckType>
	whenCheck(Check<CheckType> condition, Check<CheckType> tweak);

	public abstract <PipeCheckType> Checker<InputType, PipeCheckType> map(MapPipe<CheckType, PipeCheckType> pipe);

	public abstract Checker<InputType, CheckType> error(String message);

	public abstract Checker<InputType, CheckType> blocking();

	public Checker<InputType, CheckType> then(String message, Check<CheckType> check) {
		return thenCheck(ConfiguredCheck.message(check, message));
	}

	public abstract Checker<InputType, CheckType> thenCheck(Check<CheckType> check);

	public abstract Checker<InputType, CheckType> then(Checker<InputType, CheckType> checker);

	public abstract Checker<InputType, CheckType> then(Consumer<Checker<InputType, CheckType>> checker);

	public Checker<InputType, CheckType> orElse(String message, Check<CheckType> check) {
		return orElseCheck(ConfiguredCheck.message(check, message));
	}

	public abstract Checker<InputType, CheckType> orElseCheck(Check<CheckType> check);

	public abstract Checker<InputType, CheckType> orElse(Checker<InputType, CheckType> checker);

	public abstract Checker<InputType, CheckType> orElse(Consumer<Checker<InputType, CheckType>> checker);

	public abstract boolean check(InputType value, CheckErrors errors);

	public abstract TokenCompiler getCompiler();

	public abstract void setCompiler(TokenCompiler compiler);

	@SafeVarargs
	public final Checker<InputType, CheckType> and(Check<CheckType>... checks) {
		return with(Checks.and(checks));
	}

	@SafeVarargs
	public final Checker<InputType, CheckType> and(String message, Check<CheckType>... checks) {
		return with(ConfiguredCheck.message(Checks.and(checks), message));
	}

	@SafeVarargs
	public final Checker<InputType, CheckType> andBlocking(String message, Check<CheckType>... checks) {
		return with(ConfiguredCheck.blockMessage(Checks.and(checks), message));
	}

	@SafeVarargs
	public final Checker<InputType, CheckType> or(Check<CheckType>... checks) {
		return with(Checks.or(checks));
	}

	@SafeVarargs
	public final Checker<InputType, CheckType> or(String message, Check<CheckType>... checks) {
		return with(ConfiguredCheck.message(Checks.or(checks), message));
	}

	@SafeVarargs
	public final Checker<InputType, CheckType> orBlocking(String message, Check<CheckType>... checks) {
		return with(ConfiguredCheck.blockMessage(Checks.or(checks), message));
	}

	public boolean check(InputType value) {
		return check(value, new IgnoreCheckErrors());
	}

	public abstract Queue<Token> getTokens();

	public Checker<InputType, CheckType> notNull() {
		return with(Objects::nonNull);
	}

	public Checker<InputType, CheckType> notNull(String message) {
		return with(ConfiguredCheck.message(Objects::nonNull, message));
	}

	public <PipeCheckType> Checker<InputType, CheckType> mapNotNull(MapPipe<CheckType, PipeCheckType> pipe) {
		return mapCheck(pipe, Objects::nonNull);
	}

	public <PipeCheckType> Checker<InputType, CheckType> mapNotNull(String message, MapPipe<CheckType, PipeCheckType> pipe) {
		return mapCheck(pipe, ConfiguredCheck.message(Objects::nonNull, message));
	}

	public Checker<InputType, CheckType> when(Check<CheckType> condition, Consumer<Checker<CheckType, CheckType>> tweak) {
		var anotherChecker = Clinq.<CheckType>checker();
		tweak.accept(anotherChecker);
		return when(condition, anotherChecker);
	}
}
