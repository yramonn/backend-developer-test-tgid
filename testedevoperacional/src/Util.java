import java.util.ArrayList;
import java.util.List;

public class Util {

    public static void escolhaProdutos(List<Produto> produtos, Integer escolhaEmpresa) {
        System.out.println("Escolha os seus produtos: ");
        for (Produto produto : produtos) {
            if (produto.getEmpresa().getId().equals(escolhaEmpresa)) {
                System.out.println(produto.getId() + " - " + produto.getNome());
            }
        }
        System.out.println("0 - Finalizar compra");
    }

    public static void mostrarMeusProdutos(List<Produto> produtos, Usuario usuarioLogado) {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("MEUS PRODUTOS");

        produtos.stream().forEach(produto -> {
            if (produto.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
                System.out.println("************************************************************");
                System.out.println("Código: " + produto.getId());
                System.out.println("Produto: " + produto.getNome());
                System.out.println("Quantidade em estoque: " + produto.getQuantidade());
                System.out.println("Valor: R$" + produto.getPreco());
                System.out.println("************************************************************");
            }
        });

        System.out.println("Saldo Empresa: " + usuarioLogado.getEmpresa().getSaldo());
        System.out.println("************************************************************");
    }

    public static Venda criarVenda(List<Produto> carrinho, Empresa empresa, Cliente cliente, List<Venda> vendas, List<Produto> produtos) {

        Double total = 0.0;
        boolean estoqueSuficiente = true;

        for (Produto produto : carrinho) {
            total += produto.getPreco();
            for (Produto produtoEstoque : produtos) {
                if (produtoEstoque.getId().equals(produto.getId())) {
                    int novaQuantidade = produtoEstoque.getQuantidade() - 1;
                    produtoEstoque.setQuantidade(novaQuantidade);
                    break;
                }
            }
        }

        for (Produto produto : carrinho) {
            for (Produto produtoEstoque : produtos) {
                if (produtoEstoque.getId().equals(produto.getId()) && produtoEstoque.getQuantidade() <= 0) {
                    estoqueSuficiente = false;
                    break;
                }
            }
            if (!estoqueSuficiente) {
                System.out.println("Desculpe, não há estoque suficiente para um ou mais produtos no carrinho.");
                return null;
            }
        }

        Double comissaoSistema = total * empresa.getTaxa();
        Double valorLiquido = total - comissaoSistema;

        int idVenda = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCodigo() + 1;
        Venda venda = new Venda(idVenda, carrinho.stream().toList(), total, comissaoSistema, empresa, cliente);
        empresa.setSaldo(empresa.getSaldo() + valorLiquido);
        vendas.add(venda);
        return venda;

    }


    public static void mostrarComprasEfetuadas(List<Venda> vendas, String username) {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("COMPRAS EFETUADAS");

        vendas.stream()
                .filter(venda -> venda.getCliente().getUsername().equals(username))
                .forEach(venda -> {
                    System.out.println("************************************************************");
                    System.out.println("Compra de código: " + venda.getCodigo() + " na empresa "
                            + venda.getEmpresa().getNome() + ": ");
                    venda.getItens().forEach(item -> {
                        System.out.println(item.getId() + " - " + item.getNome() + "    R$" + item.getPreco());
                    });
                    System.out.println("Total: R$" + venda.getValor());
                    System.out.println("************************************************************");
                });
    }



    public static void mostrarVendasEfetuadas(List<Venda> vendas, Usuario usuarioLogado) {
            System.out.println();
            System.out.println("************************************************************");
            System.out.println("VENDAS EFETUADAS");

            vendas.stream().forEach(venda -> {
                if (usuarioLogado.getUsername().equals("admin") || venda.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
                    System.out.println("************************************************************");
                    System.out.println("Venda de código: " + venda.getCodigo() + " no CPF "
                            + venda.getCliente().getCpf() + ": ");
                    venda.getItens().forEach(x -> {
                        System.out.println(x.getId() + " - " + x.getNome() + " Valor:   R$" + x.getPreco());
                    });
                    System.out.println("Total Venda: R$ " + venda.getValor());
                    System.out.println("Total Taxa a ser paga: R$ " + venda.getComissaoSistema());
                    System.out.println("Total Líquido  para empresa: R$ "
                            + (venda.getValor() - venda.getComissaoSistema()));
                    System.out.println("************************************************************");
                }
            });

            System.out.println("Saldo Empresa: " + usuarioLogado.getEmpresa().getSaldo());
            System.out.println("************************************************************");
        }

    public static List<Empresa> inicializarEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        Empresa empresa = new Empresa(1, "SafeWay Padaria", "30021423000159", 0.15, 0.0);
        Empresa empresa2 = new Empresa(2, "Level Varejo", "53239160000154", 0.05, 0.0);
        Empresa empresa3 = new Empresa(3, "SafeWay Restaurante", "41361511000116", 0.20, 0.0);
        empresas.add(empresa);
        empresas.add(empresa2);
        empresas.add(empresa3);
        return empresas;
    }

    public static List<Produto> inicializarProdutos(List<Empresa> empresas) {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto(1, "Pão Frances", 5, 3.50, empresas.get(0)));
        produtos.add(new Produto(9, "Croissant", 7, 6.50, empresas.get(0)));
        produtos.add(new Produto(10, "Chá Gelado", 4, 5.50, empresas.get(0)));
        produtos.add(new Produto(8, "Sonho", 5, 8.50, empresas.get(0)));

        produtos.add(new Produto(2, "Coturno", 10, 400.0, empresas.get(1)));
        produtos.add(new Produto(3, "Jaqueta Jeans", 15, 150.0, empresas.get(1)));
        produtos.add(new Produto(4, "Calça Sarja", 15, 150.0, empresas.get(1)));

        produtos.add(new Produto(5, "Prato feito - Frango", 10, 25.0, empresas.get(2)));
        produtos.add(new Produto(6, "Prato feito - Carne", 10, 25.0, empresas.get(2)));
        produtos.add(new Produto(7, "Suco Natural", 30, 10.0, empresas.get(2)));

        return produtos;
    }

    public static List<Cliente> inicializarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente("07221134049", "Allan da Silva", "cliente", 20));
        clientes.add(new Cliente("72840700050", "Samuel da Silva", "cliente2", 24));
        return clientes;
    }

    public static List<Usuario> inicializarUsuarios(List<Empresa> empresas, List<Cliente> clientes) {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("admin", "1234", null, null));
        usuarios.add(new Usuario("empresa", "1234", null, empresas.get(0)));
        usuarios.add(new Usuario("cliente", "1234", clientes.get(0), null));
        usuarios.add(new Usuario("cliente2", "1234", clientes.get(1), null));
        usuarios.add(new Usuario("empresa2", "1234", null, empresas.get(1)));
        usuarios.add(new Usuario("empresa3", "1234", null, empresas.get(2)));
        return usuarios;
    }
}

