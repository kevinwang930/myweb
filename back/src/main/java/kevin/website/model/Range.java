package kevin.website.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Range {
    private long start;
    private long end;
    private long total;

    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }


}
