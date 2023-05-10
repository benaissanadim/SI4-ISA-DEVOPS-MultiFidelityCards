<b> <h2> I. Introduction: </b> </h2>

Notre architecture DevOps est composée des éléments suivants : 

1) Jenkins qui est notre outil d'automatisation de CI/CD .
2) Artifactory pour stocker les artifacts de notre projet . 
3) Sonar pour mesurer la qualité de code de notre projet ainsi d'autres métriques comme le coverage , code smells ... . 
4) L'outil Smee qui permet de faire la liaison entre Jenkins et Github pour pour pouvoir lancer des build à distance derrière 
un firewall . 


<b> <h2> II. Ce qui est réalisé :  </b> </h2>

On a réussi à mettre en place des conteneurs  Jenkins , Artifactory et Sonar sur notre VM à l'aide de l'outil docker-compose. Ainsi on a ajouté des agents Jenkins qui permettent d'éxecuter des pipelines dans un environnement spécifique .

Le premier agent "jenkins_agent" a été réalisé à l'aide de la méthode ssh et qui nous permet de récupérer l'image  jdk17 pour éxecuter les commandes maven dans nos pipelines Jenkins , ainsi on a utilisé notre machine virtuelle comme un agent Jenkins "vm_agent" afin d'éxecuter les commandes docker dans la pipeline en donnant la permission à l'utilisateur jenkins d'éxecuter ces commandes . 


On a mit en place une pipeline Multibranche vu qu'on utilise dans notre projet la branching stratégie Github flow qui consiste à créer une nouvelle branche pour chaque fonctionnalité et ensuite la fusionner dans master quand elle est stable , donc on a besoin de savoir pour chaque branch si notre build jenkins marche bien , notamment pour les pull request on a ajouté une protection pour la branche master afin de  vérifier le build jenkins avant merger , sinon on refuse le merge . 

<b> <h3> - Découpage de la pipeline Jenkins :  </b> </h3>

1) Lancement du Build et lancement des tests maven pour le Backend et la CLI .
2) Lancement de Sonar afin de mettre à jour le coverage et les métriques nécessaire (Vulnérabilités , bugs , code smells , ainsi pour visualiser les reponsabilités de chaque classe ) . 
3) Déploiement des artifacts  Backend et CLI et stockage dans notre repository jfrog artifactory lib-snapshot-local , récuperable dans ce lien : http://vmpx18.polytech.unice.fr:8012/artifactory/libs-snapshot-local/
4) Téléchargement des artefacts depuis Jfrog Artifactory 
5) Le build des images docker de Backend , CLI , Bank (à partir des artifacts téléchargés) , Notifier , Parking et la conservation des images dans notre repository local dockerhub avec un systèm de versionning docker, en utilisant la variable d'environnement $BUILD_ID pour tager les images docker . La construction des images ce fait à l'aide des étapes définies dans les Dockerfile ( multi-stage ) associé à chaque sous-système . 

Le processus de deploiement dans Artifactory et la construction des images est activé seuslement si on est dans la branche main qui contient une version stable du projet , ainsi si on a détecté qu'il y a un changement dans la directory correspondante ( Par exemple, on ne deploie pas et on ne construit pas d'images Notifier sauf  si on détecte un changement dans ./notifier ) . 

Ce système CI/CD nous permet de vérifier le build de notre projet à chaque modification pour s'assurer de la stabilité de notre développement , et pour conserver les versions de notre projet on stocke dans artefacts des jars avec des différentes versions , ainsi des versions images docker pour récuperer notre projet avec un comportement spécifique selon le besoin . 

<b> <h3>  - Endpoints et Credentials :   </h3> </b>

<b> 1) Jenkins :  </b>

	- http://vmpx18.polytech.unice.fr:8000/    
	- Credentials : isa   passwd : isa_1234
	

<b>  2) Artifactory :  </b>

	- http://vmpx18.polytech.unice.fr:8012/
	- Credentials :  admin  passwd : isa_DevOps_12

<b>  3) Sonar :   </b>

	- http://vmpx18.polytech.unice.fr:8009/
	- Credentials : admin   isa_12345

<b>  4) DockerHub :  </b>

	- https://hub.docker.com/repository/docker/hamza125/multi-fidelity/general
	- Credentials : login : hamza.zoubair@etu.unice.fr    passwd : isa_12345


<b> <h2> III. L'utilisation du Docker-Compose dans notre projet : </b> </h2>

L'interaction avec notre projet MultiFidelityCard ce fait à l'aide de l'outil Docker-Compose qui nous permet de définir et exécuter des applications Docker multi-conteneurs. Dans notre cas on a défini un  fichier docker-compose.yaml récupérable dans notre Repository Github dans la racine du projet , il est capable de monter tous les sous-système du projet ( Backend , Cli , Notifier , Parking , Bank , DataBase Postgres) avec un seule commande 'docker-compose up -d' , ensuite on peut interroger le système en allumant la CLI avec la commande 'docker attach cli' . 

En effet , on a 2 approches pour monter notre système :

La première consiste à construire les différentes images en local en lançant le scipt "build-all.sh" et par suite démarrer l'application avec "docker-compose up -d". 

La deuxième approche est de récupérer les images depuis le répertoire dockerhub  https://hub.docker.com/repository/docker/hamza125/multi-fidelity/general , cela nous permet d'optimiser le temps de faire le build local des images , ainsi de personnaliser notre projet à l'aide des versions des images docker . 

<b> <h2> IV. Plan de reprise d'activité : </b> </h2>

<b> <h3> 1) Problèmes liés à Jenkins / Smee / Artifactory / VM  : </b> </h3>

<b> - Jenkins : </b>

a) La déconnexion d’un agent  qui va impacter el fonctionnement des builds.
b) Agent VM ( machine virtuelle utilisée comme un agent Jenkins) : changement du mot de passe des la VM va impliquer la déconnexion de l'agent . 
c) Smee : la défaillance du serveur de Smee va couper la connexion entre Github et Jenkins pour effectuer des builds automatiques derriere le Firewall.

<b> - Artifactory : </b>

Problème de démarrage de Artifactory possible à la 1ère tentative ( cela nécessite le lancement du docker-compose up 2 fois ou plus) . 

<b> - Machine virtuelle :  </b>

Redémarrage de la VM ou bien coupure d'éléctrecité vont causer l'arret des conteneurs Docker . 


<b> <h3> 2) Procédures de redémarrage/reconstruction : </b> </h3>

- Redémarrage de Jenkins / Artifactory / Sonar  : 

	<b> a) Version manuelle : </b>

- Démarrer Jenkins : 
	. se placer dans le répertoire /home/teamc/ , et lancer docker-compose up -d , cela va démarrer les 2 conteneurs jenkins et agent (jdk17)  
	. Configuration du conteneur "agent" (jdk) : 
		* lancer un shell dans le container : sudo docker exec -it agent bash 
		* Installation des certificats SSL : apt-get update , apt-get install ca-certificates -y , update-ca-certificates . 
		* quitter le conteneur : exit 

- Démarrer Jfrog Artifactory : 

		. se placer dans le répertoire cd /home/teamc/artifactory/artifactory-oss-7.49.8 , et lancer docker-compose up -d , cela va démarrer le conteneur artifactory  

- Démarrer SonarQube : 
		
		. se placer dans le répertoire cd /home/teamc/sonar , et lancer docker-compose up -d , cela va démarrer le conteneur sonar  

	<b> b) Version automatisée : </b>

	. Lancer le script pra avec la commande : sh /home/teamc/pra.sh

- Fonctionnement du script : le principe de ce script est de ce placer dans le répertoire de chaque application et lancer le conteneur avec "docker-compose up -d" , puis lancer un shell dans le conteneur et installer les certificats SSL , donner la permission d'ecrire dans la directory /home/jenkins/.m2 dans la phase de déploiment des artefacts ( Pipeline), et enfin donner à l'utilisateur Jenkins de la VM la permission d'executer les commandes docker dans la pipeline à l'aide de l'agent VM . 


- En conséquence , le script peut redémarrer tous les conteneurs avec les configurations coreespondantes , ainsi la redémarrage d'un seul conteneur s'il est éteint 

<b> c) </b> Ainsi on a beneficié des volumes de docker pour persister nos changements à l'intérieur des conteneurs par exemple le fichier settings.xml dans l'agent jdk afin de pouvoir éxecuter "mvn deploy" dans l'agent . De meme pour Jenkins , la configuration est mappée dans /home/teamc/jenkins_compse ce qui permet de conserver les changements après redémarrage . 

	
<b> <h3> 3) Repartir d'une VM vierge : </b> </h3>

- Installation de docker et docker compose en suivant la documentation officielle de docker dans le lien suivant : https://docs.docker.com/engine/install/ubuntu/

- Mettre en place Jenkins avec un agent jdk17 en suivant la documentation cloudbees dans le lien suivant : https://www.cloudbees.com/blog/how-to-install-and-run-jenkins-with-docker-compose  . 

- Configurer Jenkins : la première fois il faut saisir le initialAdminPassword pour accéder à Jenkins , on peut le trouver en éxecutant un shell dans le conteneur Jenkins et éxecuter " cat /var/jenkins_home/secrets/initialAdminPassword " puis le copier dans l'interface Jenkins , ensuite il faut créer un utilisateur et installer les plugins suggéré par défaut . 


- Une fois Jenkins installé , il faut configurer Maven dans Jenkins dans en partant dans Administer Jenkins -> Configurer des outils globals-> Maven et choisir "install form Apache" en choisissant la version 3.6.3 .

- Ensuite il faut installer Jfrog Artifactory OSS à l'aide de  docker-compose en suivant cette documentation : https://jfrog.com/help/r/jfrog-installation-setup-documentation/install-artifactory-ha-with-docker-compose , il faut choisir une base de donée de type derby , ainsi changer le port dans le fichier ./artifactory-oss-7.49.8/.env  parceque la plage des ports utilisables sur la VM est entre 8000 et 8030 . 

- Ensuite il faut créer un repository Maven pour stocker les artefacts  en suivant cette documentation :  https://www.youtube.com/watch?v=MGXrPz9wwOY
https://www.jfrog.com/confluence/display/JFROG/Maven+Repository

- Installation de Sonarqube : L'installation se fait à l'aide du docker-compose dans le Repository Github ( ./devops/sonar/docker-compose.yaml) . 

- Installation de l'agent VM (Lancement de docker dans la pipeline )  : Il faut ouvrir l'interface de Jenkins puis partir dans la section Administer Jenkins -> Gérer les credentials  puis créer un credential avec la méthode "Utilisateur / mot de passe" en saisissant pour l'utilisateur :teamc et pour le passwd : vVLfD8nc4eR9QsCq , ensuite créer un noeud avec ce credential en  choisissant le mode SSH Agent . 

- Enfin lancer le script pra.sh mentionné precedement pour monter tous les conteneurs avec leurs configurations . 

<b> <h3> 4) Comment assurer une démo sans VM : </b> </h3>

Pour faire une démo de notre système sans VM , on peut juste récupérer les lien de Jenkins , Artifactory et Sonarqube qu'on a mentionné dans la partie II avec leurs credentials . 

Pour simuler le comprtement de la CI/CD , on doit juste faire un commit dans la branche  main en ajoutant un commentaire dans le backend et donc un build va démarrer , et on remarquera que tous les tests seront effectués et le deploiment dans Artifactory et Dockerhub . On peut vérifier on accédant à notre Artifactory et on compare la version du dernier snaphot avec la version récupérée dans la pipeline dans la phase "Download Jars from Jfrog" , et dans Dockerhub on trouvera qu'une image est ajouté récemment . 

<b> <h2> V. Retrospection : </b> </h2> 

<b> <h3> - Points de forces : </b> </h3> 

Notre système DevOps permet d'assurer un bon niveau d'integration continue en vérifiant les tests automatisés à chaque modifications Github dans les différentes Branches Git , ainsi un déploiment continu qui consiste à conserver les différentes versions de notre projet sous forme des images docker ainsi sous formes des jars (Snapshots) dans le repository Jfrog Artifactoy . 

Notre branching stratégie (Github flow) permet d'avoir un code stable en developpant chaque fonctionnalité dans une branche séparée et ensuite la fusionner dans la branche master quand elle est stable .

L'installation de SonarQube et l'jout de la pipeline nous aide à visualiser la qualité de code et les vulnérabilités , on peut béneficier ainsi du plugin SoftVis3d  pour améliorer notre projet  en terme de responsabilité de chaque composants . 


<b> <h3>  - Points de faiblesses : </b> </h3> 

Après avoir passé la soutenance finale , on a constaté qu'il y a certains points qu'on doit  améliorer dans notre système devOps : 

1) Ajouter une stratégie pour gérer les releases dans notre Pipeline . Pour l'instant on gère uniquement les snapshots qui sont défini comme une version non validée pour l'utilisation en production c'est à dire par  les utilisateurs . En effet on pourrait améliorer notre branching stratégie en ajoutant une branche "develop" qui sera moins stable que la branche "master" mais qui nous aidera à publier des Snaphots pour chaque nouvelle améliorations dans le projet , et donc quand la branche develop est totalement stable on peut merger dans la branche main qui nous servira à publier des releases qui seront déstinés à l'utilisateur , par suite on pourrait éliminer la partie des tests dans notre branche master parceque la validation est déja faite dans la branche develop. 

2) Ajouter un stratégie pour conserver une version du projet entier . Certes , on a différentes versions docker pour les sous-systèmes du projet mais ce qu'on veut améliorer c'est avoir une configuration avec les versions de chaque système pour personnaliser le comportement de notre application et donc faciliter la livraison du projet selon le besoin de l'utilisateur . 

3) Ajouter les conservations des Bundles pour les sous-systèmes Node dans Jfrog Artifactory . 










