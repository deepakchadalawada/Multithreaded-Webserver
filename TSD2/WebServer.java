import java.io.*;
import java.net.*;
import java.util.*;
// Here WebServer establish the listening socket and wait for the client to connect and starts a new thread for each request
public class WebServer
{
	static int count;
	private static ServerSocket server_listening_socket;
	public static void main(String args[]) throws Exception
	{
	
		int port;
		 Scanner in = new Scanner(System.in);
	      System.out.println("Enter port number greater than 1024");
	      port = in.nextInt();
	      System.out.println("Opening socket through port "+port);
	
		server_listening_socket = new ServerSocket(port);//listen on given port
		
		while (true)//listen for TCP connection req
		{
			try {
			Socket client_socket = server_listening_socket.accept();
        		Req_Handler http_req = new Req_Handler(client_socket);//creating an instance of HttpRequest class
        	
        		Thread thread = new Thread(http_req);//When we create a new thread of execution, we need to pass to the 
        		//Thread's constructor an instance of some class that implements the Runnable interface
        		// Start the thread.
        		
        		thread.start();
        		count+=1;
        		//Shows the thread count per each request and update the count according to the no of requests
        		System.out.println("Thread Count:  "+count);
        		
			}
			catch (Exception e){
				System.out.println(e);
			}
		}
	}
}
class Req_Handler implements Runnable//to pass instance of HttpRequest, it should implement Runnable interface
{
		Socket socket;
	
	public Req_Handler(Socket socket) throws Exception
	{
		this.socket = socket;
	}
	
	public void run()
	{
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//InputStream in_stream = socket.getInputStream(); 
			PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
			
			String input_string = buffer.readLine(); 
			System.out.println("Requested File: "+input_string);
		
			// Get and display the header lines
			/*String headerline = null;
			while ((headerline = br.readLine()).length() != 0)
			{
				System.out.println("headerline"+headerline);
			}*/
			
			String fileName="";
			StringTokenizer tokens = new StringTokenizer(input_string);
			try{
				
			
			if (tokens.hasMoreElements() && tokens.nextToken().equalsIgnoreCase("GET")
		            && tokens.hasMoreElements())
		          {fileName=tokens.nextToken();
					System.out.println("Tokened filename: "+fileName);}
			
		    else
		          throw new FileNotFoundException(
		        		  );
			
			while (fileName.indexOf("/")==0)
		          fileName=fileName.substring(1);
			System.out.println("Parsed filename: "+fileName);
			if (fileName.endsWith("/"))
		          fileName+="index.html";
			
			
			InputStream input_stream = null;
			
				input_stream = new FileInputStream(fileName);
			
			//Construct the response message.
					
			System.out.println("Searching whether this filetype(MIME) exits in the current directory or not ....");
				
				String file_type="text/plain";
				if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
					file_type ="text/html";
					System.out.println("HTML page found");
				}
				else if(fileName.endsWith(".gif") || fileName.endsWith(".GIF"))
				{
					file_type ="image/gif";
					System.out.println("GIF image found");
				}
				else if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
				{
					file_type ="image/jpeg";
					System.out.println("JPEG image found");
				}
				else if(fileName.endsWith(".java"))
				{
					file_type= "java file";
					System.out.println("Java file found");
				}
				else if(fileName.endsWith(".sh"))
				{
					file_type= "bourne/awk";
					System.out.println("Shell Scrit file found");
				}
				else if(fileName.endsWith(".class"))
				{
		
				file_type= "application/octet-stream";
				System.out.println(".Class file found");
				}
				else
					System.out.println(file_type+" Found");
				//contentTypeLine = "Content-type:" +
				//	contentType( fileName ) + Crg_reurn_ln_feed;
			
				byte[] data = new byte[1024];
				int bytes = 0;
				// Copy requested file into the socket's output stream.
				while((bytes=input_stream.read(data)) != -1) {
					out.write(data, 0, bytes);
				}
				out.close();
					
			
		} catch (FileNotFoundException e) {
			System.out.println(e);
			String s = fileName+" File not Found";
        
			out.println(s);
			 out.close();
			 e.printStackTrace();
		}

		}
		catch (IOException e) {
		      System.out.println(e);
		}
	
		catch (Exception e) {
		      System.out.println(e);
		}}}
