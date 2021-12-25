package me.theseems.clinq.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class BenefitDto {
	BigDecimal sum;

	LocalDate validFrom;

	LocalDate validTo;

	BigDecimal benefitAmount;

	BigDecimal benefitPercent;
}
