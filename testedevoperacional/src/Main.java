import java.util.*;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {

		// SIMULANDO BANCO DE DADOS

		List<Produto> carrinho = new ArrayList<Produto>();
		List<Venda> vendas = new ArrayList<Venda>();

		List<Empresa> empresas = Util.inicializarEmpresas();
		List<Produto> produtos = Util.inicializarProdutos(empresas);
		List<Cliente> clientes = Util.inicializarClientes();
		List<Usuario> usuarios = Util.inicializarUsuarios(empresas, clientes);

		executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
	}

	public static void executar(List<Usuario> usuarios, List<Cliente> clientes, List<Empresa> empresas,
			List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Entre com seu usuário e senha:");
		System.out.print("Usuário: ");
		String username = sc.next();
		System.out.print("Senha: ");
		String senha = sc.next();

		List<Usuario> usuariosSearch = usuarios.stream().filter(x -> x.getUsername().equals(username))
				.collect(Collectors.toList());
		if (usuariosSearch.size() > 0) {
			Usuario usuarioLogado = usuariosSearch.get(0);
			if ((usuarioLogado.getSenha().equals(senha))) {

				System.out.println("Escolha uma opção para iniciar");
				if (usuarioLogado.IsEmpresa()) {
					System.out.println("1 - Listar vendas");
					System.out.println("2 - Ver produtos");
					System.out.println("0 - Deslogar");
					Integer escolha = sc.nextInt();

					switch (escolha) {

					case 1: {
						Util.mostrarVendasEfetuadas(vendas, usuarioLogado);
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}
					case 2: {
						Util.mostrarMeusProdutos(produtos, usuarioLogado);
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}
					case 0: {
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}

					}

				} else {
					System.out.println("1 - Relizar Compras");
					System.out.println("2 - Ver Compras");
					System.out.println("0 - Deslogar");
					Integer escolha = sc.nextInt();
					switch (escolha) {
					case 1: {
						System.out.println("Para realizar uma compra, escolha a empresa onde deseja comprar: ");
						for (Empresa empresa : empresas) {
							System.out.println(empresa.getId() + " - " + empresa.getNome());
						}

						Integer escolhaEmpresa = sc.nextInt();
						Util.escolhaProdutos(produtos, escolhaEmpresa);
						Integer escolhaProduto;
						boolean produtoEncontrado = false;

						do {
							System.out.print("Escolha um produto (ou 0 para sair): ");
							escolhaProduto = sc.nextInt();

							for (Produto produto : produtos) {
								if (produto.getId().equals(escolhaProduto) && produto.getEmpresa().getId().equals(escolhaEmpresa)) {
									carrinho.add(produto);
									produtoEncontrado = true;
									break;
								}
							}

							if (!produtoEncontrado && escolhaProduto != 0) {
								System.out.println("Produto não encontrado ou não pertence à empresa escolhida.");
							}

						} while (escolhaProduto != 0 && !produtoEncontrado);

						System.out.println("************************************************************");
						System.out.println("Resumo da compra: ");
						carrinho.stream().forEach(x -> {
							if (x.getEmpresa().getId().equals(escolhaEmpresa)) {
								System.out.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
							}
						});
						Empresa empresaEscolhida = empresas.stream().filter(x -> x.getId().equals(escolhaEmpresa))
								.collect(Collectors.toList()).get(0);
						Cliente clienteLogado = clientes.stream()
								.filter(x -> x.getUsername().equals(usuarioLogado.getUsername()))
								.collect(Collectors.toList()).get(0);
						Venda venda = Util.criarVenda(carrinho, empresaEscolhida, clienteLogado, vendas, produtos);
						System.out.println("Total: R$" + venda.getValor());
						System.out.println("************************************************************");
						carrinho.clear();
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}

					case 2: {
						Util.mostrarComprasEfetuadas(vendas, usuarioLogado.getUsername());
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}
					case 0: {
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}

					}
				}
			} else
				System.out.println("Senha incorreta");
		} else {
			System.out.println("Usuário não encontrado");
		}
	}
}
