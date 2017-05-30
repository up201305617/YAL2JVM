.class public aval6
.super java/lang/Object


.method public static main([Ljava/lang/String;)V
.limit locals 2
.limit stack 5
ldc 17
invokestatic aval6/sqrt(I)I
istore_0
iload_0
invokestatic io/println(I)V
return
.end method

.method public static sqrt(I)I
.limit locals 13
.limit stack 5

iload_0
istore_3
iconst_0
istore 11
iconst_0
istore_1
iconst_0
istore 4
iconst_0
istore 5
while1:
ldc 6
iload 5
if_icmple while1_end
iload 11
iload_1
iadd
istore 9
iload 9
iconst_2
ishl
istore 7
iload 7
iconst_1
ior
istore_2
iload_1
iconst_1
ishl
istore 12
iload 4
iconst_2
ishl
istore 10
iload_3
ldc 10
ishr
istore 8
iload 8
iconst_3
iand
istore 6
iload 10
iload 6
ior
istore 4
iload_3
iconst_2
ishl
istore_3
iload 5
iconst_1
iadd
istore 5
iload 4
iload_2
swap
if_icmpgt if1_end
iload 12
iconst_1
ior
istore_1
iload_2
istore 11
goto if1_next
if1_end:
iload 12
istore_1
iload 11
iconst_2
ishl
istore 11
if1_next:
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
