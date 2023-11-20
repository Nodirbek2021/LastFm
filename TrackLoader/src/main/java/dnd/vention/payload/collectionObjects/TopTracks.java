package dnd.vention.payload.collectionObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import dnd.vention.payload.baseObjects.TrackObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TopTracks {
    private Integer id;
    @JsonProperty("track")
    private List<TrackObject> trackObjectList;
}
