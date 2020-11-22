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

#ifndef DASHBOARD_H
#define DASHBOARD_H

#include <QMainWindow>
#include "database.h"
#include "note.h"
#include "include/constants.h"
#include "smallnote.h"

QT_BEGIN_NAMESPACE
namespace Ui {
    class Dashboard;
}
QT_END_NAMESPACE

class Dashboard : public QMainWindow {
    Q_OBJECT

    public:
        Dashboard(QWidget *parent = nullptr, Database *db = nullptr);
        ~Dashboard();

    private:
        QObject *lastFocused;
        Ui::Dashboard *ui;
        Database *database;
        QVector<SmallNote*> sns;
        void updateView();
        void createNote(int id = constants::NEW_ID, QString name = "", QString data = "");

    public slots:
        int saveNote(int id, QString name, QString data);
        void setLastFocus(QWidget *old, QWidget *now);
        void recoverFocus();

    private slots:
        void on_newNote_clicked();
        void on_saveAll_clicked();
        void on_remove_clicked();
        void on_open_clicked();
};
#endif // DASHBOARD_H
