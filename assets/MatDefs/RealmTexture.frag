uniform sampler2D m_ColorMap;

varying vec2 texCoord;

void main() {
	vec4 col = texture2D(m_ColorMap, texCoord);
	gl_FragColor = col;
}
