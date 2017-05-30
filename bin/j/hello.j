.class public hello
.super java/lang/Object


.method public static f(II)I
.limit locals 3
.limit stack 5

iconst_0
istore_2
ldc 6
iload_2
if_icmpge if1_end
iconst_5
istore_2
goto if1_next
if1_end:
ldc "aqui"
invokestatic io/println(Ljava/lang/String;)V
if1_next:
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
.limit stack 0
.limit locals 0
return
.end method
