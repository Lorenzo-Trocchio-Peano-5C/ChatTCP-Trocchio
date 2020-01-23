/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

import java.util.ArrayList;

/**
 *
 * @author Asus_X555LD
 */
public class EventoReceiver {

    //ultimo messaggio inviato dai Clients
    private String messaggio;
    //lista dei workers che viengono creati, uno per ogni Client connesso
    private final ArrayList<SocketWorker> workers = new ArrayList<>();

    //aggiungo il client alla lista
    void addListener(SocketWorker worker) {
        this.workers.add(worker);
    }

    //rimuovo il client dalla lista
    void removeListener(SocketWorker worker) {
        this.workers.remove(worker);
    }

    //chiamata dai vari Threads quando ricevono un messaggio da client
    //questo metodo e' sycronized per evitare conflitti tra workers
    //che desiderano accedere alla stessa risorsa (cioe' nel caso in cui
    // vengono ricevuti simultaneamente i messaggi da piu' clients)
    synchronized void setNewMessaggio(String m) {
        //aggiorna l'ultimo messaggio
        this.messaggio = m;
        //chiedi ad ogni worker di inviare il messaggio ricevuto
        this.workers.forEach((worker) -> {
            worker.sendMessaggio(this.messaggio);
        });
    }
}
