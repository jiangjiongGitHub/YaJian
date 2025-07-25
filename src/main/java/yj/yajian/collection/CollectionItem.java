package yj.yajian.collection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CollectionItem {
    private Long id;
    private String title;
    private String content; // 文本内容或文件路径
    private String type; // text, image, video, file
    private String time;
}
