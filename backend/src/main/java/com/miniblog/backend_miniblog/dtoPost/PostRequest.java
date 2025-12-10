package com.miniblog.backend_miniblog.dtoPost;

import com.miniblog.backend_miniblog.model.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "Título obligatorio.")
    @Size(min = 5, max = 150, message = "Título debe tener entre 5 y 150 caracteres.")
    private String title;

    @NotBlank(message = "Contenido obligatorio.")
    @Size(min = 10, message = "Contenido debe tener al menos 10 caracteres.")
    private String content;

    @NotNull(message = "Categoría obligatorio.")
    private PostCategory category;
    
    @NotBlank(message = "Autor obligatorio.")
    private String author;
}