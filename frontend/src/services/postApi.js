// postApi.js

// Nota: En un entorno JS puro, la estructura del objeto debe ser documentada
// o inferida. Asumimos que los objetos son Post y PostPayload.

const BACKEND_URL = import.meta.env.PUBLIC_BACKEND_URL;

// ------------------------------------
// PÚBLICO: Obtener todos los posts (Feed)
// URL: GET /posts?category={category}&page={page}
// ------------------------------------
export const getPublicPosts = async (category = null, page = 0) => {
    let url = `${BACKEND_URL}/posts?page=${page}&size=10`;
    if (category) {
        url += `&category=${category}`;
    }
    
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`Error al obtener posts: ${response.statusText}`);
    }
    return response.json();
};

// ------------------------------------
// PÚBLICO: Obtener posts por autor
// URL: GET /posts/users/{author}
// ------------------------------------
export const getPostsByAuthor = async (author) => {
    const response = await fetch(`${BACKEND_URL}/posts/users/${author}`);
    if (!response.ok) {
        throw new Error(`Error al obtener posts del autor ${author}: ${response.statusText}`);
    }
    return response.json();
};

// ------------------------------------
// CRUD: Crear un nuevo post
// URL: POST /posts
// ------------------------------------
export const createPost = async (payload) => {
    const response = await fetch(`${BACKEND_URL}/posts`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
    });
    
    if (response.status === 400) {
        // Manejo de errores de validación de Spring Boot
        const errorBody = await response.json();
        console.error("Detalle del error de validación:", errorBody);
        throw new Error(`Error de validación. Revise la consola para detalles.`);
    }
    if (!response.ok) {
         throw new Error(`Error al crear post: ${response.statusText}`);
    }

    return response.json();
};

// ------------------------------------
// CRUD: Actualizar un post existente
// URL: PUT /posts/{id}
// ------------------------------------
export const updatePost = async (id, payload) => {
    const response = await fetch(`${BACKEND_URL}/posts/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
    });
    
    if (response.status === 404) {
        throw new Error(`Post con ID ${id} no encontrado.`);
    }
    if (!response.ok) {
        throw new Error(`Error al actualizar post: ${response.statusText}`);
    }

    return response.json();
};

// ------------------------------------
// CRUD: Borrar un post existente
// URL: DELETE /posts/{id}
// ------------------------------------
export const deletePost = async (id) => {
    const response = await fetch(`${BACKEND_URL}/posts/${id}`, {
        method: 'DELETE',
    });
    
    if (response.status === 404) {
        throw new Error(`Post con ID ${id} no encontrado.`);
    }
    if (!response.ok && response.status !== 204) { // 204 No Content es un éxito
        throw new Error(`Error al borrar post: ${response.statusText}`);
    }
    // Si es 204, no devuelve cuerpo.
    return true; 
};