package biz.hengartner.euroexchange.app.api.rates;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
	private Long id;

	@NotNull
    @JsonIgnore
    private String currency;

	@NotNull
	private BigDecimal rate;

	@NotNull
    @JsonIgnore
    private LocalDate date;

}

