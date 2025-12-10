package com.miniblog.backend_miniblog.service;

import com.miniblog.backend_miniblog.dtoPost.PostRequest;
import com.miniblog.backend_miniblog.dtoPost.PostResponse;
import com.miniblog.backend_miniblog.model.Post;
import com.miniblog.backend_miniblog.model.PostCategory;
import com.miniblog.backend_miniblog.repository.PostRepository;
import com.miniblog.backend_miniblog.exception.ResourceNotFoundException; // Necesitamos esta excepción
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    // --- Mapper ---
    private PostResponse mapToDto(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .category(post.getCategory())
                .author(post.getAuthor()) // Simple String
                .build();
    }
    
    // --- Consultas Públicas (Lectura) ---

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(int page, int size, PostCategory category) {
        PageRequest pageable = PageRequest.of(page, size);
        List<Post> posts;
        
        if (category != null) {
            posts = postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
        } else {
            posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        
        return posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByAuthor(String author) {
        // Usa el nuevo método findByAuthorOrderByCreatedAtDesc
        return postRepository.findByAuthorOrderByCreatedAtDesc(author).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }
    
    // --- Operaciones de Escritura (CRUD) ---
    
    @Transactional
    public PostResponse createPost(PostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setAuthor(request.getAuthor()); // Asigna el autor directamente del DTO

        Post savedPost = postRepository.save(post);
        return mapToDto(savedPost);
    }
    
    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory()); 
        post.setAuthor(request.getAuthor()); // Permite cambiar el autor, simplificado por no tener seguridad
        
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }
    
    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        postRepository.deleteById(postId);
    }
}
