package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
	static Socket cliente;

	public static void plot() throws UnknownHostException, IOException {
		Scanner tc = new Scanner(System.in);
		System.out.println("Estabelecendo conexão");
		DataOutputStream dos;
		DataInputStream in;
		System.out.println("Conexão estabelecida");
		System.out.println("Digite \"1\" para listar o diretorio do servidor.");
		System.out.println("Digite \"2\" para criar um novo diretorio no servidor.");
		System.out.println("Digite \"3\" para apagar algum diretorio do servidor.");
		System.out.println("Digite \"4\" para enviar algum arquivo para o servidor.");
		System.out.println("Digite \"5\" para apagar algum arquivo do servidor.");
		System.out.println("Digite \"6\" para finalizar.");
		try {
			switch (tc.nextInt()) {
			case 1:
				cliente = new Socket("localhost", 1125);
				System.out.println("O diretorio do padrão do servidor fica no seguinte caminho: \"c:/servidor\"");
				System.out.println("Enviando requisição de listagem para o servidor!\n");

				 dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF("listar");
				dos.flush();

				in = new DataInputStream(cliente.getInputStream());
				String listagemDiretorios = in.readUTF();
				System.out.println(listagemDiretorios);
				cliente.close();

				plot();
			case 2:
				cliente = new Socket("localhost", 1125);
				System.out.println("O diretorio padrão do servidor fica no seguinte caminho: \"c:/servidor\"");
				System.out.println("Todos os diretorios criados serão criados dentro da pasta servidor");

				System.out.print("Por gentileza, escreva o nome do novo diretorio: ");
				String nomePasta = tc.next();

				System.out.println("Enviando requisição para criar diretorio no servidor!\n");


				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF("criarDir");
				dos.flush();
				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF(nomePasta);
				dos.flush();

				in = new DataInputStream(cliente.getInputStream());
				String retorno = in.readUTF();
				System.out.println(retorno);

				plot();
			case 3:
				System.out.println("O diretorio do padrão do servidor fica no seguinte caminho: \"c:/servidor\"");
				System.out.println("O diretorio que será apagado deve estar na pasta servidor");
				
				System.out.println("Por gentileza, digite o nome do diretorio que deseja ser deletado:");
				String diretorioParaDelecao = tc.next();
				

				cliente = new Socket("localhost", 1125);
				System.out.println("Enviando requisição para deletar diretorios no servidor!\n");
				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF("deletarDiretorio");
				dos.flush();
				System.out.println("Enviando nome do diretorio..");

				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF(diretorioParaDelecao);
				dos.flush();
				System.out.println("Resposta do servidor");
				in = new DataInputStream(cliente.getInputStream());
				 retorno = in.readUTF();
				System.out.println(retorno);
				cliente.close();
				plot();

			case 4:
				System.out.println("Digite o caminho onde se encontra o arquivo, ex.: c:/cliente/lab00.pdf");
				String caminho = tc.next();

				System.out.println("O diretorio do padrão do servidor fica no seguinte caminho: \"c:/servidor\"");
				System.out.println("Digite um novo diretorio ou digite \"c\" para continuar no diretorio padrao");
				String dir = tc.next();

				if (dir.equalsIgnoreCase("c"))
					dir = "c:\\servidor\\";

				File f;
				FileInputStream file;

				if (caminho.equals("c")) {
					f = new File("c:/cliente/lab00.pdf");
					file = new FileInputStream("c:/cliente/lab00.pdf");
				} else {
					caminho= caminho.replace("\\", "/");
					f = new File(caminho);
					file = new FileInputStream(caminho);
				}

				if (caminho.equalsIgnoreCase("c"))
					caminho = "c:/servidor/";

				cliente = new Socket("localhost", 1125);
				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF("enviarArquivo");
				dos.flush();

				dos = new DataOutputStream(cliente.getOutputStream());
				System.out.println(f.getName());
				String var = f.getName();
				dos.writeUTF(var);
				dos.flush();

				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF(dir);
				dos.flush();
				
				ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
				byte[] buffer = new byte[4096];
				System.out.println("Enviando arquivo...");
				while (true) {
					int tamanho = file.read(buffer);
					if (tamanho == -1)
						break;
					output.write(buffer, 0, tamanho);
				}

				System.out.println("Arquivo enviado.");
				output.close();
				cliente.close();

				plot();

			case 5:
				System.out.println("O diretorio do padrão do servidor fica no seguinte caminho: \"c:/servidor\"");
				System.out.println("O arquivo que será apagado deve estar na pasta servidor");
				
				
				System.out.println("Por gentileza, digite o nome do diretorio que deseja ser deletado:");
				String arquivoParaDelecao = tc.next();
				
				
				cliente = new Socket("localhost", 1125);
				System.out.println("Enviando requisição para deletar arquivos no servidor!\n");
				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF("deleteArquivo");
				dos.flush();
				System.out.println("Enviando nome do arquivo..");

				dos = new DataOutputStream(cliente.getOutputStream());
				dos.writeUTF(arquivoParaDelecao);
				dos.flush();
				
				in = new DataInputStream(cliente.getInputStream());
				 retorno = in.readUTF();
				System.out.println(retorno);
				cliente.close();
				plot();

			case 6:
				System.out.println("Programa finalizado com sucesso.");
				System.exit(0);

			default:
				System.out.println("Numero invalido, digite novamente");
				plot();

			}

		} catch (InputMismatchException inputException) {
			System.err.println("Entrada invalida:");
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, inputException);
			System.out.print("Aperte \"Enter\" para continuar.");
			tc.nextLine();
			tc.nextLine().matches("\n");
			plot();
		}
	}

	public static void main(String[] args) {

		try {

			plot();

		} catch (IOException e) {
			System.out.println("Erro no cliente " + e);
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
		}

	}
}