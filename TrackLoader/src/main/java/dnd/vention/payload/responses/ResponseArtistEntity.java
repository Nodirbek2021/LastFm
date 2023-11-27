package dnd.vention.payload.responses;

import dnd.vention.payload.collectionObjects.Artists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseArtistEntity {
    private Integer id;
    private Artists artists;
}