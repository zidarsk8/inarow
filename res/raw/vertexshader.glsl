uniform mat4 uMVPMatrix;    // A constant representing the combined model/view/projection matrix.
uniform vec3 u_LightPos;     // The position of the light in eye space.

attribute vec4 a_Position;   // Per-vertex position information we will pass in.
attribute vec4 a_Color;      // Per-vertex color information we will pass in.
attribute vec3 a_Normal;     // Per-vertex normal information we will pass in.

varying vec4 v_Color;        // This will be passed into the fragment shader.

void main()                  // The entry point for our vertex shader.
{                            
    v_Color = a_Color;                                  
    gl_Position = uMVPMatrix * a_Position;         
}  