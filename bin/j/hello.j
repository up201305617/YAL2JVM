.class public hello
.super java/lang/Object


.method public static main([Ljava/lang/String;)V
.limit locals 3
.limit stack 5
iconst_1
iconst_1
iadd
istore_1
invokestatic io/read()I
istore_2
ldc "A = "
iload_1
invokestatic io/println(Ljava/lang/String;I)V
ldc "Read = "
iload_2
invokestatic io/println(Ljava/lang/String;I)V
return
.end method

