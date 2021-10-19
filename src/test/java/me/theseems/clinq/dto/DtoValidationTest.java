package me.theseems.clinq.dto;

import me.theseems.clinq.clinq.BenefitDtoClinQ;
import me.theseems.clinq.test.TestCheckErrors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DtoValidationTest {
	private void assertValid(BenefitDto dto) {
		TestCheckErrors errors = new TestCheckErrors();
		Assertions.assertTrue(BenefitDtoClinQ.INSTANCE.check(dto, errors));
	}

	private void assertInvalid(BenefitDto dto) {
		TestCheckErrors errors = new TestCheckErrors();
		Assertions.assertFalse(BenefitDtoClinQ.INSTANCE.check(dto, errors));
	}

	@Test
	public void validate_BenefitDto_Fail() {
		assertInvalid(new BenefitDto(
			BigDecimal.TEN,
			LocalDate.now(),
			LocalDate.now().plusDays(1),
			BigDecimal.ZERO,
			null
		));

		assertInvalid(new BenefitDto(
			BigDecimal.TEN,
			LocalDate.now(),
			LocalDate.now().plusDays(1),
			null,
			BigDecimal.ZERO
		));

		assertInvalid(new BenefitDto(
			BigDecimal.TEN,
			LocalDate.now(),
			LocalDate.now().plusDays(1),
			null,
			null
		));
	}

	@Test
	public void validate_Benefit_Success() {
		assertValid(new BenefitDto(
			BigDecimal.ONE,
			LocalDate.of(2018, 5, 15),
			LocalDate.of(2020, 7, 21),
			BigDecimal.valueOf(10_000),
			null
		));
	}
}
