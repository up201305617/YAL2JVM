.class public array2
.super java/lang/Object


.method public static main([Ljava/lang/String;)V
.limit locals 5
.limit stack 5
ldc 16
istore_3
iload_3
newarray int
astore_0
iconst_0
istore_1
while1:
iload_3
iload_1
if_icmple while1_end
iconst_1
aload_0
swap
iload_1
swap
iastore
iload_1
iconst_1
iadd
istore_1
goto while1
while1_end:
aload_0
invokestatic array2/sum_array([I)I
istore_2
ldc "sum of array elements = "
iload_2
invokestatic io/println(Ljava/lang/String;I)V
return
.end method

.method public static sum_array([I)I
.limit locals 3
.limit stack 5

iconst_0
istore_2
iconst_0
istore_1
while1:
aload_0
arraylength
iload_2
if_icmple while1_end
iload_1
aload_0
iload_2
iaload
iadd
istore_1
iload_2
iconst_1
iadd
istore_2
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
