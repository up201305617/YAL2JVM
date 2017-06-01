.class public aval7
.super java/lang/Object


.method public static main([Ljava/lang/String;)V
.limit locals 2
.limit stack 5
iconst_3
invokestatic aval7/Count(I)I
istore_0
iload_0
invokestatic io/println(I)V
return
.end method

.method public static Count(I)I
.limit locals 4
.limit stack 5

iconst_0
istore_1
iconst_m1
istore_3
while1:
ldc 32
iload_3
if_icmple while1_end
iload_0
iconst_1
iand
istore_2
iconst_1
iload_2
swap
if_icmpne if1_end
iload_1
iconst_1
iadd
istore_1
if1_end:
iload_0
iconst_1
ishr
istore_0
iload_3
iconst_1
iadd
istore_3
goto while1
while1_end:
iload_1
ireturn
.end method

.method static public <clinit>()V 
.limit stack 0
.limit locals 0
return
.end method
