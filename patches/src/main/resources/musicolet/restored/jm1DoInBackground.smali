.registers 9
sget-object p1, Lin/krosbits/musicolet/MyApplication;->i:Lin/krosbits/musicolet/MyApplication;
invoke-static {p1}, Ljm1;->getApplicationContext$001(Landroid/content/Context;)Landroid/content/Context;
move-result-object p1
invoke-static {p1}, Ldi0;->m(Landroid/content/Context;)Z
move-result p1
const/4 v0, 0x0
if-nez p1, :cond_0
return-object v0
:cond_0
new-instance p1, Landroid/content/Intent;
iget-object v1, p0, Ljm1;->a:Ljava/lang/String;
invoke-direct {p1, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V
sget-object v1, Lin/krosbits/musicolet/MyApplication;->i:Lin/krosbits/musicolet/MyApplication;
invoke-static {v1}, Ljm1;->getApplicationContext$002(Landroid/content/Context;)Landroid/content/Context;
move-result-object v1
const-class v2, Lin/krosbits/musicolet/MusicActivity;
invoke-static {p1, v1, v2}, Ljm1;->setClass$003(Landroid/content/Intent;Landroid/content/Context;Ljava/lang/Class;)Landroid/content/Intent;
iget-object v1, p0, Ljm1;->b:Ljava/lang/String;
const-string v2, "extshjp_"
invoke-static {p1, v2, v1}, Ljm1;->putExtra$004(Landroid/content/Intent;Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
sget-object v1, Lin/krosbits/musicolet/MyApplication;->i:Lin/krosbits/musicolet/MyApplication;
invoke-static {v1}, Ljm1;->getApplicationContext$005(Landroid/content/Context;)Landroid/content/Context;
move-result-object v1
new-instance v3, Ljava/lang/StringBuilder;
invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V
invoke-static {p1}, Ljm1;->getAction$006(Landroid/content/Intent;)Ljava/lang/String;
move-result-object v4
invoke-static {v3, v4}, Ljm1;->append$007(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
invoke-static {p1, v2}, Ljm1;->getStringExtra$008(Landroid/content/Intent;Ljava/lang/String;)Ljava/lang/String;
move-result-object v2
invoke-static {v3, v2}, Ljm1;->append$009(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
new-instance v2, Lhm1;
invoke-direct {v2}, Lhm1;-><init>()V
iput-object v1, v2, Lhm1;->a:Landroid/content/Context;
invoke-static {v3}, Ljm1;->toString$010(Ljava/lang/StringBuilder;)Ljava/lang/String;
move-result-object v3
iput-object v3, v2, Lhm1;->b:Ljava/lang/String;
const/4 v3, 0x1
new-array v4, v3, [Landroid/content/Intent;
const/4 v5, 0x0
aput-object p1, v4, v5
iput-object v4, v2, Lhm1;->c:[Landroid/content/Intent;
new-instance p1, Landroid/content/ComponentName;
sget-object v4, Lin/krosbits/musicolet/MyApplication;->i:Lin/krosbits/musicolet/MyApplication;
invoke-static {v4}, Ljm1;->getApplicationContext$011(Landroid/content/Context;)Landroid/content/Context;
move-result-object v4
const-class v6, Lin/krosbits/musicolet/MusicActivity;
invoke-direct {p1, v4, v6}, Landroid/content/ComponentName;-><init>(Landroid/content/Context;Ljava/lang/Class;)V
iput-object p1, v2, Lhm1;->d:Landroid/content/ComponentName;
iget-object p0, p0, Ljm1;->c:Ljava/lang/String;
iput-object p0, v2, Lhm1;->e:Ljava/lang/CharSequence;
invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;
move-result-object p0
invoke-virtual {v1}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;
move-result-object p1
iget p1, p1, Landroid/content/pm/ApplicationInfo;->icon:I
invoke-virtual {p0, p1}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;
move-result-object p0
sget-object p1, Landroid/graphics/Bitmap$Config;->ARGB_8888:Landroid/graphics/Bitmap$Config;
const/16 v4, 0xa8
invoke-static {v4, v4, p1}, Landroid/graphics/Bitmap;->createBitmap(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
move-result-object p1
invoke-virtual {p0, v5, v5, v4, v4}, Landroid/graphics/drawable/Drawable;->setBounds(IIII)V
new-instance v4, Landroid/graphics/Canvas;
invoke-direct {v4, p1}, Landroid/graphics/Canvas;-><init>(Landroid/graphics/Bitmap;)V
invoke-virtual {p0, v4}, Landroid/graphics/drawable/Drawable;->draw(Landroid/graphics/Canvas;)V
new-instance p0, Landroidx/core/graphics/drawable/IconCompat;
invoke-direct {p0, v3}, Landroidx/core/graphics/drawable/IconCompat;-><init>(I)V
iput-object p1, p0, Landroidx/core/graphics/drawable/IconCompat;->b:Ljava/lang/Object;
iput-object p0, v2, Lhm1;->f:Landroidx/core/graphics/drawable/IconCompat;
invoke-static {v1, v2}, Ldi0;->s(Landroid/content/Context;Lhm1;)V
return-object v0
