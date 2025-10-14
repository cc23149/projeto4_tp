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

    // ============================================================
    // 🔹 MÉTODOS ADICIONADOS PARA APRIMORAR A CLASSE
    // ============================================================

    // Getters
    public int getRaioA() { return raioA; }
    public int getRaioB() { return raioB; }

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

    // toString padronizado para gravação no arquivo (colunas fixas)
    @Override
    public String toString() {
        return transformaString("o",5) +
                transformaString(getX(),5) +
                transformaString(getY(),5) +
                transformaString(getCor().getRed(),5) +
                transformaString(getCor().getGreen(),5) +
                transformaString(getCor().getBlue(),5) +
                transformaString(getRaioA(),5) +
                transformaString(getRaioB(),5);
    }

    // Método para mover o oval (usado em deslocamentos)
    public void mover(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    // Método utilitário para verificar se é um círculo (raios iguais)
    public boolean isCircular() {
        return raioA == raioB;
    }
}
