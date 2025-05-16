package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.FileCategory;
import com.daaloul.BackEnd.enums.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "uploaded_files")
public class UploadedFile {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    private FileType type;

    private String url;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHECK (file_category IN ('CODE', 'REPORT', 'SPECIFICATION','COVER_LETTER', 'CV'))")
    private FileCategory fileCategory;

    @Version
    @Column(name = "version")
    private Integer version=0;

}