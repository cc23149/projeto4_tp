import java.awt.*; // Abstract Windowing Toolkit
public class Ponto
{
    // protected permite que o atributo seja usado em classes descendentes desta
    protected int x, y;
    protected Color cor;

    public Ponto()
    {
        x = y = 0;
        cor = Color.white;
    }

    public Ponto(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        cor = c;
    }

    public int getX() { return x; }
    public void setX(int novoValor) { x = novoValor; }
    public int getY() { return y; }
    public void setY(int novoValor) { y = novoValor; }
    public Color getCor() { return cor; }
    public void setCor(Color novoValor) { cor = novoValor; }

    public void desenhar(Color cor, Graphics g) {
        g.setColor(cor);
        g.drawLine(getX(),getY(),getX(),getY());
    }

    public String formatoDeArquivo() {
        //       p		x	        y	         corR	corG	corB
        return "p;" + x + ";" + y + ";" +
                cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue();
    }
}
