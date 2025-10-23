import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;

import java.util.ArrayList;

public class Editor extends JFrame
{
    static Color corAtual = Color.black;
    static boolean esperaPonto, esperaInicioLinha, esperaFimLinha;
    static private MeuJPanel pnlDesenho;
    private static ManterFiguras figuras;     // objeto de manutenção de vetor de figuras geométricas

    //========== ADIÇÃO =========
    //  circulo/oval
    static boolean esperaCentroCirculo = false, esperaRaioCirculo = false;
    static boolean esperaCentroOval = false, esperaRaioAOval = false, esperaRaioBOval = false;
    static int raioAux; // usado para armazenar o raioA temporariamente durante o desenho da elipse

    //========== ADIÇÃO =========
    // retângulo
    static boolean esperaCantoRetangulo = false, esperaDimensaoRetangulo = false;
    static int xInicioRet, yInicioRet;

    //========== ADIÇÃO =========
    //  Polilinha
    static boolean esperaInicioPolilinha = false, esperaPontoPolilinha = false;
    static Polilinha polilinhaAtual = null;


    //========== ADIÇÃO =========
    // Seleção
    static boolean modoSelecao = false;
    static Ponto figuraSelecionada = null;

    // Movimento e arraste de figuras
    static boolean movendoFigura = false;
    static int xAnterior, yAnterior;

    // ======= ADIÇÃO: Vetor de figuras selecionadas =======
    static ArrayList<Integer> figurasSelecionadas = new ArrayList<Integer>();


    private static JLabel statusBar1, statusBar2;
    private static Ponto p1 = new Ponto();  // ponto inicial de linha, circulo, etc.

    private final JButton btnPonto, btnLinha, btnCirculo, btnElipse, btnRetangulo, btnPolilinha, btnCor, btnAbrir,
            btnSalvar, btnApagar, btnSelIndice, btnMover, btnApagarSelecionadas, btnLimparSelecao, btnListar, btnSair, btnSelecionar;


    private JPanel pnlBotoes;   // container dos botões
    static private JInternalFrame janelaFilha;

    public Editor()
    {
        super("Editor Gráfico"); // cria o JFrame e coloca um título

        figuras = new ManterFiguras(100); // cria objeto de manutenção de vetor de figuras geométricas

        //tava dando problema com a img e ai redimensionei
        int tamanhoIcone = 15;
        Icon imgRet = new ImageIcon(new ImageIcon("botoes\\retangulo.jpg").getImage().getScaledInstance(tamanhoIcone, tamanhoIcone, Image.SCALE_SMOOTH));
        Icon imgPoli = new ImageIcon(new ImageIcon("botoes\\polilinha.jpg").getImage().getScaledInstance(tamanhoIcone, tamanhoIcone, Image.SCALE_SMOOTH));
        Icon imgSel = new ImageIcon(new ImageIcon("botoes\\selecionar.jpg").getImage().getScaledInstance(tamanhoIcone, tamanhoIcone, Image.SCALE_SMOOTH));

        Icon imgAbrir = new ImageIcon("botoes\\abrir.jpg");
        btnAbrir = new JButton("Abrir", imgAbrir);
        btnSalvar = new JButton("Salvar", new ImageIcon("botoes\\salvar.jpg"));
        btnPonto = new JButton("Ponto", new ImageIcon("botoes\\ponto.jpg"));
        btnLinha = new JButton("Linha", new ImageIcon("botoes\\linha.jpg"));
        btnCirculo = new JButton("Circulo", new ImageIcon("botoes\\circulo.jpg"));
        btnElipse = new JButton("Elipse", new ImageIcon("botoes\\elipse.jpg"));
        btnRetangulo = new JButton("Retangulo", imgRet);
        btnPolilinha = new JButton("Polilinha", imgPoli);
        btnSelecionar = new JButton("Selecionar", imgSel);
        btnCor = new JButton("Cores", new ImageIcon("botoes\\cores.jpg"));
        btnApagar = new JButton("Apagar", new ImageIcon("botoes\\apagar.jpg"));
        btnSair = new JButton("Sair", new ImageIcon("botoes\\sair.jpg"));

        btnSelIndice = new JButton("Selecionar índice");
        btnMover = new JButton("Mover (ΔX,ΔY)");
        btnApagarSelecionadas = new JButton("Apagar Selecionadas");
        btnLimparSelecao = new JButton("Limpar Seleção");
        btnListar = new JButton("Listar Figuras");

        pnlBotoes = new JPanel();
        FlowLayout flwBotoes = new FlowLayout();
        pnlBotoes.setLayout(flwBotoes);

        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnElipse);
        pnlBotoes.add(btnRetangulo);
        pnlBotoes.add(btnPolilinha);
        pnlBotoes.add(btnSelecionar);
        pnlBotoes.add(btnCor);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnSair);

        // ======= ADIÇÃO =======
        pnlBotoes.add(btnSelIndice);
        pnlBotoes.add(btnMover);
        pnlBotoes.add(btnApagarSelecionadas);
        pnlBotoes.add(btnLimparSelecao);
        pnlBotoes.add(btnListar);


        btnAbrir.addActionListener(new FazAbertura());
        btnSalvar.addActionListener(new FazSalvar());
        btnPonto.addActionListener(new DesenhaPonto());
        btnLinha.addActionListener(new DesenhaLinha());
        btnCirculo.addActionListener(new DesenhaCirculo());
        btnElipse.addActionListener(new DesenhaElipse());
        btnRetangulo.addActionListener(new DesenhaRetangulo());
        btnPolilinha.addActionListener(new DesenhaPolilinha());
        btnSelecionar.addActionListener(new SelecionarFigura());


        btnCor.addActionListener(new EscolheCor());
        btnApagar.addActionListener(new ApagarTudo());
        btnSair.addActionListener(new SairDoPrograma());

        // ======= ADIÇÃO: listeners dos novos botões =======
        btnSelIndice.addActionListener(new SelecionarPorIndice());
        btnMover.addActionListener(new MoverSelecionadas());
        btnApagarSelecionadas.addActionListener(new ApagarSelecionadas());
        btnLimparSelecao.addActionListener(new LimparSelecao());
        btnListar.addActionListener(new ListarFiguras());


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
        esperaCantoRetangulo = false;
        esperaDimensaoRetangulo = false;
        esperaInicioPolilinha = false;
        esperaPontoPolilinha = false;
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
                                case "r" :
                                    int largura = Integer.parseInt(campos[6].trim());
                                    int altura = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Retangulo(xBase, yBase, largura, altura, cor));
                                    break;
                                case "pl" :
                                    // formato: pl; xc; yc; corR; corG; corB; qtdPontos; x1; y1; x2; y2; ... xn; yn
                                    int qtd = Integer.parseInt(campos[6].trim());
                                    Polilinha pl = new Polilinha(qtd, cor);
                                    int pos = 7;
                                    for (int i = 0; i < qtd; i++) {
                                        int x = Integer.parseInt(campos[pos++].trim());
                                        int y = Integer.parseInt(campos[pos++].trim());
                                        pl.adicionarPonto(new Ponto(x, y, cor));
                                    }
                                    figuras.incluirNoFinal(pl);
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

    //Seleção
    private class SelecionarFigura implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            limparEsperas();
            modoSelecao = true;
            statusBar1.setText("Mensagem: clique sobre a figura para selecioná-la.");
        }
    }

    // ======= ADIÇÃO: Seleção por índice =======
    private class SelecionarPorIndice implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String input = JOptionPane.showInputDialog("Digite o índice da figura a selecionar:");
                if (input == null || input.isEmpty()) return;

                int indice = Integer.parseInt(input);

                if (indice < 0 || indice >= figuras.getTamanho()) {
                    JOptionPane.showMessageDialog(Editor.this, "Índice inválido!");
                    return;
                }

                if (!figurasSelecionadas.contains(indice))
                    figurasSelecionadas.add(indice);

                Ponto f = figuras.valorDe(indice);
                Graphics g = pnlDesenho.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(3)); // espessura 2 pixels a mais
                f.desenhar(Color.red, g2);
                statusBar1.setText("Mensagem: figura " + indice + " selecionada!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Editor.this, "Erro ao selecionar figura!");
            }
        }
    }

    // ======= ADIÇÃO: Mudar cor e mover figuras selecionadas =======
    private class MoverSelecionadas implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (figurasSelecionadas.isEmpty()) {
                JOptionPane.showMessageDialog(Editor.this, "Nenhuma figura selecionada.");
                return;
            }

            try {
                String dxStr = JOptionPane.showInputDialog("Digite o deslocamento em X:");
                String dyStr = JOptionPane.showInputDialog("Digite o deslocamento em Y:");
                if (dxStr == null || dyStr == null) return;

                int dx = Integer.parseInt(dxStr);
                int dy = Integer.parseInt(dyStr);

                for (int i : figurasSelecionadas) {
                    Ponto f = figuras.valorDe(i);

                    if (f instanceof Ponto) {
                        f.setX(f.getX() + dx);
                        f.setY(f.getY() + dy);
                    } else if (f instanceof Linha) {
                        Linha l = (Linha) f;
                        l.setX(l.getX() + dx);
                        l.setY(l.getY() + dy);
                        l.pontoFinal.setX(l.pontoFinal.getX() + dx);
                        l.pontoFinal.setY(l.pontoFinal.getY() + dy);
                    } else if (f instanceof Retangulo || f instanceof Circulo || f instanceof Oval) {
                        f.setX(f.getX() + dx);
                        f.setY(f.getY() + dy);
                    } else if (f instanceof Polilinha) {
                        Polilinha pl = (Polilinha) f;
                        for (int j = 0; j < pl.getQtdPontos(); j++) {
                            Ponto p = pl.getPontos().get(j);
                            p.setX(p.getX() + dx);
                            p.setY(p.getY() + dy);
                        }
                    }
                }

                pnlDesenho.repaint();
                statusBar1.setText("Mensagem: figuras movidas (" + dx + "," + dy + ").");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Editor.this, "Erro ao mover figuras selecionadas!");
            }
        }
    }

    // ======= ADIÇÃO: Apagar figuras selecionadas =======
    private class ApagarSelecionadas implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (figurasSelecionadas.isEmpty()) {
                JOptionPane.showMessageDialog(Editor.this, "Nenhuma figura selecionada.");
                return;
            }

            // remove de trás pra frente pra evitar erro de índice
            figurasSelecionadas.sort((a, b) -> b - a);
            for (int i : figurasSelecionadas)
                // corrigido: ManterFiguras possui método 'excluir', não 'remover'
                try {
                    figuras.excluir(i);
                } catch (IndexOutOfBoundsException ex) {
                    // se der erro por algum motivo, apenas ignora essa posição
                }

            figurasSelecionadas.clear();
            pnlDesenho.repaint();
            statusBar1.setText("Mensagem: figuras selecionadas apagadas.");
        }
    }

    // ======= ADIÇÃO: Limpar seleção =======
    private class LimparSelecao implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            figurasSelecionadas.clear();
            figuraSelecionada = null;
            pnlDesenho.repaint();
            statusBar1.setText("Mensagem: seleção limpa.");
        }
    }

    // ======= ADIÇÃO: Listar figuras (índices e tipos) =======
    private class ListarFiguras implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Índice : Tipo - detalhes\n");
            int n = figuras.getTamanho();
            for (int i = 0; i < n; i++) {
                Ponto f = figuras.valorDe(i);
                String tipo = f.getClass().getSimpleName();
                String detalhes = "";
                if (f instanceof Circulo) {
                    Circulo c = (Circulo) f;
                    detalhes = String.format("centro=(%d,%d) raio=%d cor=%s", c.getX(), c.getY(), c.raio, corToString(c.getCor()));
                } else if (f instanceof Oval) {
                    Oval o = (Oval) f;
                    detalhes = String.format("centro=(%d,%d) a=%d b=%d cor=%s", o.getX(), o.getY(), o.raioA, o.raioB, corToString(o.getCor()));
                } else if (f instanceof Linha) {
                    Linha l = (Linha) f;
                    detalhes = String.format("inicio=(%d,%d) fim=(%d,%d) cor=%s", l.getX(), l.getY(), l.pontoFinal.getX(), l.pontoFinal.getY(), corToString(l.getCor()));
                } else if (f instanceof Retangulo) {
                    Retangulo r = (Retangulo) f;
                    detalhes = String.format("x=%d y=%d w=%d h=%d cor=%s", r.getX(), r.getY(), r.getLargura(), r.getAltura(), corToString(r.getCor()));
                } else if (f instanceof Polilinha) {
                    Polilinha pl = (Polilinha) f;
                    detalhes = String.format("pontos=%d cor=%s", pl.getQtdPontos(), corToString(pl.getCor()));
                } else if (f instanceof Ponto) {
                    detalhes = String.format("x=%d y=%d cor=%s", f.getX(), f.getY(), corToString(f.getCor()));
                }
                sb.append(i).append(" : ").append(tipo).append(" - ").append(detalhes).append("\n");
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JScrollPane sp = new JScrollPane(area);
            sp.setPreferredSize(new Dimension(480, 320));
            JOptionPane.showMessageDialog(Editor.this, sp, "Lista de Figuras (índice e tipo)", JOptionPane.PLAIN_MESSAGE);
        }

        private String corToString(Color c) {
            if (c == null) return "null";
            return String.format("(%d,%d,%d)", c.getRed(), c.getGreen(), c.getBlue());
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

    private class DesenhaRetangulo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique no canto superior esquerdo do retângulo:");
            limparEsperas();
            esperaCantoRetangulo = true;
        }
    }

    //Polilinha
    private class DesenhaPolilinha implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique no primeiro ponto da polilinha (botão direito para encerrar)");
            limparEsperas();
            esperaInicioPolilinha = true;
            polilinhaAtual = new Polilinha(0, corAtual);
        }
    }

    private class EscolheCor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Color nova = JColorChooser.showDialog(Editor.this, "Escolha a cor", corAtual);
            if (nova != null) {
                corAtual = nova;

                // ======= ADIÇÃO =======
                // Se há várias figuras selecionadas
                if (!figurasSelecionadas.isEmpty()) {
                    for (int i : figurasSelecionadas) {
                        Ponto f = figuras.valorDe(i);
                        f.setCor(nova);
                    }
                    pnlDesenho.repaint();
                    statusBar1.setText("Mensagem: cor das figuras selecionadas alterada.");
                }
                // Caso tenha apenas uma figura selecionada diretamente
                else if (figuraSelecionada != null) {
                    figuraSelecionada.setCor(nova);
                    pnlDesenho.repaint();
                    statusBar1.setText("Mensagem: cor da figura selecionada alterada.");
                }
                // Caso nenhuma figura esteja selecionada
                else {
                    statusBar1.setText("Mensagem: cor atual atualizada (para novas figuras).");
                }
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


        public void mouseDragged(MouseEvent e) {
            if (figuraSelecionada != null) {
                int dx = e.getX() - xAnterior;
                int dy = e.getY() - yAnterior;

                // Atualiza posição conforme o tipo
                if (figuraSelecionada instanceof Ponto) {
                    figuraSelecionada.setX(figuraSelecionada.getX() + dx);
                    figuraSelecionada.setY(figuraSelecionada.getY() + dy);
                }
                else if (figuraSelecionada instanceof Linha) {
                    Linha l = (Linha) figuraSelecionada;
                    l.setX(l.getX() + dx);
                    l.setY(l.getY() + dy);
                    l.pontoFinal.setX(l.pontoFinal.getX() + dx);
                    l.pontoFinal.setY(l.pontoFinal.getY() + dy);
                }
                else if (figuraSelecionada instanceof Circulo ||
                        figuraSelecionada instanceof Oval ||
                        figuraSelecionada instanceof Retangulo) {
                    figuraSelecionada.setX(figuraSelecionada.getX() + dx);
                    figuraSelecionada.setY(figuraSelecionada.getY() + dy);
                }
                else if (figuraSelecionada instanceof Polilinha) {
                    Polilinha pl = (Polilinha) figuraSelecionada;
                    for (int i = 0; i < pl.getQtdPontos(); i++) {
                        Ponto p = pl.getPontos().get(i);
                        p.setX(p.getX() + dx);
                        p.setY(p.getY() + dy);
                    }
                }

                xAnterior = e.getX();
                yAnterior = e.getY();
                pnlDesenho.repaint();
                statusBar1.setText("Mensagem: movendo figura selecionada...");
            }
        }


        public void mouseClicked (MouseEvent e) {
            statusBar1.setText("Mensagem:");
        }

        public void mousePressed(MouseEvent e)
        {
            // ===== ADIÇÃO =====
            xAnterior = e.getX();
            yAnterior = e.getY();

            if (figuraSelecionada != null) {
                movendoFigura = true; // inicia movimento se já havia figura selecionada
            }

            if (modoSelecao)
            {
                figuraSelecionada = null;

                // percorre as figuras do topo para o fundo
                for (int i = figuras.getTamanho() - 1; i >= 0; i--)
                {
                    Ponto f = figuras.valorDe(i);

                    if (f instanceof Circulo)
                    {
                        Circulo c = (Circulo) f;
                        int dx = e.getX() - c.getX();
                        int dy = e.getY() - c.getY();
                        if (Math.sqrt(dx * dx + dy * dy) <= c.raio)
                        {
                            figuraSelecionada = f;
                            if (!figurasSelecionadas.contains(i))
                                figurasSelecionadas.add(i);
                            break;
                        }
                    }
                    else if (f instanceof Linha)
                    {
                        Linha l = (Linha) f;
                        double dist = Math.abs((l.pontoFinal.getY() - l.getY()) * e.getX() -
                                (l.pontoFinal.getX() - l.getX()) * e.getY() +
                                l.pontoFinal.getX() * l.getY() - l.pontoFinal.getY() * l.getX())
                                / Math.hypot(l.pontoFinal.getY() - l.getY(), l.pontoFinal.getX() - l.getX());
                        if (dist < 5)
                        {
                            figuraSelecionada = f;
                            if (!figurasSelecionadas.contains(i))
                                figurasSelecionadas.add(i);
                            break;
                        }
                    }
                    else if (f instanceof Retangulo)
                    {
                        Retangulo r = (Retangulo) f;
                        if (e.getX() >= r.getX() && e.getX() <= r.getX() + r.getLargura() &&
                                e.getY() >= r.getY() && e.getY() <= r.getY() + r.getAltura())
                        {
                            figuraSelecionada = f;
                            if (!figurasSelecionadas.contains(i))
                                figurasSelecionadas.add(i);
                            break;
                        }
                    }
                    else if (f instanceof Oval)
                    {
                        Oval o = (Oval) f;
                        double dx = (double)(e.getX() - o.getX()) / o.raioA;
                        double dy = (double)(e.getY() - o.getY()) / o.raioB;
                        if (dx * dx + dy * dy <= 1)
                        {
                            figuraSelecionada = f;
                            if (!figurasSelecionadas.contains(i))
                                figurasSelecionadas.add(i);
                            break;
                        }
                    }
                    else if (f instanceof Ponto)
                    {
                        if (Math.abs(e.getX() - f.getX()) < 4 && Math.abs(e.getY() - f.getY()) < 4)
                        {
                            figuraSelecionada = f;
                            if (!figurasSelecionadas.contains(i))
                                figurasSelecionadas.add(i);
                            break;
                        }
                    }
                    else if (f instanceof Polilinha)
                    {
                        Polilinha pl = (Polilinha) f;
                        for (int j = 0; j < pl.getQtdPontos() - 1; j++)
                        {
                            Ponto p1 = pl.getPontos().get(j);
                            Ponto p2 = pl.getPontos().get(j + 1);
                            double dist = Math.abs((p2.getY() - p1.getY()) * e.getX() -
                                    (p2.getX() - p1.getX()) * e.getY() +
                                    p2.getX() * p1.getY() - p2.getY() * p1.getX())
                                    / Math.hypot(p2.getY() - p1.getY(), p2.getX() - p1.getX());
                            if (dist < 5)
                            {
                                figuraSelecionada = f;
                                if (!figurasSelecionadas.contains(i))
                                    figurasSelecionadas.add(i);
                                break;
                            }
                        }
                        if (figuraSelecionada != null)
                            break;
                    }
                }

                if (figuraSelecionada != null)
                {
                    statusBar1.setText("Mensagem: figura selecionada!");
                }
                else
                {
                    statusBar1.setText("Mensagem: nenhuma figura encontrada neste ponto.");
                }

                pnlDesenho.repaint(); // redesenha para manter vermelho
                modoSelecao = false;
                return; // impede que o clique prossiga para desenho
            }

            // ===== ADIÇÃO =====
            // se clicar fora de qualquer modo e havia figuras selecionadas, desmarca todas
            if (!modoSelecao && (!figurasSelecionadas.isEmpty() || figuraSelecionada != null)
                    && !SwingUtilities.isRightMouseButton(e)) {
                figurasSelecionadas.clear();
                figuraSelecionada = null;
                pnlDesenho.repaint();
                statusBar1.setText("Mensagem: nenhuma figura selecionada.");
            }

            // ===== RESTANTE ORIGINAL =====
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
            else if (esperaInicioPolilinha || esperaPontoPolilinha)
            {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (polilinhaAtual != null && polilinhaAtual.getQtdPontos() > 1) {
                        figuras.incluirNoFinal(polilinhaAtual);
                        polilinhaAtual.desenhar(corAtual, pnlDesenho.getGraphics());
                        pnlDesenho.repaint();
                    }
                    esperaInicioPolilinha = false;
                    esperaPontoPolilinha = false;
                    polilinhaAtual = null;
                    statusBar1.setText("Mensagem: polilinha finalizada.");
                    return;
                }

                Ponto novo = new Ponto(e.getX(), e.getY(), corAtual);
                if (polilinhaAtual == null)
                    polilinhaAtual = new Polilinha(0, corAtual);
                polilinhaAtual.adicionarPonto(novo);
                polilinhaAtual.desenhar(corAtual, pnlDesenho.getGraphics());
                pnlDesenho.repaint();

                esperaInicioPolilinha = false;
                esperaPontoPolilinha = true;
                statusBar1.setText("Mensagem: clique para novo ponto ou botão direito para encerrar.");
            }
        }



        public void mouseEntered (MouseEvent e) {}
        public void mouseExited (MouseEvent e) {}

        public void mouseReleased (MouseEvent e) {
            if (movendoFigura) {
                movendoFigura = false;
                statusBar1.setText("Mensagem: movimento concluído.");
            }
        }


        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < figuras.getTamanho(); i++) {
                Ponto f = figuras.valorDe(i);

                // Se está selecionada, desenha com destaque
                if (figurasSelecionadas.contains(i) || f == figuraSelecionada) {
                    g2.setColor(Color.red);
                    g2.setStroke(new BasicStroke(3));
                    f.desenhar(Color.red, g2);
                } else {
                    g2.setColor(f.getCor());
                    g2.setStroke(new BasicStroke(1));
                    f.desenhar(f.getCor(), g2);
                }
            }
        }

    }
}