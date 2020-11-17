#include "database.h"
#include "constants.h"

Database::Database(QString path) {
    this->mydb = QSqlDatabase::addDatabase("QSQLITE");
    this->mydb.setDatabaseName(path);
}

bool Database::openConnection() {
    if(!this->mydb.open()) {
        qDebug() << ("Error opening database.");
        return false;
    }
    qDebug() << ("Connected to database.");
    /* Check if database is empty */
    QSqlQuery query;
    query.exec("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, data TEXT)");
    return true;
}

int Database::insertNote(QString name, QString data) {
    QSqlQuery query;
    query.prepare("INSERT INTO notes (name, data) VALUES (:name, :data)");
    query.bindValue(":name", name);
    query.bindValue(":data", data);
    if (query.exec()) {
        query.exec("SELECT id FROM notes ORDER BY ID DESC LIMIT 1");
        query.first();
        return query.value(0).toInt();
    }
    return constants::SAVE_ERROR;
}

int Database::updateNote(int id, QString name, QString data) {
    QSqlQuery query;
    query.prepare("UPDATE notes SET name = :name, data = :data WHERE id = :id");
    query.bindValue(":name", name);
    query.bindValue(":data", data);
    query.bindValue(":id", id);
    if (query.exec()) {
        return id;
    }
    return constants::SAVE_ERROR;
}
