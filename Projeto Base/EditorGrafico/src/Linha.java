import java.awt.*;

public class Linha extends Ponto
{
    // o ponto inicial da Linha está declarado implicitamente
    // pelo mecanismo de herança, ou seja, Linha herda de Ponto
    // tudo que foi codificado na classe Ponto
    protected Ponto pontoFinal;

    public Linha(Ponto inicio, Ponto fim, Color cor)
    {
        // super() é o construtor da classe ancestral de Linha (que é a classe Ponto)
        // abaixo estamos chamando o construtor de Ponto para que o ponto inicial da
        // Linha tenha seus valores atribuidos
        super(inicio.getX(), inicio.getY(), cor);
        pontoFinal = fim;
    }

    public Linha(int x1, int y1, int x2, int y2, Color cor)
    {
        super(x1, y1, cor);                     // instancia o 1o ponto
        pontoFinal = new Ponto(x2, y2, cor);    // precisa instanciar o 2o ponto
    }

    public void desenhar(Color corDesenho, Graphics g)
    {
        g.setColor(corDesenho);
        g.drawLine(super.x, super.y,            // ponto inicial da linha
                pontoFinal.x, pontoFinal.y); // ponto final da linha
    }

    public String formatoDeArquivo() {
        //   l		x1	y1	corR	corG	corB 	x2	y2
        return "l;" + x + ";" + y + ";" +
                cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue()+
                ";"+pontoFinal.x+";"+pontoFinal.y;
    }

    // ============================================================
    // 🔹 MÉTODOS ADICIONADOS PARA APRIMORAR A CLASSE
    // ============================================================

    // Getter e Setter do ponto final
    public Ponto getPontoFinal() {
        return pontoFinal;
    }

    public void setPontoFinal(Ponto p) {
        this.pontoFinal = p;
    }

    // Métodos auxiliares de formatação (iguais ao modelo do professor)
    private String transformaString(int valor, int posicoes) {
        String s = String.valueOf(valor);
        while (s.length() < posicoes)
            s = "0" + s;
        return s.substring(0, posicoes);
    }

    private String transformaString(String valor, int posicoes) {
        String s = valor;
        while (s.length() < posicoes)
            s = s + " ";
        return s.substring(0, posicoes);
    }

    // Método toString padronizado para gravação em arquivo (colunas fixas)
    @Override
    public String toString() {
        return transformaString("l",5) +
                transformaString(getX(),5) +
                transformaString(getY(),5) +
                transformaString(getCor().getRed(),5) +
                transformaString(getCor().getGreen(),5) +
                transformaString(getCor().getBlue(),5) +
                transformaString(pontoFinal.getX(),5) +
                transformaString(pontoFinal.getY(),5);
    }

    // Método para mover toda a linha (ambos os pontos)
    public void mover(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
        this.pontoFinal.mover(deltaX, deltaY);
    }

    // Método que calcula o comprimento da linha (útil para validações)
    public double comprimento() {
        int dx = pontoFinal.getX() - x;
        int dy = pontoFinal.getY() - y;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
