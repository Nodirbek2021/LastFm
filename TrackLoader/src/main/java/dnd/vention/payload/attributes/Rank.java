package dnd.vention.payload.attributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Rank {
    private Integer id;
    private Integer rank;

    public Rank(Integer rank) {
        this.rank = rank;
    }
}
