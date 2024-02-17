package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[columns]")
@Entity(name = "columns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Columns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "NVARCHAR(100)" , name = "column_name")
    private String columnName;
    @Column(columnDefinition = "NVARCHAR(100)")
    private String caption;
    @Column(columnDefinition = "bit", name = "is_general")
    private Boolean isGeneral;
    @Column(columnDefinition = "decimal(18, 0)",name = "group_column_id")
    private Long groupColumnId;

}
