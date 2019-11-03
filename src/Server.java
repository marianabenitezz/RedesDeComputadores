package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;




import util.Arquivo;
import util.Status;


public class Server{
    private ServerSocket sever ;

    private void criarServerSocket(int porta) throws IOException{
        sever = new ServerSocket(porta);
    }
    private Socket esperaConexao() throws IOException {
    Socket socket=	sever.accept();
    return socket;
    }
    public void trataConexao(Socket socket) throws IOException, ClassNotFoundException {
    	try {
    		
			ObjectOutputStream output = new ObjectOutputStream( socket.getOutputStream());
	    	ObjectInputStream input = new ObjectInputStream(socket.getInputStream()) ;
	    //	input.read
	    	Arquivo msg =(Arquivo) input.readObject();
	    	String operacao =msg.getOperação();
	    	if(operacao.equals("Hello")) {
	    		
	    		String nome =  (String)msg.getParam("nome");
		    	String sobrenome =  (String)msg.getParam("sobrenome");
		    	Arquivo reply  = new Arquivo("Helloreply");

		    	if(nome==null||sobrenome==null)
	    			reply.setStatus(Status.PARAMERROR);
		    	else {
		    	reply.setStatus(Status.OK);

		    	reply.setParam("mensagem", "Helloworld, "+nome+" "+sobrenome);
	    	}
		    	}
	    	/*String nome =  (String)msg.getParam("nome");
	    	String sobrenome =  (String)msg.getParam("sobrenome");*/

	    	System.out.println("mensagem recebida..");
	    	output.writeUTF("Hello world");
	    	output.flush();
    		input.close();
    		output.close();
    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
	    	System.out.println("problema no tratamento de conexao");
	    	System.out.println("erro :"+e.getMessage());


		}
    	finally {
    		fechaSocket(socket);
    	}
   
    }
    private void fechaSocket(Socket s) throws IOException {
    	s.close();
    }
    public static void main(String [] args) {
    	try {   
    		Server server = new Server();
    		System.out.println("Aguardadndo conexao");
			server.criarServerSocket(1125); 
    		System.out.println(" cliente conectado");
    		while(true) {
			Socket socket = server.esperaConexao();
			server.trataConexao(socket);
    		System.out.println("cliente finalizado");
    		}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch(ClassNotFoundException ea) {
    		
    	}
    }
}