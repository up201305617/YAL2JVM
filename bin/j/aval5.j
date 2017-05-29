.class public aval5
.super java/lang/Object


.method public static f(II)I
.limit locals 4
.limit stack 5

ldc 10
istore_3
iload_1
iload_0
if_icmpne if1_end
while1:
iload_3
iload_0
if_icmple while1_end
ldc "while"
invokestatic io/println(Ljava/lang/String;)V
iload_0
iconst_1
iadd
istore_0
goto while1
while1_end:
iload_0
iconst_2
ishl
istore_2
goto if1_next
if1_end:
iload_1
iload_0
iadd
istore_2
if1_next:
iload_2
ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 2
.limit stack 5
iconst_4
iconst_5
invokestatic aval5/f(II)I
istore_0
iload_0
invokestatic io/println(I)V
iconst_2
iconst_2
invokestatic aval5/f(II)I
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
