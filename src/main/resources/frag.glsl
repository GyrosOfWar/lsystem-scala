uniform float grayscale;

void main()
{
    if(grayscale == 1.0f) {
        gl_FragColor.r = gl_Color.r * 0.299f + gl_Color.g * 0.587f + gl_Color.b * 0.114f;
        gl_FragColor.g = gl_Color.r * 0.299f + gl_Color.g * 0.587f + gl_Color.b * 0.114f;
        gl_FragColor.b = gl_Color.r * 0.299f + gl_Color.g * 0.587f + gl_Color.b * 0.114f;
        gl_FragColor.a = 1.0f;
    }
    else {
        gl_FragColor = gl_Color;
    }
}