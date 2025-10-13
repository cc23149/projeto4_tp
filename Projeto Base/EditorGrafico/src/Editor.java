import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;

public class Editor extends JFrame
{
    static Color corAtual = Color.black;
    static boolean esperaPonto, esperaInicioLinha, esperaFimLinha;
    static private MeuJPanel pnlDesenho;
    private static ManterFiguras figuras;     // objeto de manutenção de vetor de figuras geométricas

    private static JLabel statusBar1, statusBar2;
    private static Ponto p1 = new Ponto();  // ponto inicial de linha, circulo, etc.

    private final JButton btnPonto, btnLinha, btnCirculo, btnElipse, btnCor, btnAbrir,
            btnSalvar,btnApagar, btnSair;

    private JPanel pnlBotoes;   // container dos botões

    static private JInternalFrame janelaFilha;

    public Editor()	// construtor de Editor que criará o JFrame, colocará seu
    {			          // título, estabelecerá um tamanho para o formulário e o
                    // exibirá
        super("Editor Gráfico");	      // cria o JFrame e coloca um título

        figuras = new ManterFiguras(100);      // cria objeto de manutenção de vetor de figuras geométricas

        Icon imgAbrir = new ImageIcon("botoes\\abrir.jpg");
        btnAbrir = new JButton("Abrir", imgAbrir);
        btnSalvar = new JButton("Salvar", new ImageIcon("botoes\\salvar.jpg"));
        btnPonto = new JButton("Ponto", new ImageIcon("botoes\\ponto.jpg"));
        btnLinha = new JButton("Linha", new ImageIcon("botoes\\linha.jpg"));
        btnCirculo = new JButton("Circulo", new ImageIcon("botoes\\circulo.jpg"));
        btnElipse = new JButton("Elipse", new ImageIcon("botoes\\elipse.jpg"));
        btnCor = new JButton("Cores", new ImageIcon("botoes\\cores.jpg"));
        btnApagar = new JButton("Apagar", new ImageIcon("botoes\\apagar.jpg"));
        btnSair = new JButton("Sair", new ImageIcon("botoes\\sair.jpg"));

        // cria o JPanel que armazenará os botões
        pnlBotoes = new JPanel();
        // cria o layout usado para dispor fisicamente os botões no JPanel
        FlowLayout flwBotoes = new FlowLayout();
        // informa que os componentes do pnlBotoes serão dispostos em forma livre
        pnlBotoes.setLayout(flwBotoes);

        // adiciona os controles visuais (botões) ao painel de botões, de cima
        // para baixo, da esquerda para direita.
        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnElipse);
        pnlBotoes.add(btnCor);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnSair);

        // associação de tratadores de eventos aos botões
        btnAbrir.addActionListener(new FazAbertura());
        btnSalvar.addActionListener(new FazSalvar());
        btnPonto.addActionListener(new DesenhaPonto());  // dá inicio ao fornecimento de um Ponto
        btnLinha.addActionListener(new DesenhaLinha());  // dá inicio ao fornecimento de uma Linha

        Container cntForm = getContentPane(); // acessa o painel de conteúdo do JFrame
        cntForm.setLayout(new BorderLayout());  // COnfigura layout do JFrame para Border
        cntForm.add(pnlBotoes , BorderLayout.NORTH);    // coloca JPanel no topo do Layout

        JDesktopPane panDesenho = new JDesktopPane();
        cntForm.add(panDesenho);

        // cria uma janela interna (janela filha) no modelo MDI
        janelaFilha = new JInternalFrame("Nenhum arquivo aberto", true, true, true, true);
        panDesenho.add(janelaFilha);

        // exibe o formulário
        setSize(700,500);			// dimensões do formulário em pixels
        setVisible(true);

        janelaFilha.setOpaque(true);
        // dimensiona a janela filha e a exibe
        janelaFilha.setSize(this.getWidth()/2, this.getHeight() / 2);
        janelaFilha.show();

        pnlDesenho = new MeuJPanel();
        Container cntFrame = janelaFilha.getContentPane();  // área de desenho da janela filha
        cntFrame.add(pnlDesenho);   // adiciona o JPanel à área de desenho da janela filha
    }

    public static void main(String[] args)
    {
        Editor aplicacao = new Editor();

        // ouvinte de eventos sobre janelas, implementando
        // o tratador de evento WindowClosing(), que trata
        // do que fazer quando a janela for fechada
        aplicacao.addWindowListener (
                new WindowAdapter() {   //  cria instância da interface
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                }
        );
    }

    public static void desenharObjetos(Graphics g)
    {
        pnlDesenho.paintComponent(g);
    }

    private void limparEsperas()
    {
        esperaPonto = false;
        esperaInicioLinha = false;
        esperaFimLinha = false;
    }

    private class FazAbertura implements ActionListener {
        public void actionPerformed(ActionEvent e)	// código executado no evento
        {
            JFileChooser arqEscolhido = new JFileChooser ();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = arqEscolhido.showOpenDialog(Editor.this);

            //código de verificação se um arquivo foi selecionado e obter seu nome
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File arquivo = arqEscolhido.getSelectedFile();
                System.out.println("Processando "+arquivo.getName());
                try {
                    Scanner arqFiguras = new Scanner(new File(arquivo.getName()));
                    try {

                        while (arqFiguras.hasNextLine())  // enquanto não acabou o arquivo
                        {
                            String linha = arqFiguras.nextLine();   // lemos a próxima linha do arquivo
                            String[] campos = linha.split(";");  // cria vetor de strings com os dados separados por ";"

                            // abaixo separamos os 6 valores comuns a todas as figuras geométricas
                            String tipo = campos[0].trim();
                            int xBase = Integer.parseInt(campos[1].trim());
                            int yBase = Integer.parseInt(campos[2].trim());
                            int corR  = Integer.parseInt(campos[3].trim());
                            int corG  = Integer.parseInt(campos[4].trim());
                            int corB  = Integer.parseInt(campos[5].trim());
                            Color cor = new Color(corR, corG, corB);
                            switch (tipo)
                            {
                                case "p" : // figura é um ponto
                                    figuras.incluirNoFinal(new Ponto(xBase, yBase, cor));
                                    break;
                                case "l" : // figura é uma linha
                                    int xFinal = Integer.parseInt(campos[6].trim());
                                    int yFinal = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Linha(xBase, yBase, xFinal, yFinal, cor));
                                    break;
                                case "c" : // figura é um círculo
                                    int raio = Integer.parseInt(campos[6].trim());
                                    figuras.incluirNoFinal(new Circulo(xBase, yBase, raio, cor));
                                    break;
                                case "o" : // figura é uma oval
                                    int raioA = Integer.parseInt(campos[6].trim());
                                    int raioB = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Oval(xBase, yBase, raioA, raioB, cor));
                                    break;
                            }
                        }
                        arqFiguras.close();

                        janelaFilha.setTitle(arquivo.getName());
                        desenharObjetos(pnlDesenho.getGraphics());
                    }
                    catch (Exception erroLeitura){
                        System.out.println("Erro de leitura no arquivo");
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }

    private class FazSalvar implements ActionListener {
        public void actionPerformed(ActionEvent e)  // código executado no evento
        {
            JFileChooser arqEscolhido = new JFileChooser ();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = arqEscolhido.showSaveDialog(Editor.this);

            //código de verificação se um arquivo foi selecionado e obter seu nome
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    figuras.gravarDados(arqEscolhido.getSelectedFile().getName());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private class DesenhaPonto implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            statusBar1.setText("Mensagem: clique no local do ponto desejado:");
            limparEsperas();
            esperaPonto = true;
        }
    }

    private class DesenhaLinha implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            statusBar1.setText("Mensagem: clique no ponto inicial da linha:");
            limparEsperas();
            esperaInicioLinha = true;
        }
    }


    private class MeuJPanel extends JPanel implements MouseListener, MouseMotionListener
    {
        JPanel pnlStatus = new JPanel();        // barra de status

        public MeuJPanel() {
            super();
            pnlStatus.setLayout(new GridLayout(1, 2)); // painel com 2 colunas
            statusBar1 = new JLabel("Mensagem");
            statusBar2 = new JLabel("Coordenada");
            pnlStatus.add(statusBar1); // label na coluna da esquerda
            pnlStatus.add(statusBar2); // label na coluna da direita
            getContentPane().add(pnlStatus, BorderLayout.SOUTH); // status no fundo do formulário
            addMouseListener(this);         // esta classe “ouve” cliques do mouse
            addMouseMotionListener(this);   // e “ouve” também seus movimentos
        }

        public void mouseMoved(MouseEvent e) {
            statusBar2.setText("Coordenada: "+e.getX()+","+e.getY());
        }
        public void mouseDragged(MouseEvent e) {
// não faz nada por enquanto
        }

        public void mouseClicked (MouseEvent e) {
            statusBar1.setText("Mensagem:");
        }

        // o método abaixo é chamado automaticamente quando o usuário
        // pressiona o botão esquerdo do mouse sobre o pnlDesenho ( instãncia
        // de MeuJPanel)
        public void mousePressed (MouseEvent e)
        {
            if (esperaPonto)
            {
                Ponto novoPonto = new Ponto(e.getX(), e.getY(), corAtual);
                figuras.incluirNoFinal(novoPonto);
                novoPonto.desenhar(novoPonto.getCor(), pnlDesenho.getGraphics());
                esperaPonto = false;
            }
            else
                if (esperaInicioLinha)
                {
                    // guarda em p1 o 1o ponto da Linha
                    p1.setX(e.getX());
                    p1.setY(e.getY());
                    p1.setCor(corAtual);
                    // ainda não podemos guardar uma Linha no vetor, pois
                    // não temos o 2o ponto extremo da mesma
                    esperaInicioLinha = false;
                    esperaFimLinha = true; // entra no modo de pedir o 2o ponto
                    statusBar1.setText("Clique no ponto final da linha:");
                }
                else
                    if (esperaFimLinha)
                    {
                        Ponto pontoFinal = new Ponto(e.getX(), e.getY(), corAtual);
                        Linha novaLinha = new Linha(p1, pontoFinal, corAtual);
                        figuras.incluirNoFinal(novaLinha);
                        novaLinha.desenhar(corAtual, pnlDesenho.getGraphics());
                        esperaFimLinha = false;  // linha terminada e incluída
                    }

        }
        public void mouseEntered (MouseEvent e) {
// não faz nada por enquanto
        }
        public void mouseExited (MouseEvent e) {
// não faz nada por enquanto
        }
        public void mouseReleased (MouseEvent e) {
// não faz nada por enquanto
        }

        public void paintComponent(Graphics g)
        {
            for (int atual = 0; atual < figuras.getTamanho(); atual++)
            {
                Ponto figuraAtual = figuras.valorDe(atual);
                figuraAtual.desenhar(figuraAtual.getCor(), g);
            }
        }
    }

}