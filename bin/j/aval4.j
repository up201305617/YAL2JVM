.class public aval4
.super java/lang/Object


.method public static f(II)I
.limit locals 3
.limit stack 5

loop1:
iload_1
iload_0
if_icmple loop1_end
invokestatic io/read()I
istore_2
invokestatic io/read()I
istore_0
iload_0
iload_2
iadd
istore_0
goto loop1
loop1_end:
iload_2
ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 2
.limit stack 5
iconst_5
ldc 6
invokestatic aval4/f(II)I
istore_0
iload_0
invokestatic io/println(I)V
return
.end method

.method static public <clinit>()V 
.limit stack 0
.limit locals 0
return
.end method
