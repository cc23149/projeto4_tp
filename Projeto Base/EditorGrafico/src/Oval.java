import java.awt.*;

public class Oval extends Ponto
{
    // ponto central e cor foram herdados de Ponto
    int raioA, raioB;

    public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(getX()-raioA, getY()-raioB, // centro - raio
                2*raioA,2*raioB); // centro + raio
    }

    public Oval()
    {
        super();
        setRaioA(0);
        setRaioB(0);
        setCor(Color.black);
    }

    public Oval(int xCentro, int yCentro, int novoRaioA,
                int novoRaioB, Color novaCor)
    {
        super(xCentro, yCentro, novaCor); // construtor de Ponto(x,y)
        setRaioA(novoRaioA);
        setRaioB(novoRaioB);
    }

    public Oval(Ponto centro, int novoRaioA,
                int novoRaioB, Color novaCor)
    {
        super(centro.x, centro.y, novaCor); // construtor de Ponto(x,y)
        setRaioA(novoRaioA);
        setRaioB(novoRaioB);
    }

    public void setRaioA(int novoRaio) {
        raioA = novoRaio;
    }
    public void setRaioB(int novoRaio) {
        raioB = novoRaio;
    }

    public String formatoDeArquivo() {
        //   o		xc	yc	corR	corG	corB 	raioA	raioB
        return "o;" + x + ";" + y + ";" +
                cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue()+
                ";"+raioA+";"+raioB;
    }
}
