# practiques-sad
Pràctiques de l'assignatura SAD.

## Exercises
Aquest directori conté un breu script amb Kotlin per comptar el nombre de columnes que té una terminal.

## P1
Aquest directori conté els diferents apartats de la pràctica 1. Tota la pràctica s'ha fet amb Kotlin.

### NoMVC
Conté la implementació de l'editor _EditableBufferedReader_ sense fer servir el patró MVC.

### ObserverMVC
Conté la implementació d'_EditableBufferedReader_ fent servir el model `Observer/Observable` de Java.

### PropertyMVC
Com __ObserverMVC__ però en aquest cas es fan servir les biblioteques `PropertyChangeListener/PropertyChangeSupport`.

### ObserverMVC-Multi
Primera versió poc polida d'un editor multilínia fent servir `Observer/Observable`.

### PropertyMVC-Multi
Aquesta també és una versió d'un editor multilínia, però més arreglada. Fa servir la biblioteca `PropertyChangeListener/PropertyChangeSupport` i només faltaria tenir en compte alguns casos concrets de canviar la mida de la finestra per tenir una versió definitiva.

## P2
Aquesta pràctica només conté un projecte que és un client textual de xat fet amb Kotlin. Utilitza el model habitual de tenir dos fils al client (lectura i escriptura) i N fils al servidor, un per cada client.

## P3
Aquesta pràctica conté dos projectes. Un fet amb Kotlin i l'altre híbrid de Kotlin i Java.

### ChatNIO
Xat textual que en lloc d'utilitzar el model habitual de multithreading utilitza el patró NIO. Això permet una major escalabilitat en escenaris amb molts clients.

### ChatSwing
Xat gràfic amb Swing. El servidor segueix sent textual, criteri de disseny ja que habitualment els servidors no tenen entorn gràfic. Aquest servidor és pràcticament idèntic al de ChatNIO, amb alguns petits canvis per mostrar usuaris.

El client està fet amb Java i amb la biblioteca gràfica de Swing.

## sadnotes
Projecte de l'assignatura. És un editor de notes fet amb C++ i Qt.