==============================================================Fonctionnement du client REST=============================================================

Afin de manipuler le client REST, il suffit de suivre les étapes suivantes:

1. Le projet comprend déjà un fichier "client.jar" qui peut être manipulé pour
   exécuter le client. Toutefois, si vous souhaitez modifier le client directement,
   il suffit de lancer la commande "ant clean" à partir d'un terminal dans le
   répertoire contenant le fichier "build.xml", puis par la suite reconstruire le
   client à partir de la commande "ant".

2. Une fois le fichier "client.jar" créé, il suffit d'ouvrir un terminal dans le
   répertoire où celui-ci se trouve, puis d'exécuter la commande suivante:

	./client.sh <URL du serveur à contacter> <Type de Requête> <Nom du fichier JSON contenant les factures à envoyer>

où:
	<URL du serveur à contacter> prend le format suivant: "http://<IP>:<PORT>/<REQUEST>" ou REQUEST peut être "add" pour ajouter une facture ou "get" pour recevoir les factures.
	<Type de Requête> prend les requêtes suivantes: "get" ou "post" (post correspondant à la requête "add")
	<Nom du fichier JSON...> prend le nom du fichier JSON (en indiquant l'extension) contenant les factures lors d'une requête "post"
				 (Celui-ci est moins utile pour une requête "get", mais à des fins de simplification, un nom de fichier
				 reste nécessaire).

Note: Les fichiers pour la requête "post" se trouvent à l'endroit suivant (à partir de la racine du projet): /Client/src/ca/polymtl/log8430/tp4/client/
      alors que le fichier de résultat ("result.txt") de la requête "get" devrait se trouver à la racine du projet.

=========================================================================================================================================================

=============================================================Fonctionnement de Spark=====================================================================

1. Dans le dossier Spark, aller dans le dossier ./sbin et ouvrez un terminal.
2. Entrez la commande ./start-master.sh
3. Pour vérifier que le master s'est bien lancé, ouvrez votre navigateur et mettez localhost:8000 comme url. Vous devriez voir le UI de Spark ainsi que l'URL de master.
4. Toujours dans votre terminal, entrer la commande "./start-slave.sh <URLMASTER>". URLMASTER est l'url qui se trouve sur le UI de Spark.

=========================================================================================================================================================

=============================================================Fonctionnement de Tomcat====================================================================


=========================================================================================================================================================
