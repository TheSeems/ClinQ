package ru.vbc.clinq.impl;

import ru.vbc.clinq.api.Check;
import ru.vbc.clinq.api.Checker;
import ru.vbc.clinq.api.OptionalPipe;
import ru.vbc.clinq.api.MapPipe;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class SimpleChecker<InputType, CheckType> extends Checker<InputType, CheckType> {
	private Queue<Object> current = new ArrayDeque<>();

	@Override
	public Checker<InputType, CheckType> with(Check<CheckType> check) {
		current.add(check);
		return this;
	}

	@Override
	public <PipeCheckType> Checker<InputType, PipeCheckType> map(MapPipe<CheckType, PipeCheckType> pipe) {
		current.add(pipe);

		SimpleChecker<InputType, PipeCheckType> newChecker = new SimpleChecker<>();
		newChecker.current = current;
		return newChecker;
	}

	public <PipeCheckType> Checker<InputType, PipeCheckType> pipe(OptionalPipe<CheckType, PipeCheckType> pipe) {
		current.add(pipe);

		SimpleChecker<InputType, PipeCheckType> newChecker = new SimpleChecker<>();
		newChecker.current = current;
		return newChecker;
	}

	@Override
	public boolean check(InputType value) {
		Object currentValue = value;
		for (Object o : current) {
			if (o instanceof Check) {
				if (!((Check<InputType>) o).check((InputType) currentValue)) return false;
			} else if (o instanceof OptionalPipe) {
				Optional piped = ((OptionalPipe) o).produce(currentValue);
				if (piped.isEmpty()) return false;
				currentValue = piped.get();
			} else if (o instanceof MapPipe) {
				currentValue = ((MapPipe) o).produce(currentValue);
			}
		}

		return true;
	}
}
