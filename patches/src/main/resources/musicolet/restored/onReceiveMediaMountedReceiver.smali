.registers 3
invoke-static {p0}, Lin/krosbits/musicolet/MediaMountedReceiver;->isInitialStickyBroadcast$001(Landroid/content/BroadcastReceiver;)Z
new-instance p0, Ljava/lang/StringBuilder;
const-string p1, "MMR OR "
invoke-direct {p0, p1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V
invoke-static {p0, p2}, Lin/krosbits/musicolet/MediaMountedReceiver;->append$002(Ljava/lang/StringBuilder;Ljava/lang/Object;)Ljava/lang/StringBuilder;
invoke-static {p0}, Lin/krosbits/musicolet/MediaMountedReceiver;->toString$003(Ljava/lang/StringBuilder;)Ljava/lang/String;
move-result-object p0
invoke-static {p0}, Lv82;->y(Ljava/lang/String;)V
invoke-static {}, Lin/krosbits/musicolet/MediaMountedReceiver;->a()V
return-void
