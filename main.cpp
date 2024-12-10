
#include <GL/glut.h>
#include <stdio.h>
#include <stdlib.h>
#include "camera.h"
#include "texture.h"
#include "cubeMap.h"

//widnow size
GLsizei width = 900, height = 900;


//camera inits
GLfloat camPos[3] = { 45,13, 37}; //x,y,z
GLfloat camTilt[2] = { -32,5 }; //yaw then pitch
Camera cam1(width, height, camPos, camTilt);
Camera cam2(width, height, camPos, camTilt);
int cam = 0;
Camera activeCam[2] = { cam1,cam2 };

//cubemap init
GLfloat s = 500;
string path1 = "C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/space2/";
string path2 = "C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/space1/";
Cubemap cube1(s,path1);
Cubemap cube2(s, path2);
Cubemap maps[2] = { cube1,cube2 };
int activeMap=0;


// Light source parameters
GLfloat lightPosition[] = { 0.0f, 0.0f, 0.0f, 1.0f }; // Position
GLfloat lightAmbient[] = { 0.0002f, 0.0002f, 0.0002f, 1.0f };  // Ambient component
GLfloat lightDiffuse[] = { 0.99f, 0.9529f, 0.85f, 1.0f };  // Diffuse component
GLfloat lightSpecular[] = { 0.8f, 0.8f, 0.8f, 1.0f }; // Specular component



// Material properties current values were set to match 'brass' as i like the look of it.
GLfloat sunEmission[] = { 1,1,1,1 };
GLfloat matAmbient[] = { 0.329412f, 0.223529f, 0.027451f,1.0f };
GLfloat matDiffuse[] = { 0.780392f, 0.568627f, 0.113725f, 1.0f };
GLfloat matSpecular[] = { 0.992157f, 0.941176f, 0.807843f, 1.0f };
GLfloat matEmission[] = { 0,0,0,1 };
GLfloat matShininess = 27.8974f; // Higher value for shininess


//earth texture.
Texture earth("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/earth.jpg");
Texture saturn("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/saturn.jpg");
Texture moon("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/moon.jpg");
Texture sun("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/sun.jpg");
Texture rings("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/rings.jpg");
Texture mercury("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/mercury.jpg");
Texture venus("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/venus.jpg");
Texture mars("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/mars.jpg");
Texture jupiter("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/jupiter.jpg");
Texture uranus("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/uranus.jpg");
Texture neptune("C:/Users/Colin/Documents/Computer Graphics hw/ComputerGraphicsProject/resource/planet/neptune.jpg");

//quuadric to be used in display to draw spheres.
GLUquadric* pSun = gluNewQuadric();
GLUquadric* pMercury = gluNewQuadric();
GLUquadric* pVenus = gluNewQuadric();
GLUquadric* pEarth = gluNewQuadric();
GLUquadric* pMoon = gluNewQuadric();
GLUquadric* pMars = gluNewQuadric();
GLUquadric* pJupiter = gluNewQuadric();
GLUquadric* pSaturn = gluNewQuadric();
GLUquadric* disk = gluNewQuadric();
GLUquadric* pUranus = gluNewQuadric();
GLUquadric* pNeptune = gluNewQuadric();

//rotations
GLfloat planetsRot[] = { 0,0,0,0,0,0,0,0,0,0}; //0 is rotation of sun, moving outward.
GLfloat planetsOrbit[] = {0,0,0,0,0,0,0,0,0}; //0 is first planet, sun does not orbit itself.
GLfloat pause = 1; //pauses rotations


void init() {
    glClearColor(0.0, 0.0, 0.0, 1.0);
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_TEXTURE_2D);

    //load textures

    maps[activeMap].textInit();

    sun.loadTexture();
    mercury.loadTexture();
    venus.loadTexture();
    earth.loadTexture();
    moon.loadTexture();
    mars.loadTexture();
    jupiter.loadTexture();
    saturn.loadTexture();
    rings.loadTexture();
    uranus.loadTexture();
    neptune.loadTexture();

    // Lighting setup
    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);

    glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);
    glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
    glLightfv(GL_LIGHT0, GL_SPECULAR, lightSpecular);

 
    // attenuation, farther objects get less light.
    glLightf(GL_LIGHT0, GL_CONSTANT_ATTENUATION, 1.0f);
    glLightf(GL_LIGHT0, GL_LINEAR_ATTENUATION, 0.001f);
    glLightf(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, 0.0001f);

    // Material setup 
    glMaterialfv(GL_FRONT, GL_AMBIENT, matAmbient);
    glMaterialfv(GL_FRONT, GL_DIFFUSE, matDiffuse);
    glMaterialfv(GL_FRONT, GL_SPECULAR, matSpecular);
    glMaterialfv(GL_FRONT, GL_EMISSION, sunEmission);
    glMaterialf(GL_FRONT, GL_SHININESS, matShininess);
    



    //place 2nd camera
    activeCam[1].x = 1195;
    activeCam[1].y = 1125;
    activeCam[1].z = 1480;
    activeCam[1].yaw = -40;
    activeCam[1].pitch = 35;
    activeCam[1].speed = 5;

    glDisable(GL_TEXTURE_2D);
}   


void updateRotations() {
    //rotation of planets

    planetsOrbit[0] += 0.005 * pause;
    planetsOrbit[1] += 0.0001 * pause;
    planetsOrbit[2] += 0.002 * pause;
    planetsOrbit[3] += 0.0005 * pause;
    planetsOrbit[4] += 0.001 * pause;
    planetsOrbit[5] += 0.0007 * pause;
    planetsOrbit[6] += 0.0002 * pause;
    planetsOrbit[7] += 0.0006 * pause;
    planetsOrbit[8] += 0.0011 * pause;
    planetsRot[0] += 0.0053 * pause;
    planetsRot[1] += 0.0062 * pause;
    planetsRot[2] += 0.0055 * pause;
    planetsRot[3] += 0.0057 * pause;
    planetsRot[4] += 0.0059 * pause;
    planetsRot[5] += 0.0061 * pause;
    planetsRot[6] += 0.0055 * pause;
    planetsRot[7] += 0.0063 * pause;
    planetsRot[8] += 0.0055;

    //keeps from overflowing. not sure if I have to have it but keeping it just to be safe.
    for (GLfloat i : planetsOrbit) {
        if (i > 360) {
           i -=  360;
        }
    }
    for (GLfloat i : planetsRot) {
        if (i > 360) {
            i -= 360;
        }
    }
}

// Function to draw the textured cube
void display() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity();

    //update camera
    glRotatef(activeCam[cam].pitch, 1, 0, 0);
    glRotatef(activeCam[cam].yaw, 0.0f, 1.0f, 0.0f);
    glTranslatef(-activeCam[cam].x, -activeCam[cam].y, -activeCam[cam].z);


    // Update light position
    glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);

    //update cube
    maps[activeMap].updateCube(activeCam[0].x, activeCam[0].y, activeCam[0].z);
    maps[activeMap].loadCube();
   
    //update planets rotation
    updateRotations();
  

    //draw sun
    glPushMatrix();
    glEnable(GL_TEXTURE_2D); //enable texture
    glEnable(GL_COLOR_MATERIAL); //enable color for texturues
    glMaterialfv(GL_FRONT, GL_EMISSION, sunEmission); //adds emission souruce to sun.
    gluQuadricDrawStyle(pSun, GLU_FILL); //draw style for quadratic(our spheres). fill, lines, or points
    sun.bind(); //binds the sun texture.
    glColor3f(lightDiffuse[0], lightDiffuse[1], lightDiffuse[2]); //set to light color
    gluQuadricTexture(pSun, GL_TRUE); //allows for texturing of the quadric
    gluQuadricNormals(pSun, GLU_SMOOTH);
    glRotatef(planetsRot[0], 0, 1, 0); //rotate the sun at point 0,0,0 about y axis.
    gluSphere(pSun, 4.0, 32, 16); //draw sun.
   


    glEnable(GL_LIGHTING); //trun on lighting after drawing sun to keep it the same color as the light source.
    glEnable(GL_LIGHT0);
    glEnable(GL_LIGHT1);

    //material settings for planets.
    glMaterialfv(GL_FRONT, GL_EMISSION, matEmission);
    glEnable(GL_COLOR_MATERIAL);

    //draw mercury
    glPushMatrix();
    glRotatef(planetsOrbit[0], 0, 1, 0);
    glColor3f(0.8, 0.8, 0.8);
    gluQuadricDrawStyle(pMercury, GLU_FILL);
    mercury.bind();
    gluQuadricTexture(pMercury, GL_TRUE);
    gluQuadricNormals(pMercury, GLU_SMOOTH);
    glTranslatef(5, 0, 5);
    glRotatef(planetsRot[1], 0, 1, 0);
    gluSphere(pMercury, .5, 32, 16);
    glPopMatrix();

    //draw venus
    glPushMatrix();
    glRotatef(planetsOrbit[1], 0, 1, 0);
    glColor3f(0.8, 0.8, 0.8);
    gluQuadricDrawStyle(pVenus, GLU_FILL);
    venus.bind();
    gluQuadricTexture(pVenus, GL_TRUE);
    gluQuadricNormals(pVenus, GLU_SMOOTH);
    glTranslatef(10, 0, 10);
    glRotatef(planetsRot[2], 0, 1, 0);
    gluSphere(pVenus, 0.9, 32, 16);
    glPopMatrix();

    //draw earth
    glPushMatrix();
    glRotatef(planetsOrbit[2], 0, 1, 0);
    glColor3f(0.8, 0.8, 0.8);
    gluQuadricDrawStyle(pEarth, GLU_FILL);
    earth.bind();
    gluQuadricTexture(pEarth, GL_TRUE);
    gluQuadricNormals(pEarth, GLU_SMOOTH);
    glTranslatef(15, 0, 15);
    glRotatef(planetsRot[3], 0, 1, 0);
    glRotatef(-90, 1, 0, 0); //rotate texturue to be right side up.
    gluSphere(pEarth, 1.0, 32, 16);
  

    //draw moon
    glPushMatrix();
   
   // glRotatef(planetsOrbit[2], 0, 1, 0);
    glColor3f(1.0, 1.0, 1.0);
    gluQuadricDrawStyle(pMoon, GLU_FILL);
    moon.bind();
    gluQuadricTexture(pMoon, GL_TRUE);
    gluQuadricNormals(pMoon, GLU_SMOOTH); 
    glTranslatef(2*cos(planetsOrbit[2]), 2 * sin(planetsOrbit[2]), 0.0);          // translet to simulate orbit
    gluSphere(pMoon, 0.20, 24, 10);
    glPopMatrix();
    glPopMatrix();


    //draw mars
    glPushMatrix();
    glRotatef(planetsOrbit[3], 0, 1, 0);
    glColor3f(0.8, 0.8, 0.8);
    gluQuadricDrawStyle(pMars, GLU_FILL);
    mars.bind();
    gluQuadricTexture(pMars, GL_TRUE);
    gluQuadricNormals(pMars, GLU_SMOOTH);
    glTranslatef(20, 0, 20);
    glRotatef(planetsRot[4], 0, 1, 0);
    glRotatef(-90, 1, 0, 0); //rotate texturue to be right side up.
    gluSphere(pMars, 0.7, 32, 16);
    glPopMatrix();

    //draw jupiter
    glPushMatrix();
    glRotatef(planetsOrbit[4], 0, 1, 0);
    glColor3f(0.8, 0.8, 0.8);
    gluQuadricDrawStyle(pJupiter, GLU_FILL);
    jupiter.bind();
    gluQuadricTexture(pJupiter, GL_TRUE);
    gluQuadricNormals(pJupiter, GLU_SMOOTH);
    glTranslatef(30, 0, 30);
    glRotatef(planetsRot[5], 0, 1, 0);
    glRotatef(-90, 1, 0, 0); //rotate texturue to be right side up.
    gluSphere(pJupiter, 2, 32, 16);
    glPopMatrix();

    
   
    //draw satern
    glPushMatrix();
    glRotatef(planetsOrbit[5], 0, 1, 0);
    glColor3f(0.6, 0.6, 0.6);
    gluQuadricDrawStyle(pSaturn, GLU_FILL);
    saturn.bind();
    glRotatef(planetsOrbit[5], 0, 1, 0);
    gluQuadricTexture(pSaturn, GL_TRUE);
    gluQuadricNormals(pSaturn, GLU_SMOOTH);

    glTranslatef(40, 0, 40);
    glRotatef(planetsRot[6], 0, 1, 0);
    glRotatef(-90, 1, 0, 0); //rotate texturue to be right side up.
    gluSphere(pSaturn, 1.75, 32, 16);
    
    // draw rings
    glColor3f(1, 1, 1);
    gluQuadricDrawStyle(disk, GLU_FILL);
    rings.bind();
    glRotatef(-40, 1, 0, 0);
    gluQuadricTexture(disk, GL_TRUE);
    gluQuadricNormals(disk, GLU_FLAT);

    gluDisk(disk, 2.5, 3.25, 32, 20);

    glPopMatrix();
    

    //draw uranus
    glPushMatrix();
    glRotatef(planetsOrbit[6], 0, 1, 0);
    glColor3f(0.8, 0.8, 0.8);
    gluQuadricDrawStyle(pUranus, GLU_FILL);
    uranus.bind();
    gluQuadricTexture(pUranus, GL_TRUE);
    gluQuadricNormals(pUranus, GLU_SMOOTH);
    glTranslatef(50, 0, 50);
    glRotatef(planetsRot[7], 0, 1, 0);
    glRotatef(-90, 1, 0, 0); //rotate texturue to be right side up.
    gluSphere(pUranus, 1.5, 32, 16);
    glPopMatrix();

    //draw neptune
    glPushMatrix();
    glRotatef(planetsOrbit[7], 0, 1, 0);
    glColor3f(0.8, 0.8, 0.8);
    gluQuadricDrawStyle(pNeptune, GLU_FILL);
    neptune.bind();
    gluQuadricTexture(pNeptune, GL_TRUE);
    gluQuadricNormals(pNeptune, GLU_SMOOTH);
    glTranslatef(65, 0, 65);
    glRotatef(planetsRot[8], 0, 1, 0);
    glRotatef(-90, 1, 0, 0); //rotate texturue to be right side up.
    gluSphere(pNeptune, 1.25, 32, 16);
    glPopMatrix();


    glPopMatrix();

    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHT0);
    glDisable(GL_LIGHT1);
    glDisable(GL_LIGHTING);
    glFlush();
    glutSwapBuffers();
    glutPostRedisplay();
}

// Reshape function
void reshape(int w, int h) {
    width = w;
    height = h;
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(45.0, (float)w / (float)h, 1.0, 5000.0);
    glMatrixMode(GL_MODELVIEW);
    
}



void keyboard(unsigned char key, int x, int y) {
    switch (key) {
    case '1':
    case '2':
        
        activeMap = key - '1';
        maps[activeMap].textInit();
        break;
    case '\t':
        cam = 1-cam; //swap between two cams
        break;
    case 32:
        pause = 1 - pause; //pause rotations
        break;
    }


    activeCam[cam].keypressCam(key);
}
void mousePassiveMotion(int x, int y) {
    activeCam[cam].width = width;
    activeCam[cam].height = height;
    activeCam[cam].mouseCam(x, y);
}

void menu(int option) {
    //change sun color.
    switch (option) {
    case 0:
        lightDiffuse[0] = 0.99;
        lightDiffuse[1] = 0.9529;
        lightDiffuse[2] = 0.85;
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
        break;
    case 1:
        lightDiffuse[0] = 1.0;
        lightDiffuse[1] = 0.0;
        lightDiffuse[2] = 0.812;
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
        break;
    case 2:
        lightDiffuse[0] = 0.0;
        lightDiffuse[1] = 0.0;
        lightDiffuse[2] = 1.0;
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
        break;
    case 3:
        lightDiffuse[0] = 0.;
        lightDiffuse[1] = 1.0;
        lightDiffuse[2] = 0.0;
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
        break;
    case 4:
        lightDiffuse[0] = (float)(rand()) / (float)(RAND_MAX);
        lightDiffuse[1] = (float)(rand()) / (float)(RAND_MAX);
        lightDiffuse[2] = (float)(rand()) / (float)(RAND_MAX);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
        break;
    }
    glutPostRedisplay();
}



// Main function
int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
    glutInitWindowSize(width, height);
    glutCreateWindow("Textured Cube");
    init();
    glutDisplayFunc(display);
    glutReshapeFunc(reshape);
    glutKeyboardFunc(keyboard);
    glutPassiveMotionFunc(mousePassiveMotion);

    //menu
    glutCreateMenu(menu);
    glutAddMenuEntry("Natural Light", 0);
    glutAddMenuEntry("pink Light", 1);
    glutAddMenuEntry("Blue Light", 2);
    glutAddMenuEntry("Green Light", 3);
    glutAddMenuEntry("Random color", 4);
    glutAttachMenu(GLUT_RIGHT_BUTTON);


    glutIdleFunc(display);
    glutMainLoop();
    return 0;
}