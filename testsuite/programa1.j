.class public programa1
.super java/lang/Object

.field static mn I
.field static data [I
.field static mx I

.method public static det([I)V
.limit locals 5
.limit stack 5

iconst_0
istore_3
aload_0
arraylength
iconst_1
isub
istore 4
while1:
iload 4
iload_3
if_icmple while1_end
aload_0
iload_3
iaload
istore_1
iload_3
iconst_1
iadd
istore_3
aload_0
iload_3
iaload
istore_2
iload_1
iload_2
invokestatic library1/max(II)I
putstatic programa1/mx I
iload_1
iload_2
invokestatic library1/min(II)I
putstatic programa1/mn I
goto while1
while1_end:
return
.end method

.method public static main([Ljava/lang/String;)V
.limit locals 1
.limit stack 5
ldc 9
getstatic programa1/data [I
swap
iconst_4
swap
iastore
getstatic programa1/data [I
invokestatic programa1/det([I)V

ldc "max: "
getstatic programa1/mx I
invokestatic io/println(Ljava/lang/String;I)V
ldc "min: "
getstatic programa1/mn I
invokestatic io/println(Ljava/lang/String;I)V
return
.end method

.method static public <clinit>()V 
.limit stack 2
.limit locals 1
bipush 100
newarray int
putstatic programa1/data [I
return
.end method
