package yj.yajian.bookmark;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Bookmark {
    private Long id;
    private String title;
    private String url;
    private Integer count;
    private Set<String> keys;

    public Bookmark() {}

    public Bookmark(Long id, String title, String url, Integer count) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.count = count;
    }
}

