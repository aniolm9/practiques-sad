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
        int insertNote(QString name, QString data);
        int updateNote(int id, QString name, QString data);
    private:
        QSqlDatabase mydb;
};

#endif // DATABASE_H
