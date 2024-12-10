#ifndef TEXTURE_H
#define TEXTURE_H
using namespace std;
#include <iostream>
#include <string>
#include <GL/glut.h>


class Texture {
public:
	unsigned int id;
	int width, height;
	string filename;
	Texture();
	Texture(string f);
	void loadTexture();
	void bind();

};


#endif
