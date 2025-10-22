import java.awt.*;
import java.util.ArrayList;

public class Polilinha extends Ponto
{
    private ArrayList<Ponto> pontos; // lista de pontos da polilinha
    private int quantidade;          // quantidade de pontos (para salvar no arquivo)

    public Polilinha(int quantidade, Color cor)
    {
        super(0, 0, cor);
        this.quantidade = quantidade;
        pontos = new ArrayList<Ponto>();
    }

    public void adicionarPonto(Ponto p)
    {
        pontos.add(p);
        quantidade = pontos.size();
    }

    public int getQuantidade()
    {
        return quantidade;
    }



    public int getQtdPontos()
    {
        return pontos.size();
    }

    public ArrayList<Ponto> getPontos()
    {
        return pontos;
    }

    public void desenhar(Color corDesenho, Graphics g)
    {
        g.setColor(corDesenho);

        if (pontos.size() > 1)
        {
            for (int i = 0; i < pontos.size() - 1; i++)
            {
                Ponto p1 = pontos.get(i);
                Ponto p2 = pontos.get(i + 1);
                g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }
        else if (pontos.size() == 1)
        {
            // desenha um ponto único (caso tenha só um)
            Ponto p = pontos.get(0);
            g.drawLine(p.getX(), p.getY(), p.getX(), p.getY());
        }
    }

    public String formatoDeArquivo()
    {
        // Formato: pl;x1;y1;corR;corG;corB;qtd;x2;y2;x3;y3;...
        if (pontos.size() == 0) return ""; // segurança

        StringBuilder sb = new StringBuilder();
        // primeiro ponto (x1,y1)
        sb.append("pl;");
        sb.append(pontos.get(0).getX()).append(";").append(pontos.get(0).getY()).append(";");
        sb.append(cor.getRed()).append(";").append(cor.getGreen()).append(";").append(cor.getBlue()).append(";");
        sb.append(pontos.size());

        for (int i = 1; i < pontos.size(); i++)
        {
            Ponto p = pontos.get(i);
            sb.append(";").append(p.getX()).append(";").append(p.getY());
        }

        return sb.toString();
    }
}
