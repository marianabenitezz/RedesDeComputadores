package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Arquivo;
import util.Status;

public class Client{
    public static void main(String[]args) throws UnknownHostException, IOException{
     
        try {        	
        	//cria conexão entre cliente e servidor
        	System.out.println("Estabelecendo conexão");
        	Socket socket = new Socket("localhost", 1125);
        	System.out.println("Conexão estabelecida");
        	
        	//cria streams I/O
        	ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        	ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        	System.out.println("Enviando mensagem...");
        	
        	/*Protocolo
        	 * Hello
        	 * nome: String
        	 * 
        	 * Helloreply
        	 * ok, erro, paramerror
        	 * mensagem: String
        	 * */
        	Arquivo arq = new Arquivo("Hello");
        	arq.setStatus(Status.SOLICITACAO);
        	arq.setParam("nome", "João");
        	arq.setParam("sobrenome", "Paulo");
        	
        	output.writeObject(arq);
        	output.flush(); //envio do final da mensagem
        	
        	System.out.println("Mensagem "+arq+" \nenviada!");
        	
        	arq =(Arquivo) input.readObject();
        	System.out.println("Resposta: "+arq);
        	
        	if(arq.getStatus() == Status.OK) {
        		String resposta = (String) arq.getParam("mensagem"); /**/
        		System.out.println("Mensagem: \n"+resposta);
        	}else {
        		System.out.println("Erro: "+ arq.getStatus());
        	}
        	
        	input.close();
        	output.close();
        	socket.close();
        	
        	
        } catch (IOException e) {
        	System.out.println("Erro no cliente "+e);
        	Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
        	System.out.println("Erro no cast "+ex.getMessage());
        	Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}

    }
}