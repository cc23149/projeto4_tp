import java.awt.*;

public class Polilinha extends Ponto
{
    // Herdamos x, y e cor do primeiro ponto (inicial)
    private Ponto[] pontos; // vetor com os pontos da polilinha
    private int qtdPontos;  // quantidade de pontos armazenados

    public Polilinha(int capacidade, Color cor)
    {
        super(0, 0, cor); // inicializa com valores neutros
        pontos = new Ponto[capacidade];
        qtdPontos = 0;
    }

    public void adicionarPonto(Ponto p)
    {
        if (qtdPontos < pontos.length)
            pontos[qtdPontos++] = p;
    }

    @Override
    public void desenhar(Color corDesenho, Graphics g)
    {
        g.setColor(corDesenho);
        for (int i = 0; i < qtdPontos - 1; i++)
        {
            Ponto p1 = pontos[i];
            Ponto p2 = pontos[i + 1];
            g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    @Override
    public String formatoDeArquivo()
    {
        // Exemplo: pl; corR; corG; corB; n; x1; y1; x2; y2; ... xn; yn
        StringBuilder sb = new StringBuilder();
        sb.append("pl;")
                .append(cor.getRed()).append(";")
                .append(cor.getGreen()).append(";")
                .append(cor.getBlue()).append(";")
                .append(qtdPontos);

        for (int i = 0; i < qtdPontos; i++)
            sb.append(";").append(pontos[i].getX()).append(";").append(pontos[i].getY());

        return sb.toString();
    }

    public int getQtdPontos() { return qtdPontos; }
    public Ponto getPonto(int i) { return pontos[i]; }
}
