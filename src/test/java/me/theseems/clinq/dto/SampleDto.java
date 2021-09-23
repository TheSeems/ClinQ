package me.theseems.clinq.dto;

import java.util.List;

public class SampleDto {
	private final String name;
	private final List<Integer> scores;

	public String getName() {
		return name;
	}

	public List<Integer> getScores() {
		return scores;
	}

	public SampleDto(String name, List<Integer> scores) {
		this.name = name;
		this.scores = scores;
	}
}
