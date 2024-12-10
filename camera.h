#ifndef CAMERA_CLASS_H
#define CAMERA_CLASS_H
 
#include <iostream>
#include <GL/glut.h>
#include <math.h>
#include <cstdlib>

class Camera {
public:
	GLsizei width, height;
	GLfloat x, y, z;
	GLfloat yaw, pitch;
	GLfloat speed, sensitivity;


	Camera(GLsizei w, GLsizei h);
	Camera(GLsizei w, GLsizei h, GLfloat pos[], GLfloat tilt[]);
	
	//adjusts camera based on keyboard input.
	void keypressCam(unsigned char key);

	//adjust camera based on mouse movment.
	void mouseCam(int x, int y);

	//update the camera
	void updateCam(int window);
};

#endif
