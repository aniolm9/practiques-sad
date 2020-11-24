/*
 * Copyright (c) 2020, Aniol Marti & Hugo Martínez
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You should have received a copy of the License. If not,
 * see <https://opensource.org/licenses/BSD-3-Clause>.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#include "database.h"
#include "include/constants.h"

/* Class constructor. */
Database::Database(QString path): QObject() {
    this->mydb = QSqlDatabase::addDatabase("QSQLITE");
    this->mydb.setDatabaseName(path);
}

/* Opens a connection with the database. */
bool Database::openConnection() {
    if(!this->mydb.open()) {
        return false;
    }
    /* Check if database is empty */
    QSqlQuery query;
    query.exec("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, data TEXT)");
    return true;
}

/* Inserts a new note to the DB and returns the assigned ID if successful. */
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

/* Updates an existing note and returns its ID if successful. */
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

/* Deletes a note from the database, by id. */
int Database::deleteNote(int id) {
    QSqlQuery query;
    query.prepare("DELETE FROM notes WHERE id = :id");
    query.bindValue(":id", id);
    if (query.exec()) {
        return id;
    }
    return constants::SAVE_ERROR;
}
