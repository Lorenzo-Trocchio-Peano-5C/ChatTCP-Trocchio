/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus_X555LD
 */
class SocketWorker implements Runnable, EventoListener, EventoPublisher {

    ServerSocket serverS;
    private GUIServer gui;
    private PrintWriter pw;
    EventoReceiver receiver;

    //Constructor: inizializza le variabili
    SocketWorker(int numeroPorta, GUIServer gui) {
        gui.appendMessage("Il server è avviato sulla porta " + numeroPorta);
        try {
            this.gui = gui;
            serverS = new ServerSocket(numeroPorta);
        } catch (IOException ex) {
            Logger.getLogger(SocketWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    //Questo metodo e' invocato dal metodo setNewMessaggio nella 
    //classe EventReceiver
    //e rappresenta la richiesta di inviare il messaggio che e' stato appena 
    //ricevuto da uno dei client connessi
    public void sendMessaggio(String messaggio) {
        //Invia lo stesso messaggio appena ricevuto 
        pw.println("Server->> " + messaggio);
    }

    // Questa e' la funzione che viene lanciata quando il nuovo "Thread" viene generato
    public void run() {
        Socket sock = null;
        try {
            sock = serverS.accept();
            BufferedReader in = null;
            try {
                // connessione con il socket per ricevere (in) e mandare(out) il testo
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                pw = new PrintWriter(sock.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Errore: in|out fallito");
                System.exit(-1);
            }

            String line = "";
            int clientPort = sock.getPort(); //il "nome" del mittente (client)
            while (line != null) {
                try {
                    line = in.readLine();
                    gui.appendMessage("\nDa: " + sock.getInetAddress() + "\n" + line + "\n");
                    System.out.println(clientPort + ">> " + line);
                } catch (IOException e) {
                    System.out.println("Lettura da socket fallito");
                    System.exit(-1);
                }
            }
            try {
                sock.close();
                System.out.println("Connessione con client: " + sock + " terminata!");
            } catch (IOException e) {
                System.out.println("Errore connessione con client: " + sock);
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketWorker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            gui.appendMessage("La connessione con " + sock.getInetAddress() +" è stata chiusa");

        }
    }

    @Override
    public void registraReceiver(EventoReceiver r) {
        this.receiver = r;
    }

    @Override
    public void messaggioReceived(String m) {
        this.receiver.setNewMessaggio(m);
    }
}

interface EventoListener {

    public void sendMessaggio(String m);
}

interface EventoPublisher {

    public void registraReceiver(EventoReceiver r);

    public void messaggioReceived(String m);
}
