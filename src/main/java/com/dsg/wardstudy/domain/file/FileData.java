package com.dsg.wardstudy.domain.file;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "file_data")
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private long size;

    private String filePath;

    @Lob
    @Column(name = "imagedata", length = 1000)
    private byte[] imageData;

    @Builder
    public FileData(String name, String type, long size, String filePath, byte[] imageData) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.filePath = filePath;
        this.imageData = imageData;
    }
}
