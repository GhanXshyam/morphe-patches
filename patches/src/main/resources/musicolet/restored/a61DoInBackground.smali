.registers 5
check-cast p1, [Ljava/lang/Void;
iget-object p1, p0, La61;->a:Ljava/util/ArrayList;
const/4 v0, 0x1
iput-boolean v0, p0, La61;->c:Z
:goto_0
invoke-static {p1}, La61;->isEmpty$001(Ljava/util/ArrayList;)Z
move-result v0
const/4 v1, 0x0
const/4 v2, 0x0
if-nez v0, :cond_0
invoke-static {p1, v2}, La61;->remove$002(Ljava/util/ArrayList;I)Ljava/lang/Object;
move-result-object v0
check-cast v0, Lz51;
iput-object v0, p0, La61;->b:Lz51;
invoke-static {v0}, La61;->run$003(Ljava/lang/Runnable;)V
iput-object v1, p0, La61;->b:Lz51;
goto :goto_0
:cond_0
iput-boolean v2, p0, La61;->c:Z
return-object v1
