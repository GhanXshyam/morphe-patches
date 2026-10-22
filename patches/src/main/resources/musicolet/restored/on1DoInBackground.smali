.registers 5
iget-object p1, p0, Lon1;->c:Lqn1;
invoke-static {p1}, Lon1;->getWritableDatabase$001(Landroid/database/sqlite/SQLiteOpenHelper;)Landroid/database/sqlite/SQLiteDatabase;
move-result-object p1
new-instance v0, Landroid/content/ContentValues;
const/4 v1, 0x1
invoke-direct {v0, v1}, Landroid/content/ContentValues;-><init>(I)V
iget-wide v1, p0, Lon1;->a:J
invoke-static {v1, v2}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;
move-result-object v1
const-string v2, "COL_LASTPOS"
invoke-static {v0, v2, v1}, Lon1;->put$002(Landroid/content/ContentValues;Ljava/lang/String;Ljava/lang/Long;)V
iget-object p0, p0, Lon1;->b:Ldm0;
iget-object p0, p0, Ldm0;->c:Lkn1;
iget-object p0, p0, Lkn1;->f:Lro;
invoke-static {p0}, Lon1;->toString$003(Lro;)Ljava/lang/String;
move-result-object p0
filled-new-array {p0}, [Ljava/lang/String;
move-result-object p0
const-string v1, "TABLE_SONGS"
const-string v2, "COL_PATH=?"
invoke-static {p1, v1, v0, v2, p0}, Lon1;->update$004(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
const/4 p0, 0x0
return-object p0
