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

#ifndef DATABASE_H
#define DATABASE_H

#include <QString>
#include <QtSql>
#include <QSqlQuery>

QT_BEGIN_NAMESPACE
namespace Ui {
    class Database;
}
QT_END_NAMESPACE

class Database: public QObject {
    Q_OBJECT

    public:
        Database(QString path);
        bool openConnection();
        bool closeConnection();
        int insertNote(QString name, QString data);
        int updateNote(int id, QString name, QString data);
        int deleteNote(int id);

    private:
        QSqlDatabase mydb;

};

#endif // DATABASE_H
