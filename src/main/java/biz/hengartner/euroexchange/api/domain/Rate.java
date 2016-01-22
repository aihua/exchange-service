package biz.hengartner.euroexchange.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"currency","date"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rate {

	public Rate(String currency, BigDecimal rate, LocalDate date) {
		this(null, currency, rate, date);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String currency;

	@NotNull
	private BigDecimal rate;

	@NotNull
	private LocalDate date;

}

