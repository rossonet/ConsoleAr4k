#!/bin/bash
# Installa l'ambiente di sviluppo e produzione
# per la console Ar4k
#
# TODO: implementare la parametrizzazione da interfaccia dai nodi
# by Ambrosini

piattaforma=$(uname -i)
dir_locale=$(pwd)

# Installa le dipendenze con sudo
verifica_git=$(git --version 2> /dev/null | grep -i git | wc -l)
if [ $verifica_git -ne 1 ]
then 
	echo "git non rilevato, installo..."
	if [ "$(whoami)" = "root" ]
	then
		yum install -y git
	else	
		echo "Sarà richiesta la password di root"
		su -c "yum install -y git"
	fi
fi


# Rileva la presenza di Java nel sistema
# ed eventualmente installa
verifica_java=$(java -version 2>&1 | grep '^Java(TM).*build 1.7.*$' | wc -l)
if [ $verifica_java -ne 1 ]
then 
	echo "Java non presente, installo..."
	mkdir .java_tmp

	verifica_wget=$(wget --version 2> /dev/null | wc -l)
	if [ $verifica_wget -lt 1 ]
	then 
		echo "wget non rilevato, è necessario per installare Java, procedo..."
		if [ "$(whoami)" = "root" ]
		then
			yum install -y wget
		else	
			echo "Sarà richiesta la password di root"
			su -c "yum install -y wget"
		fi
	fi

	# Scarico la JVM per l'architettura specifica
	cd .java_tmp
	verifica_architettura_x64=$(echo $piattaforma | grep -i 'x86_64' | wc -l)

	if [ $verifica_architettura_x64 -eq 1 ]	
	then
		wget 'http://boot.ar4k.net/ar4k/jdk-7u79-linux-x64.rpm'
	else
		wget 'http://boot.ar4k.net/ar4k/jdk-7u79-linux-i586.rpm'
	fi

	# Scarica la crittografia full per Java
	wget 'http://boot.ar4k.net/ar4k/UnlimitedJCEPolicyJDK7.zip'
	unzip UnlimitedJCEPolicyJDK7.zip
	

	if [ "$(whoami)" = "root" ]
	then
		yum localinstall -y --nogpg jdk-7u79-linux-*
		mv -f UnlimitedJCEPolicy/US_export_policy.jar UnlimitedJCEPolicy/local_policy.jar /usr/java/latest/jre/lib/security/
	else	
		echo "Sarà richiesta la password di root"
		su -c "yum localinstall -y --nogpg jdk-7u79-linux-* ; mv -f UnlimitedJCEPolicy/US_export_policy.jar UnlimitedJCEPolicy/local_policy.jar /usr/java/latest/jre/lib/security/"
	fi
	cd $dir_locale
	rm -rf .java_tmp
fi

# Configura la variabile d'ambiente JAVA_HOME se non configurata
if [ " $(echo $JAVA_HOME)" = " " ]
then
	echo "Configuro JAVA_HOME a /usr/java/latest"
	export JAVA_HOME=/usr/java/latest
	echo "export JAVA_HOME=/usr/java/latest" >> ~/.bash_profile
else
	echo "JAVA_HOME trovata"
	echo $JAVA_HOME
fi

# Scarica il pacchetto git
git clone https://github.com/rossonet/ConsoleAr4k.git

# lancia la compilazione
cd ConsoleAr4k
#./compila.sh
# lancia l'applicativo - se possibile, verrà lanciato il default browser alla fine dell'operazione -
#./ar4k.sh
./grailsw run-app -Drossonet.codattivazione=$1

exit 0
