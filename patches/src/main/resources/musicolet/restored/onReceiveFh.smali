.registers 3
:try_start_0
sget-object p0, Lin/krosbits/musicolet/MyApplication;->i:Lin/krosbits/musicolet/MyApplication;
invoke-static {p0}, Lin/krosbits/musicolet/fh;->getApplicationContext$001(Landroid/content/Context;)Landroid/content/Context;
const-string p0, "tclass"
invoke-static {p2, p0}, Lin/krosbits/musicolet/fh;->getStringExtra$002(Landroid/content/Intent;Ljava/lang/String;)Ljava/lang/String;
move-result-object p0
invoke-static {p0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;
:try_end_0
.catchall {:try_start_0 .. :try_end_0} :catchall_0
return-void
:catchall_0
move-exception p0
invoke-static {p0}, Lin/krosbits/musicolet/fh;->printStackTrace$007(Ljava/lang/Throwable;)V
return-void
