package yj.yajian.bookmark;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @GetMapping
    public List<Bookmark> getBookmarks() {
        return Arrays.asList(
                new Bookmark(1, "GitHub", "https://github.com", 24),
                new Bookmark(2, "Stack Overflow", "https://stackoverflow.com", 18),
                new Bookmark(3, "MDN Web Docs", "https://developer.mozilla.org", 32),
                new Bookmark(4, "LeetCode", "https://leetcode.com", 15),
                new Bookmark(5, "CSS-Tricks", "https://css-tricks.com", 12),
                new Bookmark(6, "DEV Community", "https://dev.to", 8),
                new Bookmark(7, "Medium", "https://medium.com", 20),
                new Bookmark(8, "freeCodeCamp", "https://www.freecodecamp.org", 16),
                new Bookmark(9, "Dribbble", "https://dribbble.com", 10),
                new Bookmark(10, "Figma", "https://figma.com", 22),
                new Bookmark(11, "Trello", "https://trello.com", 14),
                new Bookmark(12, "Notion", "https://notion.so", 19)
        );
    }

}

