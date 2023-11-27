package dnd.vention.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import dnd.vention.payload.collectionObjects.Tracks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ResponseTrackByArtistEntity {
    private Integer id;
    @JsonProperty("toptracks")
    private Tracks tracks;
}
