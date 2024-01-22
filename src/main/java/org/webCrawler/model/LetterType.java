package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[letter_type]")
@Entity(name = "letterType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterType {
    @Id
    private Long id;
    @Column(columnDefinition = "NVARCHAR(50)")
    private String name;
}
