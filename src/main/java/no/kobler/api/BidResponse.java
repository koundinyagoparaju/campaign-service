package no.kobler.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse {
    private Long bidId;
    private BigDecimal bidAmount;
}
