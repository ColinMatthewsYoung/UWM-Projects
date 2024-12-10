#ifndef CUBEMAP_H
#define CUBEMAP_H
#include "texture.h"
#include <vector>
#include <string>
using namespace std;


class Cubemap {
public:
	//constructor
	Cubemap(GLfloat s, string f);
	//vars
	Texture texture1;
	Texture texture2;
	Texture texture3;
	Texture texture4;
	Texture texture5;
	Texture texture6;
	GLfloat size;
	GLfloat texCoords[4][2];
	GLfloat vertices[8][3];
	string path;
	
	//functions
	void textInit();
	void loadCube();
	void updateCube(GLfloat x, GLfloat y, GLfloat z);
};

#endif
