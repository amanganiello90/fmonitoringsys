# Folder Monitoring System

## Cosa fa
Questo progetto **maven** contiene **due web services REST**, che monitorano il contenuto della cartella **src/main/resources/checkFolder** (solo file diretti, e non il contenuto delle sottocartelle).

I servizi esposti sono i seguenti:

1. **getfilesInfo** su **/fmonitoringsys/api/filesinfo**. Prende come parametro di ricerca un intervallo di data/ora rispetto all' ultima modifica dei file, e restituisce la lista dei file contenuti nella cartella elencando il percorso, nome, dimensione, timestamp ultima modifica e Hash MD5
2. **getfileFromMd5** su **/fmonitoringsys/api/file**. Prende come parametro l'Hash MD5 e restituisce le informazioni corrispettive del file, ovvero percorso, nome, dimensione, timestamp ultima modifica e Hash MD5 (che era già come parametro)


## Struttura

* Il progetto è strutturato in **spring boot**, il quale **contiene un tomcat (servlet container) integrato**
* Le dipendenze esterne sono specificate nel file **pom.xml**
* I servizi web rest si trovano sotto al package che termina con **web.rest** . Lì si trova il **Controller**
* Sotto al package che termina con **service** si trova il servizio, richiamato dal controller, che effettua la logica di business
* Sotto al package che termina con **to** ci sono gli oggetti che mappano la risposta e tutte le descrizioni riguardanti il file
* Sotto a **src/main/resources/static** c'è la pagina **index.html** visualizzata sull'indirizzo di default del microservizio

## Come lanciarlo

* Come prerequisito bisogna aver installato almeno **java 8** ed avere la rete internet in modo da scaricare le dipendenze esterne
* Lanciare da riga di comando **mvnw.cmd spring-boot:run** (se si usa windows, altrimenti per linux invece di mvnw.cmd usare ./mvnw)
* In alternativa importando in **Eclipse**, è possibile lanciare direttamente la classe **spring boot main**, ovvero la classe **FMonitoryngSysApplication.java**
* Su **http://localhost:8080** ci sarà una pagina html che spiega le funzionalità con degli esempi

### Servizi esposti

I servizi esposti sono due, e sono pubblicati su **http://localhost:8080**. Di seguito esempi di invocazione e risultati di entrambi.

1. _http://localhost:8080/fmonitoringsys/api/filesinfo?start=2015-09-26T01:30:00.000&end=2030-09-26T01:30:00.000_ :


Risposta:

```
{"message":"Operazione conclusa con successo","files":[{"path":"/Users/angelo-mac/Desktop/github/fmonitoringsys/target/classes/checkFolder/primoFile.txt","name":"primoFile.txt","size":0,"lastModify":"2021-11-18T11:57:04","hashMd5":"d41d8cd98f00b204e9800998ecf8427e"},{"path":"/Users/angelo-mac/Desktop/github/fmonitoringsys/target/classes/checkFolder/modello.xml","name":"modello.xml","size":295,"lastModify":"2021-11-18T12:05:54","hashMd5":"6c1f8f4b223de9b3c8d77db12e20b369"},{"path":"/Users/angelo-mac/Desktop/github/fmonitoringsys/target/classes/checkFolder/esempio.json","name":"esempio.json","size":59,"lastModify":"2021-11-18T12:05:03","hashMd5":"d8c2984c1312a1ed8a483357f0caff5f"}]}
```

Se non c'è nessun file che ricade nell'intervallo, la risposta sarà con messaggio di successo ma l'array files sarà vuoto:


```
{"message":"Operazione conclusa con successo","files":[]}
```

2. _http://localhost:8080/fmonitoringsys/api/file?md5=d8c2984c1312a1ed8a483357f0caff5f_


Risposta:

```
{"message":"Operazione conclusa con successo","files":[{"path":"/Users/angelo-mac/Desktop/github/fmonitoringsys/target/classes/checkFolder/esempio.json","name":"esempio.json","size":59,"lastModify":"2021-11-18T12:47:32","hashMd5":"d8c2984c1312a1ed8a483357f0caff5f"}]}
```

Se passi un md5 che non ha corrispondenza con un file, la risposta sarà:

```
{"message":"Non è stato trovato nessun file corrispondente al tuo checksum MD5","files":[]}
```


N.B In tutti casi nella risposta se ci sono errori sarà popolato solo il campo **message**. Per esempio se la cartella da monitorare è inesistente o non ci sono file:


```
{"message":"La cartella checkFolder da monitorare non esiste oppure è vuota!"","files":[]}
```
