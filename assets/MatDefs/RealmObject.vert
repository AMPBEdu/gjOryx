uniform mat4 g_WorldViewProjectionMatrix;
uniform bool m_Flip;
uniform int m_Row;
uniform int m_Column;

attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;

void main() {
	gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
	
	texCoord = inTexCoord;
	
}
