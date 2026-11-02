# AKTUELLES

[x] Starter-Template: Transparenz, Content-Rendering und Statusbar testen

    [ ] bei ausgeblendeter Toolbar testen, ob und wie sich die Statusbar-Farbe unabhängig davon ändern lässt.
    [ ] bei ausgeblendeter bottom bar: wie verhält sich nav bar? sollte vermutlich dann scrim behalten oder?

[ ] Toolbar Dialog: Fullscreen-Modus berücksichtigen (System-Bar-Abstand aktuell vorhanden)
[ ] Side Navigation an Toolbar ausrichten
[ ] Styled*-Klassen:
  - Einheitlichen Dokumentations Header nutzen:
  - Enthält gewünschtes Design pro Theme
  - Enthält Theme-Regeln
  - ...
  - Als Single Source of Truth evaluieren

### Demo App

  <details>

    [x] Theme Einstellungen immer links? oder nur auf root seite?
	- Toolbar Dialog Demo.
	- Forms Demo.
	- Real-World-Beispiele statt isolierter Komponenten.
		=> mit kommentar section am anfang/ende damit man darauf auch im readme sektionen machen kann
	- Settings-Beispiel mit echtem State.
	- Theme-Einstellungen demonstrieren
	[x] Starter-Template bereitstellen.

  </details>

### Features - SPÄTER

[ ] drawer?

### meine Compose* Libraries

  <details>

    - Dialoge zunächst direkt integrieren?
    - Changelog zunächst direkt integrieren?
    - preferences zunächst direkt integrieren?

    ALLES IN TOOLBOX vorerst implementieren?

    - Später mögliche Aufteilung:
      - compose-dialog
      - compose-changelog
      - compose-preference

    - Als Extension-Modul veröffentlichen.
    - UI von Styled-Implementierungen trennen (ähnlich ComposeTable).

  </details>

### TOOLBOX - APP

  <details>

    [ ] ToolbarState-Architektur evaluieren (Code-Beispiel)
    [ ] Back Handling Support für mouse und so weiter?
        plattformübergreifend:
        - Browser Back
        - Maus Back
        - System Back
    [ ] Adaptive App wäre vermutlich dort sinnvoll - muss das nun noch so komplex und platform spezifisch sein?

  </details>
    


