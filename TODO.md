# AKTUELLES

[x] demo: setup in popup?
[x] ProvideStyledCustomisation
[x] Dialog window: init position wie center oder so?
[x] Expandable Toolbar

[x] Starter-Template: Transparenz, Content-Rendering und Statusbar testen

    [ ] bei ausgeblendeter Toolbar testen, ob und wie sich die Statusbar-Farbe unabhängig davon ändern lässt.
    [ ] bei ausgeblendeter bottom bar: wie verhält sich nav bar? sollte vermutlich dann scrim behalten oder?

[x] dev tools: action für release mit Auto version / test ja/nein und target maven oder github

[ ] Toolbar Dialog: Fullscreen-Modus berücksichtigen (System-Bar-Abstand aktuell vorhanden)
[ ] Side Navigation an Toolbar ausrichten
[ ] Styled*-Klassen:
  - Einheitlichen Dokumentations Header nutzen:
  - Enthält gewünschtes Design pro Theme
  - Enthält Theme-Regeln
  - ...
  - Als Single Source of Truth evaluieren

### Demo App

    [x] Theme Einstellungen immer links? oder nur auf root seite?

	- Toolbar Dialog Demo.
	- Forms Demo.
	- Real-World-Beispiele statt isolierter Komponenten.
		=> mit kommentar section am anfang/ende damit man darauf auch im readme sektionen machen kann
	- Settings-Beispiel mit echtem State.
	- Theme-Einstellungen demonstrieren.
	- Content-Padding für FAB zeigen.
	[x] Starter-Template bereitstellen.
	- Semitransparente Toolbar unterstützen.
	- Semitransparente Navigation unterstützen.
	- Verhalten von Content unter Overlays demonstrieren (bei semi transparenten toolbars und navigation bars).
  - 
### Features - SPÄTER

[ ] drawer?

### meine Compose* Libraries

  <details>

    - Dialoge zunächst direkt integrieren?
    - Changelog zunächst direkt integrieren?
    - preferences zunächst direkt integrieren?

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
    


