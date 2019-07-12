package no.kobler.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name="campaigns")
@NamedQueries({
        @NamedQuery(name="no.kobler.api.Campaigns.findAll", query = "select c from Campaign c"),
        @NamedQuery(name="no.kobler.api.Campaign.findByKeywords", query = "select c from Campaign c join Keyword k on c.id = k.campaign.id where k.text in (:keywords)")
})
public class Campaign {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaign")
    private List<Keyword> keywords;
    private BigDecimal budget;
    private BigDecimal spending;

    public Campaign(String name, BigDecimal budget, BigDecimal spending) {
        this.name = name;
        this.budget = budget;
        this.spending = spending;
    }

    @JsonProperty(value = "keywords")
    public List<String> getKeywordTexts() {
        return keywords.stream().map(Keyword::getText).collect(Collectors.toList());
    }
}
