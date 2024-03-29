# Include java runtime classes
-libraryjars  <java.home>/lib/rt.jar

# Output a source map file
-printmapping proguard.map
-optimizationpasses 1
-dontoptimize
# Keep filenames and line numbers
-keepattributes SourceFile, LineNumberTable
-dontwarn **
-dontshrink
-keepattributes *Annotation*

-keep public class ru.allformine.afmcm.AFMCoreMod {
    public protected *;
}

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclassmembers public class ru.allformine.afmcm.handlers.packet.PacketHandler, ru.allformine.afmcm.gui.DebugGui, ru.allformine.afmcm.messaging.WindowedMessageGuiScreen {
    public protected <methods>;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}