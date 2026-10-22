.registers 5
iget p1, p0, Lht0;->a:I
const/4 v0, 0x0
if-nez p1, :cond_0
sput-object p0, Lin/krosbits/musicolet/MediaMountedReceiver;->b:Lht0;
invoke-static {p0}, Lht0;->isCancelled$001(Landroid/os/AsyncTask;)Z
const-wide/16 v1, 0x1388
:try_start_0
invoke-static {v1, v2}, Ljava/lang/Thread;->sleep(J)V
invoke-static {p0}, Lht0;->isCancelled$002(Landroid/os/AsyncTask;)Z
invoke-static {}, Lin/krosbits/musicolet/MyApplication;->q()Z
invoke-static {p0}, Lht0;->isCancelled$003(Landroid/os/AsyncTask;)Z
const-wide/16 v1, 0xfa0
invoke-static {v1, v2}, Ljava/lang/Thread;->sleep(J)V
invoke-static {p0}, Lht0;->isCancelled$004(Landroid/os/AsyncTask;)Z
invoke-static {}, Lin/krosbits/musicolet/MyApplication;->q()Z
:try_end_0
.catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0
goto :goto_0
:catch_0
move-exception p0
invoke-static {p0}, Lht0;->printStackTrace$005(Ljava/lang/Throwable;)V
:goto_0
sput-object v0, Lin/krosbits/musicolet/MediaMountedReceiver;->b:Lht0;
goto :goto_1
:cond_0
iget p0, p0, Lht0;->a:I
const/4 p1, 0x1
if-ne p0, p1, :cond_1
invoke-static {}, Ls71;->i()V
:cond_1
:goto_1
return-object v0
