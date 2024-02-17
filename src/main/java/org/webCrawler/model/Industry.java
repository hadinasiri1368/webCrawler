package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[industry]")
@Entity(name = "industry")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "tinyint")
    private Integer code;
    @Column(columnDefinition = "NVARCHAR(50)")
    private String name;
}
