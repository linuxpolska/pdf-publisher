package com.example.linux.pdfPublisheru.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static java.util.Objects.isNull;

@Getter
@Setter
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileDto {

    String fileName;
    String fileSize;
    String fileExtension;
    String fileSource;
    String fileDestination;

    public FileDto(String fileName, String fileSize, String fileExtension, String fileSource, String fileDestination) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
        this.fileSource = fileSource;
        this.fileDestination = fileDestination;
    }

    public boolean isEmpty() {
        return isNull(fileName)
                && isNull(fileSize)
                && isNull(fileExtension)
                && isNull(fileSource)
                && isNull(fileDestination);
    }
}

