.class public aval8
.super java/lang/Object


.method public static max1()I
.limit locals 5
.limit stack 5

invokestatic io/read()I
istore_1
invokestatic io/read()I
istore_2
iload_2
istore_0
iload_2
iload_1
swap
if_icmple if1_end
iload_1
istore_0
if1_end:
iconst_2
iconst_4
imul
istore_3
ldc "a"
iload_1
invokestatic io/println(Ljava/lang/String;I)V
ldc -23
iload_1
swap
if_icmpge if2_end
iconst_0
istore 4
goto if2_next
if2_end:
ldc -2
iconst_4
imul
istore 4
if2_next:
iload_0
ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 2
.limit stack 5
invokestatic aval8/max1()I
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
