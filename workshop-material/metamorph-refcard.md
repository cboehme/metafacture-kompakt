# Metamorph: Kurzübersicht

## Literale verarbeiten

Die Grundoperationen von Metamorph erlauben die Selektion und Verarbeitung von
Literalen.

### Literale selektieren

Das Selektieren von Literalen ist eine Grundoperation in Metamorph. Sie 
geschieht über das `data`-Statement. Das `source`-Attribut definiert, welche Literalnamen selektiert werden sollen:

```xml
<data source="SOURCE-PATTERN" />
```

Im `source`-Attribut muss ein vollständiger Pfad zu einem Literal inklusive
aller übergeordneten Entitäten angeben werden. Die einzelnen Elemente im Pfad
werden durch Punkte voneinander abgetrennt, beispielsweise so:
_'entität1.entität2.literal'_. Folgende Zeichen haben eine Sonderbedeutung:

 * Das **Fragezeichen (?)** steht für ein beliebiges Zeichen. Zum Beispiel
   selektiert das Muster _'ab?'_ alle Literalpfade mit drei Zeichen, die mit den
   Buchstaben _'ab'_ beginnen.
 * Das **Sternchen (*)** ist ein Platzhalter für beliebig viele Zeichen. Das
   Muster _'ab*'_ selektiert bspw. alle Literalpfade die mit den Buchstaben 
   _'ab'_ beginnen.
 * Das **Pipe-Symbol (|)** erlaubt es mehr als ein Muster im `source`-Attribut
   anzugeben. Der Ausdruck _'ab|cd'_ erfasst beispielweise alle Literale, die 
   _'ab'_ oder _'cd'_ heißen.


### Literale umbenennen

Mit dem optionalen `name`-Attribut kann ein Ausgabename für ein Literal 
festgelegt werden:

```xml
<data source="SOURCE-PATTERN" name="NAME" />
```

Im Gegensatz zum `source`-Attribut definiert das `name`-Attribut keinen 
Pfadmuster sondern einen einzelnen Literalnamen auch wenn der Name Punkte 
enthält. Ist das `name`-Attribut nicht vorhanden, wird der ursprüngliche 
Literalname verwendet.


### Literalwerte modifizieren

Im Rumpf von `data`-Statements kann eine Kette von Funktionen definiert werden.
Jeder empfangene Literalwert durchläuft diese Kette Funktion für Funktion. Das
Ergebnis einer Funktion ist die Eingabe der nächsten Funktion. Liefert eine
Funktion kein Ergebnis, wird die Verarbeitung abgebrochen und kein Literal
ausgegeben:

```xml
<data source=="SOURCE-PATTERN">
	<lowercase />
	<replace match="REPLACE-PATTERN" with="WITH-VALUE" />
</data>
```

### Literale aufspalten

Funktionen in `data`-Statements können die Folgefunktion nicht nur einmal
sondern auch mehrfach aufrufen. Dies ermöglicht es einen empfangenen Literalwert
in mehrere Literale zu zerlegen, die dann nacheinander ausgegeben werden:

```xml
<data source="SOURCE-PATTERN">
	<split delimiter="DELIMITER" />
</data>
```

## Literale kombinieren

Um mehrere Literale zusammenzufassen, werden in Metamorph Kollektoren verwendet.
Diese sammeln die Ausgabe mehrerer `data`-Statements (oder untergeordneter
Kollektoren) und generieren aus den gesammelten Literalen eine Ausgabe.

### Mehrere Literale zu einem Neuen kombinieren

Der `combine`-Kollektor ermöglicht es, Literalwerte in Variablen zu sammeln,
die dann verwendet werden können, um den Namen und Wert des generierten 
Literals zu bilden:

```xml
<combine name="NAME-${VAR2}" value="${VAR1}: ${VAR2}">
	<data source="SOURCE1" name="VAR1" />
	<data source="SOURCE2" name="VAR2" />
</combine>
```

Die `data`-Statements innerhalb des `combine`-Kollektors erzeugen keine 
Literale, sondern legen die empfangenen Werte in Variablen ab. Diese Variablen
können dann in die `name`- und `value`-Attributen des `combine`-Kollektors in 
der Schreibweise '${VARIABLENNAME}' eingefügt werden.


### Entitäten anlegen

Zur Gruppierung von Literalen in einer Entität kann der `entity`-Kollektor 
verwendet werden:

```xml
<entity name="ENTITY-NAME">
	<data source="SOURCE1" />
	<data source="SOURCE2" />
</entity>
```

Der Kollektor fasst die Literale in seinem Rumpf bei der Ausgabe in eine
Entität, deren Name über das `name`-Attribut des Kollektors festgelegt ist.


### Steuern der Ausgabe von Kollektoren

In der Standardeinstellung generieren die Kollektoren eine Ausgabe, sobald alle
enthaltenen Statements mindestens einen Wert generiert haben. Die empfangenen
Werte werden nach der Ausgabe nicht zurückgesetzt. D.h. sobald ein weiterer Wert
emfpangen wird, kann eine neue Ausgabe generiert werden.

#### `flushWith`: Unvollständige Kollektoren ausgeben

Alle Kollektoren unterstützen das `flushWith`-Attribut. Dieses kann ein
Pfadmuster enthalten, das ein Literal, eine Entität oder das Datensatzende
beschreibt (das Datensatzende wird mit dem Pfadmuster _'record'_ beschrieben).
Die Verarbeitung der Werte aus dem Rumpf eines mit `flushWith` annotierten
Kollektors erfolgt erst, wenn eine Folge von Ereignissen auf das Pfadmuster im
`flushWith`-Attribut passt. In diesem Fall generiert der Kollektor eine Ausgabe
unabhängig davon, ob alle enthaltenen Statements bereits einen Wert geliefert
haben oder nicht.

#### `reset`: Zurücksetzen von Kollektoren

Mit dem `reset`-Attribut kann ein Kollektor nach der Ausgabe wieder 
zurückgesetzt werden. D.h. er vergisst alle bereits empfangenen Werte von den
Statements in seinem Rumpf.
