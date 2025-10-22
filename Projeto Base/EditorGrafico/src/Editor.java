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

    //========== do projeto =========
    // flags adicionais para circulo/oval
    static boolean esperaCentroCirculo = false, esperaRaioCirculo = false;
    static boolean esperaCentroOval = false, esperaRaioAOval = false, esperaRaioBOval = false;
    static int raioAux; // usado para armazenar o raioA temporariamente durante o desenho da elipse

    //========== ADIÇÃO =========
    // flags adicionais para retângulo
    static boolean esperaCantoRetangulo = false, esperaDimensaoRetangulo = false;
    static int xInicioRet, yInicioRet;

    private static JLabel statusBar1, statusBar2;
    private static Ponto p1 = new Ponto();  // ponto inicial de linha, circulo, etc.

    private final JButton btnPonto, btnLinha, btnCirculo, btnElipse, btnRetangulo, btnCor, btnAbrir,
            btnSalvar, btnApagar, btnSair;

    private JPanel pnlBotoes;   // container dos botões
    static private JInternalFrame janelaFilha;

    public Editor()
    {
        super("Editor Gráfico"); // cria o JFrame e coloca um título

        figuras = new ManterFiguras(100); // cria objeto de manutenção de vetor de figuras geométricas

        //Dei um jeito de ficar visualmente igual aos outros
        int tamanhoIcone = 15;                                                                                                                      // Não sei se podia usar isso para ajustar o tamanho da imagem
        Icon imgRet = new ImageIcon(new ImageIcon("botoes\\retangulo.jpg").getImage().getScaledInstance(tamanhoIcone, tamanhoIcone, Image.SCALE_SMOOTH));

        Icon imgAbrir = new ImageIcon("botoes\\abrir.jpg");
        btnAbrir = new JButton("Abrir", imgAbrir);
        btnSalvar = new JButton("Salvar", new ImageIcon("botoes\\salvar.jpg"));
        btnPonto = new JButton("Ponto", new ImageIcon("botoes\\ponto.jpg"));
        btnLinha = new JButton("Linha", new ImageIcon("botoes\\linha.jpg"));
        btnCirculo = new JButton("Circulo", new ImageIcon("botoes\\circulo.jpg"));
        btnElipse = new JButton("Elipse", new ImageIcon("botoes\\elipse.jpg"));
        btnRetangulo = new JButton("Retangulo", imgRet);
        btnCor = new JButton("Cores", new ImageIcon("botoes\\cores.jpg"));
        btnApagar = new JButton("Apagar", new ImageIcon("botoes\\apagar.jpg"));
        btnSair = new JButton("Sair", new ImageIcon("botoes\\sair.jpg"));

        pnlBotoes = new JPanel();
        FlowLayout flwBotoes = new FlowLayout();
        pnlBotoes.setLayout(flwBotoes);

        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnElipse);
        pnlBotoes.add(btnRetangulo); // do projeto
        pnlBotoes.add(btnCor);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnSair);

        btnAbrir.addActionListener(new FazAbertura());
        btnSalvar.addActionListener(new FazSalvar());
        btnPonto.addActionListener(new DesenhaPonto());
        btnLinha.addActionListener(new DesenhaLinha());
        btnCirculo.addActionListener(new DesenhaCirculo());
        btnElipse.addActionListener(new DesenhaElipse());
        btnRetangulo.addActionListener(new DesenhaRetangulo()); // do projeto
        btnCor.addActionListener(new EscolheCor());
        btnApagar.addActionListener(new ApagarTudo());
        btnSair.addActionListener(new SairDoPrograma());

        Container cntForm = getContentPane();
        cntForm.setLayout(new BorderLayout());
        cntForm.add(pnlBotoes , BorderLayout.NORTH);

        JDesktopPane panDesenho = new JDesktopPane();
        cntForm.add(panDesenho);

        janelaFilha = new JInternalFrame("Nenhum arquivo aberto", true, true, true, true);
        panDesenho.add(janelaFilha);

        setSize(700,500);
        setVisible(true);

        janelaFilha.setOpaque(true);
        janelaFilha.setSize(this.getWidth()/2, this.getHeight() / 2);
        janelaFilha.show();

        pnlDesenho = new MeuJPanel();
        Container cntFrame = janelaFilha.getContentPane();
        cntFrame.add(pnlDesenho);
    }

    public static void main(String[] args)
    {
        Editor aplicacao = new Editor();

        aplicacao.addWindowListener(
                new WindowAdapter() {
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
        esperaCentroCirculo = false;
        esperaRaioCirculo = false;
        esperaCentroOval = false;
        esperaRaioAOval = false;
        esperaRaioBOval = false;
        esperaCantoRetangulo = false; // Do Projeto
        esperaDimensaoRetangulo = false; // Do Projeto
    }

    private class FazAbertura implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser arqEscolhido = new JFileChooser ();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = arqEscolhido.showOpenDialog(Editor.this);

            if (result == JFileChooser.APPROVE_OPTION)
            {
                File arquivo = arqEscolhido.getSelectedFile();
                try {
                    Scanner arqFiguras = new Scanner(new File(arquivo.getName()));
                    try {
                        while (arqFiguras.hasNextLine())
                        {
                            String linha = arqFiguras.nextLine();
                            String[] campos = linha.split(";");

                            String tipo = campos[0].trim();
                            int xBase = Integer.parseInt(campos[1].trim());
                            int yBase = Integer.parseInt(campos[2].trim());
                            int corR  = Integer.parseInt(campos[3].trim());
                            int corG  = Integer.parseInt(campos[4].trim());
                            int corB  = Integer.parseInt(campos[5].trim());
                            Color cor = new Color(corR, corG, corB);
                            switch (tipo)
                            {
                                case "p" :
                                    figuras.incluirNoFinal(new Ponto(xBase, yBase, cor));
                                    break;
                                case "l" :
                                    int xFinal = Integer.parseInt(campos[6].trim());
                                    int yFinal = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Linha(xBase, yBase, xFinal, yFinal, cor));
                                    break;
                                case "c" :
                                    int raio = Integer.parseInt(campos[6].trim());
                                    figuras.incluirNoFinal(new Circulo(xBase, yBase, raio, cor));
                                    break;
                                case "o" :
                                    int raioA = Integer.parseInt(campos[6].trim());
                                    int raioB = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Oval(xBase, yBase, raioA, raioB, cor));
                                    break;
                                case "r" : // ADIÇÃO
                                    int largura = Integer.parseInt(campos[6].trim());
                                    int altura = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Retangulo(xBase, yBase, largura, altura, cor));
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
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser arqEscolhido = new JFileChooser ();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = arqEscolhido.showSaveDialog(Editor.this);

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

    private class DesenhaCirculo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique no centro do círculo:");
            limparEsperas();
            esperaCentroCirculo = true;
        }
    }

    private class DesenhaElipse implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique no centro da elipse:");
            limparEsperas();
            esperaCentroOval = true;
        }
    }

    //============ ADIÇÃO DO RETÂNGULO ============
    private class DesenhaRetangulo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique no canto superior esquerdo do retângulo:");
            limparEsperas();
            esperaCantoRetangulo = true;
        }
    }

    private class EscolheCor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Color nova = JColorChooser.showDialog(Editor.this, "Escolha a cor", corAtual);
            if (nova != null) {
                corAtual = nova;
                statusBar1.setText("Mensagem: cor atual atualizada.");
            }
        }
    }

    private class ApagarTudo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            figuras = new ManterFiguras(100);
            pnlDesenho.repaint();
            statusBar1.setText("Mensagem: área limpa.");
        }
    }

    private class SairDoPrograma implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int resposta = JOptionPane.showConfirmDialog(
                    Editor.this,
                    "Deseja realmente sair?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION)
                System.exit(0);
        }
    }

    private class MeuJPanel extends JPanel implements MouseListener, MouseMotionListener
    {
        JPanel pnlStatus = new JPanel();

        public MeuJPanel() {
            super();
            pnlStatus.setLayout(new GridLayout(1, 2));
            statusBar1 = new JLabel("Mensagem");
            statusBar2 = new JLabel("Coordenada");
            pnlStatus.add(statusBar1);
            pnlStatus.add(statusBar2);
            getContentPane().add(pnlStatus, BorderLayout.SOUTH);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void mouseMoved(MouseEvent e) {
            statusBar2.setText("Coordenada: "+e.getX()+","+e.getY());
        }
        public void mouseDragged(MouseEvent e) {}

        public void mouseClicked (MouseEvent e) {
            statusBar1.setText("Mensagem:");
        }

        public void mousePressed (MouseEvent e)
        {
            if (esperaPonto)
            {
                Ponto novoPonto = new Ponto(e.getX(), e.getY(), corAtual);
                figuras.incluirNoFinal(novoPonto);
                novoPonto.desenhar(novoPonto.getCor(), pnlDesenho.getGraphics());
                esperaPonto = false;
            }
            else if (esperaInicioLinha)
            {
                p1.setX(e.getX());
                p1.setY(e.getY());
                p1.setCor(corAtual);
                esperaInicioLinha = false;
                esperaFimLinha = true;
                statusBar1.setText("Clique no ponto final da linha:");
            }
            else if (esperaFimLinha)
            {
                Ponto pontoFinal = new Ponto(e.getX(), e.getY(), corAtual);
                Linha novaLinha = new Linha(p1, pontoFinal, corAtual);
                figuras.incluirNoFinal(novaLinha);
                novaLinha.desenhar(corAtual, pnlDesenho.getGraphics());
                esperaFimLinha = false;
            }

            //========== Do Projeto ===========

            else if (esperaCentroCirculo)
            {
                p1.setX(e.getX());
                p1.setY(e.getY());
                p1.setCor(corAtual);
                esperaCentroCirculo = false;
                esperaRaioCirculo = true;
                statusBar1.setText("Mensagem: clique em um ponto da circunferência (para definir o raio):");
            }
            else if (esperaRaioCirculo)
            {
                int dx = e.getX() - p1.getX();
                int dy = e.getY() - p1.getY();
                int raio = (int)Math.sqrt(dx*dx + dy*dy);
                Circulo c = new Circulo(p1, raio, corAtual);
                figuras.incluirNoFinal(c);
                c.desenhar(corAtual, pnlDesenho.getGraphics());
                pnlDesenho.repaint();
                esperaRaioCirculo = false;
                statusBar1.setText("Mensagem:");
            }
            else if (esperaCentroOval)
            {
                p1.setX(e.getX());
                p1.setY(e.getY());
                p1.setCor(corAtual);
                esperaCentroOval = false;
                esperaRaioAOval = true;
                statusBar1.setText("Mensagem: clique em um ponto para definir raio A (horizontal):");
            }
            else if (esperaRaioAOval)
            {
                raioAux = Math.abs(e.getX() - p1.getX());
                esperaRaioAOval = false;
                esperaRaioBOval = true;
                statusBar1.setText("Mensagem: clique em um ponto para definir raio B (vertical):");
            }
            else if (esperaRaioBOval)
            {
                int raioB = Math.abs(e.getY() - p1.getY());
                Oval o = new Oval(p1, raioAux, raioB, corAtual);
                figuras.incluirNoFinal(o);
                o.desenhar(corAtual, pnlDesenho.getGraphics());
                pnlDesenho.repaint();
                esperaRaioBOval = false;
                statusBar1.setText("Mensagem:");
            }

            //========= ADIÇÃO DO RETÂNGULO =========
            else if (esperaCantoRetangulo)
            {
                xInicioRet = e.getX();
                yInicioRet = e.getY();
                esperaCantoRetangulo = false;
                esperaDimensaoRetangulo = true;
                statusBar1.setText("Mensagem: clique no canto oposto do retângulo:");
            }
            else if (esperaDimensaoRetangulo)
            {
                int largura = Math.abs(e.getX() - xInicioRet);
                int altura = Math.abs(e.getY() - yInicioRet);
                int xFinal = Math.min(e.getX(), xInicioRet);
                int yFinal = Math.min(e.getY(), yInicioRet);
                Retangulo r = new Retangulo(xFinal, yFinal, largura, altura, corAtual);
                figuras.incluirNoFinal(r);
                r.desenhar(corAtual, pnlDesenho.getGraphics());
                pnlDesenho.repaint();
                esperaDimensaoRetangulo = false;
                statusBar1.setText("Mensagem:");
            }
        }

        public void mouseEntered (MouseEvent e) {}
        public void mouseExited (MouseEvent e) {}
        public void mouseReleased (MouseEvent e) {}

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g); // limpa o fundo antes de redesenhar

            for (int atual = 0; atual < figuras.getTamanho(); atual++)
            {
                Ponto figuraAtual = figuras.valorDe(atual);
                figuraAtual.desenhar(figuraAtual.getCor(), g);
            }
        }
    }
}
