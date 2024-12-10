#include"cubeMap.h"


Cubemap::Cubemap(GLfloat s, string facePath) :
    //faces of the cube
    texture1(),
    texture2(),
    texture3(),
    texture4(),
    texture5(),
    texture6()
{
    path = facePath; //path for location to the 6 jpgs
    size = s; //distance of cube. 100x100x100 seems to be best fit.

    //text cords. streach jpg across the full square
    texCoords[0][0] = 0; texCoords[0][1] = 0;
    texCoords[1][0] = 1; texCoords[1][1] = 0;
    texCoords[2][0] = 1; texCoords[2][1] = 1;
    texCoords[3][0] = 0; texCoords[3][1] = 1;

    //box cords. makes a cube.
    vertices[0][0] = -size; vertices[0][1] = -size; vertices[0][2] = -size;
    vertices[1][0] = -size; vertices[1][1] = -size; vertices[1][2] =  size;
    vertices[2][0] = -size; vertices[2][1] =  size; vertices[2][2] = -size;
    vertices[3][0] = -size; vertices[3][1] =  size; vertices[3][2] =  size;
    vertices[4][0] =  size; vertices[4][1] = -size; vertices[4][2] = -size;
    vertices[5][0] =  size; vertices[5][1] = -size; vertices[5][2] =  size;
    vertices[6][0] =  size; vertices[6][1] =  size; vertices[6][2] = -size;
    vertices[7][0] =  size; vertices[7][1] =  size; vertices[7][2] =  size;
    
    
}
//load up textures
void Cubemap::textInit() {
    texture1.filename = path + "front.jpg";
    texture1.loadTexture();
    texture2.filename = path + "back.jpg";
    texture2.loadTexture();
    texture3.filename = path + "right.jpg";
    texture3.loadTexture();
    texture4.filename = path + "left.jpg";
    texture4.loadTexture();
    texture5.filename = path + "top.jpg";
    texture5.loadTexture();
    texture6.filename = path + "bottom.jpg";
    texture6.loadTexture();
}

//draws each face of the cube and gives it the correct texture.
void Cubemap::loadCube() {
    glEnable(GL_TEXTURE_2D);
    texture1.bind();
    glBegin(GL_QUADS);
    glTexCoord2fv(texCoords[0]); glVertex3fv(vertices[0]);
    glTexCoord2fv(texCoords[3]); glVertex3fv(vertices[2]);
    glTexCoord2fv(texCoords[2]); glVertex3fv(vertices[6]);
    glTexCoord2fv(texCoords[1]); glVertex3fv(vertices[4]);
    glEnd();


    texture2.bind();
    glBegin(GL_QUADS);
    glTexCoord2fv(texCoords[1]); glVertex3fv(vertices[1]);
    glTexCoord2fv(texCoords[2]); glVertex3fv(vertices[3]);
    glTexCoord2fv(texCoords[3]); glVertex3fv(vertices[7]);
    glTexCoord2fv(texCoords[0]); glVertex3fv(vertices[5]);
    glEnd();
    // Right face (texture3)

    texture3.bind();
    glBegin(GL_QUADS);
    glTexCoord2fv(texCoords[1]); glVertex3fv(vertices[5]);
    glTexCoord2fv(texCoords[2]); glVertex3fv(vertices[7]);
    glTexCoord2fv(texCoords[3]); glVertex3fv(vertices[6]);
    glTexCoord2fv(texCoords[0]); glVertex3fv(vertices[4]);
    glEnd();
    // Left face (texture4)

    texture4.bind();
    glBegin(GL_QUADS);
    glTexCoord2fv(texCoords[1]); glVertex3fv(vertices[0]);
    glTexCoord2fv(texCoords[2]); glVertex3fv(vertices[2]);
    glTexCoord2fv(texCoords[3]); glVertex3fv(vertices[3]);
    glTexCoord2fv(texCoords[0]); glVertex3fv(vertices[1]);
    glEnd();
    // Top face (texture5)

    texture5.bind();
    glBegin(GL_QUADS);
    glTexCoord2fv(texCoords[0]); glVertex3fv(vertices[2]);
    glTexCoord2fv(texCoords[3]); glVertex3fv(vertices[3]);
    glTexCoord2fv(texCoords[2]); glVertex3fv(vertices[7]);
    glTexCoord2fv(texCoords[1]); glVertex3fv(vertices[6]);
    glEnd();

    // Bottom face (texture6)

    texture6.bind();
    glBegin(GL_QUADS);
    glTexCoord2fv(texCoords[1]); glVertex3fv(vertices[5]);
    glTexCoord2fv(texCoords[2]); glVertex3fv(vertices[4]);
    glTexCoord2fv(texCoords[3]); glVertex3fv(vertices[0]);
    glTexCoord2fv(texCoords[0]); glVertex3fv(vertices[1]);

    glEnd();

    glDisable(GL_TEXTURE_2D);
}

//update the position of the cube map so the camera stays centered.
void Cubemap::updateCube(GLfloat x, GLfloat y, GLfloat z) {

    vertices[0][0] = -size + x; vertices[0][1] = -size + y; vertices[0][2] = -size + z;
    vertices[1][0] = -size + x; vertices[1][1] = -size + y; vertices[1][2] =  size + z;
    vertices[2][0] = -size + x; vertices[2][1] =  size + y; vertices[2][2] = -size + z;
    vertices[3][0] = -size + x; vertices[3][1] =  size + y; vertices[3][2] =  size + z;
    vertices[4][0] =  size + x; vertices[4][1] = -size + y; vertices[4][2] = -size + z;
    vertices[5][0] =  size + x; vertices[5][1] = -size + y; vertices[5][2] =  size + z;
    vertices[6][0] =  size + x; vertices[6][1] =  size + y; vertices[6][2] = -size + z;
    vertices[7][0] =  size + x; vertices[7][1] =  size + y; vertices[7][2] =  size + z;
}
