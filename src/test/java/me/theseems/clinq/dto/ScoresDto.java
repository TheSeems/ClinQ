package me.theseems.clinq.dto;

import java.util.List;

public class ScoresDto {
	private final String name;
	private final List<Integer> scores;

	public String getName() {
		return name;
	}

	public List<Integer> getScores() {
		return scores;
	}

	public ScoresDto(String name, List<Integer> scores) {
		this.name = name;
		this.scores = scores;
	}
}
