#ConsoleAr4k
Console per la gestione di dell'insfrastruttura Ar4k.

![alt text](http://www.rossonet.org/wp-content/uploads/2015/01/logoRossonet4.png "Rossonet")

[http://www.rossonet.org](http://www.rossonet.org)

Licenza: [LGPL 3.0](https://www.gnu.org/licenses/lgpl.html)
Per maggiori dettagli sulla licenza rimando a [questa voce](http://it.wikipedia.org/wiki/GNU_Lesser_General_Public_License) di Wikipedia

![alt text](https://www.gnu.org/graphics/gplv3-88x31.png "LGPL Logo")

##Guida rapida per il deploy

La [spin Rossonet](http://www.rossonet.org/archives/94) di [Fedora 21](http://it.wikipedia.org/wiki/Fedora_%28informatica%29) è predisposta per contenere tutti gli strumenti che abitualmente usiamo in Rossonet.

###Compatibilità

La procedura illustrata vale per macchine CentOS/RedHat/Fedora (il codice è per una sessione di [Bash](https://it.wikipedia.org/wiki/Bash)) 

###Installazione automatica

Per installare in automatico tutto il sistema compreso le dipendenza,
utilizzare il seguente comando.
Se non eseguito con privilegi di root, verrà chiesta l'autenticazione 
per installare Java e git.

```bash
sh <(curl -L -s http://boot.ar4k.net/interfaccia) [cod.attivazione]
```

Se "curl" non fosse presente nel sistema, installarlo con:
```bash
yum install curl
```

###Installazione manuale

Per scaricare l'intero sistema:
```bash
git clone https://github.com/rossonet/ConsoleAr4k.git
```

Per lavorare con git in bash:
```bash
git config --global push.default matching
git config credential.helper store
```

Per creare un'applicazione in un unico file .jar con tutte le librerie incluse e Tomcat 7 integrato:
```bash
./compila.sh
```
esecuzione:
```bash
./ar4k.sh
```

Per aggiornare tutto il progetto e eseguirlo in ambiente di sviluppo (Ctrl-C per interrompere l'esecuzione):
```bash
./rigenera.sh && ./grailsw run-app
```

Per eseguirlo in ambiente di sviluppo (Ctrl-C per interrompere l'esecuzione):
```bash
./grailsw run-app
```

Per eseguire i test:
```bash
./grailsw test-app
```

Per creare un war installabile su Tomcat >= 7
```bash
./grailsw war
```
##Documentazione progetto

[Classi Groovy](http://rossonet.github.io/ConsoleAr4k/web-app/docs/gapi/index.html)

[Classi Java](http://rossonet.github.io/ConsoleAr4k/web-app/docs/api/index.html)

##Avvio in Cloud
è possibile avviare l'installazione in cloud direttamente su Amazon AWS o scaricare un'app virtuale utilizzabile con Oracle VirtualBox o VMware.
L'installazione è basata su un sistema operativo CentOS 6.x preconfigurato. [Il codice sorgente degli script per la gestione delle immagini in cloud utilizzato è disponibile su GitHub](https://github.com/rossonet/Strumenti-RCloud).

###Sistema operativo APP virtuali
<img src="http://www.rossonet.org/wp-content/uploads/2015/01/centOS_logo.png" align="left" width="150px" alt="CentOS" >
Il sistema operativo CentOS, basato su RedHat Enterprise Linux, è completamente open source. [Tutti i dettagli del progetto sono disponibili nel sito www.centos.org](https://www.centos.org/), una visione d'insieme del progetto e della sua storia è [disponibile su Wikipedia](https://it.wikipedia.org/wiki/CentOS). Il codice sorgente delle nostre personalizzazioni [è disponibile in questo repository git](https://github.com/rossonet/Strumenti-RCloud).

###App virtuale Amazon AWS
<img src="http://www.rossonet.org/wp-content/uploads/2015/01/AmazonWebservices_Logo.svg_.png" align="right" width="150px" alt="AWS" >
L'app virtuale preparata per l'esecuzione in Amazon AWS al primo avvio scarica la versione più recente di Ar4kConsole, la compila e avvia il server.

###App in formato OVA
<img src="http://www.rossonet.org/wp-content/uploads/2015/01/virtualbox-ova-512px.png" align="left" width="150px" alt="OVA" >
Il formato OVA è uno standard aperto per il confezionamento di macchine virtuali. In particolare è possibili importare l'applicazione virtuale in formato OVA in ambienti di virtualizzazione VMware e VirtualBox. [Maggiori dettagli su Wikipedia](https://it.wikipedia.org/wiki/Open_Virtualization_Format)

###Oracle VirtualBox
<img src="http://www.rossonet.org/wp-content/uploads/2015/11/virtualbox2.png" align="right" width="150px" alt="VirtualBox" >
Oracle VirtualBox è un software open source che permette di eseguire sistemi operativi virtuali su piattaforme i386/x86. In pratica permette di ospitare computer simulati via software, le macchine virtuali, su sistemi operativi Windows,Linux e Macintosh.
Maggiori dettagli e le procedure di installazioni sono [disponibili online](https://www.virtualbox.org/) 

