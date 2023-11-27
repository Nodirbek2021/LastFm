package dnd.vention.payload.collectionObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import dnd.vention.payload.attributes.Page;
import dnd.vention.payload.baseObjects.ArtistObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Artists {
    private Integer id;
    @JsonProperty("artist")
    private List<ArtistObject> artistObjectList;

    @JsonProperty("@attr")
    private Page page;
}