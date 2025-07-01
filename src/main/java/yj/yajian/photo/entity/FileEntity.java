package yj.yajian.photo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 演示实体类 - 支持Builder模式初始化
 * 创建日期：2025年6月25日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    // 核心字段（多种类型）
    private Long id;                // ID（长整型）
    private String name;            // 名称（字符串）
    private Long size;              // 大小（高精度小数）
    private List<String> tags;      // 标签（列表）
}