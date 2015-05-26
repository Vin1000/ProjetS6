# ProjetS6 - SearchUs
Recherche d’information dans un sous-ensemble du site avec Lucene


##Utilisation de boostrap for gwt

1. ajouter xmlns:b="urn:import:com.github.gwtbootstrap.client.ui" du début du fichier

2. puis pour ajouter un élément de type ui Binder de GWT ->
```html
<b:heading size="2">Hello World GWT-Bootstrap</b:heading>
```

##Selenium
Set up windows local:
- Aller dans le dossier tools\Selenium\server\win
- Exécuter startServer.bat

##Database
Set up windows local:
- Créer une bd nommée Opus
- Exécuter le fichier tools\Database\CreateDbStructure.sql
Set up Linux:
    - Aller dans tools\Database en command line (modifier le script FullSetUp.sh si Postgres est installé ou pas)
    - Exécuter les commandes:
        - chmod a+x FullSetUp.sh
        - ./FullSetUp.sh


