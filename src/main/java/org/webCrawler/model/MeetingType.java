package org.webCrawler.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "[meeting_type]")
@Entity(name = "meetingType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "NVARCHAR(50)")
    private String name;
}
