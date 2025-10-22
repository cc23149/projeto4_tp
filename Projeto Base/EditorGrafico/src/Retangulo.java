import java.awt.*;

public class Retangulo extends Ponto
{
    // ponto inicial herdado (x, y) e cor também
    private int largura, altura;

    public Retangulo(int x, int y, int largura, int altura, Color cor)
    {
        super(x, y, cor);
        this.largura = largura;
        this.altura = altura;
    }

    public Retangulo(Ponto cantoSuperiorEsquerdo, int largura, int altura, Color cor)
    {
        super(cantoSuperiorEsquerdo.getX(), cantoSuperiorEsquerdo.getY(), cor);
        this.largura = largura;
        this.altura = altura;
    }

    public void desenhar(Color corDesenho, Graphics g)
    {
        g.setColor(corDesenho);
        g.drawRect(getX(), getY(), largura, altura);
    }

    public String formatoDeArquivo()
    {
        //   r      x     y     corR  corG  corB  largura  altura
        return "r;" + x + ";" + y + ";" +
                cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue() +
                ";" + largura + ";" + altura;
    }
}
