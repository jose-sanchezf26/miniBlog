package com.miniblog.backend_miniblog.dtoPost;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

import com.miniblog.backend_miniblog.model.PostCategory;

@Data
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private PostCategory category;
    private String author;
}