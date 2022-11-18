package fi.csc.processor.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest extends BaseEvent {
    private String keywords;
    private SearchFilter filters;
}
