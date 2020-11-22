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

#ifndef NOTE_H
#define NOTE_H

#include <QMainWindow>
#include <QCloseEvent>
#include <string>
#include "include/constants.h"

QT_BEGIN_NAMESPACE
namespace Ui {
    class Note;
}
QT_END_NAMESPACE

class Note : public QMainWindow {
    Q_OBJECT

    public:
        Note(QWidget *parent = nullptr, int id = constants::NEW_ID, QString name = "", QString data = "");
        ~Note();

    private:
        Ui::Note *ui;
        bool saved = true;
        int id;
        void closeEvent(QCloseEvent *event);

    private slots:
        void on_closeNote_clicked();
        void on_textEdit_textChanged();
        void on_saveNote_clicked();
        void on_lineEdit_textEdited(const QString &arg1);

    signals:
        int save(int id, QString name, QString data);
        void giveFocus();
};
#endif // NOTE_H
