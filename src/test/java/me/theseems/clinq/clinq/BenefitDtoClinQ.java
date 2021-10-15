package me.theseems.clinq.clinq;

import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.checks.Args;
import me.theseems.clinq.dto.BenefitDto;
import me.theseems.clinq.test.utils.CheckUtils;
import me.theseems.clinq.api.Checker;
import me.theseems.clinq.api.ClinQ;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class BenefitDtoClinQ {
	public static final BenefitDtoClinQ INSTANCE = new BenefitDtoClinQ();

	private BenefitDtoClinQ() {
		checker = ClinQ.<BenefitDto>checker()
			.notNull()
			.error("Нет данных")
			.mapNotNull(this::amount)
			.error("Сумма комиссии должна быть указана")
			.mapNotNull(this::dateFrom)
			.error("Дата \"от\" должна быть указана")
			.mapNotNull(this::dateTo)
			.error("Дата \"до\" должна быть указана")
			.mapCheck(this::amount, this::greaterThanZero)
			.error("Сумма комиссии должна быть больше нуля")
			.and(dto -> CheckUtils.isBefore(dto.getValidFrom(), dto.getValidTo()))
			.error("Даты расположены в неправильном порядке")
			.and(dto -> Args.onlyOneNonNull(dto.getBenefitPercent(), dto.getBenefitAmount()))
			.error("Должен быть указан либо процент по комиссии, либо сумма комиссии");

		checker.when(dto -> dto.getBenefitAmount() != null, amountChecker -> amountChecker
			.mapCheck(this::commissionPercent, Objects::isNull)
			.error("Процент по комиссии не должен быть указан, если указана сумма")
			.mapCheck(this::commissionAmount, this::greaterThanZero)
			.error("Сумма комиссии должна быть больше нуля")
		);

		checker.when(dto -> dto.getBenefitPercent() != null, percentChecker -> percentChecker
			.mapCheck(this::commissionAmount, Objects::isNull)
			.error("Сумма комиссии не должна быть указана, если указан процент")
			.mapCheck(this::commissionPercent, this::greaterThanZero)
			.error("Процент по комиссии должен быть больше нуля")
		);
	}

	public boolean check(BenefitDto commissionCalculateInDto, CheckErrors errors) {
		return checker.check(commissionCalculateInDto, errors);
	}

	private static Checker<BenefitDto, BenefitDto> checker;

	private LocalDate dateFrom(BenefitDto dto) {
		return dto.getValidFrom();
	}

	private LocalDate dateTo(BenefitDto dto) {
		return dto.getValidFrom();
	}

	private BigDecimal amount(BenefitDto dto) {
		return dto.getSum();
	}

	private BigDecimal commissionPercent(BenefitDto dto) {
		return dto.getBenefitPercent();
	}

	private BigDecimal commissionAmount(BenefitDto dto) {
		return dto.getBenefitAmount();
	}

	private boolean greaterThanZero(BigDecimal value) {
		return value.compareTo(BigDecimal.ZERO) > 0;
	}
}
