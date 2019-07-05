package no.kobler.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignRequest {
    private String name;
    private List<String> keywords;
    private BigDecimal budget;
}
