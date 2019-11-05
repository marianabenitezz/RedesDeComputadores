package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {
	private ServerSocket sever;
	private static String caminhoDir = "c:\\servidor";

	private void criarServerSocket(int porta) throws IOException {
		sever = new ServerSocket(porta);
	}

	private Socket esperaConexao() throws IOException {
		Socket socket = sever.accept();
		return socket;
	}

	public static String visualizarArquivos() throws IOException {
		StringBuilder b = new StringBuilder();
		b.append("");
		Path dir = Paths.get(caminhoDir);
		Files.walk(dir).forEach(Files -> {
			b.append(Files.toString() + "\n");
		});

		return b.toString();
	}

	public static void deleteArquivos(File listaDelecao) {
		if (listaDelecao.isFile()) {
			listaDelecao.delete();
		} else {
			File files[] = listaDelecao.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteArquivos(files[i]);
			}
			
		}
		if(listaDelecao.exists())
		listaDelecao.delete();
	}

	private void fechaSocket(Socket s) throws IOException {
		s.close();
	}

	public static void main(String[] args) {
		
		File diretorio = new File(caminhoDir);
		if(!diretorio.exists())
		diretorio.mkdir();
		try {

			while (true) {

				ServerSocket server = new ServerSocket(1125);
				System.out.println("Aguardadndo conexao");
				System.out.println("Servidor aguardando...");
				DataOutputStream dos;

				Socket ser = server.accept();

				DataInputStream in = new DataInputStream(ser.getInputStream());
				String mensagem = in.readUTF();

				switch (mensagem) {
				case "listar":

					System.out.println("Requisição de listagem dos diretorios do servidor:");
					dos = new DataOutputStream(ser.getOutputStream());
					String lista = visualizarArquivos();
					dos.writeUTF(lista);
					dos.flush();
					server.close();

					break;
				case "criarDir":

					System.out.println("Requisição para criar diretorio no servidor!");
					in = new DataInputStream(ser.getInputStream());
					String nomeDiretorio = in.readUTF();

					// criar diretorio
					 diretorio = new File(caminhoDir+"\\" + nomeDiretorio);
					diretorio.mkdir();

					// printa nova lista de diretorios
					dos = new DataOutputStream(ser.getOutputStream());
					String novaLista = visualizarArquivos();
					dos.writeUTF(novaLista);

					dos.flush();
					server.close();

					break;

				case "deleteArquivo":
					System.out.println("recebendo diretorio do arquivo para deleção...");
					System.out.println();
					in = new DataInputStream(ser.getInputStream());
					String nomeDir = in.readUTF();
					System.out.println("Arquivo: " + nomeDir);
					File arquivoPraDelecao = new File(caminhoDir+"\\" + nomeDir);
					if (arquivoPraDelecao.isFile()) {
						arquivoPraDelecao.delete();
						System.out.println("Arquivo deletado");
					} else
						System.out.println("Arquivo não existe");

					dos = new DataOutputStream(ser.getOutputStream());
					novaLista = visualizarArquivos();
					dos.writeUTF(novaLista);
					dos.flush();
					server.close();
					break;
				case "deletarDiretorio":
					System.out.println("recebendo diretorio para deleção...");
					System.out.println();
					in = new DataInputStream(ser.getInputStream());
					nomeDir = in.readUTF();
					System.out.println("Diretorio: " + caminhoDir+"\\" + nomeDir);
					server.close();
					File dirToDelete = new File(caminhoDir+"\\" + nomeDir);
					if (dirToDelete.exists()) {
						deleteArquivos(dirToDelete);
						System.out.println("Diretorio deletado");
					} else
						System.out.println("Diretorio não existe");
					dos = new DataOutputStream(ser.getOutputStream());
					novaLista = visualizarArquivos();
					dos.writeUTF(novaLista);
					dos.flush();
					server.close();
					break;

				case "enviarArquivo":
					System.out.println("recebendo nome do arquivo...");
					in = new DataInputStream(ser.getInputStream());
					String nomeArquivo = in.readUTF();
					System.out.println(nomeArquivo);

					System.out.println("recebendo diretorio do arquivo...");

					in = new DataInputStream(ser.getInputStream());
					String dir = in.readUTF();
					System.out.println(dir);

					ObjectInputStream input = new ObjectInputStream(ser.getInputStream());

					FileOutputStream file;
					file = new FileOutputStream(dir + nomeArquivo);

					byte[] buffer = new byte[4096];
					System.out.println("recebendo arquivo...");
					while (true) {
						int tamanho = input.read(buffer);
						if (tamanho == -1)
							break;
						file.write(buffer, 0, tamanho);
					}

					DataOutputStream out = new DataOutputStream(ser.getOutputStream());
					out.writeUTF("sua mensagem foi recebida");// retornando mensagem
					server.close();
					break;

				}


			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}