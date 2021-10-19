package me.theseems.clinq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BenefitDto {
	private BigDecimal sum;

	private LocalDate validFrom;

	private LocalDate validTo;

	private BigDecimal benefitAmount;

	private BigDecimal benefitPercent;
}
