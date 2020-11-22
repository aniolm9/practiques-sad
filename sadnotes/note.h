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
