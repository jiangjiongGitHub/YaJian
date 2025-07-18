package yj.yajian.bookmark;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bookmark {
    private int id;
    private String title;
    private String url;
    private int count;

    public Bookmark() {}

    public Bookmark(int id, String title, String url, int count) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.count = count;
    }
}

