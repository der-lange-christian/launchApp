What it is
==========

Simple application launcher experiment because my synapse-launcher do not want to execute scripts.
At the moment it is property based, so you can but also have to add every command you want to execute.

Why
===

Fun by coding and experimenting with JavaFX.


Help
====

Project was created with the following maven-plugin:

http://www.zenjava.com/2012/11/24/from-zero-to-javafx-in-5-minutes/

mvn archetype:generate -DarchetypeGroupId=com.zenjava -DarchetypeArtifactId=javafx-basic-archetype -DarchetypeVersion=1.1

mvn jfx:build-native


Simple Update
=============

My own installation of the application is at /opt/LauncherApp and has a ln -s to /usr/bin/launcherApp. So I can start
the app by synapse and execute my scripts.
To update the app I run
mvn jfx:build-native

cp target/bundles/LauncherApp/app/* /opt/LauncherApp/app


Bugs
====
mvn jfx:build-native
creates a runtime/jre/lib/amd64/server - directory with jvm.so but when I want to start the app it needs the libjvm.so
in a client-directory.

jvm.dll is not found [..launcher/target/bundles/LauncherApp/runtime/jre/lib/amd64/client/libjvm.so]

Simple renaming of the directory from server to client make it running.
