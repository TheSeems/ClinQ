package me.theseems.clinq;

import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.impl.compiler.error.ListCheckErrors;
import me.theseems.clinq.dto.BenefitDto;
import me.theseems.clinq.clinq.BenefitDtoClinQ;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DtoValidationTest {
	private void assertValid(BenefitDto dto) {
		CheckErrors errors = new ListCheckErrors();
		System.out.println(dto);
		Assertions.assertTrue(BenefitDtoClinQ.INSTANCE.check(dto, errors));
		System.out.println(errors);
		System.out.println();
	}

	private void assertInvalid(BenefitDto dto) {
		CheckErrors errors = new ListCheckErrors();
		System.out.println(dto);
		Assertions.assertFalse(BenefitDtoClinQ.INSTANCE.check(dto, errors));
		System.out.println(errors);
		System.out.println();
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
