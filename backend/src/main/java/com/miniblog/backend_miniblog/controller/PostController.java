package com.miniblog.backend_miniblog.controller;

import com.miniblog.backend_miniblog.dtoPost.PostRequest;
import com.miniblog.backend_miniblog.dtoPost.PostResponse;
import com.miniblog.backend_miniblog.model.PostCategory;
import com.miniblog.backend_miniblog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Validated
// @CrossOrigin(origins = "http://localhost:4321")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    // ------------------------------------
    // 1. GET /posts: Listar posts recientes (PÚBLICO)
    // También soporta: GET /posts?category=TECNOLOGIA&page=0&size=10
    // ------------------------------------
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(required = false) PostCategory category, // Filtrado opcional
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PostResponse> posts = postService.getAllPosts(page, size, category);
        return ResponseEntity.ok(posts);
    }
    
    // ------------------------------------
    // 2. GET /users/{author}/posts: Listar posts de un usuario (PÚBLICO)
    // ------------------------------------
    @GetMapping("/users/{author}") // Usamos /users/{author} para mapear la URL solicitada
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(@PathVariable String author) {
        List<PostResponse> posts = postService.getPostsByAuthor(author);
        return ResponseEntity.ok(posts);
    }

    // ------------------------------------
    // 3. POST /posts: Crear post (YA NO REQUIERE TOKEN)
    // ------------------------------------
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        // @Valid activa las validaciones de las anotaciones en PostRequest
        PostResponse newPost = postService.createPost(request);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    // ------------------------------------
    // 4. PUT /posts/{id}: Editar post (YA NO REQUIERE TOKEN)
    // ------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id, 
            @Valid @RequestBody PostRequest request) {
        PostResponse updatedPost = postService.updatePost(id, request);
        return ResponseEntity.ok(updatedPost);
    }

    // ------------------------------------
    // 5. DELETE /posts/{id}: Borrar post (YA NO REQUIERE TOKEN)
    // ------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}
