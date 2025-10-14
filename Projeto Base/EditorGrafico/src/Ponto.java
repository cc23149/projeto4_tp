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

   //===========================Métodos para o projeto=============================

    // Sobrecarga para desenhar usando a própria cor do ponto
    public void desenhar(Graphics g) {
        desenhar(this.cor, g);
    }

    // Métodos auxiliares (iguais ao modelo do professor)
    public String transformaString(int valor, int quantasPosicoes) {
        String cadeia = String.valueOf(valor);
        while (cadeia.length() < quantasPosicoes)
            cadeia = "0" + cadeia;
        return cadeia.substring(0, quantasPosicoes);
    }

    public String transformaString(String valor, int quantasPosicoes) {
        String cadeia = valor;
        while (cadeia.length() < quantasPosicoes)
            cadeia = cadeia + " ";
        return cadeia.substring(0, quantasPosicoes);
    }

    // Método toString padronizado para gravação em arquivo (colunas fixas)
    @Override
    public String toString() {
        return transformaString("p",5) +
                transformaString(getX(),5) +
                transformaString(getY(),5) +
                transformaString(getCor().getRed(),5) +
                transformaString(getCor().getGreen(),5) +
                transformaString(getCor().getBlue(),5);
    }

    // Método utilitário para mover o ponto (usado em figuras com deslocamento)
    public void mover(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    // Método para comparar igualdade geométrica (útil para seleção de figuras)
    public boolean mesmoLocal(Ponto outro) {
        return this.x == outro.x && this.y == outro.y;
    }
}
