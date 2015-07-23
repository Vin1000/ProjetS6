# ProjetS6 - SearchUs
Moteur de recherche permettant de chercher dans un FileSystem basé sur ElasticSearch

## Installation d'ElasticSearch
Version utilisé : 1.3.8

Installer les plugin Fs-River et JS-Lang de la façon suivante :
```bash
/usr/share/elasticsearch/bin/plugin -install fr.pilato.elasticsearch.river/fsriver/1.3.1
/usr/share/elasticsearch/bin/plugin -install elasticsearch/elasticsearch-lang-javascript/2.3.1
```

## Creation de l'index
1. Téléverser les fichiers à indexé dans le dossier /var/www/files
2. Installer NodeJs sur la machine
2. Dossier tools/ElasticSearch/serverBootstrap
3. Executer la commande "npm install" pour aller chercher toute les dependences (NOTE : Ne pas commiter le dossier node_modules)
4. Executer la commande "node createIndex.js IP=XXX.XXX.XXX.XXX" (L'ip est optionnel, si aucune ip, utilise l'ip du serveur par defaut definit dans le fichier createIndex.js)

Note : Sur linux, le package node est nommé nodejs.

##Database
**Set up windows local:**
- Créer une bd nommée Opus
- Exécuter le fichier tools\Database\CreateDbStructure.sql

**Set up Linux:**
- Aller dans tools\Database en command line (modifier le script FullSetUp.sh si Postgres est installé ou pas)
- Exécuter les commandes:
    - chmod a+x FullSetUp.sh
    - ./FullSetUp.sh

Pour ajouter un fichier, exécuter la ligne suivante (changer le 0 pour 1 pour admin only)
add_test_file(cip, IsForAdminOnly, label, path, description)
```SQL
SELECT file.add_test_file('babm2002', 0, 'NewFile', 'C:\temp\NewFile.win','funny file');
```

Pour sortir une liste de fichiers pour lesquels un user a des permissions à partir d'une liste de path retournés par Elastic Search
utiliser la fonction file.get_AllowedFilesFromList(cip, pathList)
voir l'exemple suivant pour le format de la liste de paths
```SQL
SELECT * FROM file.get_AllowedFilesFromList('babm2002', '''C:\temp\NewFile.win'', ''C:\temp\TestFilev2''');
```

Si on veut retourner la liste entière paths auxquels un user a accès
utiliser la fonction file.get_currentVersionFiles(cip)
```SQL
SELECT * FROM file.get_currentVersionFiles('babm2002');
```

# Pour se connecter à la BD : une fois sur la VM
sudo -u postgres psql postgres;
\c Opus;

Pour ajouter un utilisateur par ici les amis
```SQL
INSERT INTO users(administrative_user_id, last_name, first_name, email_address) 
VALUES ('abcd1234', 'TEST', 'TEST', 'TEST@Test.com');

INSERT INTO user_group(member_id, group_id, user_id)
SELECT user_id ,2 ,1
FROM users WHERE administrative_user_id = ('abcd1234');
```

Pour ajouter les privillèges d'administrateur à un utilisateur, exécuter la commande suivante
```SQL
INSERT INTO user_group(member_id, group_id, user_id)
SELECT user_id ,1 ,1
FROM users WHERE administrative_user_id = ('abcd1234');
```

Pour enlever les privillèges d'administrateur à un utilisateur exécuter la commande suivante
```SQL
DELETE from user_group ug 
WHERE ug.group_id = 1 
	AND ug.member_id in(SELECT user_id from users where administrative_user_id = 'abcd1234');
```

##Selenium
**Set up windows local:**
- Aller dans le dossier tools\Selenium\server\
- Exécuter startServer.bat

**Set up Linux:**
- Aller dans le dossier tools\Selenium\server\
- exécuter la commande java -jar selenium-server-standalone-2.45.0.jar

##Utilisation de boostrap for gwt

1. ajouter xmlns:b="urn:import:com.github.gwtbootstrap.client.ui" du début du fichier

2. puis pour ajouter un élément de type ui Binder de GWT ->
```html
<b:heading size="2">Hello World GWT-Bootstrap</b:heading>
```