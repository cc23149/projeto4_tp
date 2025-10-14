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
        this.raio = Math.max(0, raio); // garante que o raio não seja negativo
    }

    public Circulo(int xCentro, int yCentro, int raio, Color cor)
    {
        super(xCentro, yCentro, cor);
        this.raio = Math.max(0, raio);
    }

    public void desenhar(Color corDesenho, Graphics g) {
        // Usa a cor do objeto caso nenhuma seja passada
        if (corDesenho == null)
            corDesenho = this.cor;

        g.setColor(corDesenho);
        g.drawOval(getX() - raio, getY() - raio, // centro - raio
                2 * raio, 2 * raio); // centro + raio
    }

    public String formatoDeArquivo() {
        //       c		xc	     yc	corR	corG	corB 	raio
        return "c;" + x + ";" + y + ";" +
                cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue() +
                ";" + raio;
    }

 //====================Métodos para o projeto =========================

    // Getter e Setter para o raio
    public int getRaio() {
        return raio;
    }

    public void setRaio(int novoRaio) {
        if (novoRaio >= 0)
            this.raio = novoRaio;
    }

    // Método auxiliar (reutilizável em toString)
    private String transformaString(String valor, int posicoes) {
        String s = valor;
        while (s.length() < posicoes)
            s = s + " ";
        return s.substring(0, posicoes);
    }

    private String transformaString(int valor, int posicoes) {
        String s = String.valueOf(valor);
        while (s.length() < posicoes)
            s = "0" + s;
        return s.substring(0, posicoes);
    }

    // toString padronizado para gravação em arquivo (colunas fixas)
    @Override
    public String toString() {
        return transformaString("c", 5) +
                transformaString(getX(), 5) +
                transformaString(getY(), 5) +
                transformaString(getCor().getRed(), 5) +
                transformaString(getCor().getGreen(), 5) +
                transformaString(getCor().getBlue(), 5) +
                transformaString(getRaio(), 5);
    }
}
