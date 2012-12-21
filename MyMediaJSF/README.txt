Apache MyFaces, Tomahawk JSF-2 example
derived from MyGourmet 01
Simple JSF form with input and simple output
- Weiterführung dieses Projekts seit dem 12.10.2012. Vorprojekt: "SimpleFormJSF_MyFaces"


    <h:commandLink value=”Entfernen” action=”#{playlist.deletePlaylistItem}” immediate=”true”>
      <f:setPropertyActionListener target=”#{playlist.pliIdToDelete}” value=”#{plitem.mediafile.id}”/>
    </h:commandLink>

    <h:outputText value="#{plitem.mediafile.id}"/>

    <h:commandButton action="#{playlist.deletePlaylistItem}" value="Entfernen">
      <f:setPropertyActionListener target=”#{playlist.pliIdToDelete}” value=”#{plitem.mediafile.id}”/>
    </h:commandButton>


=== Nächste Schritte ===
- Umstellung auf Spring-3 mit MyGourmet-15 Beispielprojekt
- Playlist in DB speichern über Spring
- Alle Playlists beim "login" aus Datenbank lesen und anzeigen


=== Known issues ===
---

=== Später ===
- Arbeiten mit dem Jetty-Maven-Plugin für continuous deployment: http://wiki.eclipse.org/Jetty/Feature/Jetty_Maven_Plugin
- Nutzung von Jetty statt Tomcat?!?


=== DONE ===
= 20121031 =
- Aufruf der Startseite nicht korrekt: "http://localhost:8080/MyMediaJSF/login.jsf" ist die korrekte Adresse
  -> Korrektur auf richtige URL
Korrektur in web.xml für Startseitenaufruf: <url-pattern>*.xhtml</url-pattern> statt *.jsf

= 20121016 =
- created, modified, last_played in playlist eingefügt
- mediaFileId eingefügt
- Managed bean von playlistitem entfernt

= 20121015 =
- Aufräumen in Klasse Playlist und PlaylistItem
- ID3 Tags sind nicht vorhanden (MP3 files prüfen!) -> DONE
- Trennung von JSF-Frontend-Bean und Modell-Bean

= 20121012 =
- Scannen eines Ordners geht jetzt und alle Dateien werden hinzugefügt. Ordner werden rekursiv gescannt.
- Tabellenkomponente zur Anzeige eines Items
- Scannen des Ordner "/opt/testdaten" und Anzeige von Titel/Interpret der darin enthaltenen mp3-Dateien
- PROJECT_STAGE auf "Production" gesetzt
- Umstellung auf SLF4J trotz Eclipse-Maven-Fehlermeldung: ** LOGGING **
  'Failed to load class "org.slf4j.impl.StaticLoggerBinder".' loggt Tomcat in catalina.out (= stdout) statt in access_log bzw. catalina_2012...
- log4j in pom.xml entfernt

= 20121011 =
- Umstellung auf Java-7 (Tomcat muss auch mit Java-7 gestartet sein)
- Umbenennung der Beispielklassen/Methoden/Felder...
- Ausgabe der MP3-Meta-Informationen (siehe MediaFile.toString()) auf einer JSF-Seite
- Ablösung von BeagleBuddy durch JAudioTagger Library
- Testdaten werden von "/opt/testdaten/" gelesen


<plugins>
    <plugin>
    <!-- Continuous deployment: http://wiki.eclipse.org/Jetty/Feature/Jetty_Maven_Plugin -->
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>7.2.2.v20101205</version>
        <configuration>
            <webAppConfig>
                <contextPath>/${project.artifactId}</contextPath>
            </webAppConfig>
        </configuration>
    </plugin>
</plugins>
