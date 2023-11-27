package dnd.vention.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import dnd.vention.payload.collectionObjects.TopTracks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ResponseTopTracksEntity {
    private Integer id;
    @JsonProperty("tracks")
    private TopTracks toptracks;
}
