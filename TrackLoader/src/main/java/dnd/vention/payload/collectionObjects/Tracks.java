package dnd.vention.payload.collectionObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import dnd.vention.payload.attributes.Page;
import dnd.vention.payload.baseObjects.TrackObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tracks {
    private Integer id;
    @JsonProperty("track")
    private List<TrackObject> trackObjectList;
    @JsonProperty("@attr")
    private Page page;
}
