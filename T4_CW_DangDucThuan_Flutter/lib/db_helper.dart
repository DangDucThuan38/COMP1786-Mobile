import 'package:sqflite/sqflite.dart' as sql;

class SQLHelper{
  static Future<void> createTables(sql.Database database) async{
    await database.execute(""" CREATE TABLE data(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name TEXT,
    date TEXT,
    destination TEXT,
    description TEXT,
    level TEXT,
    length TEXT,
    park TEXT,
    location TEXT,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    )""");
  }
  static Future<sql.Database> db() async{
    return sql.openDatabase("database_name.db",version: 1,
    onCreate:  (sql.Database database, version) async{
      await createTables(database);
    });
  }
  static Future<int> createData(
      String name,
      String date,
      String description,
      String destination,
      String length,
      String level,
      String location,
      String park) async{
    final db= await SQLHelper.db();
    final data = {
      'name': name,
      'date' : date,
      'description':description,
      'destination':destination,
      'length' : length,
      'level': level,
      'location': location,
      'park': park
    };
    final id = await db.insert('data', data,conflictAlgorithm: sql.ConflictAlgorithm.replace);
    return id;
  }

  static Future<List<Map<String, dynamic>>> getAllData() async{
    final db= await SQLHelper.db();
    return db.query('data', orderBy: 'id');
  }

  static Future<List<Map<String, dynamic>>> getSingleData(int id) async{
    final db= await SQLHelper.db();
    return db.query('data',where: "id = ?",whereArgs: [id], limit: 1);
  }

  static Future<int> updateData(int id,String name,
      String date,
      String description,
      String destination,
      String length,
      String level,
      String location,
      String park)async{
    final db= await SQLHelper.db();
    final data = {
      'name': name,
      'date' : date,
      'description':description,
      'destination':destination,
      'length' : length,
      'level': level,
      'location': location,
      'park': 'park',
      'createdAt': DateTime.now().toString()
    };
    final result =
        await db.update('data', data, where: "id = ?",whereArgs: [id]);
    return result;

  }
  static Future<void> deleteData(int id) async{
    final db= await SQLHelper.db();
    try{
      await db.delete('data',where: "id = ?",whereArgs: [id]);
    }catch(e){}
  }


}