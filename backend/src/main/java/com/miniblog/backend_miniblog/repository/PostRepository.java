package com.miniblog.backend_miniblog.repository;

import com.miniblog.backend_miniblog.model.Post;
import com.miniblog.backend_miniblog.model.PostCategory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    // Posts recientes (Feed principal)
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Posts de un autor específico (buscando por el campo author)
    List<Post> findByAuthorOrderByCreatedAtDesc(String author); 
    
    // Filtrar por Categoría
    List<Post> findByCategoryOrderByCreatedAtDesc(PostCategory category, Pageable pageable);
}
