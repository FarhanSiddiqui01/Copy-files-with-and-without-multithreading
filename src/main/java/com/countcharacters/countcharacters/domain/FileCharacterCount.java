package com.countcharacters.countcharacters.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileCharacterCount {

    private String filePath;

    private Integer charCount;
}
