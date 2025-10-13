import java.io.*;

public class ManterFiguras implements ManterDados<Ponto>
{
    // Declaração do vetor de Estudantes e demais atributos
    // necessários para manutenção de vetores de Estudante

    private Ponto[] dados;  // vetor onde instâncias de figuras serão armazenadas
    private int quantosDados;   // tamanho lógico do vetor dados
    private Situacao situacao;  // situação atual do programa
    private int posicaoAtual;   // qual índice estamos visitando
    private int ondeEsta;       // indica onde no vetor está o dado procurado ou onde deveria estar

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
        // a leitura será feita pela aplicação, que detectará qual o tipo
        // específico de cada figura geomátrica lida, e instanciará uma
        // figura do tipo específico para ser lida do arquivo (ponto, linha,
        // circulo, oval, etc.)
    }

    @Override
    // Uma maneira alternativa de gravar dados em arquivo texto é por meio da
    // classe PrintWriter, que usa como parâmetro um FileWriter
    // PrintWriter possui o método println que pula de linha após cada escrita.
    // No caso abaixo, o método formatoDeArquivo deve gerar cada linha de dados
    // separando os campos com ";", sem necessidade de manter tamanho fixo para
    // cada campo e registro como vínhamos fazendo com arquivos texto (de tamanho fixo)
    // desde o 1o semestre
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
        // pesquisa sequencial, pois as figuras geométricas não estarão
        // ordenadas
        for (ondeEsta = 0; ondeEsta < quantosDados; ondeEsta++)
            if (dados[ondeEsta].getX() == procurado.getX() &&  // mesmo X
                dados[ondeEsta].getY() == procurado.getY() &&  // mesmo Y
                dados[ondeEsta].getClass() == procurado.getClass()) // mesma classe (mesma figura)
                return true;    // ondeEsta indexa a figura encontrada

        return false;
    }

    public int getOnde() {
        return ondeEsta;
    }

    public int getPosicaoAtual() {
        return posicaoAtual;
    }

    @Override
    public void ordenar() {
        // não ordena nada no caso desta aplicação
    }

    @Override
    public boolean estaVazio() {
        return quantosDados == 0;
    }

    @Override
    public Situacao getSituacao() {
        return situacao;
    }

    @Override
    public void setSituacao(Situacao novaSituacao) {
        situacao = novaSituacao;
    }

    @Override
    public int getTamanho() {
        return quantosDados;
    }

    @Override
    public boolean estaNoInicio() {
        return posicaoAtual == 0;
    }

    @Override
    public boolean estaNoFim() {
        return posicaoAtual == quantosDados-1;
    }

    @Override
    public void irAoInicio() {
        posicaoAtual = 0;
    }

    @Override
    public void irAoFim() {
        posicaoAtual = quantosDados - 1;
    }

    @Override
    public void irAoAnterior() {
        if (posicaoAtual > 0)
            posicaoAtual--;
    }

    @Override
    public void irAoProximo() {
        if (posicaoAtual < quantosDados - 1)
            posicaoAtual++;
    }
}
