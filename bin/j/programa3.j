.class public programa3
.super java/lang/Object


.method public static main([Ljava/lang/String;)V
.limit locals 5
.limit stack 5
ldc 100
newarray int
astore_1
iconst_1
aload_1
swap
iconst_0
swap
iastore
iconst_2
aload_1
swap
ldc 99
swap
iastore
aload_1
invokestatic programa3/f1([I)[I
astore_0
aload_0
iconst_0
iaload
istore_3
aload_0
ldc 99
iaload
istore_2
ldc "first: "
iload_3
invokestatic io/println(Ljava/lang/String;I)V
ldc "last: "
iload_2
invokestatic io/println(Ljava/lang/String;I)V
ldc 100
invokestatic programa3/f2(I)[I
astore_0
aload_0
iconst_0
iaload
istore_3
aload_0
ldc 99
iaload
istore_2
ldc "first: "
iload_3
invokestatic io/println(Ljava/lang/String;I)V
ldc "last: "
iload_2
invokestatic io/println(Ljava/lang/String;I)V
return
.end method

.method public static f1([I)[I
.limit locals 4
.limit stack 5

iconst_0
istore_2
aload_0
arraylength
istore_3
iload_3
newarray int
astore_1
loop1:
aload_0
arraylength
iload_2
if_icmple loop1_end
aload_0
iload_2
iaload
aload_1
swap
iload_2
swap
iastore
iload_2
iconst_1
iadd
istore_2
goto loop1
loop1_end:
aload_1
areturn
.end method

.method public static f2(I)[I
.limit locals 3
.limit stack 5

iload_0
newarray int
astore_1
iconst_0
istore_2
loop1:
aload_1
arraylength
iload_2
if_icmple loop1_end
iconst_1
aload_1
swap
iload_2
swap
iastore
iload_2
iconst_1
iadd
istore_2
goto loop1
loop1_end:
aload_1
areturn
.end method

.method static public <clinit>()V 
.limit stack 0
.limit locals 0
return
.end method
