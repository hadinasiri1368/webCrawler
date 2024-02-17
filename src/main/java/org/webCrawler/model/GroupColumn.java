package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[group_column]")
@Entity(name = "group_column")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "NVARCHAR(50)")
    private String name;
    @Column(columnDefinition = "tinyint")
    private Integer code;
}
