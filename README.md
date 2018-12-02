# Fonctionnement de Spark

1. Télécharger Spark 2.4.0 https://spark.apache.org/downloads.html.
2. Décompresser l'archive.
3. Dans ce dossier spark, remplacer conf/spark-env.sh par le fichier spark-env.sh de ce repo.
4. Aller dans le dossier sbin et lancer les commandes:
    $ ./start-master.sh
    $ ./start-slave.sh <URL du master>

    L'url du master se trouve sur http://localhost:8065 une fois le master lancé.

# Fonctionnement de Tomcat

1. Décompresser le server Tomcat
2. À partir de factures-api/, lancer la commande suivante:
    $ mvn package
3. Prendre target/factures-api.war et le placer dans apache-tomcat-9.0.13/webapps
4. Prendre tp4.properties et le placer dans apache-tomcat-9.0.13/
    Ce fichier contient des propriétés importantes à remplir:
    SPARK_HOME: L'emplacement du fichier d'installation de Spark.
    SPARK_MASTER: L'url du master du cluster.
    FREQUENCY_JAR: L'emplacement de frequency-job.jar.
5. Démarrer le server avec la commande: 
    $ ./bin/startup.sh

# Fonctionnement du client REST

Afin de manipuler le client REST (situé dans le folder Client/), il suffit de suivre les étapes suivantes:

1. Le projet comprend déjà un fichier "client.jar" qui peut être manipulé pour
   exécuter le client. Toutefois, si vous souhaitez modifier le client directement,
   il suffit de lancer la commande "ant clean" à partir d'un terminal dans le
   répertoire contenant le fichier "build.xml", puis par la suite reconstruire le
   client à partir de la commande "ant".

2. Une fois le fichier "client.jar" créé, il suffit d'ouvrir un terminal dans le
   répertoire où celui-ci se trouve, puis d'exécuter la commande suivante:

	./client.sh <URL du serveur à contacter> <Type de Requête> <Fichier>

    où:
	<URL du serveur à contacter> prend le format suivant: "http://<IP>:8080/factures-api/webapi/resource/<REQUEST>" ou REQUEST peut être "add" pour ajouter une facture ou "get" pour recevoir les factures.
	<Type de Requête> prend les requêtes suivantes: "get" ou "post" (post correspondant à la requête "add")
	<Fichier> Pour une requête POST, le fichier JSON qui contient la facture à envoyée (avec l'extension). Lors d'une requête GET, le fichier qui recevra le résultat du GET.
