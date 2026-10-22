.registers 5
iget-object p1, p0, Lnn1;->g:Lqn1;
invoke-static {p1}, Lnn1;->getWritableDatabase$001(Landroid/database/sqlite/SQLiteOpenHelper;)Landroid/database/sqlite/SQLiteDatabase;
move-result-object p1
new-instance v0, Landroid/content/ContentValues;
const/4 v1, 0x5
invoke-direct {v0, v1}, Landroid/content/ContentValues;-><init>(I)V
iget-wide v1, p0, Lnn1;->a:J
invoke-static {v1, v2}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;
move-result-object v1
const-string v2, "COL_LAST_PLAYED"
invoke-static {v0, v2, v1}, Lnn1;->put$002(Landroid/content/ContentValues;Ljava/lang/String;Ljava/lang/Long;)V
iget v1, p0, Lnn1;->b:I
invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
move-result-object v1
const-string v2, "COL_NUM_PLAYED"
invoke-static {v0, v2, v1}, Lnn1;->put$003(Landroid/content/ContentValues;Ljava/lang/String;Ljava/lang/Integer;)V
iget v1, p0, Lnn1;->c:I
invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
move-result-object v1
const-string v2, "COL_NUM_PLAYED_W"
invoke-static {v0, v2, v1}, Lnn1;->put$004(Landroid/content/ContentValues;Ljava/lang/String;Ljava/lang/Integer;)V
iget v1, p0, Lnn1;->d:I
invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
move-result-object v1
const-string v2, "COL_NUM_PLAYED_M"
invoke-static {v0, v2, v1}, Lnn1;->put$005(Landroid/content/ContentValues;Ljava/lang/String;Ljava/lang/Integer;)V
iget v1, p0, Lnn1;->e:I
invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
move-result-object v1
const-string v2, "COL_NUM_PLAYED_Y"
invoke-static {v0, v2, v1}, Lnn1;->put$006(Landroid/content/ContentValues;Ljava/lang/String;Ljava/lang/Integer;)V
iget-object p0, p0, Lnn1;->f:Ldm0;
iget-object p0, p0, Ldm0;->c:Lkn1;
iget-object p0, p0, Lkn1;->f:Lro;
invoke-static {p0}, Lnn1;->toString$007(Lro;)Ljava/lang/String;
move-result-object p0
filled-new-array {p0}, [Ljava/lang/String;
move-result-object p0
const-string v1, "TABLE_SONGS"
const-string v2, "COL_PATH=?"
invoke-static {p1, v1, v0, v2, p0}, Lnn1;->update$008(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
const/4 p0, 0x0
return-object p0
