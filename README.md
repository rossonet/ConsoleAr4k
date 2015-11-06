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

[API Groovy](http://rossonet.github.io/ConsoleAr4k/web-app/docs/gapi/index.html)
[API Java](http://rossonet.github.io/ConsoleAr4k/web-app/docs/api/index.html)
