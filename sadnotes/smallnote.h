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

#ifndef SMALLNOTE_H
#define SMALLNOTE_H

#include <QObject>
#include <QString>
#include <QTextEdit>

QT_BEGIN_NAMESPACE
namespace Ui {
    class SmallNote;
}
QT_END_NAMESPACE

class SmallNote: public QObject {
    Q_OBJECT

    public:
        SmallNote(int id, QString name, QString data);
        ~SmallNote();
        int getId();
        bool getStatus();
        QString getName();
        QString getData();
        QTextEdit* getText();
        void setStatus(bool status);

    private:
        int id;
        bool updated = false;
        QString name;
        QString data;
        QTextEdit *view;

    private slots:
        void on_view_textChanged();
};
#endif // SMALLNOTE_H
