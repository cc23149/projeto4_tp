// Na classe do vetor de manutenção de "Classe"

// Ao invés de se usar BufferedReader, e separar os dados em um registro de tamanho fixo
// podemos também usar a classe Scanner para leitura de arquivos texto.
// Supondo que os dados estão disponíveis em formato csv, separando os campos com ";"

    public void lerArquivo(String nomeDeArquivo) throws IOException {
        Scanner sc = new Scanner(new File(nomeDeArquivo));
        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            String[] partes = linha.split(";");
            // separar os campos
            tipo campo1 = (tipo) partes[0];
            tipo2 campo2 = (tipo2) partes[1];
            ...
            tipoN campoN = tipoN.parseTipo(partes[1]);
            Classe c = new Classe(campo1, campo2, ..., campoN);
            incluirEm(qtosDados, m);
        }
        sc.close();
    }

    // Uma maneira alternativa de gravar dados em arquivo texto é por meio da
    // classe PrintWriter, que usa como parâmetro um FileWriter
    // PrintWriter possui o método println que pula de linha após cada escrita.
    // No caso abaixo, o método formatoDeArquivo deve gerar cada linha de dados
    // separando os campos com ";", sem necessidade de manter tamanho fixo para
    // cada campo e registro
    public void gravarDados(String nomeDeArquivo) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(nomeDeArquivo));
        for (int i = 0; i < qtosDados; i++) {
            pw.println(dados[i].formatoDeArquivo());
        }
        pw.close();
    }