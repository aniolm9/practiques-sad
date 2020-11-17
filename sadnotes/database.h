#ifndef DATABASE_H
#define DATABASE_H

#include <QString>
#include <QtSql>
#include <QSqlQuery>

class Database {
    public:
        Database(QString path);
        bool openConnection();
        bool closeConnection();
        bool insertNote(QString name, QString data);
        bool updateNote(int id, QString name, QString data);
    private:
        QSqlDatabase mydb;
};

#endif // DATABASE_H
