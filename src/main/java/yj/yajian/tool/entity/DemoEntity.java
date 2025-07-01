package yj.yajian.tool.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 演示实体类 - 支持Builder模式初始化
 * 创建日期：2025年6月25日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoEntity {
    // 核心字段（多种类型）
    private Long id;                // ID（长整型）
    private String name;            // 名称（字符串）
    private BigDecimal price;       // 价格（高精度小数）
    private LocalDate expireDate;   // 过期日期（日期）
    private Boolean active;         // 激活状态（布尔）
    private Category category;      // 分类（枚举）
    private List<String> tags;      // 标签（列表）
    private LocalDateTime createdAt;// 创建时间（日期时间）

    // 枚举类型：产品分类
    public enum Category {
        ELECTRONICS, CLOTHING, BOOKS, FOOD, OTHER
    }

}