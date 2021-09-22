package ru.theseems.clinq.api;

import ru.theseems.clinq.impl.compiler.TokenCompiler;
import ru.theseems.clinq.utils.Checks;

import java.util.function.Consumer;

public abstract class Checker<InputType, CheckType> {
	public abstract Checker<InputType, CheckType> with(Check<CheckType> check);

	public abstract <PipeCheckType> Checker<InputType, CheckType>
	with(MapPipe<CheckType, PipeCheckType> pipe, Consumer<Checker<InputType, PipeCheckType>> tweak);

	public abstract <PipeCheckType> Checker<InputType, CheckType>
	mapCheck(MapPipe<CheckType, PipeCheckType> pipe, Check<PipeCheckType> check);

	public abstract Checker<InputType, CheckType>
	when(Check<CheckType> condition, Checker<CheckType, CheckType> tweak);

	public abstract <PipeCheckType> Checker<InputType, PipeCheckType> map(MapPipe<CheckType, PipeCheckType> pipe);

	public abstract <PipeCheckType> Checker<InputType, PipeCheckType> pipe(OptionalPipe<CheckType, PipeCheckType> pipe);

	public abstract boolean check(InputType value);

	public abstract TokenCompiler compiler();

	@SafeVarargs
	public final Checker<InputType, CheckType> and(Check<CheckType>... checks) {
		return with(Checks.and(checks));
	}

	@SafeVarargs
	public final Checker<InputType, CheckType> or(Check<CheckType>... checks) {
		return with(Checks.or(checks));
	}
}
