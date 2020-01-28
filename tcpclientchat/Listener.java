/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Asus_X555LD
 */
class Listener implements Runnable {

    private Socket client;

    //Constructor
    Listener(Socket socketClient) {
        this.client = socketClient;
        System.out.println("In ascolto con: " + socketClient);
    }

    public void run() {

        // connetti al socket per poter leggere i dati che arrivano al Client
        // (client <-- server)
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.out.println("IO Error!!!");
            System.exit(-1);
        }

        //scrivi su terminale il testo ricevuto
        String testoDaServer = "";
        try {
            while ((testoDaServer = br.readLine()) != null) {
                System.out.println(testoDaServer);
                //nel caso il testo ricevuto dal Server contiene "Bye." termina il Client
                if (testoDaServer.contains("Bye.")) {
                    client.close();
                    System.exit(0);
                    break;
                }
            }
        } catch (IOException e) {
            try {
                System.out.println("Connessione terminata dal Server");
                client.close();
                System.exit(-1);
            } catch (IOException ex) {
                System.out.println("Error nella chiusura del Socket");
                System.exit(-1);
            }
        }
    }
}
