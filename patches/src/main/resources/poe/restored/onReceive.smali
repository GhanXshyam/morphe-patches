.registers 5
const-string p1, "extra_download_id"
const-wide/16 v0, -0x1
invoke-virtual {p2, p1, v0, v1}, Landroid/content/Intent;->getLongExtra(Ljava/lang/String;J)J
move-result-wide p1
iget-object v0, p0, Lcom/poe/util/j2;->a:Lkotlin/jvm/internal/z;
iget-object v0, v0, Lkotlin/jvm/internal/z;->element:Ljava/lang/Object;
if-nez v0, :cond_0
goto :goto_0
:cond_0
check-cast v0, Ljava/lang/Number;
invoke-virtual {v0}, Ljava/lang/Number;->longValue()J
move-result-wide v0
cmp-long p1, p1, v0
if-eqz p1, :cond_1
:goto_0
return-void
:cond_1
iget-object p1, p0, Lcom/poe/util/j2;->b:Lcom/poe/util/k2;
invoke-virtual {p1}, Lcom/poe/util/k2;->invoke()Ljava/lang/Object;
iget-object p0, p0, Lcom/poe/util/j2;->c:Lkotlinx/coroutines/m;
invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;
move-result-object p1
invoke-virtual {p0, p1}, Lkotlinx/coroutines/m;->d(Ljava/lang/Object;)V
return-void
