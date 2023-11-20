package dnd.vention.payload.baseObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import dnd.vention.payload.attributes.Rank;
import dnd.vention.payload.baseObjects.ArtistObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TrackObject {
    private Integer id;
    private String name;
    private Integer duration;
    private Integer playcount;
    private Integer listeners;
    private String mbid;
    private String url;
    @JsonProperty("@attr")
    private Rank rank;
    private ArtistObject artist;
}
