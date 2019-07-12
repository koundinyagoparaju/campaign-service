package no.kobler.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="keywords")
@Data
@NoArgsConstructor
public class Keyword {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(name = "text")
    private String text;

    public Keyword(String text, Campaign campaign) {
        this.text = text;
        this.campaign = campaign;
    }
}
