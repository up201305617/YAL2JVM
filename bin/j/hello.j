.class public hello
.super java/lang/Object

.field static data [I

.method public static f(II)I
.limit locals 3
.limit stack 2

iload_0
iload_1
imul
istore_2
iconst_5
istore_2
iconst_5
istore_2
iload_2
ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 4
.limit stack 5
iconst_1
iconst_1
iadd
istore_0
invokestatic io/read()I
istore_1
iload_0
iload_1
invokestatic hello/f(II)I
istore_2
ldc "A = "
iload_0
invokestatic io/println(Ljava/lang/String;I)V
ldc "Read = "
iload_1
invokestatic io/println(Ljava/lang/String;I)V
ldc "F = "
iload_2
invokestatic io/println(Ljava/lang/String;I)V
return
.end method

.method static public <clinit>()V 
.limit stack 2
.limit locals 1
bipush 100
newarray int
putstatic hello/data [I
return
.end method
