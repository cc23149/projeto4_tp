import java.io.*;

public class ManterFiguras implements ManterDados<Ponto>
{
    // Declaração do vetor de figuras e demais atributos
    private Ponto[] dados;              // vetor onde instâncias de figuras serão armazenadas
    private int quantosDados;           // tamanho lógico do vetor dados
    private Situacao situacao;          // situação atual do programa
    private int posicaoAtual;           // qual índice estamos visitando
    private int ondeEsta;               // indica onde no vetor está o dado procurado ou onde deveria estar

    public ManterFiguras(int tamanhoFisico)
    {
        dados = new Ponto[tamanhoFisico];
        quantosDados = 0;
        situacao = Situacao.navegando;
        posicaoAtual = -1;  // não visita nenhuma posição ainda
    }

    @Override
    public void lerDados(String nomeArquivo) throws FileNotFoundException, Exception
    {
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(nomeArquivo))) {
            quantosDados = 0; // reset
            while (sc.hasNextLine()) {
                String linha = sc.nextLine().trim();
                if (linha.isEmpty()) continue;
                String[] campos = linha.split(";");
                String tipo = campos[0].trim();
                int xBase = Integer.parseInt(campos[1].trim());
                int yBase = Integer.parseInt(campos[2].trim());
                int corR  = Integer.parseInt(campos[3].trim());
                int corG  = Integer.parseInt(campos[4].trim());
                int corB  = Integer.parseInt(campos[5].trim());
                java.awt.Color cor = new java.awt.Color(corR, corG, corB);

                switch (tipo) {
                    case "p":
                        //Ponto
                        incluirNoFinal(new Ponto(xBase, yBase, cor));
                        break;
                        //Linha
                    case "l":
                        int xFinal = Integer.parseInt(campos[6].trim());
                        int yFinal = Integer.parseInt(campos[7].trim());
                        incluirNoFinal(new Linha(xBase, yBase, xFinal, yFinal, cor));
                        break;
                        //circulo
                    case "c":
                        int raio = Integer.parseInt(campos[6].trim());
                        incluirNoFinal(new Circulo(xBase, yBase, raio, cor));
                        break;
                        //Oval/Elipse
                    case "o":
                        int raioA = Integer.parseInt(campos[6].trim());
                        int raioB = Integer.parseInt(campos[7].trim());
                        incluirNoFinal(new Oval(xBase, yBase, raioA, raioB, cor));
                        break;
                        //retangulo
                    case "r":
                        int largura = Integer.parseInt(campos[6].trim());
                        int altura = Integer.parseInt(campos[7].trim());
                        incluirNoFinal(new Retangulo(xBase, yBase, largura, altura, cor));
                        break;
                    //polilinha
                    case "pl":
                        //pl; corR; corG; corB; n; x1; y1; x2; y2; ... xn; yn
                        java.awt.Color corPoli = new java.awt.Color(corR, corG, corB);
                        int qtd = Integer.parseInt(campos[6].trim());
                        Polilinha pl = new Polilinha(qtd, corPoli);
                        int pos = 7;
                        for (int i = 0; i < qtd; i++) {
                            int x = Integer.parseInt(campos[pos++].trim());
                            int y = Integer.parseInt(campos[pos++].trim());
                            pl.adicionarPonto(new Ponto(x, y, corPoli));
                        }
                        incluirNoFinal(pl);
                        break;

                    default:
                        System.out.println("Aviso: tipo desconhecido encontrado no arquivo: " + tipo);
                }
            }
        }
    }

    @Override
    public void gravarDados(String nomeDeArquivo) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(nomeDeArquivo));
        for (int i = 0; i < quantosDados; i++) {
            pw.println(dados[i].formatoDeArquivo());
        }
        pw.close();
    }

    @Override
    public void incluirNoFinal(Ponto novoDado)
    {
        if (quantosDados >= dados.length)
            expandirVetor();        // aumenta tamanho físico do vetor

        dados[quantosDados++] = novoDado;
    }

    public void expandirVetor()
    {
        int novoTam = dados.length * 2;
        Ponto[] novo = new Ponto[novoTam];
        for (int i = 0; i < quantosDados; i++)
            novo[i] = dados[i];
        dados = novo;
    }

    @Override
    public void incluirEm(Ponto novoDado, int posicaoDeInclusao) throws Exception
    {
        if (quantosDados >= dados.length)
            expandirVetor();        // aumenta tamanho físico do vetor

        if (posicaoDeInclusao < 0 || posicaoDeInclusao > quantosDados)
            throw new Exception("Posição inválida para inclusão!");

        for (int indice = quantosDados; indice > posicaoDeInclusao; indice--)
            dados[indice] = dados[indice-1];

        dados[posicaoDeInclusao] = novoDado;
        quantosDados++;
    }

    @Override
    public void excluir(int posicaoDeExclusao) throws IndexOutOfBoundsException
    {
        if (posicaoDeExclusao < 0 || posicaoDeExclusao >= quantosDados)
            throw new IndexOutOfBoundsException("Posição inválida para acesso aos dados!");

        quantosDados--;
        for (int indice = posicaoDeExclusao; indice < quantosDados; indice++)
            dados[indice] = dados[indice+1];
        dados[quantosDados] = null;
    }

    @Override
    public Ponto valorDe(int posicaoDeAcesso) throws IndexOutOfBoundsException
    {
        if (posicaoDeAcesso >= 0 && posicaoDeAcesso < quantosDados)
            return dados[posicaoDeAcesso];

        throw new IndexOutOfBoundsException("Índice inválido!");
    }

    @Override
    public void alterar(Ponto novosDados, int posicaoDeAlteracao) throws IndexOutOfBoundsException
    {
        if (posicaoDeAlteracao >= 0 && posicaoDeAlteracao < quantosDados)
            dados[posicaoDeAlteracao] = novosDados;
        else
            throw new IndexOutOfBoundsException("Índice inválido para alteração!");
    }

    @Override
    public boolean existe(Ponto procurado)
    {
        for (ondeEsta = 0; ondeEsta < quantosDados; ondeEsta++)
            if (dados[ondeEsta].getX() == procurado.getX() &&
                    dados[ondeEsta].getY() == procurado.getY() &&
                    dados[ondeEsta].getClass() == procurado.getClass())
                return true;
        return false;
    }

    public int getOnde() { return ondeEsta; }
    public int getPosicaoAtual() { return posicaoAtual; }
    @Override public void ordenar() {}
    @Override public boolean estaVazio() { return quantosDados == 0; }
    @Override public Situacao getSituacao() { return situacao; }
    @Override public void setSituacao(Situacao novaSituacao) { situacao = novaSituacao; }
    @Override public int getTamanho() { return quantosDados; }
    @Override public boolean estaNoInicio() { return posicaoAtual == 0; }
    @Override public boolean estaNoFim() { return posicaoAtual == quantosDados-1; }
    @Override public void irAoInicio() { posicaoAtual = 0; }
    @Override public void irAoFim() { posicaoAtual = quantosDados - 1; }
    @Override public void irAoAnterior() { if (posicaoAtual > 0) posicaoAtual--; }
    @Override public void irAoProximo() { if (posicaoAtual < quantosDados - 1) posicaoAtual++; }



}
