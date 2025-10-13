import java.awt.*;

public class Circulo extends Ponto
{
    // o ponto central do Circulo está declarado implicitamente
    // pelo mecanismo de herança, ou seja, Circulo herda de Ponto
    // tudo que foi codificado na classe Ponto
    // Num circulo, esse ponto base será o ponto central da figura

    protected int raio;

    public Circulo(Ponto centro, int raio, Color cor)
    {
        super(centro.x, centro.y, cor); // construtor de Ponto (superior, ancestral)
        this.raio = raio;
    }

    public Circulo(int xCentro, int yCentro, int raio, Color cor)
    {
        super(xCentro, yCentro, cor);
        this.raio = raio;
    }

    public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(getX()-raio, getY()-raio, // centro - raio
                2*raio,2*raio); // centro + raio
    }

    public String formatoDeArquivo() {
        //       c		xc	     yc	corR	corG	corB 	raio
        return "c;" + x + ";" + y + ";" +
                cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue()+
                ";"+raio;
    }

}
