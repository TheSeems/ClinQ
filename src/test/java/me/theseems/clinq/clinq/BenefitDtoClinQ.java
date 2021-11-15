package me.theseems.clinq.clinq;

import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.api.checker.Checker;
import me.theseems.clinq.api.validator.ClinqValidator;
import me.theseems.clinq.checks.Args;
import me.theseems.clinq.dto.BenefitDto;
import me.theseems.clinq.test.utils.CheckUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BenefitDtoClinQ extends ClinqValidator<BenefitDto> {
	@Override
	public Checker<BenefitDto, ?> bake() {
		var checker = Clinq.<BenefitDto>checker()
			.notNull("No data")
			.mapNotNull(BenefitDto::getBenefitAmount)
				.error("Benefit amount must be specified")
			.mapNotNull(this::dateFrom)
				.error("Date \"from\" must be specified")
			.mapNotNull(this::dateTo)
				.error("Date \"to\" must be specified");

		checker = checker
			.mapCheck(this::amount, this::greaterThanZero)
				.error("Benefit amount must be greater than zero")
			.and(dto -> CheckUtils.isBefore(dto.getValidFrom(), dto.getValidTo()))
				.error("Dates \"from\" and \"to\" are specified in an incorrect order")
			.and(dto -> Args.onlyOneNonNull(dto.getBenefitPercent(), dto.getBenefitAmount()))
				.error("You must specify either benefit percent or benefit amount");

		checker.when(dto -> dto.getBenefitAmount() != null, amountChecker -> amountChecker
			.mapCheck(this::benefitAmount, this::greaterThanZero)
				.error("Benefit amount must be greater than zero")
		);

		checker.when(dto -> dto.getBenefitPercent() != null, percentChecker -> percentChecker
			.mapCheck(this::benefitPercent, this::greaterThanZero)
				.error("Benefit percent must be greater than zero")
		);

		return checker;
	}

	private LocalDate dateFrom(BenefitDto dto) {
		return dto.getValidFrom();
	}

	private LocalDate dateTo(BenefitDto dto) {
		return dto.getValidFrom();
	}

	private BigDecimal amount(BenefitDto dto) {
		return dto.getSum();
	}

	private BigDecimal benefitPercent(BenefitDto dto) {
		return dto.getBenefitPercent();
	}

	private BigDecimal benefitAmount(BenefitDto dto) {
		return dto.getBenefitAmount();
	}

	private boolean greaterThanZero(BigDecimal value) {
		return value.compareTo(BigDecimal.ZERO) > 0;
	}
}
