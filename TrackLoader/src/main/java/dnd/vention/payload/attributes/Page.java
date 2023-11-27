package dnd.vention.payload.attributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Page {
    private Integer id;
    private String artist;
    private Integer page;
    private Integer perPage;
    private Integer totalPages;
    private Integer total;
}