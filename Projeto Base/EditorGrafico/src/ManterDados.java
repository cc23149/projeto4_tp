import java.io.FileNotFoundException;
import java.io.IOException;

public interface ManterDados<TipoGenerico>
{
    // os métodos definidos abaixo são necessários em
    // qualquer classe de manutenção de vetor de dados
    void lerDados(String nomeArquivo) throws FileNotFoundException, Exception;
    void gravarDados(String nomeArquivo) throws IOException;
    void incluirNoFinal(TipoGenerico novoDado);
    void incluirEm(TipoGenerico novoDado, int posicaoDeInclusao) throws Exception;
    void excluir(int posicaoDeExclusao);        // remove estudante dessa posição
    TipoGenerico valorDe(int posicaoDeAcesso);     // retorna estudante do índice posicaoDeAcesso
    void alterar(TipoGenerico novosDados, int posicaoDeAlteracao);
    boolean existe(TipoGenerico procurado);    // informa se procurado está ou não no vetor
    void ordenar();                 // ordena o vetor com os dados
    boolean estaVazio();            // informa se vetor com os dados está ou não vazio
    Situacao getSituacao();         // retorna situação atual do programa
    void setSituacao(Situacao novaSituacao);    // muda situação do programa
    int getTamanho();           // retorna tamanho físico do vetor com os dados

    // métodos necessários para navegação dentro do vetor com os dados
    boolean estaNoInicio();
    boolean estaNoFim();
    void irAoInicio();
    void irAoFim();
    void irAoAnterior();
    void irAoProximo();
    int getOnde();  // retorna o índice em que a pesquisa binária parou
}
