#include"camera.h"


Camera::Camera(GLsizei w, GLsizei h) {
    width = w;
    height = h;
    x = 0.0f;
    y = 0.0f;
    z = 5.0f;
    yaw = 0.0f;
    pitch = 0.0f;
    speed = 0.75f;
    sensitivity = 0.1f;
}

Camera::Camera(GLsizei w, GLsizei h, GLfloat pos[], GLfloat tilt[]) {
    width = w; //width and height should always be set to window size.
    height = h;
    x = pos[0];
    y = pos[1];
    z = pos[2];
    yaw = tilt[0]; //left right
    pitch = tilt[1];//up down
    speed = 0.5f; //movement speed
    sensitivity = 0.1f; //looking around speed
}


void Camera::keypressCam(unsigned char key) {
    switch (key) {
    case 'w':
        x += speed * sin(yaw * 3.141592f / 180.0f); //accounts for camera tilt. as we look around we still want w to be moving forward relative to the camera.
        z -= speed * cos(yaw * 3.141592f / 180.0f);
        break;
    case 's':
        x -= speed * sin(yaw * 3.141592f / 180.0f);
        z += speed * cos(yaw * 3.141592f / 180.0f);
        break;
    case 'a':
        x -= speed * cos(yaw * 3.141592f / 180.0f);
        z -= speed * sin(yaw * 3.141592f / 180.0f);
        break;
    case 'd':
        x += speed * cos(yaw * 3.141592f / 180.0f);
        z += speed * sin(yaw * 3.141592f / 180.0f);
        break;
    case 'e':
        y += speed;
        break;
    case 'q':
        y -= speed;
        break;
 
    case '\x1b':
        //close window
        glutDestroyWindow(glutGetWindow()); //might be overkill but I want to makesure the program shuts down properly.
        exit(0);
        break;

    }
}

void Camera::mouseCam(int x, int y) {
    
    //update yaw and pitch based off movement of mouse.
    x= x - width / 2; 
    y = y - height / 2;
    yaw += (GLfloat)x * sensitivity;
    pitch += (GLfloat)y * sensitivity;
    
    //keep mouse center of screen.
    glutWarpPointer(width / 2, height / 2);
}
