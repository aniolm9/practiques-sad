### Explicació funcionalitat
* Bloc de notes ...

### Eines emprades
* Qt
* C++
* Valgrind
* GitHub

### Implementació
Es va començar desenvolupant dos programes paral·lelament:
* QTNotes:
  * Inicialment creava i guardava notes com a arxius de text a una ubicació determinada de l'ordinador, més endavant utilitza una base de dades SQLite.
  * Botons amb idiverses funcionalitats (amb icones), desar, obrir, copiar, enganxar i tancar.
* sadnotes:
  * Estructura de dashboard i nota que s'obre per separat.
  * Suport multiplataforma.
  * Vam decidir ajuntar els dos projectes a _sadnotes_.
  * Explicar les diferents classes, MOLT BREUMENT.

### Problemes trobats 
* Biblioteca filesystem dona problemes sobretot al executar amb Windows. Es migra a full Qt.
* Saber quin era l'últim element seleccionat (focus) abans de clicar a _Eliminar_ o a _Obrir_.
* Saber a quina SmallNote pertany el QTextEdit guardat a lastFocus.

### Bibliografia
* Qt Documentation
* Qt Forums
* Stackoverflow
* Youtube 😂
