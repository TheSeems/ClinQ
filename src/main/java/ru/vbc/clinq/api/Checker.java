package ru.vbc.clinq.api;

import ru.vbc.clinq.utils.Checks;

public abstract class Checker<InputType, CheckType> {
	public abstract Checker<InputType, CheckType> with(Check<CheckType> check);

	public abstract <PipeCheckType> Checker<InputType, PipeCheckType> map(MapPipe<CheckType, PipeCheckType> pipe);
	public abstract <PipeCheckType> Checker<InputType, PipeCheckType> pipe(OptionalPipe<CheckType, PipeCheckType> pipe);

	public abstract boolean check(InputType value);

	@SafeVarargs
	public final Checker<InputType, CheckType> and(Check<CheckType>... checks) {
		return with(Checks.and(checks));
	}

	@SafeVarargs
	public final Checker<InputType, CheckType> or(Check<CheckType>... checks) {
		return with(Checks.or(checks));
	}
}
