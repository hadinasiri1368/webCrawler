package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[meeting_status]")
@Entity(name = "MeetingStatus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "NVARCHAR(50)")
    private String name;
}
