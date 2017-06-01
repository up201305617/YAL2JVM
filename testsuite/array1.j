.class public array1
.super java/lang/Object


.method public static print_array(I)V
.limit locals 4
.limit stack 5

iload_0
newarray int
astore_1
iconst_0
istore_3
while1:
iload_0
iload_3
if_icmple while1_end
iload_3
aload_1
swap
iload_3
swap
iastore
iload_3
iconst_1
iadd
istore_3
goto while1
while1_end:
iconst_0
istore_3
while2:
iload_0
iload_3
if_icmple while2_end
aload_1
iload_3
iaload
istore_2
ldc "a: "
iload_2
invokestatic io/print(Ljava/lang/String;I)V
iload_3
iconst_1
iadd
istore_3
goto while2
while2_end:
return
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 1
.limit stack 5
ldc 10
invokestatic array1/print_array(I)V

return
.end method

.method static public <clinit>()V 
.limit stack 0
.limit locals 0
return
.end method
