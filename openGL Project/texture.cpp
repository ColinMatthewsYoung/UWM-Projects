#include"texture.h"
#define STB_IMAGE_IMPLEMENTATION
#include "stb_image.h"
#define GL_CLAMP_TO_EDGE 0x812f

Texture::Texture() {};

//path to texture
Texture::Texture(string f){
    filename = f;
}


//Load in the texture.
void Texture::loadTexture() {
    const char* path = filename.c_str();
    if (path == NULL) {
        fprintf(stderr, "Texture not found %s\n", filename);
        return;
    }
    int c;
    stbi_set_flip_vertically_on_load(1);
    unsigned char* data = stbi_load(path, &width, &height, &c, 0);
    if (data) {
        glGenTextures(1, &id);
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        stbi_image_free(data);
    }
    else {
        fprintf(stderr, "Failed to load texture: %s\n", filename);
        exit(1);
    }
}

//binds the texture so it can be drawn to current object.
void Texture::bind() {
    glBindTexture(GL_TEXTURE_2D, id);
}
