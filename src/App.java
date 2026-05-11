import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

public class App {

    static String nomeArquivoDados;
    static Scanner teclado;
    static Produto[] produtosCadastrados;
    static int quantosProdutos = 0;

    static Fila<Pedido> filaPedidos = new Fila<>();
    static Fila<Character> filaCaracteres = new Fila<>();

    static final String nomeArquivoPedidos = "pedidos.txt";

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("AEDs II COMERCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        T valor;

        System.out.println(mensagem);
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar por um produto, por codigo");
        System.out.println("3 - Procurar por um produto, por nome");
        System.out.println("4 - Iniciar novo pedido");
        System.out.println("5 - Fechar pedido");
        System.out.println("6 - Extrair lote de pedidos");
        System.out.println("7 - Testes preliminares da fila");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opcao: ");
        return Integer.parseInt(teclado.nextLine());
    }

    static int menuTeste() {
        cabecalho();
        System.out.println("1 - Listar todos os caracteres da fila");
        System.out.println("2 - Enfileirar caracteres do primeiro e segundo nome");
        System.out.println("3 - Desenfileirar caractere");
        System.out.println("4 - Contar ocorrencias de um caractere");
        System.out.println("0 - Voltar");
        System.out.print("Digite sua opcao: ");
        return Integer.parseInt(teclado.nextLine());
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {
        Scanner arquivo = null;
        int numProdutos;
        String linha;
        Produto produto;
        Produto[] produtosCadastrados;

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));

            numProdutos = Integer.parseInt(arquivo.nextLine());
            produtosCadastrados = new Produto[numProdutos];

            for (int i = 0; i < numProdutos; i++) {
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                produtosCadastrados[i] = produto;
            }
            quantosProdutos = numProdutos;

        } catch (IOException excecaoArquivo) {
            produtosCadastrados = null;
        } finally {
            if (arquivo != null) {
                arquivo.close();
            }
        }

        return produtosCadastrados;
    }

    static Produto localizarProduto() {
        Produto produto = null;
        boolean localizado = false;

        cabecalho();
        System.out.println("Localizando um produto...");
        Integer idProduto = lerOpcao("Digite o codigo identificador do produto desejado: ", Integer.class);
        if (idProduto == null) {
            return null;
        }

        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
            if (produtosCadastrados[i].hashCode() == idProduto) {
                produto = produtosCadastrados[i];
                localizado = true;
            }
        }

        return produto;
    }

    static Produto localizarProdutoDescricao() {
        Produto produto = null;
        boolean localizado = false;
        String descricao;

        cabecalho();
        System.out.println("Localizando um produto...");
        System.out.println("Digite o nome ou a descricao do produto desejado:");
        descricao = teclado.nextLine();
        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
            if (produtosCadastrados[i].descricao.equalsIgnoreCase(descricao)) {
                produto = produtosCadastrados[i];
                localizado = true;
            }
        }

        return produto;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        String mensagem = "Dados invalidos para o produto!";

        if (produto != null) {
            mensagem = String.format("Dados do produto:\n%s", produto);
        }

        System.out.println(mensagem);
    }

    static void listarTodosOsProdutos() {
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println(String.format("%02d - %s", (i + 1), produtosCadastrados[i].toString()));
        }
    }

    public static Pedido iniciarPedido() {
        Integer formaPagamento = lerOpcao(
                "Digite a forma de pagamento do pedido, sendo 1 para pagamento a vista e 2 para pagamento a prazo",
                Integer.class);
        if (formaPagamento == null) {
            System.out.println("Forma de pagamento invalida.");
            return null;
        }

        Pedido pedido = new Pedido(LocalDate.now(), formaPagamento);
        Produto produto;
        Integer numProdutos;
        Integer quantidade;

        listarTodosOsProdutos();
        System.out.println("Incluindo produtos no pedido...");
        numProdutos = lerOpcao("Quantos produtos serao incluidos no pedido?", Integer.class);
        if (numProdutos == null || numProdutos < 0) {
            System.out.println("Quantidade invalida.");
            return pedido;
        }

        for (int i = 0; i < numProdutos; i++) {
            produto = localizarProdutoDescricao();
            if (produto == null) {
                System.out.println("Produto nao encontrado");
                i--;
            } else {
                quantidade = lerOpcao("Quantos itens desse produto serao incluidos no pedido?", Integer.class);
                if (quantidade == null || quantidade <= 0) {
                    System.out.println("Quantidade invalida.");
                    i--;
                } else {
                    pedido.incluirProduto(produto, quantidade);
                }
            }
        }

        return pedido;
    }

    public static void finalizarPedido(Pedido pedido) {
        if (pedido == null) {
            System.out.println("Nao ha pedido aberto para finalizar.");
            return;
        }

        filaPedidos.enfileirar(pedido);
        System.out.println("Pedido finalizado e inserido no final da fila.");
        System.out.println(pedido);
    }

    public static void extrairLotePedidos() {
        if (filaPedidos.vazia()) {
            System.out.println("Nao ha pedidos aguardando processamento.");
            return;
        }

        Integer quantidade = lerOpcao("Quantos pedidos deseja extrair?", Integer.class);
        if (quantidade == null || quantidade < 0) {
            System.out.println("Quantidade invalida.");
            return;
        }

        Fila<Pedido> lote = filaPedidos.extrairLote(quantidade);
        System.out.println("Pedidos extraidos:");
        lote.listarTodosOsItensFila();
    }

    public static void listarTodosOsCaracteresFila() {
        filaCaracteres.listarTodosOsItensFila();
    }

    public static void enfileirarNomeCompleto() {
        System.out.print("Digite seu primeiro e segundo nome: ");
        String nome = teclado.nextLine();

        for (int i = 0; i < nome.length(); i++) {
            filaCaracteres.enfileirar(nome.charAt(i));
        }

        System.out.println("Caracteres enfileirados.");
    }

    public static void desenfileirarCaractere() {
        if (filaCaracteres.vazia()) {
            System.out.println("Fila esta vazia!");
            return;
        }

        System.out.println("Caractere desenfileirado: " + filaCaracteres.desenfileirar());
    }

    public static void contarOcorrenciasCaractere() {
        System.out.print("Digite o caractere a ser contado: ");
        String entrada = teclado.nextLine();
        if (entrada.isEmpty()) {
            System.out.println("Nenhum caractere informado.");
            return;
        }

        char caractere = entrada.charAt(0);
        int ocorrencias = filaCaracteres.contarOcorrencias(caractere);
        System.out.println("Ocorrencias de '" + caractere + "': " + ocorrencias);
    }

    public static void executarTestesFila() {
        int opcao;

        do {
            opcao = menuTeste();
            switch (opcao) {
                case 1 -> listarTodosOsCaracteresFila();
                case 2 -> enfileirarNomeCompleto();
                case 3 -> desenfileirarCaractere();
                case 4 -> contarOcorrenciasCaractere();
            }
            pausa();
        } while (opcao != 0);
    }

    public static void salvarPedidosEmArquivo() {
        try (PrintWriter arquivo = new PrintWriter(nomeArquivoPedidos, Charset.forName("UTF-8"))) {
            arquivo.print(filaPedidos.toString());
        } catch (IOException erro) {
            System.out.println("Nao foi possivel salvar os pedidos em arquivo.");
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in, Charset.forName("UTF-8"));

        nomeArquivoDados = "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);

        Pedido pedido = null;
        int opcao;

        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> mostrarProduto(localizarProduto());
                case 3 -> mostrarProduto(localizarProdutoDescricao());
                case 4 -> pedido = iniciarPedido();
                case 5 -> {
                    finalizarPedido(pedido);
                    pedido = null;
                }
                case 6 -> extrairLotePedidos();
                case 7 -> executarTestesFila();
            }
            pausa();
        } while (opcao != 0);

        salvarPedidosEmArquivo();
        teclado.close();
    }
}
